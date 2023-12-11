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

import java.util.Base64;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.ListUtils;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.XmlUtils;

public class CollectionToXmlBytes extends ToXmlBytes {

    private final Logger log = LoggerFactory.getLogger(CollectionToXmlBytes.class);

    public CollectionToXmlBytes(XmlPluginsConfig config) {
        super(config);
    }

    /**
     * Creates an XML string representation of the provided map.
     */
    public byte[] convert(Schema schema, Map<?, ?> value) {
        final Document doc = getDocumentBuilder().newDocument();

        final Element root = doc.createElement(getConfig().getRootElementName());
        doc.appendChild(root);

        if (getConfig().schemasEnabled()) {
            log.debug("Unable to derive schemas from Map objects");
        }

        processMap(doc, root, value);

        return convertXmlDocToBytes(doc);
    }

    /**
     * Creates an XML string representation of the provided collection.
     */
    public byte[] convert(Schema schema, Collection<?> value) {
        final Document doc = getDocumentBuilder().newDocument();

        final Element root = doc.createElement(getConfig().getRootElementName());
        doc.appendChild(root);

        if (getConfig().schemasEnabled()) {
            log.debug("Unable to derive schemas from Collection objects");
        }

        processItem(doc, root, "entry", value);

        return convertXmlDocToBytes(doc);
    }


    private void processMap(Document doc, Element parentElement, Map<?, ?> map) {
        for (final Object key : map.keySet()) {
            final Object value = map.get(key);
            processItem(doc, parentElement, key, value);
        }
    }

    private void processStruct(Document doc, Element parentElement, Struct struct) {
        for (final Field field : struct.schema().fields()) {
            final Object value = struct.get(field);

            processItem(doc, parentElement, field.name(), value);
        }
    }


    private boolean isStructured(Object obj) {
        return obj instanceof Collection ||
               obj instanceof Map ||
               obj instanceof Struct;
    }

    private boolean cannotBeXmlTag(Object obj) {
        return obj instanceof byte[] ||
               XmlUtils.isValidXmlElementName(obj.toString()) != true;
    }



    private void processItem(Document doc, Element parentElement, Object key, Object value) {

        if (key == null) {
            key = "null";
        }

        if (value == null) {
            final Element nullElement = doc.createElement(key.toString());
            parentElement.appendChild(nullElement);
            return;
        }

        // ------- key and value are both non-null

        if (key.getClass().isArray() && !(key instanceof byte[])) {
            processItem(doc, parentElement, ListUtils.nullSafeArrayToList((Object[])key), value);
            return;
        }
        if (value.getClass().isArray() && !(value instanceof byte[])) {
            processItem(doc, parentElement, key, ListUtils.nullSafeArrayToList((Object[])value));
            return;
        }

        // ------- key and value are both non-null, non-arrays


        if ((isStructured(key) || cannotBeXmlTag(key)) && (isStructured(value) || value instanceof byte[])) {
            final Element entryElement = addNode(doc, parentElement, "entry");

            if (key instanceof byte[]) {
                processItem(doc, entryElement, "key",
                    new String(Base64.getEncoder().encode((byte[]) key)));
            }
            else if (key instanceof Collection) {
                addArrayElements(doc, entryElement, (Collection<?>)key, "key");
            }
            else if (key instanceof Map) {
                final Element keyElement = addNode(doc, entryElement, "key");
                processMap(doc, keyElement, (Map<?,?>)key);
            }
            else if (key instanceof Struct) {
                final Element keyElement = addNode(doc, entryElement, "key");
                processStruct(doc, keyElement, (Struct)key);
            }
            else if (cannotBeXmlTag(key)) {
                processItem(doc, entryElement, "key", key);
            }

            if (value instanceof byte[]) {
                processItem(doc, entryElement, "value",
                        new String(Base64.getEncoder().encode((byte[]) value)));
            }
            else if (value instanceof Collection) {
                addArrayElements(doc, entryElement, (Collection<?>)value, "value");
            }
            else {
                final Element valueElement = addNode(doc, entryElement, "value");

                if (value instanceof Map) {
                    processMap(doc, valueElement, (Map<?,?>)value);
                }
                else if (value instanceof Struct) {
                    processStruct(doc, valueElement, (Struct)value);
                }
            }
        }
        else if (isStructured(key) || cannotBeXmlTag(key)) {
            // key is structured
            // value is primitive/unstructured

            final Element entryElement = addNode(doc, parentElement, "entry");

            if (key instanceof byte[]) {
                processItem(doc, entryElement, "key",
                    new String(Base64.getEncoder().encode((byte[]) key)));
            }
            else if (key instanceof Collection) {
                addArrayElements(doc, entryElement, (Collection<?>)key, "key");
            }
            else if (key instanceof Map) {
                final Element keyElement = addNode(doc, entryElement, "key");
                processMap(doc, keyElement, (Map<?,?>)key);
            }
            else if (key instanceof Struct) {
                final Element keyElement = addNode(doc, entryElement, "key");
                processStruct(doc, keyElement, (Struct)key);
            }
            else if (cannotBeXmlTag(key)) {
                processItem(doc, entryElement, "key", key);
            }

            addXmlTextNode(doc, entryElement, "value", value.toString());
        }
        else if (isStructured(value)) {
            // key is primitive/unstructured
            // value is structured

            if (value instanceof Collection) {
                addArrayElements(doc, parentElement, (Collection<?>)value, key.toString());
            }
            else {
                final Element valueElement = addNode(doc, parentElement, key.toString());

                if (value instanceof Map) {
                    processMap(doc, valueElement, (Map<?,?>)value);
                }
                else if (value instanceof Struct) {
                    processStruct(doc, valueElement, (Struct)value);
                }
            }
        }
        else if (value instanceof byte[]) {
            addXmlTextNode(doc, parentElement, key.toString(),
                    new String(Base64.getEncoder().encode((byte[]) value)));
        }
        else {
            addXmlTextNode(doc, parentElement, key.toString(), value.toString());
        }
    }




    private void addArrayElements(Document doc, Element parentElement, Collection<?> list, String field) {
        for (final Object obj : list) {

            final Element element = doc.createElement(field);
            if (obj == null) {
                // do nothing
            }
            else if (obj instanceof Collection) {
                addArrayElements(doc, element, (Collection<?>) obj, "entry");
            }
            else if (obj instanceof Map) {
                processMap(doc, element, (Map<?, ?>) obj);
            }
            else if (obj instanceof Struct) {
                processStruct(doc, element, (Struct) obj);
            }
            else {
                element.appendChild(doc.createTextNode(obj.toString()));
            }

            parentElement.appendChild(element);
        }
    }
}
