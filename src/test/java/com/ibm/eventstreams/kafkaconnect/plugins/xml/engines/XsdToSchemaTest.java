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

import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.connect.data.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class XsdToSchemaTest {

    private final String currentTestCase;
    private final boolean ambiguous;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false },
            { "001", false },
            { "002", false },
            { "003", false },
            { "004", true }, // array/struct
            { "005", true }, // array/struct
            { "006", false },
            { "007", false },
            { "008", false },
            { "009", false },
            { "010", true }, // map
            { "011", true }, // map
            { "012", true }, // map
            { "013", true }, // map
            { "014", true }, // map
            { "015", true }, // map
            { "016", true }, // map
            { "017", true },
            { "018", true },
            { "019", true },
            { "020", true },
            { "021", true },
            { "022", true },
            { "023", true },
            { "024", false },
            { "025", true },
            { "026", true },
            { "027", false },
            { "028", true },
            { "029", true },
            { "030", true },
            { "031", true },
            { "032", true },
            { "033", true },
            { "034", true },
            { "035", true },
            { "036", true },
            { "037", false },
            { "038", true },
            { "040", true },
            { "047", false }
        });
    }

    public XsdToSchemaTest(String testCase, boolean ambiguous) {
        this.currentTestCase = testCase;
        this.ambiguous = ambiguous;
    }

    @Test
    public void runTestCase() {
        final XmlPluginsConfig config = ConfigGenerators.withSchema(currentTestCase);
        final XsdToSchema generator = new XsdToSchema(config);
        final Schema converted = generator.getSchema();

        final Schema expected;
        if (ambiguous) {
            expected = StructGenerators.generic(currentTestCase).schema();
        }
        else {
            expected = StructGenerators.get(currentTestCase).schema();
        }

        Comparisons.compareSchema(expected, converted);
        assertEquals(expected, converted);
    }
}
