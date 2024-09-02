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
import org.apache.kafka.connect.data.Struct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class XmlConverterSinkTest {

    private final XmlConverter converter = new XmlConverter();

    private final String currentTestCase;
    private final boolean ambiguousWithSchema;
    private final boolean ambiguousWithoutSchema;
    private final boolean skipNoSchema;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false, false, false },
            { "001", false, false, false },
            { "002", false, false, false },
            { "003", false, true, false },  // ambiguous float vs double requires a schema
            { "004", true, false, false },  // list vs struct containing a list
            { "005", true, false, false },  // list vs struct containing a list
            { "006", false, false, false },
            { "007", false, true, false },
            { "008", false, false, false },
            { "009", false, false, false },
            { "010", true, true, false },   // maps
            { "011", true, true, false },   // maps
            { "012", true, true, false },   // maps
            { "013", true, false, false },
            { "014", true, true, false },   // maps
            { "015", true, true, false },
            { "016", true, true, false },   // maps
            { "017", true, true, false },   // maps
            { "018", true, true, false },   // maps
            { "019", true, true, false },   // maps
            { "020", true, true, false },   // maps
            { "021", true, true, false },   // maps
            { "022", true, true, false },   // maps
            { "023", true, true, false },   // maps
            { "024", true, true, false },
            { "025", true, true, false },
//            { "026", true, true, false },
            { "027", true, true, false },
            { "028", true, true, false },
            { "029", true, true, true },
            { "030", true, true, true },
            { "031", true, true, true },
            { "032", true, true, true },
            { "033", true, true, true },
            { "034", true, true, true },
            { "035", true, true, true },
            { "036", true, true, true },
            { "037", true, true, false },
            { "038", true, true, true },
//            { "039", true, true, true },
            { "040", true, true, true },
            { "047", false, true, false },
            { "052", false, false, false },
            { "053", false, false, false },
            { "054", false, true, false }
        });
    }

    public XmlConverterSinkTest(String testCase, boolean withSchema, boolean withoutSchema, boolean skipNoSchema) {
        this.currentTestCase = testCase;
        this.ambiguousWithSchema = withSchema;
        this.ambiguousWithoutSchema = withoutSchema;
        this.skipNoSchema = skipNoSchema;
    }

    @Test
    public void withoutSchema() {
        if (skipNoSchema) {
            return;
        }

        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final byte[] input = ByteGenerators.getXml(currentTestCase);
        final SchemaAndValue output = converter.toConnectData("TOPIC", input);
        assertNull(output.schema());

        final Map<?, ?> expected;
        if (ambiguousWithoutSchema) {
            expected = MapGenerators.generic(currentTestCase);
        }
        else {
            expected = MapGenerators.get(currentTestCase);
        }

        Comparisons.compare(expected, (Map<?, ?>) output.value());
        assertEquals(expected, output.value());
    }


    @Test
    public void withSchema() {
        converter.configure(ConfigGenerators.withSchemaProps(currentTestCase), false);

        final byte[] input = ByteGenerators.getXml(currentTestCase);

        final SchemaAndValue output = converter.toConnectData("TOPIC", input);

        final SchemaAndValue expected;
        if (ambiguousWithSchema) {
            expected = StructGenerators.generic(currentTestCase);
        }
        else {
            expected = StructGenerators.get(currentTestCase);
        }

        Comparisons.compareSchema(expected.schema(), output.schema());
        assertEquals(expected.schema(), output.schema());
        Comparisons.compareStruct((Struct)expected.value(), (Struct)output.value());

        if (!"047".equals(currentTestCase)) {
            assertEquals(expected, output);
        }
    }
}
