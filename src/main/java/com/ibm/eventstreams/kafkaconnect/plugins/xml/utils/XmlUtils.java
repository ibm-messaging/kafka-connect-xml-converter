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

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlUtils {

    /**
     * Identifies if the spec for a xs:element indicates that this
     *  element should be represented as an optional field.
     */
    public static boolean isElementOptional(Element xsdElement) {
        return xsdElement.getAttribute("minOccurs").equals("0") ||
               xsdElement.getAttribute("maxOccurs").equals("0");
    }

    /**
     * Identifies if the spec for a xs:element indicates that this
     *  element should be represented as a list.
     */
    public static boolean isElementAList(Element xsdElement) {
        final String maxOccurs = xsdElement.getAttribute("maxOccurs");
        final String minOccurs = xsdElement.getAttribute("minOccurs");
        return (maxOccurs.isBlank() == false &&
                maxOccurs.equals("1") == false) ||
               (minOccurs.isBlank() == false &&
                minOccurs.equals("1") == false &&
                minOccurs.equals("0") == false);
    }

    /**
     * Identifies if the spec for a xs:attribute indicates that
     *  this attribute should be represented as an optional field.
     */
    public static boolean isAttributeOptional(NamedNodeMap xsdAttribute) {
        boolean isOptional = true;
        final Node optionalNode = xsdAttribute.getNamedItem("use");
        if (optionalNode != null) {
            final String optionalValue = optionalNode.getNodeValue();
            isOptional = !("required".equals(optionalValue));
        }
        return isOptional;
    }


    /**
     * Guesses the most likely type of the provided string
     *  value found in an XML document.
     */
    public static Object guessType(Object input) {
        if (input == null) {
            return null;
        }
        if (input.equals("true")) {
            return true;
        }
        else if (input.equals("false")) {
            return false;
        }
        else {
            try {
                final double attempt = Double.parseDouble(input.toString());
                if (attempt % 1 == 0) {
                    return (int) attempt;
                }
                else {
                    return attempt;
                }
            }
            catch (final Exception e) {}
        }
        return input;
    }


    /**
     * Checks if a provided string is okay to use as an
     *  XML element name.
     *
     * Implements the naming rules defined in https://www.w3.org/TR/xml/#d0e804
     */
    public static boolean isValidXmlElementName(CharSequence str) {
        if (str.length() == 0) {
            return false;
        }

        final int firstchar = Character.codePointAt(str, 0);
        if (!isNameStartChar(firstchar)) {
            return false;
        }

        for (int i = Character.charCount(firstchar); i < str.length();) {
            final int nextchar = Character.codePointAt(str, i);

            if (!isXMLNameChar(nextchar)) {
                return false;
            }

            i += Character.charCount(nextchar);
        }

        return true;
    }


    // NameStartChar      ::=      ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] |
    //                             [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] |
    //                             [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] |
    //                             [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
    //                             [#x10000-#xEFFFF]
    private static boolean isNameStartChar(int namestartchar) {
        return namestartchar == ':' ||
            (namestartchar >= 'A' && namestartchar <= 'Z') ||
            namestartchar == '_' ||
            (namestartchar >= 'a' && namestartchar <= 'z') ||
            (namestartchar >= 0xC0 && namestartchar <= 0xD6) ||
            (namestartchar >= 0xD8 && namestartchar <= 0xF6) ||
            (namestartchar >= 0xF8 && namestartchar <= 0x2FF) ||
            (namestartchar >= 0x370 && namestartchar <= 0x37D) ||
            (namestartchar >= 0x37F && namestartchar <= 0x1FFF) ||
            (namestartchar >= 0x200C && namestartchar <= 0x200D) ||
            (namestartchar >= 0x2070 && namestartchar <= 0x218F) ||
            (namestartchar >= 0x2C00 && namestartchar <= 0x2FEF) ||
            (namestartchar >= 0x3001 && namestartchar <= 0xD7FF) ||
            (namestartchar >= 0xF900 && namestartchar <= 0xFDCF) ||
            (namestartchar >= 0xFDF0 && namestartchar <= 0xFFFD) ||
            (namestartchar >= 0x10000 && namestartchar <= 0xEFFFF);
    }


    // NameChar       ::=      NameStartChar | "-" | "." | [0-9] |
    //                         #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
    private static boolean isXMLNameChar(int namechar) {
        return isNameStartChar(namechar) ||
            namechar == '-' ||
            namechar == '.' ||
            (namechar >= '0' && namechar <= '9') ||
            namechar == 0xB7 ||
            (namechar >= 0x0300 && namechar <= 0x036F) ||
            (namechar >= 0x203F && namechar <= 0x2040);
    }
}
