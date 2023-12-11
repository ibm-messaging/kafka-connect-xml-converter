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

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;

public abstract class ToXmlBytes {

    private final Logger log = LoggerFactory.getLogger(ToXmlBytes.class);

    /** XML document builder - used to create new document trees */
    private DocumentBuilder dBuilder;

    private final XmlPluginsConfig config;

    private Transformer transformer;


    public ToXmlBytes(XmlPluginsConfig config) {
        this.config = config;

        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        }
        catch (final ParserConfigurationException e) {
            log.error("Failed to set up XML document builder", e);
            throw new SerializationException(e);
        }

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        }
        catch (final TransformerConfigurationException e) {
            log.error("Failed to create XML transformer", e);
            throw new SerializationException(e);
        }
    }

    protected XmlPluginsConfig getConfig() {
        return config;
    }

    protected DocumentBuilder getDocumentBuilder() {
        return dBuilder;
    }

    protected byte[] convertXmlDocToBytes(Document doc) {
        final DOMSource source = new DOMSource(doc);
        final StringWriter sw = new StringWriter();
        final StreamResult srResult = new StreamResult(sw);
        try {
            transformer.transform(source, srResult);
        }
        catch (final TransformerException e) {
            log.error("Failed to transform XML doc", e);
            throw new SerializationException(e);
        }
        return sw.toString().getBytes();
    }


    protected Element addNode(Document doc, Element parent, String name) {
        final Element element = doc.createElement(name);
        parent.appendChild(element);
        return element;
    }

    protected void addXmlTextNode(Document doc, Element parent, String key, String value) {
        if (value != null) {
            final Element element = doc.createElement(key);
            element.appendChild(doc.createTextNode(value));
            parent.appendChild(element);
        }
    }
}
