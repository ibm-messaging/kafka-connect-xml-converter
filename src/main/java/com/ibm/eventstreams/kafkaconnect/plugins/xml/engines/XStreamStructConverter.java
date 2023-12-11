/**
 * Copyright 2023 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibm.eventstreams.kafkaconnect.plugins.xml.engines;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.Struct;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.MismatchingSchemaException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamStructConverter implements Converter {

    private Schema schema;

    public void registerSchema(Schema schema) {
        this.schema = schema;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return Struct.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {}

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        if (schema.type() == Type.STRUCT) {
            final Struct newObject = new Struct(schema);
            xmlToStruct(reader, schema, newObject);
            return newObject;
        }
        else if (schema.type() == Type.MAP) {
            final Map<Object, Object> newObject = new LinkedHashMap<>();
            xmlToMap(reader, schema, newObject);
            return newObject;
        }
        else if (schema.type() == Type.ARRAY) {
            final List<Object> list = new ArrayList<>();
            xmlToList(reader, schema, list);
            return list;
        }
        else {
            final String value = reader.getValue();
            return parseAsPrimitive(schema, value);
        }
    }


    private void xmlToList(HierarchicalStreamReader reader, Schema listSchema, List<Object> list) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();

            final String val = reader.getValue();
            put(list, listSchema.valueSchema(), val, Collections.emptyMap(), reader);

            reader.moveUp();
        }
    }


    @SuppressWarnings("unchecked")
    private void xmlToMap(HierarchicalStreamReader reader, Schema schema, Map<Object, Object> object) {

        //---------------------------------------------------------------------
        //
        // A map is a series of "entries"
        //
        // An entry is made up of a "key" and a "value"
        //
        // This can be "simple"
        //   e.g.
        //      <a-simple-map>
        //          <some-key>some-value</some-key>
        //          <another-key>another-value</another-key>
        //
        // Or it can be "complex"
        //   e.g.
        //      <a-complex-map>
        //          <entry>
        //               <key>some-key</key>
        //               <value>some-value</value>
        //          </entry>
        //          <entry>
        //               <key>another-key</key>
        //               <value>another-value</value>
        //          </entry>
        //
        //---------------------------------------------------------------------


        final Schema keySchema = schema.keySchema();
        final Schema valueSchema = schema.valueSchema();

        while (reader.hasMoreChildren()) {
            //
            // get the next entry in the map - an entry is made up of a key and a value
            //
            Object key = null;
            Object value = null;


            // we're ready to move down into the entries
            reader.moveDown();


            // check if we can do this as a single simple map entry
            final boolean mapIsSimple = reader.hasMoreChildren() == false;
            if (mapIsSimple) {
                final String entry = reader.getValue();
                if (entry != null && !entry.isBlank()) {
                    object.put(parseAsPrimitive(keySchema, reader.getNodeName()),
                               parseAsPrimitive(valueSchema, entry));
                }
                reader.moveUp();
                return;
            }



            //-----------------------------------------------------------
            //  KEY
            //-----------------------------------------------------------

            // attributes of the parent will be represented as additional map entries
            //  so read these before moving down into the child nodes
            //
            // for example, given this XML, at this point, we want to read
            //  key1=value1 and key2=value2 into the Map<String, String> before
            //  we move down into the child nodes to parse key3 and key4
            //
            //   <parent key1=value1 key2=value2>
            //       <key3>value3</key3>
            //       <key4>value4</key4>
            //       ...
            //
            final Map<String, String> keyAttrs = getAttributes(reader);


            // move down into the key
            reader.moveDown();


            //
            // what type of key are we expecting?
            //

            if (keySchema.type() == Type.STRUCT) {
                key = new Struct(keySchema);

                processStruct(reader, (Struct)key, keyAttrs, keySchema);
            }
            else if (keySchema.type() == Type.ARRAY) {
                key = new ArrayList<>();

                // key arrays are represented by multiple
                //  instances of the same child in the XML
                String nodeName = reader.getNodeName();
                while (nodeName.equals("key")) {
                    final String val = reader.getValue();
                    put((List<Object>)key, keySchema.valueSchema(), val, keyAttrs, reader);

                    reader.moveUp();
                    reader.moveDown();
                    nodeName = reader.getNodeName();
                }

                // NOTE : at this point, we only know we've finished looking
                //  at keys because we've gone too far and looked at the next
                //  value which was not a key.
                // This means we've left the reader pointing at the first
                //  value node immediately after the keys.
            }
            else if (keySchema.type() == Type.MAP) {
                key = new LinkedHashMap<Object, Object>();
                processMap(reader, (Map<Object, Object>) key, keyAttrs, keySchema);
            }
            else {
                final String keyStr = reader.getValue();
                key = parseAsPrimitive(keySchema, keyStr);
            }


            // finished with the key - move back up ready to
            //  start processing the value
            if (keySchema.type() != Type.ARRAY) {
                reader.moveUp();
            }


            //-----------------------------------------------------------
            //  VALUE
            //-----------------------------------------------------------

            reader.getValue();

            // as with keys, attributes of the parent value will be
            //  represented as additional map entries so read these first
            final Map<String, String> valueAttrs = getAttributes(reader);


            //
            // what type of value are we expecting?
            //

            if (valueSchema.type() == Type.STRUCT) {
                reader.moveDown();

                value = new Struct(valueSchema);
                processStruct(reader, (Struct)value, valueAttrs, valueSchema);
            }
            else if (valueSchema.type() == Type.ARRAY) {
                value = new ArrayList<Object>();
                xmlToList(reader, valueSchema, (List<Object>) value);
            }
            else if (valueSchema.type() == Type.MAP) {
                reader.moveDown();

                value = new LinkedHashMap<Object, Object>();
                processMap(reader, (Map<Object,Object>) value, valueAttrs, valueSchema);
            }
            else {
                // move down into the value
                if (keySchema.type() != Type.ARRAY) {
                    reader.moveDown();
                }

                final String valueStr = reader.getValue();
                value = parseAsPrimitive(valueSchema, valueStr);
            }


            // finished with the value - move back up ready to
            //  start processing the next entry
            if (valueSchema.type() != Type.ARRAY) {
                reader.moveUp();
            }


            // we have parsed the key and the value -
            //  this entry is complete

            // add it to the map and move on
            object.put(key, value);


            // return the parser, ready for the next entry
            reader.moveUp();
        }
    }



    private void xmlToStruct(HierarchicalStreamReader reader, Schema schema, Struct object) {

        while (reader.hasMoreChildren()) {

            final Map<String, String> attrs = getAttributes(reader);

            reader.moveDown();

            final String nodeName = reader.getNodeName();

            final Field nextField = schema.field(nodeName);
            if (nextField == null) {
                throw new MismatchingSchemaException();
            }
            final Schema nextSchema = nextField.schema();

            if (nextSchema.type() == Type.ARRAY) {
                final String val = reader.getValue();

                List<Object> currentAry = object.getArray(nodeName);
                if (currentAry == null) {
                    currentAry = new ArrayList<>();
                    object.put(nextField, currentAry);
                }
                put(currentAry, nextSchema.valueSchema(), val, attrs, reader);
            }
            else if (nextSchema.type() == Type.STRUCT) {
                final Struct nextObject = new Struct(nextSchema);
                processStruct(reader, nextObject, attrs, nextSchema);
                object.put(nextField, nextObject);
            }
            else if (nextSchema.type() == Type.MAP) {
                final Map<Object, Object> mapItem = new LinkedHashMap<>();
                processMap(reader, mapItem, attrs, nextSchema);
                object.put(nextField, mapItem);
            }
            else {
                final String val = reader.getValue();
                object.put(nextField, parseAsPrimitive(nextField.schema(), val));
            }

            reader.moveUp();
        }
    }


    private void processMap(HierarchicalStreamReader reader, Map<Object, Object> map, Map<String, String> attributes, Schema mapSchema) {
        while (reader.hasMoreChildren()) {
            xmlToMap(reader, mapSchema, map);
        }

        // add any attributes from the node to the map
        addAttributesToMap(attributes, map, mapSchema);

        // add the node value if there is one
        addValueToMap(reader, map, mapSchema.valueSchema());
    }

    private void processStruct(HierarchicalStreamReader reader, Struct struct, Map<String, String> attributes, Schema schema) {
        // prepare lists to hold the array elements of the struct
        schema.fields()
            .stream()
            .filter(field -> (field.schema().type() == Type.ARRAY && !field.schema().isOptional()))
            .map(Field::name)
            .forEach(arrayName -> {
                struct.put(arrayName, new ArrayList<>());
            });

        // populate the struct
        xmlToStruct(reader, schema, struct);

        // add any attributes from the node to the struct
        addAttributesToStruct(attributes, struct, schema);

        // add the node value if there is one
        addValueToStruct(reader, struct, schema);
    }


    private void put(List<Object> list, Schema listItemSchema, String valueStr, Map<String, String> attrs, HierarchicalStreamReader reader) {
        switch (listItemSchema.type()) {
            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case BOOLEAN:
            case FLOAT32:
            case FLOAT64:
            case STRING:
            case BYTES:
                list.add(parseAsPrimitive(listItemSchema, valueStr));
                break;
            case ARRAY:
                final List<Object> nestedList = new ArrayList<>();
                xmlToList(reader, listItemSchema, nestedList);
                list.add(nestedList);
                break;
            case STRUCT:
                final Struct obj = new Struct(listItemSchema);

                addAttributesToStruct(attrs, obj, listItemSchema);

                final Field field = listItemSchema.field("entry");
                if (field != null) {
                    if (field.schema().type() == Type.ARRAY) {
                        final List<Object> entriesList = new ArrayList<>();
                        obj.put(field, entriesList);
                    }
                    else {
                        obj.put(field, parseAsPrimitive(field.schema(), valueStr));
                    }
                }

                if (reader.hasMoreChildren()) {
                    xmlToStruct(reader, listItemSchema, obj);
                }

                list.add(obj);
                break;
            case MAP:
                final Map<Object, Object> mapObj = new LinkedHashMap<>();
                xmlToMap(reader, listItemSchema, mapObj);
                list.add(mapObj);
                break;
        }
    }


    private Object parseAsPrimitive(Schema valueSchema, String valueStr) {
        switch (valueSchema.type()) {
            case INT8:
            case INT16:
                return Short.parseShort(valueStr);
            case INT32:
                return Integer.parseInt(valueStr);
            case INT64:
                return Long.parseLong(valueStr);
            case BOOLEAN:
                return Boolean.parseBoolean(valueStr);
            case FLOAT32:
                return Float.parseFloat(valueStr);
            case FLOAT64:
                return Double.parseDouble(valueStr);
            case STRING:
                return valueStr;
            case BYTES:
                if ("xs:byte".equals(valueSchema.doc())) {
                    return new byte[] { Byte.parseByte(valueStr) };
                }
                else {
                    return Base64.getDecoder().decode(valueStr);
                }
            default:
                return valueStr;
        }
    }

    private Map<String, String> getAttributes(HierarchicalStreamReader reader) {
        final Map<String, String> attributes = new HashMap<>();
        try {
            for (int i = 0; i < reader.getAttributeCount(); i++) {
                final String attrName = reader.getAttributeName(i);
                final String attrValue = reader.getAttribute(i);
                attributes.put(attrName, attrValue);
            }
        }
        catch (final Exception exc) {}

        return attributes;
    }

    private void addValueToStruct(HierarchicalStreamReader reader, Struct struct, Schema schema) {
        final String value = reader.getValue();
        if (value != null && !value.isBlank()) {
            final Field field = schema.field("entry");
            if (field != null) {
                struct.put(field,
                           parseAsPrimitive(field.schema(),
                                            value));
            }
        }
    }

    private void addValueToMap(HierarchicalStreamReader reader, Map<Object, Object> map, Schema schema) {
        final String value = reader.getValue();
        if (value != null && !value.isBlank()) {
            map.put("entry",
                    parseAsPrimitive(schema, value));
        }
    }

    private void addAttributesToStruct(Map<String, String> attributes, Struct struct, Schema schema) {
        for (final String fieldName : attributes.keySet()) {
            final Field field = schema.field(fieldName);
            struct.put(field,
                       parseAsPrimitive(field.schema(),
                                        attributes.get(fieldName)));
        }
    }

    private void addAttributesToMap(Map<String, String> attributes, Map<Object, Object> map, Schema schema) {
        for (final String field : attributes.keySet()) {
            map.put(parseAsPrimitive(schema.keySchema(), field),
                    parseAsPrimitive(schema.valueSchema(), attributes.get(field)));
        }
    }
}