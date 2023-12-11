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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class XmlUtilsValidElementsTest {

    private final String currentTestCase;

    @Parameterized.Parameters
    public static String[] validXmlElementNames() {
        return new String[] {
            "hello",
            "hello-world",
            "hello.world",
            "hello123",
            "hello_world",
            "_hello_world",
            "test·here",
            "verifyӰ",
            "checkص",
            "validaऑ",
            "element-ৱ",
            "nameਕ",
            "lengthઅ",
            "xmlଳ",
            "மinked",
            "validateణ",
            "xmlಱ",
            "innerഅcheck",
            "ච",
            "testingඎ",
            "multipleቁ",
            "allᐃvalid",
            "ᘳurl",
            "dirᠣ"
        };
    }

    public XmlUtilsValidElementsTest(String test) {
        this.currentTestCase = test;
    }


    @Test
    public void validXmlElementName() {
        assertTrue(currentTestCase, XmlUtils.isValidXmlElementName(currentTestCase));
    }
}
