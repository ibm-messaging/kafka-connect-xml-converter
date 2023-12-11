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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.storage.ConverterConfig;
import org.apache.kafka.connect.storage.ConverterType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class JsonConverterSinkTest {

    private final String currentTestCase;
    private final boolean runWithSchema;
    private final boolean runWithoutSchema;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", true, false },
            { "001", true, false },
            { "002", true, false },
            { "003", true, false },
            { "004", true, true },
            { "005", true, false },
            { "006", true, true },
            { "007", false, false },
            { "008", true, false },
            { "009", true, true },
            { "010", false, false },
            { "025", false, false }
        });
    }

    public JsonConverterSinkTest(String testCase, boolean withSchema, boolean withoutSchema) {
        this.currentTestCase = testCase;
        this.runWithSchema = withSchema;
        this.runWithoutSchema = withoutSchema;
    }


    @Test
    public void withoutSchema() {
        if (!runWithoutSchema) {
            return;
        }

        final byte[] input = ByteGenerators.getJson(currentTestCase);
        final SchemaAndValue output = convertData(input, false);

        final Map<?, ?> expected = MapGenerators.get(currentTestCase);

        assertNull(output.schema());
        assertEquals(expected, output.value());
    }


    @Test
    public void withSchema() {
        if (!runWithSchema) {
            return;
        }

        final byte[] input = ByteGenerators.getCombinedJson(currentTestCase);
        final SchemaAndValue output = convertData(input, true);

        final SchemaAndValue expected = StructGenerators.get(currentTestCase);

        Comparisons.compareSchema(expected.schema(), output.schema());

        assertEquals(expected, output);
    }


    private SchemaAndValue convertData(byte[] data, boolean useSchemas) {
        final JsonConverter converter = new JsonConverter();

        final Map<String, String> config = new HashMap<>();
        config.put(ConverterConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        config.put(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, Boolean.toString(useSchemas));
        converter.configure(config, false);

        final SchemaAndValue value = converter.toConnectData("TOPIC", data);
        converter.close();

        return value;
    }
}
