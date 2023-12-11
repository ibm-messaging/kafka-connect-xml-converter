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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.ConverterConfig;
import org.apache.kafka.connect.storage.ConverterType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.RecordGenerators;

@RunWith(Parameterized.class)
public class JsonConverterSourceTest {

    private final String currentTestCase;
    private final boolean runWithSchema;
    private final boolean runWithoutSchema;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", true, true },
            { "001", true, true },
            { "002", true, true },
            { "003", true, true },
            { "004", true, true },
            { "005", true, true },
            { "006", true, true },
            { "008", true, true },
            { "009", true, true },
            { "010", true, true },
            { "025", true, true },
            { "027", true, true }
        });
    }

    public JsonConverterSourceTest(String testCase, boolean withSchema, boolean withoutSchema) {
        this.currentTestCase = testCase;
        this.runWithSchema = withSchema;
        this.runWithoutSchema = withoutSchema;
    }


    @Test
    public void withoutSchema() throws IOException {
        if (!runWithoutSchema) {
            return;
        }

        final JsonConverter converter = new JsonConverter();
        final Map<String, String> config = new HashMap<>();
        config.put(ConverterConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        config.put(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, "false");
        converter.configure(config, false);

        final byte[] output = converter.fromConnectData("TOPIC", null, MapGenerators.get(currentTestCase));

        converter.close();

        final byte[] expected = ByteGenerators.getJson(currentTestCase);

        Comparisons.compareJson(expected, output);
    }


    @Test
    public void withSchema() throws IOException {
        if (!runWithSchema) {
            return;
        }

        final JsonConverter converter = new JsonConverter();
        final Map<String, String> config = new HashMap<>();
        config.put(ConverterConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        config.put(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, "true");
        converter.configure(config, false);

        final SourceRecord input = RecordGenerators.struct(currentTestCase);

        final byte[] output = converter.fromConnectData(input.topic(), input.valueSchema(), input.value());

        converter.close();

        final byte[] expected = ByteGenerators.getCombinedJson(currentTestCase);

        Comparisons.compareJson(expected, output);
    }
}
