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

import java.io.File;

public class FileGenerators {

    public static File getXml(String testCaseId) {
        return new File("./src/test/resources/" + testCaseId + ".xml");
    }
    public static File getXsd(String testCaseId) {
        return new File("./src/test/resources/" + testCaseId + ".xsd");
    }
    public static File getCombined(String testCaseId) {
        return new File("./src/test/resources/" + testCaseId + "-combined.xml");
    }
    public static File getGenericXml(String testCaseId) {
        return new File("./src/test/resources/" + testCaseId + "-generic.xml");
    }
    public static File getJson(String testCaseId) {
        return new File("./src/test/resources/" + testCaseId + ".json");
    }
    public static File getCombinedJson(String testCaseId) {
        return new File("./src/test/resources/" + testCaseId + "-combined.json");
    }
}
