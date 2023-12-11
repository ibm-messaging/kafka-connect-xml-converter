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

import java.io.IOException;
import java.nio.file.Files;

public class ByteGenerators {

    public static byte[] getXml(String testCaseId) {
        try {
            return Files.readAllBytes(FileGenerators.getXml(testCaseId).toPath());
        }
        catch (final IOException e) {
            return new byte[0];
        }
    }

    public static byte[] getGenericXml(String testCaseId) {
        try {
            return Files.readAllBytes(FileGenerators.getGenericXml(testCaseId).toPath());
        }
        catch (final IOException e) {
            return new byte[0];
        }
    }


    public static byte[] getJson(String testCaseId) {
        try {
            return Files.readAllBytes(FileGenerators.getJson(testCaseId).toPath());
        }
        catch (final IOException e) {
            return new byte[0];
        }
    }


    public static byte[] getCombinedJson(String testCaseId) {
        try {
            return Files.readAllBytes(FileGenerators.getCombinedJson(testCaseId).toPath());
        }
        catch (final IOException e) {
            return new byte[0];
        }
    }

}
