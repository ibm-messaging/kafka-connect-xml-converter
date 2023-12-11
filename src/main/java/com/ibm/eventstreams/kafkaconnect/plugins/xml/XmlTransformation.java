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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.storage.ConverterConfig;
import org.apache.kafka.connect.storage.ConverterType;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.CollectionToXmlBytes;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.StructToXmlBytes;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.XmlBytesToStruct;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;


public class XmlTransformation<R extends ConnectRecord<R>> implements Transformation<R> {

    private final Logger log = LoggerFactory.getLogger(XmlTransformation.class);

    private XmlPluginsConfig config;

    private XmlBytesToStruct     xmlToStruct = null;
    private StructToXmlBytes     structToXml = null;
    private CollectionToXmlBytes collectionToStruct = null;

    /**
     * Transforms the provided record to/from XML.
     *
     *  Infers the desired transformation from the type of the input record.
     *
     *   If the input record is a string, we assume it's an XML string that
     *    we are being asked to parse and turn into a structured object.
     *   If the input record is a structured object, we assume that we're
     *    being asked to represent it as an XML string.
     */
    @Override
    public R apply(R record) {
        final Object value = record.value();

        // string -> object

        if (value instanceof String) {
            return convert(record, ((String) value).getBytes());
        }
        if (value instanceof byte[]) {
            return convert(record, (byte[]) value);
        }

        // object -> string

        if (value instanceof Struct) {
            final byte[] data = convert(record.valueSchema(), (Struct)record.value());
            return convertToString(record, data);
        }
        if (value instanceof Map) {
            final byte[] data = convert(record.valueSchema(), (Map<?, ?>)record.value());
            return convertToString(record, data);
        }
        if (value instanceof Collection) {
            final byte[] data = convert(record.valueSchema(), (Collection<?>)record.value());
            return convertToString(record, data);
        }

        // everything else is (currently) unsupported

        throw new NotImplementedException(value.getClass());
    }


    private R convert(R record, byte[] value) {
        if (xmlToStruct == null) {
            xmlToStruct = new XmlBytesToStruct(config);
        }
        final SchemaAndValue converted = xmlToStruct.convert(value);

        return record.newRecord(record.topic(),
                                record.kafkaPartition(),
                                record.keySchema(),
                                record.key(),
                                converted.schema(),
                                converted.value(),
                                record.timestamp());
    }


    private R convertToString(R record, byte[] stringData) {
        return record.newRecord(record.topic(),
            record.kafkaPartition(),
            record.keySchema(),
            record.key(),
            Schema.STRING_SCHEMA,
            new String(stringData),
            record.timestamp());
    }


    private byte[] convert(Schema schema, Struct value) {
        if (structToXml == null) {
            structToXml = new StructToXmlBytes(config);
        }
        return structToXml.convert(schema, value);
    }

    private byte[] convert(Schema schema, Collection<?> value) {
        if (collectionToStruct == null) {
            collectionToStruct = new CollectionToXmlBytes(config);
        }
        return collectionToStruct.convert(schema, value);
    }

    private byte[] convert(Schema schema, Map<?, ?> value) {
        if (collectionToStruct == null) {
            collectionToStruct = new CollectionToXmlBytes(config);
        }
        return collectionToStruct.convert(schema, value);
    }




    @Override
    public ConfigDef config() {
        return XmlPluginsConfig.configDef();
    }


    @Override
    public void configure(Map<String, ?> configs) {
        log.info("Configuring transformation {}", configs);

        final Map<String, Object> conf = new HashMap<>(configs);
        conf.put(ConverterConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        config = new XmlPluginsConfig(conf);
    }




    @Override
    public void close() {
        this.xmlToStruct = null;
        this.structToXml = null;
        this.collectionToStruct = null;
    }
}
