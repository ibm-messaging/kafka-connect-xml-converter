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
package com.ibm.eventstreams.kafkaconnect.plugins.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.xml.transform.Source;

import org.apache.kafka.connect.data.SchemaAndValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xmlunit.builder.Input;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class XmlConverterSourceTest {

    private final String currentTestCase;
    private final boolean ambiguousMap;
    private final boolean ambiguousStruct;

    private final XmlConverter converter = new XmlConverter();



    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false, false },
            { "001", true, true },    // uses attributes
            { "002", false, false },
            { "003", false, false },
            { "004", false, false },
            { "005", false, false },
            { "006", true, true },    // uses attributes
            { "007", false, false },
            { "008", false, false },
            { "009", false, false },
            { "010", true, false },   // ambiguous use of maps
            { "011", true, false },   // ambiguous use of maps
            { "012", true, false },   // ambiguous use of maps
            { "013", false, false },
            { "014", true, false },
            { "015", false, false },
            { "016", false, false },
            { "017", false, false },
            { "018", true, true },
            { "019", true, true },
            { "020", true, true },
            { "021", true, true },
            { "022", true, true },
            { "023", true, true },
            { "024", false, true },
            { "025", true, true },
            { "026", false, false },
            { "027", false, false },
            { "028", true, false },
            { "029", true, true },
            { "030", true, true },
            { "031", true, true },
            { "032", true, true },
            { "033", true, true },
            { "034", true, true },
            { "035", true, true },
            { "036", true, true },
//            { "037", true, true },
            { "038", true, true },
//            { "039", true, true },
//            { "040", true, true }
//            { "043", true, true },
            { "044", false, false },
            { "049", false, false },
            { "055", false, false }
        });
    }

    public XmlConverterSourceTest(String testCase, boolean ambiguousMap, boolean ambiguousStruct) {
        this.currentTestCase = testCase;
        this.ambiguousMap = ambiguousMap;
        this.ambiguousStruct = ambiguousStruct;
    }


    @Test
    public void map() {
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final Map<?, ?> input = MapGenerators.get(currentTestCase);
        final byte[] output = converter.fromConnectData("TOPIC", null, input);

        final Source expected;
        if (ambiguousMap) {
            expected = Input.fromFile(FileGenerators.getGenericXml(currentTestCase)).build();
        }
        else {
            expected = Input.fromFile(FileGenerators.getXml(currentTestCase)).build();
        }

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }


    @Test
    public void struct() {
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final SchemaAndValue input = StructGenerators.get(currentTestCase);
        final byte[] output = converter.fromConnectData("TOPIC", input.schema(), input.value());

        final Source expected;
        if (ambiguousStruct) {
            expected = Input.fromFile(FileGenerators.getGenericXml(currentTestCase)).build();
        }
        else {
            expected = Input.fromFile(FileGenerators.getXml(currentTestCase)).build();
        }

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }
}