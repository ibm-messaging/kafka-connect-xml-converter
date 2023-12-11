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
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.storage.ConverterConfig;
import org.apache.kafka.connect.storage.ConverterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.CollectionToXmlBytes;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.StructToXmlBytes;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.engines.XmlBytesToStruct;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;


public class XmlConverter implements Converter {

    private final Logger log = LoggerFactory.getLogger(XmlConverter.class);

    private XmlPluginsConfig config;

    private StructToXmlBytes     structToXml = null;
    private CollectionToXmlBytes collectionToStruct = null;
    private XmlBytesToStruct     xmlToStruct        = null;


    @Override
    public ConfigDef config() {
        return XmlPluginsConfig.configDef();
    }


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("Configuring converter {}", configs);

        final Map<String, Object> conf = new HashMap<>(configs);
        conf.put(ConverterConfig.TYPE_CONFIG,
            isKey ? ConverterType.KEY.getName() : ConverterType.VALUE.getName());
        config = new XmlPluginsConfig(conf);
    }


    /**
     * Converts an object into an XML representation of that object.
     */
    @Override
    public byte[] fromConnectData(String topic, Schema schema, Object value) {
        if (value == null) {
            return new byte[0];
        }
        if (value instanceof Struct) {
            if (structToXml == null) {
                structToXml = new StructToXmlBytes(config);
            }
            return structToXml.convert(schema, (Struct) value);
        }
        if (value instanceof Map) {
            if (collectionToStruct == null) {
                collectionToStruct = new CollectionToXmlBytes(config);
            }
            return collectionToStruct.convert(schema, (Map<?, ?>) value);
        }
        if (value instanceof Collection) {
            if (collectionToStruct == null) {
                collectionToStruct = new CollectionToXmlBytes(config);
            }
            return collectionToStruct.convert(schema, (Collection<?>) value);
        }

        throw new NotImplementedException(value.getClass());
    }

    /**
     * Converts an XML string into a Connect struct.
     */
    @Override
    public SchemaAndValue toConnectData(String topic, byte[] value) {
        if (value == null || value.length == 0) {
            return SchemaAndValue.NULL;
        }

        if (xmlToStruct == null) {
            xmlToStruct = new XmlBytesToStruct(config);
        }
        return xmlToStruct.convert(value);
    }
}
