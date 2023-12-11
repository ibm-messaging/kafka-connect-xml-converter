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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.xml.transform.Source;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xmlunit.builder.Input;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;

@RunWith(Parameterized.class)
public class CollectionToXmlTest {

    private final String currentTestCase;
    private final boolean ambiguousMap;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false },
            { "001", true },    // uses attributes
            { "002", false },
            { "003", false },
            { "004", false },
            { "005", false },
            { "006", true },    // uses attributes
            { "007", true },
            { "008", false },
            { "009", false },
            { "010", true },   // ambiguous use of maps
            { "011", true },   // ambiguous use of maps
            { "012", true },   // ambiguous use of maps
            { "013", true },   // ambiguous use of maps
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
            { "024", false },
            { "025", true },
            { "026", false },
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
//            { "037", false },
            { "038", true },
//            { "039", false },
//            { "040", false }
            { "043", true },
            { "045", false },
            { "046", false }
        });
    }

    public CollectionToXmlTest(String testCase, boolean ambiguousMap) {
        this.currentTestCase = testCase;
        this.ambiguousMap = ambiguousMap;
    }

    @Test
    public void test() {
        final XmlPluginsConfig config = ConfigGenerators.defaultRootNoSchemas();
        final CollectionToXmlBytes converter = new CollectionToXmlBytes(config);

        final Map<?, ?> input = MapGenerators.get(currentTestCase);
        final byte[] output = converter.convert(null, input);

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
}
