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

import java.io.File;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;
import org.apache.kafka.connect.storage.ConverterConfig;

public class XmlPluginsConfig extends ConverterConfig {

    public static final String SCHEMAS_ENABLE_CONFIG = "schemas.enable";
    public static final boolean SCHEMAS_ENABLE_DEFAULT = true;
    private static final String SCHEMAS_ENABLE_DOC = "Include schemas within each of the serialized values.";
    private static final String SCHEMAS_ENABLE_DISPLAY = "Enable schemas";

    public static final String XML_ROOT_ELEMENT_NAME_CONFIG = "root.element.name";
    public static final String XML_ROOT_ELEMENT_DEFAULT = "root";
    private static final String XML_ROOT_ELEMENT_DOC = "Name to use for the root element of serialized events if no name is included within the event";
    private static final String XML_ROOT_ELEMENT_DISPLAY = "Root element";

    public static final String XML_SCHEMA_EXTERNAL_PATH_CONFIG = "xsd.schema.path";
    private static final String XML_SCHEMA_EXTERNAL_PATH_DOC = "Location of an XSD schema to use";
    private static final String XML_SCHEMA_EXTERNAL_DISPLAY = "Schema (xsd) location";

    public static final String XML_ROOT_FLAT_CONFIG = "xml.doc.flat.enable";
    public static final boolean XML_ROOT_FLAT_DEFAULT = false;
    private static final String XML_ROOT_FLAT_DOC = "Set to true if XML messages will only contain a single primitive value (e.g. <root>hello world</root>)";
    private static final String XML_ROOT_FLAT_DISPLAY = "Flat";

    private final static ConfigDef CONFIG;


    static {
        final String group = "Options";
        int orderInGroup = 0;
        CONFIG = ConverterConfig.newConfigDef();
        CONFIG.define(SCHEMAS_ENABLE_CONFIG, Type.BOOLEAN, SCHEMAS_ENABLE_DEFAULT,
                      Importance.HIGH, SCHEMAS_ENABLE_DOC,
                      group, orderInGroup++,
                      Width.MEDIUM, SCHEMAS_ENABLE_DISPLAY);
        CONFIG.define(XML_ROOT_ELEMENT_NAME_CONFIG, Type.STRING, XML_ROOT_ELEMENT_DEFAULT,
                      Importance.HIGH, XML_ROOT_ELEMENT_DOC,
                      group, orderInGroup++,
                      Width.MEDIUM, XML_ROOT_ELEMENT_DISPLAY);
        CONFIG.define(XML_SCHEMA_EXTERNAL_PATH_CONFIG, Type.STRING, null,
                      Importance.HIGH, XML_SCHEMA_EXTERNAL_PATH_DOC,
                      group, orderInGroup++,
                      Width.MEDIUM, XML_SCHEMA_EXTERNAL_DISPLAY);
        CONFIG.define(XML_ROOT_FLAT_CONFIG, Type.BOOLEAN, XML_ROOT_FLAT_DEFAULT,
                      Importance.HIGH, XML_ROOT_FLAT_DOC,
                      group, orderInGroup++,
                      Width.SHORT, XML_ROOT_FLAT_DISPLAY);
    }

    public static ConfigDef configDef() {
        return CONFIG;
    }

    private final boolean schemasEnabled;
    private final String rootElementName;
    private final boolean flatDoc;
    private File xsdSchema = null;


    public XmlPluginsConfig(Map<String, ?> props) {
        super(CONFIG, props);
        this.schemasEnabled = getBoolean(SCHEMAS_ENABLE_CONFIG);
        this.rootElementName = getString(XML_ROOT_ELEMENT_NAME_CONFIG);
        this.flatDoc = getBoolean(XML_ROOT_FLAT_CONFIG);

        final String xsdSchemaFile = getString(XML_SCHEMA_EXTERNAL_PATH_CONFIG);
        if (xsdSchemaFile != null) {
            xsdSchema = new File(xsdSchemaFile);
        }
    }

    public boolean schemasEnabled() {
        return schemasEnabled;
    }

    public String getRootElementName() {
        return rootElementName;
    }

    public boolean isFlatDoc() {
        return flatDoc;
    }

    public File getXsdSchema() {
        return xsdSchema;
    }
}
