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

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;

import javax.xml.XMLConstants;

import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.XmlUtils;

public class StructToXmlBytes extends ToXmlBytes {

    private final Logger log = LoggerFactory.getLogger(StructToXmlBytes.class);

    public StructToXmlBytes(XmlPluginsConfig config) {
        super(config);
    }

    public byte[] convert(Schema schema, Struct value) {
        // use the schema name as the root element for the XML doc
        //  we create, unless there is no schema name or it isn't
        //  suitable for use as an XML tag
        String schemaName = schema.name();
        if (schemaName == null || XmlUtils.isValidXmlElementName(schemaName) == false) {
            schemaName = getConfig().getRootElementName();
        }

        final Document doc = createXmlDoc(schema, value, schemaName);
        return convertXmlDocToBytes(doc);
    }
    public byte[] convert(Schema schema, Collection<?> value) {
        final Document doc = createXmlDoc(schema, value, getConfig().getRootElementName());
        return convertXmlDocToBytes(doc);
    }
    public byte[] convert(Schema schema, Map<?, ?> value) {
        final Document doc = createXmlDoc(schema, value, getConfig().getRootElementName());
        return convertXmlDocToBytes(doc);
    }

    private Document createXmlDoc(Schema schema, Object value, String rootName) {
        final Document doc = getDocumentBuilder().newDocument();

        final Element root = doc.createElement(rootName);
        doc.appendChild(root);

        if (getConfig().schemasEnabled()) {
            doc.setXmlStandalone(true);
            root.setAttribute("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
            root.setAttribute("xsi:noNamespaceSchemaLocation", "#connectSchema");

            final Element schemaElement = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:schema");
            schemaElement.setAttribute("xmlns:xs", XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaElement.setAttribute("id", "connectSchema");

            final Element schemaRootElement = addNode(doc, schemaElement, "xs:element");
            schemaRootElement.setAttribute("name", rootName);

            final Element complexTypeDefinition = addNode(doc, schemaRootElement, "xs:complexType");
            final Element schemaSequence = addNode(doc, complexTypeDefinition, "xs:sequence");

            final Element schemaItem = addNode(doc, schemaSequence, "xs:any");
            schemaItem.setAttribute("processContents", "skip");
            schemaItem.setAttribute("namespace", XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaItem.setAttribute("minOccurs", "0");
            schemaItem.setAttribute("maxOccurs", "1");

            if (schema.type() == Type.STRUCT) {
                for (final Field field : schema.fields()) {
                    processSchema(doc, schemaSequence, field.name(), field.schema());
                }
            }
            else if (schema.type() == Type.ARRAY) {
                processSchema(doc, schemaSequence, "entry", schema);
            }
            else if (schema.type() == Type.MAP) {
                processMapSchema(doc, schemaSequence, schema);
            }
            else {
                log.error("Unknown type found in schema {}", schema.type().getName());
            }

            root.appendChild(schemaElement);
        }

        if (value instanceof Struct) {
            processStruct(doc, root, (Struct)value);
        }
        else if (value instanceof Collection) {
            addListElements(doc, root, (Collection<?>)value, "entry");
        }
        else if (value instanceof Map) {
            processMap(doc, root, (Map<?, ?>)value);
        }
        else {
            throw new NotImplementedException(value.getClass());
        }

        return doc;
    }



    private void processSchema(Document doc, Element sequenceElement, String name, Schema schema) {
        final Element schemaElement = addNode(doc, sequenceElement, "xs:element");
        schemaElement.setAttribute("name", name);

        switch (schema.type()) {
            case ARRAY:
                schemaElement.setAttribute("maxOccurs", "unbounded");

                if (schema.valueSchema().type().isPrimitive()) {
                    schemaElement.setAttribute("type", getXmlType(schema.valueSchema().type()));

                    if (schema.valueSchema().isOptional()) {
                        schemaElement.setAttribute("minOccurs", "0");
                    }
                }
                else {
                    final Element arrayTypeDef = addNode(doc, schemaElement, "xs:complexType");
                    final Element arraySeq = addNode(doc, arrayTypeDef, "xs:sequence");

                    if (schema.valueSchema().type() == Type.STRUCT) {
                        for (final Field field : schema.valueSchema().fields()) {
                            processSchema(doc, arraySeq, field.name(), field.schema());
                        }
                    }
                    else if (schema.valueSchema().type() == Type.ARRAY) {
                        processSchema(doc, arraySeq, "entry", schema.valueSchema());
                    }
                    else if (schema.valueSchema().type() == Type.MAP) {
                        processMapSchema(doc, arraySeq, schema.valueSchema());
                    }
                }
                break;

            case STRUCT:
                final Element structTypeDef = addNode(doc, schemaElement, "xs:complexType");
                final Element structSeq = addNode(doc, structTypeDef, "xs:sequence");

                for (final Field structItem : schema.fields()) {
                    processSchema(doc, structSeq, structItem.name(), structItem.schema());
                }
                break;

            case MAP:
                final Element mapItemTypeDef = addNode(doc, schemaElement, "xs:complexType");
                final Element mapItemSeq = addNode(doc, mapItemTypeDef, "xs:sequence");
                processMapSchema(doc, mapItemSeq, schema);
                break;

            default:
                schemaElement.setAttribute("type", getXmlType(schema.type()));

                if (schema.isOptional()) {
                    schemaElement.setAttribute("minOccurs", "0");
                    schemaElement.setAttribute("maxOccurs", "1");
                }

                break;
        }
    }


    private void processMapSchema(Document doc, Element parentElement, Schema mapSchema) {
        final Element mapItemEntry = addNode(doc, parentElement, "xs:element");
        mapItemEntry.setAttribute("name", "entry");
        mapItemEntry.setAttribute("maxOccurs", "unbounded");
        mapItemEntry.setAttribute("minOccurs", "0");

        final Element mapItemEntryTypeDef = addNode(doc, mapItemEntry, "xs:complexType");
        final Element mapItemEntrySeq = addNode(doc, mapItemEntryTypeDef, "xs:sequence");

        processSchema(doc, mapItemEntrySeq, "key", mapSchema.keySchema());
        processSchema(doc, mapItemEntrySeq, "value", mapSchema.valueSchema());
    }


    private void processStruct(Document doc, Element parentElement, Struct source) {
        if (source == null) {
            return;
        }
        for (final Field field : source.schema().fields()) {
            final Schema fieldSchema = field.schema();

            switch (fieldSchema.type()) {
                case ARRAY:
                    addArrayElements(doc, parentElement, source, field.name(), field.schema().valueSchema());
                    break;

                case STRUCT:
                    final Struct elementValue = source.getStruct(field.name());
                    if (elementValue != null) {
                        final Element structElement = doc.createElement(field.name());
                        processStruct(doc, structElement, elementValue);
                        parentElement.appendChild(structElement);
                    }
                    break;

                case MAP:
                    final String mapName = field.name();
                    final Element mapElement = doc.createElement(field.name());

                    final Map<Object, Object> map = source.getMap(mapName);
                    processMap(doc, mapElement, map);
                    parentElement.appendChild(mapElement);
                    break;

                case BYTES:
                    final Element bytesElement = doc.createElement(field.name());
                    final byte[] bytes ;
                    String strRepresentation;

                    if (Decimal.LOGICAL_NAME.equals(fieldSchema.name())){
                        strRepresentation = source.get(field.name()).toString();
                    } else {
                       bytes =  source.getBytes(field.name());
                       if (bytes.length == 1 && "xs:byte".equals(fieldSchema.doc())) {
                           strRepresentation = Byte.toString(bytes[0]);
                       } else {
                           strRepresentation = Base64.getEncoder().encodeToString(bytes);
                       }
                    }
                    bytesElement.appendChild(doc.createTextNode(strRepresentation));

                    parentElement.appendChild(bytesElement);
                    break;

                default:
                    final Object value = source.get(field.name());
                    addXmlTextNode(doc, parentElement, field.name(), value == null ? null : value.toString());
                    break;
            }
        }
    }


    private void processMap(Document doc, Element mapElement, Map<?, ?> map) {
        for (final Object key : map.keySet()) {
            final Element mapItemElement = doc.createElement("entry");

            if (key == null) {
                addNode(doc, mapItemElement, "key");
            }
            else if (key instanceof Struct) {
                final Element mapItemKeyElement = addNode(doc, mapItemElement, "key");
                processStruct(doc, mapItemKeyElement, (Struct) key);
            }
            else if (key instanceof String) {
                final Element mapItemKeyElement = addNode(doc, mapItemElement, "key");
                mapItemKeyElement.setTextContent(key.toString());
            }
            else if (key instanceof byte[]) {
                final Element mapItemKeyElement = addNode(doc, mapItemElement, "key");
                mapItemKeyElement.setTextContent(Base64.getEncoder().encodeToString((byte[])key));
            }
            // else if (key.getClass().isArray()) {
            //     addListElements(doc, mapItemElement, ListUtils.nullSafeArrayToList((Object[])key), "key");
            //}
            else if (key instanceof Collection) {
                addListElements(doc, mapItemElement, (Collection<?>)key, "key");
            }
            else if (key instanceof Map) {
                final Element mapItemKeyElement = addNode(doc, mapItemElement, "key");
                processMap(doc, mapItemKeyElement, (Map<?,?>) key);
            }
            else {
                final Element mapItemKeyElement = addNode(doc, mapItemElement, "key");
                mapItemKeyElement.setTextContent(key.toString());
            }


            final Object value = map.get(key);
            if (value instanceof Struct) {
                final Element mapItemValueElement = addNode(doc, mapItemElement, "value");
                processStruct(doc, mapItemValueElement, (Struct) value);
            }
            else if (value instanceof String) {
                addXmlTextNode(doc, mapItemElement, "value", value.toString());
            }
            else if (value instanceof byte[]) {
                final Element mapItemValueElement = addNode(doc, mapItemElement, "value");
                mapItemValueElement.setTextContent(Base64.getEncoder().encodeToString((byte[])value));
            }
            // else if (value.getClass().isArray()) {
            //    addListElements(doc, mapItemElement, ListUtils.nullSafeArrayToList((Object[])value), "value");
            // }
            else if (value instanceof Collection) {
                addListElements(doc, mapItemElement, (Collection<?>)value, "value");
            }
            else if (value instanceof Map) {
                final Element mapItemValueElement = addNode(doc, mapItemElement, "value");
                processMap(doc, mapItemValueElement, (Map<?,?>) value);
            }
            else {
                final Element mapItemValueElement = addNode(doc, mapItemElement, "value");
                mapItemValueElement.setTextContent(value.toString());
            }

            mapElement.appendChild(mapItemElement);
        }
    }


    private void addArrayElements(Document doc, Element parentElement, Struct source, String field, Schema itemSchema) {
        for (final Object obj : source.getArray(field)) {

            final Element element = doc.createElement(field);
            if (obj instanceof Struct) {
                processStruct(doc, element, (Struct) obj);
            }
            else if (obj instanceof Collection) {
                addListElements(doc, element, (Collection<?>) obj, "entry");
            }
            else if (obj instanceof Map) {
                processMap(doc, element, (Map<?, ?>) obj);
            }
            else if (obj != null) {
                element.appendChild(doc.createTextNode(obj.toString()));
            }

            parentElement.appendChild(element);
        }
    }


    private String getXmlType(Type type) {
        switch (type) {
            case INT8:
            case INT16:
                return "xs:short";
            case INT32:
                return "xs:integer";
            case INT64:
                return "xs:long";
            case FLOAT64:
                return "xs:double";
            case FLOAT32:
                return "xs:float";
            case BYTES:
                return "xs:base64Binary";
            default:
                return "xs:" + type.getName();
        }
    }


    private void addListElements(Document doc, Element parentElement, Collection<?> list, String elementName) {
        for (final Object entry : list) {
            if (entry instanceof Struct) {
                final Element entryElement = addNode(doc, parentElement, elementName);
                processStruct(doc, entryElement, (Struct) entry);
            }
            else if (entry instanceof Collection) {
                final Element entryElement = addNode(doc, parentElement, elementName);
                addListElements(doc, entryElement, (Collection<?>) entry, "entry");
            }
            else if (entry instanceof Map) {
                final Element entryElement = addNode(doc, parentElement, elementName);
                processMap(doc, entryElement, (Map<?,?>) entry);
            }
            else {
                addXmlTextNode(doc, parentElement, elementName, entry.toString());
            }
        }
    }
}
