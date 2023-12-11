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
package com.ibm.eventstreams.kafkaconnect.plugins.xml.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static final List<Object> nullSafeArrayToList(Object[] array) {
        final List<Object> list = new ArrayList<>(array.length);
        Collections.addAll(list, array);
        return list;
    }


    // returns a mutable list, unlike List.of()
    public static final List<Object> create(Object item1, Object item2) {
        final List<Object> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        return list;
    }


    @SuppressWarnings("unchecked")
    public static final void addItemToList(Object list, Object item) {
        ((List<Object>) list).add(item);
    }
}
