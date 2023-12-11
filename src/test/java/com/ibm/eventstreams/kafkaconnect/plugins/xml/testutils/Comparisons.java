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
package com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Comparisons {

    private static final Logger log = LoggerFactory.getLogger(Comparisons.class);

    private static final ObjectMapper jsonReader = new ObjectMapper();

    public static void compareJson(byte[] expected, byte[] actual) throws IOException {
        assertEquals(jsonReader.readTree(expected),
                     jsonReader.readTree(actual));
    }


    public static void compareSchema(Schema expected, Schema actual) {
        compareSchema(expected, actual, "");
    }

    private static void compareSchema(Schema expected, Schema actual, String location) {
        assertEquals("mismatch in optional status for schema at " + location,
                     expected.isOptional(), actual.isOptional());
        assertEquals("mismatch in type for schema at " + location,
                     expected.type(), actual.type());
        assertEquals("mismatch in version for schema at " + location,
                     expected.version(), actual.version());

        if (expected.type() == Type.STRUCT) {
            final List<Field> expectedFields = expected.fields();
            final List<Field> actualFields = actual.fields();
            assertEquals("mismatch in number of items at " + location,
                         expectedFields.size(), actualFields.size());

            for (int i = 0; i < expectedFields.size(); i++) {
                final Field expectedField = expectedFields.get(i);
                final Field actualField = actual.field(expectedField.name());

                compareField(expectedField, actualField, location + "." + expectedField.name());
            }
        }
    }

    private static void compareField(Field expected, Field actual, String location) {
        assertEquals("mismatch in optional status for field at " + location,
                     expected.schema().isOptional(),
                     actual.schema().isOptional());
        assertEquals("mismatch in type for field at " + location,
                     expected.schema().type(),
                     actual.schema().type());
        assertEquals("mismatch in index for field at " + location,
                     expected.index(),
                     actual.index());
        assertEquals("mismatch in name for field at " + location,
                     expected.name(), actual.name());

        if (expected.schema().type() == Type.STRUCT) {
            compareSchema(expected.schema(), actual.schema(), location);
        }
        else if (expected.schema().type() == Type.ARRAY) {
            final Schema expectedSchema = expected.schema().valueSchema();
            final Schema actualSchema = actual.schema().valueSchema();
            compareSchema(expectedSchema, actualSchema, location + "[]");
        }
        else {
            assertEquals("mismatch in schema at " + location,
                         expected.schema(), actual.schema());
        }
    }




    public static void compareStruct(Struct expected, Struct actual) {
        compareStruct(expected, actual, "");
    }

    private static void compareStruct(Struct expected, Struct actual, String location) {
        final Schema expectedSchema = expected.schema();
        for (final Field expectedField : expectedSchema.fields()) {
            final Object expectedValue = expected.get(expectedField);
            final Object actualValue = actual.get(expectedField);

            compareStructItem(expectedValue, actualValue, location + "." + expectedField.name());
        }
    }

    private static void compareStructItem(Object expected, Object actual, String location) {
        if (expected == null) {
            assertNull("unexpected value in " + location, actual);
        }
        else if (expected instanceof Struct) {
            compareStruct((Struct)expected, (Struct)actual, location);
        }
        else if (expected instanceof Collection) {
            final Object[] expectedList = ((Collection<?>) expected).toArray();
            final Object[] actualList = ((Collection<?>) actual).toArray();
            assertEquals("mismatch in number of items at " + location,
                         expectedList.length, actualList.length);

            for (int i = 0; i < expectedList.length; i++) {
                final Object expectedItem = expectedList[i];
                final Object actualItem = actualList[i];

                compareStructItem(expectedItem, actualItem, location + "[" + i + "]");
            }
        }
        else if (expected instanceof byte[]) {
            assertArrayEquals("mismatch at " + location, (byte[])expected, (byte[])actual);
        }
        else if (expected.getClass().isArray()) {
            assertArrayEquals("mismatch at " + location, (Object[])expected, (Object[])actual);
        }
        else {
            assertEquals("mismatch at " + location, expected, actual);
        }
    }


    public static void compare(Map<?, ?> expected, Map<?, ?> map) {
        try {
            compareMap(expected, map, "");
        }
        catch (final AssertionError e) {
            log.error("Assertion error", e);
            log.error("Expected " + String.valueOf(expected));
            log.error("Actual " + String.valueOf(map));
            throw e;
        }
    }

    private static void compareMap(Map<?, ?> expected, Map<?, ?> map, String location) {
        final Set<?> expectedKeys = expected.keySet();
        final Set<?> actualKeys = map.keySet();
        assertEquals("mismatch in keys at " + location,
                     expectedKeys, actualKeys);

        compareItem(expectedKeys, actualKeys, location);

        for (final Object key : expectedKeys) {
            final Object expectedValue = expected.get(key);
            final Object actualValue = map.get(key);

            compareItem(expectedValue, actualValue, location + "." + key);
        }
    }

    @SuppressWarnings("unchecked")
    private static void compareItem(Object expected, Object actual, String location) {
        if (expected == null) {
            assertNull("unexpected value at " + location, actual);
        }
        else if (expected instanceof Map) {
            assertTrue("mismatch in type at " + location, actual instanceof Map);

            compareMap((Map<String, Object>) expected,
                       (Map<String, Object>) actual,
                       location);
        }
        else if (expected instanceof List) {
            assertTrue("mismatch in type at " + location, actual instanceof List);

            final List<?> expectedList = (List<?>) expected;
            final List<?> actualList = (List<?>) actual;
            assertEquals("mismatch in number of items at " + location,
                         expectedList.size(), actualList.size());

            for (int i = 0; i < expectedList.size(); i++) {
                final Object expectedItem = expectedList.get(i);
                final Object actualItem = actualList.get(i);

                compareItem(expectedItem, actualItem, location + "[" + i + "]");
            }
        }
        else if (expected instanceof Set) {
            assertTrue("mismatch in type at " + location, actual instanceof Set);

            final Set<?> expectedSet = (Set<?>) expected;
            final Set<?> actualSet = (Set<?>) actual;
            assertEquals("mismatch in number of items at " + location,
                         expectedSet.size(), actualSet.size());

            for (final Object expectedItem : expectedSet) {
                assertTrue("missing item at " + location, actualSet.contains(expectedItem));
            }
        }
        else if (expected instanceof byte[]) {
            assertTrue("unexpected type at " + location, actual instanceof byte[]);
            assertArrayEquals("mismatch in array at " + location, (byte[])expected, (byte[])actual);
        }
        else if (expected.getClass().isArray()) {
            final Object[] expectedValue = (Object[]) expected;

            if (actual.getClass().isArray()) {
                final Object[] actualValue = (Object[]) actual;

                assertArrayEquals("mismatch in array at " + location,
                        expectedValue, actualValue);
            }
            else if (actual instanceof List) {
                compareItem(List.of(expectedValue), actual, location);
            }
            else {
                assertEquals("different item at " + location,
                        expected, actual);
            }
        }
        else if (expected instanceof Float) {
            final Float expectedValue = (Float) expected;
            final Float actualValue = (Float) expected;

            assertEquals("different float value at " + location,
                         expectedValue, actualValue, 0.01f);
        }
        else if (expected instanceof Double) {
            final Double expectedValue = (Double) expected;
            final Double actualValue = (Double) expected;

            assertEquals("different double value at " + location,
                         expectedValue, actualValue, 0.01f);
        }
        else {
            assertEquals("different item at " + location,
                         expected, actual);
        }
    }

}
