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

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.connect.storage.ConverterType;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlPluginsConfig;

public class ConfigGenerators {

    public static XmlPluginsConfig defaultRootNoSchemas() {
        return new XmlPluginsConfig(defaultRootNoSchemasProps());
    }

    public static Map<String, String> defaultRootNoSchemasProps() {
        final Map<String, String> props = new HashMap<>();
        props.put(XmlPluginsConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        props.put(XmlPluginsConfig.SCHEMAS_ENABLE_CONFIG, "false");
        return props;
    }

    public static Map<String, String> customRootNoSchemasProps(String root) {
        final Map<String, String> props = new HashMap<>();
        props.put(XmlPluginsConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        props.put(XmlPluginsConfig.SCHEMAS_ENABLE_CONFIG, "false");
        props.put(XmlPluginsConfig.XML_ROOT_ELEMENT_NAME_CONFIG, root);
        return props;
    }

    public static Map<String, String> flatNoSchemasProps() {
        final Map<String, String> props = new HashMap<>();
        props.put(XmlPluginsConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        props.put(XmlPluginsConfig.SCHEMAS_ENABLE_CONFIG, "false");
        props.put(XmlPluginsConfig.XML_ROOT_FLAT_CONFIG, "true");
        return props;
    }

    public static Map<String, String> flatSchemasProps(String testCase) {
        final Map<String, String> props = new HashMap<>();
        props.put(XmlPluginsConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        props.put(XmlPluginsConfig.XML_SCHEMA_EXTERNAL_PATH_CONFIG,
                  FileGenerators.getXsd(testCase).getAbsolutePath());
        props.put(XmlPluginsConfig.SCHEMAS_ENABLE_CONFIG, "true");
        props.put(XmlPluginsConfig.XML_ROOT_FLAT_CONFIG, "true");
        return props;
    }


    public static XmlPluginsConfig withSchema(String testCase) {
        return new XmlPluginsConfig(withSchemaProps(testCase));
    }

    public static Map<String, String> withSchemaProps(String testCase) {
        final Map<String, String> props = new HashMap<>();
        props.put(XmlPluginsConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        props.put(XmlPluginsConfig.XML_SCHEMA_EXTERNAL_PATH_CONFIG,
                  FileGenerators.getXsd(testCase).getAbsolutePath());
        props.put(XmlPluginsConfig.SCHEMAS_ENABLE_CONFIG, "true");
        return props;
    }

    public static Map<String, String> customRootWithSchemaProps(String testCase, String root) {
        final Map<String, String> props = new HashMap<>();
        props.put(XmlPluginsConfig.TYPE_CONFIG, ConverterType.VALUE.getName());
        props.put(XmlPluginsConfig.XML_SCHEMA_EXTERNAL_PATH_CONFIG,
                  FileGenerators.getXsd(testCase).getAbsolutePath());
        props.put(XmlPluginsConfig.SCHEMAS_ENABLE_CONFIG, "true");
        props.put(XmlPluginsConfig.XML_ROOT_ELEMENT_NAME_CONFIG, root);
        return props;
    }

}
