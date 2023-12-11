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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.ListUtils;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.XmlUtils;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamMapConverter implements Converter {

    private final Logger log = LoggerFactory.getLogger(XStreamMapConverter.class);

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return AbstractMap.class.isAssignableFrom(type);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return xmlToMap(reader);
    }



    private Map<String, Object> xmlToMap(HierarchicalStreamReader reader, Map<String, Object> map) {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> attrs = new LinkedHashMap<>();

        while (reader.hasMoreChildren()) {
            // get attributes of current node
            attrs = attrsToMap(reader);

            // move down one level
            reader.moveDown();

            if (reader.hasMoreChildren()) {
                // child nodes found at the lower level - process recursively
                final Map<String, Object> innerItem = xmlToMap(reader);

                if (attrs.size() > 0) {
                    mergeAttributesIntoNode(reader.getNodeName(), innerItem, attrs);

                    // clear attributes now they have been used
                    attrs = new LinkedHashMap<>();
                }

                list.add(innerItem);
            }
            else {
                if (attrs.size() > 0) {
                    if (! reader.getValue().isBlank()) {
                        attrs.put("entry", XmlUtils.guessType(reader.getValue()));
                    }
                    list.add(Map.of(reader.getNodeName(), attrs));

                    // clear attributes now they have been used
                    attrs = new LinkedHashMap<>();
                }
                else {
                    list.add(Map.of(reader.getNodeName(),
                                    XmlUtils.guessType(reader.getValue())));
                }
            }

            // finished processing children - move back up
            reader.moveUp();
        }

        // if there are any remaining attributes that haven't been added
        //  add them now
        if (attrs.size() > 0) {
            list.add(Map.of(reader.getNodeName(), attrs));
        }

        // return as a map
        map.put(reader.getNodeName(), convertMapToList(flatten(list)));

        return map;
    }


    @SuppressWarnings("unchecked")
    private void mergeAttributesIntoNode(String nodeKey, Map<?, ?> parent, Map<String, Object> attrs) {
        Map<String, Object> node;
        if (parent.containsKey(nodeKey) && parent.get(nodeKey) instanceof Map) {
            node = ((Map<String, Object>) parent.get(nodeKey));
        }
        else {
            log.error("Map not found in parent element");
            return;
        }
        for (final String key : attrs.keySet()) {
            node.put(key, attrs.get(key));
        }
    }


    private Map<String, Object> attrsToMap(HierarchicalStreamReader reader) {
        final Map<String, Object> attrsMap = new LinkedHashMap<>();
        final Iterator<?> attrNames = reader.getAttributeNames();
        while (attrNames.hasNext()) {
            final String attrName = attrNames.next().toString();
            attrsMap.put(attrName, XmlUtils.guessType(reader.getAttribute(attrName)));
        }
        return attrsMap;
    }


    private Map<String, Object> xmlToMap(HierarchicalStreamReader reader) {
        return xmlToMap(reader, new LinkedHashMap<String, Object>());
    }


    private boolean isEntry(Map<String, ?> map) {
        return map.size() == 1 && map.containsKey("entry");
    }


    @SuppressWarnings("unchecked")
    private Object convertMapToList(Map<String, Object> map) {
        for (final String key : map.keySet()) {
            final Object itemToConvert = map.get(key);
            if (itemToConvert instanceof List) {
                final List<?> listToConvert = (List<?>) itemToConvert;
                boolean shouldConvert = true;
                final List<Object> converted = new ArrayList<>();
                for (final Object listItem : listToConvert) {
                    shouldConvert = shouldConvert &&
                                    listItem instanceof Map &&
                                    isEntry((Map<String, ?>) listItem);
                    if (shouldConvert) {
                        converted.add(((Map<?,?>)listItem).get("entry"));
                    }
                }
                if (shouldConvert) {
                    map.put(key, converted);
                }
            }
        }

        return map;
    }


    private Map<String, Object> flatten(List<Map<String, Object>> list) {
        final Map<String, Object> output = new LinkedHashMap<>();

        for (final Map<String, Object> listItem : list) {

            for (final String key : listItem.keySet()) {
                final Object nextItem = listItem.get(key);

                if (output.containsKey(key)) {
                    // already an item with this key

                    if (output.get(key) instanceof List) {
                        // the existing item is already a list - add the new item to it
                        ListUtils.addItemToList(output.get(key), nextItem);
                    }
                    else {
                        // the existing item is a singleton
                        //  replace it with a list containing the existing item and the new item
                        output.put(key,
                                   ListUtils.create(output.get(key), nextItem));
                    }
                }
                else {
                    // not seen anything with this key before
                    //  create as a singleton
                    output.put(key, listItem.get(key));
                }
            }
        }

        return output;
    }


    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {}
}
