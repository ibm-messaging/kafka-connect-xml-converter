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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

@RunWith(Parameterized.class)
public class XStreamMapConverterTest {

    private final String currentTestCase;
    private final boolean ambiguous;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false },
            { "001", false },
            { "002", false },
            { "003", true },
            { "004", false },
            { "005", false },
            { "006", false },
            { "007", true },
            { "008", false },
            { "009", false },
            { "010", true },
            { "011", true },
            { "012", true },
            { "013", false },
            { "014", true },
            { "015", true },
            { "016", true },
            { "017", true },
            { "018", true },
            { "019", true },
            { "020", true },
            { "021", true },
            { "022", true },
            { "023", true },
            { "024", true },
            { "025", true },
            // { "026", false }
            { "027", true },
            { "028", true },
//            { "029", true },
//            { "030", true },
//            { "031", true },
//            { "032", true },
//            { "033", true },
//            { "034", true },
//            { "035", true },
//            { "036", true },
            { "037", true }
//            { "038", true },
//            { "039", true },
//            { "040", true },
        });
    }

    public XStreamMapConverterTest(String testCase, boolean ambiguous) {
        this.currentTestCase = testCase;
        this.ambiguous = ambiguous;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void runTestCase() {
        final String ROOT = "root";
        final XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
        xstream.registerConverter(new XStreamMapConverter());
        xstream.alias(ROOT, Map.class);

        final File input = FileGenerators.getXml(currentTestCase);

        final Map<String, Object> output = (Map<String, Object>) xstream.fromXML(input);
        final Map<String, Object> value = (Map<String, Object>) output.get(ROOT);

        final Map<?, ?> expected;
        if (ambiguous) {
            expected = MapGenerators.generic(currentTestCase);
        }
        else {
            expected = MapGenerators.get(currentTestCase);
        }

        Comparisons.compare(expected, value);
        assertEquals(expected, value);
    }
}
