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

import org.apache.kafka.connect.source.SourceRecord;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Input.Builder;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.RecordGenerators;

@RunWith(Parameterized.class)
public class XmlTransformationStructTest {

    private final XmlTransformation<SourceRecord> transformer = new XmlTransformation<>();

    private final String currentTestCase;
    private final boolean ambiguousStruct;


    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false },
            { "001", true  },    // uses attributes
            { "002", false },
            { "003", false },
            { "004", false },
            { "005", false },
            { "006", true  },    // uses attributes
            { "007", false },
            { "008", true  },    // optional elements
            { "009", false },
            { "010", false },
            { "011", false },
            { "012", false },
            { "013", false },
            { "014", false },
            { "015", false },
            { "016", false },
            { "017", false },
            { "018", true },
            { "019", true },
            { "020", true },
            { "021", true },
            { "022", true },
            { "023", true },
            { "024", true },
            { "025", true },
            { "026", false },
            { "027", false },
            { "028", false },
            { "029", true },
            { "030", true },
            { "031", true },
            { "032", true },
            { "033", true },
            { "034", true },
            { "035", true },
            { "036", true },
            { "037", true },
            { "038", true },
            { "039", true },
            { "040", true },
            { "041", false },
            { "042", false },
            { "043", false },
            { "044", false },
            { "046", false },
            { "048", false },
            { "049", false }
        });
    }

    public XmlTransformationStructTest(String testCase, boolean ambiguousStruct) {
        this.currentTestCase = testCase;
        this.ambiguousStruct = ambiguousStruct;
    }

    @After
    public void cleanup() {
        transformer.close();
    }

    @Test
    public void struct() {
        transformer.configure(ConfigGenerators.defaultRootNoSchemasProps());

        final SourceRecord input = RecordGenerators.struct(currentTestCase);
        final SourceRecord output = transformer.apply(input);
        final String outputXml = (String) output.value();

        final byte[] expected;
        if (ambiguousStruct) {
            expected = ByteGenerators.getGenericXml(currentTestCase);
        }
        else {
            expected = ByteGenerators.getXml(currentTestCase);
        }

        final Builder expectedXml = Input.fromByteArray(expected);

        assertThat(
                Input.fromString(outputXml),
                isIdenticalTo(expectedXml)
                    .ignoreWhitespace()
                    .ignoreComments());
    }
}
