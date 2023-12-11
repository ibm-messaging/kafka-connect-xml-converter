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

import java.util.Collection;
import java.util.List;

import javax.xml.transform.Source;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xmlunit.builder.Input;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ListGenerators;

@RunWith(Parameterized.class)
public class ListToXmlTest {

    private final String currentTestCase;

    @Parameterized.Parameters
    public static Collection<String> testCases() {
        return List.of("004", "005", "050");
    }

    public ListToXmlTest(String testCase) {
        this.currentTestCase = testCase;
    }

    @Test
    public void runTestCase() {
        final XmlPluginsConfig config = ConfigGenerators.defaultRootNoSchemas();
        final CollectionToXmlBytes converter = new CollectionToXmlBytes(config);

        final List<Object> input = ListGenerators.get(currentTestCase);
        final byte[] output = converter.convert(null, input);

        final Source expected = Input.fromFile(FileGenerators.getXml(currentTestCase)).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }
}
