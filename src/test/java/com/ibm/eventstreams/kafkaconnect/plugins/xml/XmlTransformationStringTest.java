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
import java.util.Map;

import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.RecordGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class XmlTransformationStringTest {

    private final XmlTransformation<SourceRecord> transformer = new XmlTransformation<>();

    private final String currentTestCase;
    private final boolean ambiguousWithSchema;
    private final boolean ambiguousWithoutSchema;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false, false },
            { "001", false, false },
            { "002", false, false },
            { "003", false, true },  // ambiguous float vs double requires a schema
            { "004", true, false },  // list vs struct containing a list
            { "005", true, false },  // list vs struct containing a list
            { "006", false, false },
            { "007", false, true },
            { "008", false, false },
            { "009", false, false },
            { "010", true, true }, // map structs
            { "011", true, true }, // map structs
            { "012", true, true }, // map structs
            { "013", true, true },
            { "014", true, true },
            { "015", true, true },
            { "016", true, true },
            { "017", true, true },
            { "018", true, true },
            { "019", true, true },
            { "020", true, true },
            { "021", true, true },
            { "022", true, true },
            { "023", true, true },
            { "024", true, true },
            { "025", true, true },
            { "027", false, true },
            { "028", true, true },
            { "037", false, false }
        });
    }

    public XmlTransformationStringTest(String testCase, boolean withSchema, boolean withoutSchema) {
        this.currentTestCase = testCase;
        this.ambiguousWithSchema = withSchema;
        this.ambiguousWithoutSchema = withoutSchema;
    }

    @After
    public void cleanup() {
        transformer.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stringNoSchema() {
        transformer.configure(ConfigGenerators.defaultRootNoSchemasProps());

        final SourceRecord input = RecordGenerators.string(currentTestCase);
        final SourceRecord output = transformer.apply(input);

        final Map<?, ?> expected = ambiguousWithoutSchema ? MapGenerators.generic(currentTestCase) : MapGenerators.get(currentTestCase);

        assertNull(output.valueSchema());

        Comparisons.compare(expected, (Map<String, Object>) output.value());
        assertEquals(expected, output.value());
    }


    @Test
    public void stringWithSchema() {
        transformer.configure(ConfigGenerators.withSchemaProps(currentTestCase));

        final SourceRecord input = RecordGenerators.string(currentTestCase);
        final SourceRecord output = transformer.apply(input);

        final SchemaAndValue expected = ambiguousWithSchema ? StructGenerators.generic(currentTestCase) : StructGenerators.get(currentTestCase);

        Comparisons.compareSchema(expected.schema(), output.valueSchema());
        assertEquals(expected.schema(), output.valueSchema());
        assertEquals(expected.value(), output.value());
    }


    @Test
    @SuppressWarnings("unchecked")
    public void bytesNoSchema() {
        transformer.configure(ConfigGenerators.defaultRootNoSchemasProps());

        final SourceRecord input = RecordGenerators.bytes(currentTestCase);
        final SourceRecord output = transformer.apply(input);

        final Map<?, ?> expected = ambiguousWithoutSchema ? MapGenerators.generic(currentTestCase) : MapGenerators.get(currentTestCase);

        assertNull(output.valueSchema());

        Comparisons.compare(expected, (Map<String, Object>) output.value());
        assertEquals(expected, output.value());
    }


    @Test
    public void bytesWithSchema() {
        transformer.configure(ConfigGenerators.withSchemaProps(currentTestCase));

        final SourceRecord input = RecordGenerators.bytes(currentTestCase);
        final SourceRecord output = transformer.apply(input);

        final SchemaAndValue expected = ambiguousWithSchema ? StructGenerators.generic(currentTestCase) : StructGenerators.get(currentTestCase);

        assertEquals(expected.schema(), output.valueSchema());
        assertEquals(expected.value(), output.value());
    }
}
