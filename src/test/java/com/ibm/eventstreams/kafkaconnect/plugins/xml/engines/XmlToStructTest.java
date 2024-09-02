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

import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class XmlToStructTest {

    private final String currentTestCase;
    private final boolean ambiguous;
    private final boolean skip;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false, false },
            { "001", false, false },
            { "002", false, false },
            { "003", false, false },
            { "004", true, false }, // array/struct
            { "005", true, false }, // array/struct
            { "006", false, false },
            { "007", false, false },
            { "008", false, false },
            { "009", false, false },
            { "010", true, false }, // map
            { "011", true, false }, // map
            { "012", true, false }, // map
            { "013", true, false }, // map
            { "014", true, false }, // map
            { "015", true, false }, // map
            { "016", true, false }, // map
            { "017", true, false }, // map
            { "018", true, false }, // map
            { "019", true, false },
            { "020", true, false },
            { "021", true, false },
            { "022", true, false },
            { "023", true, false },
            { "024", true, false },
            { "025", true, false },
            { "026", true, true },
            { "027", false, false },
            { "028", true, false },
            { "029", true, false },
            { "030", true, false },
            { "031", true, false },
            { "032", true, false },
            { "033", true, false },
            { "034", true, false },
            { "035", true, false },
            { "036", true, false },
            { "037", false, false },
            { "038", true, false },
            { "039", false, true },
            { "040", true, false },
            { "041", true, true },
            { "042", true, true },
            { "043", true, false },
            { "047", false, false },
            { "052", false, false },
            { "053", false, false },
            { "054", true, false }
        });
    }

    public XmlToStructTest(String testCase, boolean ambiguous, boolean skip) {
        this.currentTestCase = testCase;
        this.ambiguous = ambiguous;
        this.skip = skip;
    }

    @Test
    public void runTestCase() {
        if (skip) {
            return;
        }

        final XmlPluginsConfig config = ConfigGenerators.withSchema(currentTestCase);
        final XmlBytesToStruct converter = new XmlBytesToStruct(config);

        final byte[] input = ByteGenerators.getXml(currentTestCase);
        final SchemaAndValue output = converter.convert(input);

        final SchemaAndValue expected;
        if (ambiguous) {
            expected = StructGenerators.generic(currentTestCase);
        }
        else {
            expected = StructGenerators.get(currentTestCase);
        }

        Comparisons.compareSchema(expected.schema(), output.schema());
        Comparisons.compareStruct((Struct)expected.value(), (Struct) output.value());
        assertEquals(expected.schema(), output.schema());

        if (!"047".equals(currentTestCase)) {
            assertEquals(expected.value(), output.value());
            assertEquals(expected, output);
        }
    }
}
