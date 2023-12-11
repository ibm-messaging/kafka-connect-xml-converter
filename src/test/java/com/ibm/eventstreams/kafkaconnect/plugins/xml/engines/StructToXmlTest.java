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

import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xmlunit.builder.Input;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

@RunWith(Parameterized.class)
public class StructToXmlTest {

    private final String currentTestCase;
    private final boolean runWithSchema;
    private final boolean runWithoutSchema;


    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", true, true },
            { "001", false, false },    // uses attributes
            { "002", true, true },
            { "003", true, true },
            { "004", true, true },
            { "005", true, true },
            { "006", false, false },    // uses attributes
            { "007", false, true },     // uses ambiguous types
            { "008", false, false },
            { "009", true, true },
            { "010", true, true },
            { "011", true, true },
            { "012", true, true },
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
            { "024", false, false },
            { "025", true, false },
            { "026", false, false },
            { "027", true, true },
            { "028", true, true },
            { "029", false, false },
            { "030", false, false },
            { "031", false, false },
            { "032", false, false },
            { "033", false, false },
            { "034", false, false },
            { "035", false, false },
            { "036", false, false },
            { "037", false, false },
            { "038", false, false },
            // { "039", true, true },
            { "040", false, false },
            { "043", true, true },
            { "046", true, true },
            { "048", true, true },
            { "049", false, false }
        });
    }


    public StructToXmlTest(String testCase, boolean withSchema, boolean withoutSchema) {
        this.currentTestCase = testCase;
        this.runWithSchema = withSchema;
        this.runWithoutSchema = withoutSchema;
    }


    @Test
    public void runNoSchemasWithoutErrors() {
        final XmlPluginsConfig config = ConfigGenerators.defaultRootNoSchemas();
        final StructToXmlBytes converter = new StructToXmlBytes(config);
        final SchemaAndValue input = StructGenerators.get(currentTestCase);
        if (input.value() instanceof Struct) {
            converter.convert(input.schema(), (Struct) input.value());
        }
        else if (input.value() instanceof Collection) {
            converter.convert(input.schema(), (Collection<?>) input.value());
        }
        else if (input.value() instanceof Map) {
            converter.convert(input.schema(), (Map<?, ?>) input.value());
        }
    }


    @Test
    public void noSchemas() {
        if (!runWithoutSchema) {
            return;
        }

        final XmlPluginsConfig config = ConfigGenerators.defaultRootNoSchemas();
        final StructToXmlBytes converter = new StructToXmlBytes(config);

        final SchemaAndValue input = StructGenerators.get(currentTestCase);

        byte[] output = new byte[0];
        if (input.value() instanceof Struct) {
            output = converter.convert(input.schema(), (Struct) input.value());
        }
        else if (input.value() instanceof Collection) {
            output = converter.convert(input.schema(), (Collection<?>) input.value());
        }
        else if (input.value() instanceof Map) {
            output = converter.convert(input.schema(), (Map<?, ?>) input.value());
        }

        final Source expected = Input.fromFile(FileGenerators.getXml(currentTestCase)).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }


    @Test
    public void runSchemasWithoutErrors() {
        final XmlPluginsConfig config = ConfigGenerators.withSchema(currentTestCase);
        final StructToXmlBytes converter = new StructToXmlBytes(config);
        final SchemaAndValue input = StructGenerators.get(currentTestCase);
        if (input.value() instanceof Struct) {
            converter.convert(input.schema(), (Struct) input.value());
        }
        else if (input.value() instanceof Collection) {
            converter.convert(input.schema(), (Collection<?>) input.value());
        }
        else if (input.value() instanceof Map) {
            converter.convert(input.schema(), (Map<?, ?>) input.value());
        }
    }


    @Test
    public void separateSchemas() {
        if (!runWithSchema) {
            return;
        }

        final XmlPluginsConfig config = ConfigGenerators.withSchema(currentTestCase);
        final StructToXmlBytes converter = new StructToXmlBytes(config);

        final SchemaAndValue input = StructGenerators.get(currentTestCase);

        byte[] output = new byte[0];
        if (input.value() instanceof Struct) {
            output = converter.convert(input.schema(), (Struct) input.value());
        }
        else if (input.value() instanceof Collection) {
            output = converter.convert(input.schema(), (Collection<?>) input.value());
        }
        else if (input.value() instanceof Map) {
            output = converter.convert(input.schema(), (Map<?, ?>) input.value());
        }

        final Source expected = Input.fromFile(FileGenerators.getCombined(currentTestCase)).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }
}
