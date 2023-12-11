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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventstreams.connect.mqsource.builders.BaseRecordBuilder;

/**
 * Turns MQ messages containing XML strings into structured
 *  Kafka Connect records.
 */
public class XmlMQRecordBuilder extends BaseRecordBuilder {

    private final Logger log = LoggerFactory.getLogger(XmlMQRecordBuilder.class);

    private XmlConverter converter;

    private static final String CONFIG_PREFIX = "mq.record.builder.";


    @Override
    public void configure(Map<String, String> props) {
        super.configure(props);

        log.info("Configuring record builder {}", props);

        converter = new XmlConverter();
        final HashMap<String, String> config = new HashMap<>();
        for (final String key : props.keySet()) {
            if (key.startsWith(CONFIG_PREFIX)) {
                config.put(key.substring(CONFIG_PREFIX.length()),
                           props.get(key));
            }
        }

        converter.configure(config, false);
    }


    @Override
    public SchemaAndValue getValue(final JMSContext context, final String topic, final boolean messageBodyJms, final Message message) throws JMSException {
        final byte[] payload;

        if (message instanceof BytesMessage) {
            payload = message.getBody(byte[].class);
        }
        else if (message instanceof TextMessage) {
            final String s = message.getBody(String.class);
            payload = s.getBytes(UTF_8);
        }
        else {
            throw new ConnectException("Unsupported JMS message type");
        }

        return converter.toConnectData(topic, payload);
    }
}
