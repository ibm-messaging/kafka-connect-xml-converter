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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.XmlUtils;

public class XsdToSchema {

    private final Logger log = LoggerFactory.getLogger(XsdToSchema.class);

    private DocumentBuilder builder;
    private final XmlPluginsConfig config;


    public XsdToSchema(XmlPluginsConfig config) {
        this.config = config;

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (final ParserConfigurationException e) {
            log.error("Failed to create XML document builder", e);
            throw new SerializationException(e);
        }
    }


    public Schema getSchema() {
        final File xsdFile = config.getXsdSchema();
        if (xsdFile == null) {
            return null;
        }

        try {
            final Document xsdDocument = builder.parse(xsdFile);
            final Node xsdSchema = getXsSchema(xsdDocument);

            final Map<String, Element> complexTypes = getDeclaredComplexTypes(xsdSchema);

            final String rootElementName = config.getRootElementName();
            final Element root = getElementByName(xsdSchema, rootElementName);
            if (root == null) {
                throw new SerializationException("Expected root element '" + rootElementName + "' not found");
            }

            return convertElementToSchema(root, false, false, complexTypes, rootElementName);
        }
        catch (final SAXParseException e) {
            log.error("Failed to parse schema document", e);
            throw new SerializationException("Invalid XML provided for schema");
        }
        catch (IOException | SAXException e) {
            log.error("Failed to parse schema document", e);
            throw new SerializationException(e);
        }
    }


    private Element getElementByName(Node xsdSchema, String name) {
        final NodeList xsdElements = xsdSchema.getChildNodes();
        for (int i = 0; i < xsdElements.getLength(); i++) {
            final Node nodeElem = xsdElements.item(i);
            if (nodeElem.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) nodeElem;
                if (name.equals(element.getAttribute("name"))) {
                    return element;
                }
            }
        }
        return null;
    }


    private Map<String, Element> getDeclaredComplexTypes(Node xsdSchema) {
        final Map<String, Element> complexTypesByName = new HashMap<>();

        final Element xsdSchemaElement = (Element) xsdSchema;
        final NodeList types = xsdSchemaElement.getElementsByTagName("xs:complexType");
        for (int i = 0; i < types.getLength(); i++) {
            final Element complexType = (Element) types.item(i);
            if (complexType.hasAttribute("name")) {
                complexTypesByName.put(complexType.getAttribute("name"),
                                       complexType);
            }
        }

        return complexTypesByName;
    }

    private static class ChildElements {
        NodeList elements = new EmptyNodeList();
        NodeList attributes = new EmptyNodeList();
        Element entry = null;
        boolean optionalElements = false;
    }

    private Schema convertElementToSchema(Element xsdElement, boolean optionalElement, boolean listElement, Map<String, Element> declaredComplexTypes, String elementName) {
        ChildElements fieldNodes = null;

        if (xsdElement.hasAttribute("type")) {
            final String elementType = xsdElement.getAttribute("type");
            final boolean isOptional = optionalElement || XmlUtils.isElementOptional(xsdElement);
            final boolean isList = listElement || XmlUtils.isElementAList(xsdElement);

            final Schema type = getPrimitiveSchema(elementType, isOptional, isList);
            if (type != null) {
                return type;
            }

            final Element declaredComplexType = declaredComplexTypes.get(elementType);
            if (declaredComplexType != null) {
                fieldNodes = getChildElementsFromComplexType(declaredComplexType);
            }
        }
        else {
            fieldNodes = getChildElements(xsdElement);
        }

        final SchemaBuilder builder = SchemaBuilder.struct();
        if (elementName != null) {
            builder.name(elementName);
        }

        if (fieldNodes.entry != null) {
            String entrySchemaType = "xs:string";
            if (fieldNodes.entry.hasAttribute("base")) {
                entrySchemaType = fieldNodes.entry.getAttribute("base");
            }
            final Schema entrySchema = getPrimitiveSchema(entrySchemaType, false, false);
            builder.field("entry", entrySchema);
        }

        for (int i = 0; i < fieldNodes.elements.getLength(); i++) {
            final Node fieldNode = fieldNodes.elements.item(i);
            final short nodeType = fieldNode.getNodeType();

            if (nodeType == Node.ELEMENT_NODE) {
                final Element fieldElement = (Element) fieldNode;

                final String name = fieldElement.getAttribute("name");
                final boolean isOptional = fieldNodes.optionalElements || XmlUtils.isElementOptional(fieldElement);
                final boolean isList = XmlUtils.isElementAList(fieldElement);

                if (fieldElement.hasAttribute("type")) {
                    final String type = fieldElement.getAttribute("type");
                    if (declaredComplexTypes.containsKey(type)) {
                        final Schema sch = convertElementToSchema(fieldElement, isOptional, isList, declaredComplexTypes, null);
                        builder.field(name, sch);
                    }
                    else {
                        final Schema fieldSchema = getPrimitiveSchema(type, isOptional && !isList, isList);
                        if (fieldSchema == null) {
                            throw new NotImplementedException("Unsupported element type " + type);
                        }
                        builder.field(name, fieldSchema);
                    }
                }
                else {
                    builder.field(name, convertElementToSchema(fieldElement, isOptional, isList, declaredComplexTypes, null));
                }
            }
        }

        for (int i = 0; i < fieldNodes.attributes.getLength(); i++) {
            final Node attributeNode = fieldNodes.attributes.item(i);
            final NamedNodeMap attrs = attributeNode.getAttributes();

            final Node nameNode = attrs.getNamedItem("name");
            if (nameNode == null) {
                break;
            }
            final String name = nameNode.getNodeValue();

            final boolean isOptional = XmlUtils.isAttributeOptional(attrs);
            final boolean isList = false;

            String type = "xs:string";
            final Node stringNode = attrs.getNamedItem("type");
            if (stringNode != null) {
                type = stringNode.getNodeValue();
            }

            final Schema fieldSchema = getPrimitiveSchema(type, isOptional, isList);
            if (fieldSchema == null) {
                throw new NotImplementedException("Unsupported attribute type " + type);
            }
            builder.field(name, fieldSchema);
        }

        if (listElement) {
            final SchemaBuilder result = SchemaBuilder.array(builder.build());
            if (optionalElement) {
                result.optional();
            }
            return result.build();
        }
        else {
            if (optionalElement) {
                builder.optional();
            }
            return builder.build();
        }
    }


    private ChildElements getChildElements(Element element) {

        // element
        //   |
        //   +-- complexType
        //         |
        //         +-- sequence

        final Element complexType = getChild(element, "xs:complexType");
        return getChildElementsFromComplexType(complexType);
    }

    private ChildElements getChildElementsFromComplexType(Element complexType) {
        final ChildElements childElements = new ChildElements();

        final Element simpleContent = getChild(complexType, "xs:simpleContent");
        if (simpleContent != null) {
            final Element extension = getChild(simpleContent, "xs:extension");
            if (extension != null) {
                childElements.entry = extension;

                final List<Node> attributes = new ArrayList<Node>();
                final NodeList children = extension.getChildNodes();
                for (int x = 0; x < children.getLength(); x++) {
                    final Node child = children.item(x);
                    if ("xs:attribute".equals(child.getNodeName())) {
                        attributes.add(child);
                    }
                }
                childElements.attributes = new FilteredNodeList(attributes);

                return childElements;
            }
        }

        final List<Node> attributes = new ArrayList<Node>();
        final NodeList children = complexType.getChildNodes();
        for (int x = 0; x < children.getLength(); x++) {
            final Node child = children.item(x);
            if ("xs:attribute".equals(child.getNodeName())) {
                attributes.add(child);
            }
        }
        childElements.attributes = new FilteredNodeList(attributes);

        Element sequence = getChild(complexType, "xs:sequence");
        if (sequence != null) {
            childElements.elements = sequence.getChildNodes();
            return childElements;
        }

        sequence = getChild(complexType, "xs:all");
        if (sequence != null) {
            childElements.elements = sequence.getChildNodes();
            return childElements;
        }

        sequence = getChild(complexType, "xs:choice");
        if (sequence != null) {
            childElements.elements = sequence.getChildNodes();
            childElements.optionalElements = true;
            return childElements;
        }

        return childElements;
    }

    private static class EmptyNodeList implements NodeList {
        @Override
        public Node item(int index) {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }
    }

    private static class FilteredNodeList implements NodeList {
        private final List<Node> list;
        FilteredNodeList(List<Node> list) {
            this.list = list;
        }

        @Override
        public Node item(int index) {
            return list.get(index);
        }

        @Override
        public int getLength() {
            return list.size();
        }
    }


    private Element getChild(Element parent, String tagName) {
        final NodeList children = parent.getElementsByTagName(tagName);
        if (children.getLength() > 0) {
            return (Element) children.item(0);
        }
        return null;
    }


    private Schema getPrimitiveSchema(String xsType, boolean isOptional, boolean isList) {
        Schema type = null;

        switch (xsType) {
            case "xs:anySimpleType":
            case "xs:anyURI":
            case "xs:date":
            case "xs:dateTime":
            case "xs:duration":
            case "xs:hexBinary":
            case "xs:string":
            case "xs:time":
            case "xs:normalizedString":
                type = isOptional ?
                            Schema.OPTIONAL_STRING_SCHEMA :
                            Schema.STRING_SCHEMA;
                break;
            case "xs:decimal":
            case "xs:double":
                type = isOptional ?
                            Schema.OPTIONAL_FLOAT64_SCHEMA :
                            Schema.FLOAT64_SCHEMA;
                break;
            case "xs:float":
                type = isOptional ?
                            Schema.OPTIONAL_FLOAT32_SCHEMA :
                            Schema.FLOAT32_SCHEMA;
                break;
            case "xs:long":
            case "xs:unsignedLong":
                type = isOptional ?
                            Schema.OPTIONAL_INT64_SCHEMA :
                            Schema.INT64_SCHEMA;
                break;
            case "xs:integer":
            case "xs:int":
            case "xs:nonNegativeInteger":
            case "xs:positiveInteger":
            case "xs:unsignedInt":
            case "xs:nonPositiveInteger":
            case "xs:negativeInteger":
                type = isOptional ?
                            Schema.OPTIONAL_INT32_SCHEMA :
                            Schema.INT32_SCHEMA;
                break;
            case "xs:short":
            case "xs:unsignedShort":
                type = isOptional ?
                            Schema.OPTIONAL_INT16_SCHEMA :
                            Schema.INT16_SCHEMA;
                break;
            case "xs:gDay":
            case "xs:gMonth":
            case "xs:gMonthDay":
            case "xs:gYear":
            case "xs:gYearMonth":
                type = isOptional ?
                            Schema.OPTIONAL_STRING_SCHEMA :
                            Schema.STRING_SCHEMA;
                break;
            case "xs:boolean":
                type = isOptional ?
                            Schema.OPTIONAL_BOOLEAN_SCHEMA :
                            Schema.BOOLEAN_SCHEMA;
                break;
            case "xs:base64Binary":
                type = isOptional ?
                            Schema.OPTIONAL_BYTES_SCHEMA :
                            Schema.BYTES_SCHEMA;
                break;
            case "xs:byte":
            case "xs:unsignedByte":
                type = isOptional ?
                    SchemaBuilder.bytes().doc("xs:byte").optional().build() :
                    SchemaBuilder.bytes().doc("xs:byte").build();
                break;
            default:
                log.debug("unrecognised schema {}", type);
                return null;
        }

        return isList ?
                SchemaBuilder.array(type).build() :
                type;
    }


    private Node getXsSchema(Document xsdDocument) {
        final NodeList schemas = xsdDocument.getElementsByTagName("xs:schema");
        if (schemas.getLength() == 0) {
            throw new SerializationException("No schema found");
        }
        else if (schemas.getLength() > 1) {
            log.info("Multiple schemas found in XSD document");
        }
        return schemas.item(0);
    }

}
