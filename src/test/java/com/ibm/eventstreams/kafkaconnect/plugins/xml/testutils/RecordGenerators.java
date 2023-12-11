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

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.source.SourceRecord;

public class RecordGenerators {

    private final static String TOPIC_NAME = "TOPIC";

    private static SourceRecord createRecord(Schema schema, Object data) {
        return new SourceRecord(Collections.singletonMap("partition", "1"),
                                Collections.singletonMap("timestamp", "1"),
                                TOPIC_NAME,
                                schema,
                                data);
    }

    public static SourceRecord struct(String testCaseId) {
        final SchemaAndValue data = StructGenerators.get(testCaseId);
        return createRecord(data.schema(), data.value());
    }

    public static SourceRecord map(String testCaseId) {
        final Map<?, ?> data = MapGenerators.get(testCaseId);
        return createRecord(null, data);
    }

    public static SourceRecord string(String testCaseId) {
        final String data = new String(ByteGenerators.getXml(testCaseId));
        return createRecord(null, data);
    }

    public static SourceRecord bytes(String testCaseId) {
        final byte[] data = ByteGenerators.getXml(testCaseId);
        return createRecord(null, data);
    }

    public static SourceRecord nullString() {
        return createRecord(Schema.OPTIONAL_STRING_SCHEMA, null);
    }

    public static SourceRecord integer() {
        return createRecord(Schema.INT32_SCHEMA, 123);
    }
}
