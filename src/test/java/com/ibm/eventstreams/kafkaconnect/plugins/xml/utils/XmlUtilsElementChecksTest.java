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
package com.ibm.eventstreams.kafkaconnect.plugins.xml.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtilsElementChecksTest {

    private Document testXmlDocument;

    @Before
    public void init() throws ParserConfigurationException {
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        testXmlDocument = domBuilder.newDocument();
    }



    @Test
    public void elementsAreNotOptionalByDefault() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        assertFalse(XmlUtils.isElementOptional(testElement));
    }

    @Test
    public void elementsAreNotListsByDefault() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        assertFalse(XmlUtils.isElementAList(testElement));
    }


    @Test
    public void elementsWithNullMinOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("minOccurs", null);
        assertFalse(XmlUtils.isElementOptional(testElement));
        assertFalse(XmlUtils.isElementAList(testElement));
    }

    @Test
    public void elementsWithNullMaxOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("maxOccurs", null);
        assertFalse(XmlUtils.isElementOptional(testElement));
        assertFalse(XmlUtils.isElementAList(testElement));
    }


    @Test
    public void elementsWithEmptyMinOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("minOccurs", "");
        assertFalse(XmlUtils.isElementOptional(testElement));
        assertFalse(XmlUtils.isElementAList(testElement));
    }

    @Test
    public void elementsWithEmptyMaxOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("maxOccurs", "");
        assertFalse(XmlUtils.isElementOptional(testElement));
        assertFalse(XmlUtils.isElementAList(testElement));
    }


    @Test
    public void elementsWithZeroMinOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("minOccurs", "0");
        assertTrue(XmlUtils.isElementOptional(testElement));
    }

    @Test
    public void elementsWithZeroMaxOccursAreOptional() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("maxOccurs", "0");
        assertTrue(XmlUtils.isElementOptional(testElement));
    }


    @Test
    public void elementsWithOneMinOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("minOccurs", "1");
        assertFalse(XmlUtils.isElementOptional(testElement));
    }

    @Test
    public void elementsWithOneMaxOccurs() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("maxOccurs", "1");
        assertFalse(XmlUtils.isElementAList(testElement));
    }


    @Test
    public void elementsWithMultipleMinElementsAreNotOptional() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("minOccurs", "3");
        assertFalse(XmlUtils.isElementOptional(testElement));
    }

    @Test
    public void elementsWithMultipleMinElementsAreLists() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("minOccurs", "3");
        assertTrue(XmlUtils.isElementAList(testElement));
    }


    @Test
    public void elementsWithMultipleMaxElementsAreLists() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("maxOccurs", "3");
        assertTrue(XmlUtils.isElementAList(testElement));
    }


    @Test
    public void elementsWithUnboundedMaxElementsAreLists() {
        final Element testElement = testXmlDocument.createElement("xs:element");
        testElement.setAttribute("maxOccurs", "unbounded");
        assertTrue(XmlUtils.isElementAList(testElement));
    }
}
