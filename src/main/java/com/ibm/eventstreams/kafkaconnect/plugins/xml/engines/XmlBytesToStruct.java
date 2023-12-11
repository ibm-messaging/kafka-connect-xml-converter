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

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.utils.XmlUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

public class XmlBytesToStruct {

    private final Logger log = LoggerFactory.getLogger(XmlBytesToStruct.class);


    private final XStream xstream;
    private final XmlPluginsConfig config;
    private final Schema schema;


    public XmlBytesToStruct(XmlPluginsConfig config) {
        this.config = config;

        xstream = new XStream(new StaxDriver(new NoNameCoder()));

        if (config.schemasEnabled() && config.getXsdSchema() != null) {
            // TODO - new feature idea: if schemas are enabled, but no schema path is provided
            //  to an external schema, we could find a schema within the XML payload

            final XsdToSchema schemaConverter = new XsdToSchema(config);
            schema = schemaConverter.getSchema();

            final XStreamStructConverter converter = new XStreamStructConverter();
            xstream.allowTypes(new Class[] { Struct.class });
            xstream.registerConverter(converter);
            xstream.alias(config.getRootElementName(), Struct.class);
            converter.registerSchema(schema);
        }
        else {
            schema = null;

            xstream.registerConverter(new XStreamMapConverter());
            xstream.alias(config.getRootElementName(), config.isFlatDoc() ? String.class : Map.class);
        }
    }


    @SuppressWarnings("unchecked")
    public SchemaAndValue convert(byte[] value) throws SerializationException {
        try (Reader xmlReader = new InputStreamReader(new ByteArrayInputStream(value))) {
            final Object obj = xstream.fromXML(xmlReader);
            Object val;
            if (schema != null) {
                val = obj;
            }
            else {
                if (config.isFlatDoc()) {
                    val = XmlUtils.guessType(obj);
                }
                else {
                    val = ((Map<String, Object>)obj).get(config.getRootElementName());
                }
            }

            return new SchemaAndValue(schema, val);
        }
        catch (final CannotResolveClassException crce) {
            log.error("Failed to create schema and value", crce);
            throw new SerializationException("Expected root element '" + config.getRootElementName() + "' not found");
        }
        catch (final XStreamException xse) {
            log.error("Failed to deserialize message data", xse);
            if (xse.getCause() != null && xse.getCause() instanceof SerializationException) {
                final SerializationException se = (SerializationException) xse.getCause();
                throw se;
            }
            else {
                throw new SerializationException("Failed to deserialize message data", xse);
            }
        }
        catch (final Throwable thr) {
            log.error("Failed to create schema and value", thr);
            thr.printStackTrace();
            throw new SerializationException(thr);
        }
    }
}
