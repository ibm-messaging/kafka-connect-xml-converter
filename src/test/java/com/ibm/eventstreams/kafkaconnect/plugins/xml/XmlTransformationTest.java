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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.Test;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Input.Builder;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.RecordGenerators;

public class XmlTransformationTest {

    private final XmlTransformation<SourceRecord> transformer = new XmlTransformation<>();

    @Test
    public void config() {
        final ConfigDef configSpec = transformer.config();
        assertTrue(configSpec.configKeys().containsKey(XmlPluginsConfig.XML_ROOT_ELEMENT_NAME_CONFIG));
    }

    @Test
    public void unsupported() {
        final NotImplementedException thrown = assertThrows(NotImplementedException.class, () -> {
            transformer.configure(ConfigGenerators.defaultRootNoSchemasProps());

            final SourceRecord test = RecordGenerators.integer();
            transformer.apply(test);
        });
        assertEquals("Unsupported value type java.lang.Integer", thrown.getMessage());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void customRootStringNoSchema() {
        transformer.configure(ConfigGenerators.customRootNoSchemasProps("mydoc"));

        final SourceRecord input = RecordGenerators.string("000-customroot");
        final SourceRecord output = transformer.apply(input);

        final Map<?, ?> expected = MapGenerators.get("000");

        assertNull(output.valueSchema());

        Comparisons.compare(expected, (Map<String, Object>) output.value());
        assertEquals(expected, output.value());
    }

    @Test
    public void reuse() {
        transformer.configure(ConfigGenerators.defaultRootNoSchemasProps());

        for (final String testcase : new String[] { "002", "003", "004" }) {

            final SourceRecord inputStruct = RecordGenerators.struct(testcase);
            final SourceRecord outputStruct = transformer.apply(inputStruct);
            final String outputStructXml = (String) outputStruct.value();

            final SourceRecord inputMap = RecordGenerators.map(testcase);
            final SourceRecord outputMap = transformer.apply(inputMap);
            final String outputMapXml = (String) outputMap.value();

            final byte[] expected = ByteGenerators.getXml(testcase);
            final Builder expectedXml = Input.fromByteArray(expected);

            assertThat(
                    Input.fromString(outputStructXml),
                    isIdenticalTo(expectedXml)
                        .ignoreWhitespace()
                        .ignoreComments());

            assertThat(
                    Input.fromString(outputMapXml),
                    isIdenticalTo(expectedXml)
                        .ignoreWhitespace()
                        .ignoreComments());

        }
    }
}
