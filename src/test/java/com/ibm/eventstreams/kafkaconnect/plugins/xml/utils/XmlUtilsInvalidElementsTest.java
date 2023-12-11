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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class XmlUtilsInvalidElementsTest {

    private final String currentTestCase;

    @Parameterized.Parameters
    public static String[] invalidXmlElementNames() {
        return new String[] {
            "This,That",
            " HELLO ",
            "Hello World",
            "Hello!",
            "!Hello",
            "123",
            "Hello&World",
            "&HelloWorld",
            "12$",
            "$Dollars",
            "A@B",
            "@AB",
            "This;Is;Invalid",
            ";ThisInvalid",
            "",
            "⁁",
            "Unusual⁁",
            "†",
            "incorrect-†-char",
            "⁇",
            "question⁇",
            "×yz",
            "one÷two",
            "disallowed‾",
            "mile‰",
            new String(Character.toChars(0xFFFF)),
            new String(Character.toChars(0xFDDD))
        };
    }

    public XmlUtilsInvalidElementsTest(String test) {
        this.currentTestCase = test;
    }


    @Test
    public void invalidXmlElementName() {
        assertFalse(currentTestCase, XmlUtils.isValidXmlElementName(currentTestCase));
    }
}
