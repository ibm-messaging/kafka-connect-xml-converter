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

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.Test;
import org.xmlunit.builder.Input;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.XsdToSchema;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ByteGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.Comparisons;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ConfigGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.ListGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.MapGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.RecordGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;

public class XmlConverterTest {

    @Test
    public void config() {
        final XmlConverter converter = new XmlConverter();
        final ConfigDef configSpec = converter.config();
        assertTrue(configSpec.configKeys().containsKey(XmlPluginsConfig.XML_ROOT_ELEMENT_NAME_CONFIG));
    }

    @Test
    public void unsupported() {
        final NotImplementedException thrown = assertThrows(NotImplementedException.class, () -> {
            final XmlConverter converter = new XmlConverter();
            converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

            final SourceRecord test = RecordGenerators.integer();
            converter.fromConnectData(test.topic(), test.valueSchema(), test.value());
        });
        assertEquals("Unsupported value type java.lang.Integer", thrown.getMessage());
    }

    @Test
    public void nullRecord() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final SourceRecord test = RecordGenerators.nullString();
        final byte[] output = converter.fromConnectData(test.topic(), test.valueSchema(), test.value());

        assertEquals(0, output.length);
    }

    @Test
    public void nullData() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final SchemaAndValue output = converter.toConnectData("TOPIC", null);

        assertEquals(SchemaAndValue.NULL, output);
    }

    @Test
    public void emptyData() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final SchemaAndValue output = converter.toConnectData("TOPIC", new byte[0]);

        assertEquals(SchemaAndValue.NULL, output);
    }

    @Test
    public void invalidXml() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final byte[] input = ByteGenerators.getJson("001");

        final SerializationException thrown = assertThrows(SerializationException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertEquals("Failed to deserialize message data", thrown.getMessage());
    }

    @Test
    public void missingSchemaFile() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.withSchemaProps("000-no-file"), false);

        final byte[] input = ByteGenerators.getXml("000");

        final SerializationException thrown = assertThrows(SerializationException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertTrue(thrown.getCause() instanceof FileNotFoundException);
    }


    @Test
    public void missingRoot() {
        final XmlConverter converter = new XmlConverter();
        final Map<String, String> props = ConfigGenerators.defaultRootNoSchemasProps();
        props.put(XmlPluginsConfig.XML_ROOT_ELEMENT_NAME_CONFIG, "doc");
        converter.configure(props, false);

        final byte[] input = ByteGenerators.getXml("001");

        final SerializationException thrown = assertThrows(SerializationException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertEquals("Expected root element 'doc' not found", thrown.getMessage());
    }

    @Test
    public void missingRootSchema() {
        final XmlConverter converter = new XmlConverter();
        final Map<String, String> props = ConfigGenerators.withSchemaProps("001");
        props.put(XmlPluginsConfig.XML_ROOT_ELEMENT_NAME_CONFIG, "doc");
        converter.configure(props, false);

        final byte[] input = ByteGenerators.getXml("001");

        final SerializationException thrown = assertThrows(SerializationException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertEquals("Expected root element 'doc' not found", thrown.getMessage());
    }

    @Test
    public void unusedMapSchema() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.withSchemaProps("012"), false);

        final Map<?, ?> input = MapGenerators.get("012");
        final byte[] output = converter.fromConnectData("TOPIC", null, input);

        final Source expected = Input.fromFile(FileGenerators.getGenericXml("012")).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }

    @Test
    public void unusedListSchema() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.withSchemaProps("004"), false);

        final List<?> input = ListGenerators.get("004");
        final byte[] output = converter.fromConnectData("TOPIC", null, input);

        final Source expected = Input.fromFile(FileGenerators.getGenericXml("004")).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }

    @Test
    public void mismatchingSchema() {
        final XmlConverter converter = new XmlConverter();
        final Map<String, String> props = ConfigGenerators.withSchemaProps("001");
        converter.configure(props, false);

        final byte[] input = ByteGenerators.getXml("011");

        final SerializationException thrown = assertThrows(SerializationException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertEquals("Schema does not match message data", thrown.getMessage());
    }

    @Test
    public void customRootNoSchema() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.customRootNoSchemasProps("mydoc"), false);

        final byte[] input = ByteGenerators.getXml("000-customroot");

        final SchemaAndValue output = converter.toConnectData("TOPIC", input);

        assertNull(output.schema());

        final Map<?, ?> expected = MapGenerators.get("000");
        Comparisons.compare(expected, (Map<?, ?>) output.value());
        assertEquals(expected, output.value());
    }

    @Test
    public void customRootSchema() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.customRootWithSchemaProps("000-customroot", "mydoc"), false);

        final byte[] input = ByteGenerators.getXml("000-customroot");

        final SchemaAndValue output = converter.toConnectData("TOPIC", input);

        final SchemaAndValue expected = StructGenerators.get("000-customroot");

        Comparisons.compareSchema(expected.schema(), output.schema());
        Comparisons.compareStruct((Struct)expected.value(), (Struct)output.value());
        assertEquals(expected, output);
    }

    @Test
    public void unknownElementTypeSchema() {
        final XmlConverter converter = new XmlConverter();
        final Map<String, String> props = ConfigGenerators.withSchemaProps("001-unknowntypes");
        converter.configure(props, false);

        final byte[] input = ByteGenerators.getXml("001");

        final NotImplementedException thrown = assertThrows(NotImplementedException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertEquals("Unsupported element type xs:stringlike", thrown.getMessage());
    }

    @Test
    public void unknownAttributeTypeSchema() {
        final XmlConverter converter = new XmlConverter();
        final Map<String, String> props = ConfigGenerators.withSchemaProps("006-unknowntypes");
        converter.configure(props, false);

        final byte[] input = ByteGenerators.getXml("006");

        final NotImplementedException thrown = assertThrows(NotImplementedException.class, () -> {
            converter.toConnectData("TOPIC", input);
        });
        assertEquals("Unsupported attribute type xs:stringlike", thrown.getMessage());
    }

    @Test
    public void useSchemaNameAsXmlTag() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final SchemaAndValue input = StructGenerators.get("041");
        final byte[] output = converter.fromConnectData("TOPIC", input.schema(), input.value());

        final Source expected = Input.fromFile(FileGenerators.getXml("041")).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }

    @Test
    public void ignoreUnsafeSchemaNameAsXmlTag() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final SchemaAndValue input = StructGenerators.get("042");
        final byte[] output = converter.fromConnectData("TOPIC", input.schema(), input.value());

        final Source expected = Input.fromFile(FileGenerators.getXml("042")).build();

        assertThat(
                Input.fromByteArray(output),
                isIdenticalTo(expected)
                    .ignoreWhitespace()
                    .ignoreComments());
    }

    @Test
    public void reuseConverter() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.defaultRootNoSchemasProps(), false);

        final String[] testcases = { "000", "002", "004" };

        for (final String testcase : testcases) {
            final Source expectedOutput = Input.fromFile(FileGenerators.getXml(testcase)).build();

            final SchemaAndValue structInput = StructGenerators.get(testcase);
            final byte[] structOutput = converter.fromConnectData("TOPIC", structInput.schema(), structInput.value());
            assertThat(
                    Input.fromByteArray(structOutput),
                    isIdenticalTo(expectedOutput)
                        .ignoreWhitespace()
                        .ignoreComments());

            final Map<?, ?> mapInput = MapGenerators.get(testcase);
            final byte[] mapOutput = converter.fromConnectData("TOPIC", null, mapInput);
            assertThat(
                    Input.fromByteArray(mapOutput),
                    isIdenticalTo(expectedOutput)
                        .ignoreWhitespace()
                        .ignoreComments());

            final byte[] xmlInput = ByteGenerators.getXml(testcase);
            final SchemaAndValue convertBack = converter.toConnectData("TOPIC", xmlInput);
            assertEquals(mapInput, convertBack.value());
        }
    }

    @Test
    public void handleNullSchema() {
        final XmlPluginsConfig config = ConfigGenerators.defaultRootNoSchemas();
        final XsdToSchema generator = new XsdToSchema(config);
        final Schema converted = generator.getSchema();
        assertNull(converted);
    }

    @Test
    public void handleFlatDocumentsWithoutSchema() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.flatNoSchemasProps(), false);

        final byte[] input = ByteGenerators.getXml("051");
        final SchemaAndValue output = converter.toConnectData("TOPIC", input);
        assertNull(output.schema());
        assertEquals(1234, output.value());
    }

    @Test
    public void handleFlatDocumentsWithSchema() {
        final XmlConverter converter = new XmlConverter();
        converter.configure(ConfigGenerators.flatSchemasProps("051"), false);

        final byte[] input = ByteGenerators.getXml("051");
        final SchemaAndValue output = converter.toConnectData("TOPIC", input);
        assertEquals(Schema.INT32_SCHEMA, output.schema());
        assertEquals(1234, output.value());
    }
}
