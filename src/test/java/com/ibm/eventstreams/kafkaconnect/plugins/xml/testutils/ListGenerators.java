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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;

public class ListGenerators {

    public static List<Object> get(String testCaseId) {
        switch (testCaseId) {
            case "004":
                return List.of("list", "of", "strings");
            case "005": {
                final Map<String, Object> item0 = new LinkedHashMap<>();
                item0.put("message", "list of objects");
                item0.put("count", 123);
                final Map<String, Object> item1 = new LinkedHashMap<>();
                item1.put("message", "another object");
                item1.put("count", 456);
                return List.of(item0, item1);
            }
            case "050": {
                final Map<List<Integer>, List<String>> map = new LinkedHashMap<>();
                map.put(List.of(1, 2, 3), List.of("num ONE", "num TWO", "num THREE"));
                map.put(List.of(100, 200), List.of("ONE HUNDRED", "TWO HUNDRED"));

                final List<List<String>> list = new ArrayList<>();
                list.add(List.of("first", "second", "third"));
                list.add(List.of("tenth", "eleventh", "twelth", "thirteenth"));

                final List<Float> numbers = List.of(1.1f, 2.2f, 3.3f, 100.123f);

                return List.of(map, list, numbers);
            }
            default:
                throw new NotImplementedException("Unrecognised test case");
        }
    }
}
