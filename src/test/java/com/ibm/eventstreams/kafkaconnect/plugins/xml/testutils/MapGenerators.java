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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.connect.data.Decimal;

public class MapGenerators {

    public static Map<?, ?> get(String testCaseId) {
        final Map<Object, Object> value = new LinkedHashMap<>();

        switch (testCaseId) {
            case "000":
                value.put("test-1", 123);
                value.put("test-2", 1.23);
                value.put("test-3", "xyz");
                value.put("test-4", true);
                break;
            case "001":
                final Map<String, Object> customer = new LinkedHashMap<>();
                customer.put("name", "Helen Velazquez");
                final Map<String, String> phone = new LinkedHashMap<>();
                phone.put("type", "landline");
                phone.put("number", "0911 910 5491");
                customer.put("phone", phone);
                customer.put("email", "mus.donec.dignissim@yahoo.ca");
                customer.put("address", "3249 Hendrerit Av.");
                customer.put("postalZip", "F2 1IX");
                customer.put("region", "Dunbartonshire");
                final Map<String, Object> product0 = new LinkedHashMap<>();
                product0.put("brand", "Acme Inc");
                product0.put("item", "Awesome-ivator");
                product0.put("quantity", 1);
                final Map<String, Object> product1 = new LinkedHashMap<>();
                product1.put("brand", "Globex");
                product1.put("item", "Widget");
                product1.put("quantity", 2);
                final Map<String, String> order = new LinkedHashMap<>();
                order.put("date", "2023-11-05 22:11:00");

                value.put("customer", customer);
                value.put("product", List.of(product0, product1));
                value.put("order", order);
                break;
            case "002": {
                final Map<String, Object> test1 = new LinkedHashMap<>();
                final Map<String, Object> test1a = new LinkedHashMap<>();
                test1a.put("test1aa", List.of(Integer.valueOf(10), Integer.valueOf(20), Integer.valueOf(30)));
                test1a.put("test1ab", List.of(1.1d, 1.2d));
                test1a.put("test1ac", List.of(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true)));
                test1a.put("test1ad", List.of("abc", "def", "ghi", "jkl"));
                test1.put("test1a", test1a);
                test1.put("test1b", "ppp");
                test1.put("test1c", "qqq");
                value.put("test1", test1);
                break;
            }
            case "003":
                final List<String> strings = List.of("one", "two");
                final List<Float> floats = List.of(Float.valueOf(1.1f), Float.valueOf(1.2f));
                final List<Double> doubles = List.of(Double.valueOf(1.1), Double.valueOf(1.2));
                final List<Short> shorts = List.of(Short.valueOf((short) 1), Short.valueOf((short) 2));
                final List<Integer> ints = List.of(Integer.valueOf(1), Integer.valueOf(2));
                final List<Long> longs = List.of(Long.valueOf(1), Long.valueOf(2));
                final List<Boolean> booleans = List.of(true, false);
                final Map<String, List<?>> lists = new LinkedHashMap<>();
                lists.put("strings", strings);
                lists.put("floats", floats);
                lists.put("doubles", doubles);
                lists.put("shorts", shorts);
                lists.put("ints", ints);
                lists.put("longs", longs);
                lists.put("booleans", booleans);
                final Map<String, Object> items = new LinkedHashMap<>();
                items.put("string", "three");
                items.put("float", Float.valueOf(1.3f));
                items.put("double", Double.valueOf(1.3));
                items.put("short", Short.valueOf((short) 3));
                items.put("int", Integer.valueOf(3));
                items.put("long", Long.valueOf(3));
                items.put("boolean", false);
                final Map<String, Object> optionals = new LinkedHashMap<>();
                optionals.put("string", "four");
                optionals.put("float", Float.valueOf(1.4f));
                optionals.put("double", Double.valueOf(1.4));
                optionals.put("short", Short.valueOf((short) 4));
                optionals.put("int", Integer.valueOf(4));
                optionals.put("long", Long.valueOf(4));
                optionals.put("boolean", true);
                value.put("lists", lists);
                value.put("items", items);
                value.put("optionals", optionals);
                break;
            case "004":
                value.put("entry", ListGenerators.get(testCaseId));
                break;
            case "005":
                value.put("entry", ListGenerators.get(testCaseId));
                break;
            case "006": {
                final Map<String, Object> test1 = new LinkedHashMap<>();
                test1.put("test2", "one");
                final Map<String, Object> test5_0 = new LinkedHashMap<>();
                test5_0.put("entry", "test message");
                test5_0.put("test6", "three");
                final Map<String, Object> test5_1 = new LinkedHashMap<>();
                test5_1.put("entry", "next item");
                test5_1.put("test6", "four");
                final Map<String, Object> test5_2 = new LinkedHashMap<>();
                test5_2.put("entry", "final item");
                test5_2.put("test7", "five");
                final Map<String, Object> test3 = new LinkedHashMap<>();
                test3.put("test5", List.of(test5_0, test5_1, test5_2));
                test3.put("test4", "two");
                final Map<String, Object> outer = new LinkedHashMap<>();
                outer.put("test3", test3);

                value.put("test1", test1);
                value.put("outer", outer);
                break;
            }
            case "007":
                value.put("type0", "string");
                value.put("type1", "https://www.ibm.com");
                value.put("type2", "TEST VALUE".getBytes());
                value.put("type3", false);
                value.put("type4", "1996-08-03");
                value.put("type5", "1971-02-07T22:24:55.93");
                value.put("type6", -3334129.7650942);
                value.put("type7", -3103999.81);
                value.put("type8", "P2Y8M26DT11H11M4.44S");
                value.put("type9", 60891.24f);
                value.put("type10", "---15");
                value.put("type11", "--12");
                value.put("type12", "--03-10");
                value.put("type13", "2019");
                value.put("type14", "1980-01");
                value.put("type15", "212D32");
                value.put("type17", "string");
                value.put("type18", "14:45:47.61");
                value.put("type19", 937);
                value.put("type20", -1361l);
                value.put("type21", -164);
                value.put("type22", (short)-2851);
                value.put("type24", 1130);
                value.put("type25", 4759);
                value.put("type26", 8937l);
                value.put("type27", 1319);
                value.put("type28", (short)398);
                value.put("type30", -2147483260);
                value.put("type31", -2147475003);
                value.put("type32", "string");
                value.put("optType0", "string");
                value.put("optType1", "https://ibm.com/products/event-automation");
                value.put("optType2", "TEST VALUE".getBytes());
                value.put("optType3", true);
                value.put("optType4", "2010-04-04");
                value.put("optType5", "2005-08-25T05:13:09.44");
                value.put("optType6", -4417739.7650942);
                value.put("optType7", 414280.2);
                value.put("optType8", "P3Y5M10DT9H18M1.61S");
                value.put("optType9", 32129.77f);
                value.put("optType10", "---27");
                value.put("optType11", "--11");
                value.put("optType12", "--11-02");
                value.put("optType13", "1983");
                value.put("optType14", "1984-09");
                value.put("optType15", "21");
                value.put("optType17", "string");
                value.put("optType18", "06:19:58.75");
                value.put("optType19", -4104);
                value.put("optType20", 2642l);
                value.put("optType21", 734);
                value.put("optType22", (short)-2796);
                value.put("optType24", 2418);
                value.put("optType25", 4471);
                value.put("optType26", 9218l);
                value.put("optType27", 2361);
                value.put("optType28", (short)3702);
                value.put("optType30", -2147475934);
                value.put("optType31", -2147475373);
                value.put("optType32", "string");
                break;
            case "008":
                final Map<String, Object> a = new LinkedHashMap<>();
                a.put("level3Value6", "third");
                a.put("level3Value7", "option");
                final Map<String, Object> level1Value1 = new LinkedHashMap<>();
                level1Value1.put("level2Option3", a);
                final Map<String, Object> b = new LinkedHashMap<>();
                b.put("level3Value6", "g");
                b.put("level3Value7", "h");
                final Map<String, Object> c = new LinkedHashMap<>();
                c.put("level3Value6", "i");
                c.put("level3Value7", "j");
                final Map<String, Object> level1Value2 = new LinkedHashMap<>();
                level1Value2.put("level2RepeatingItem2", b);
                level1Value2.put("level2RepeatingItem3", c);
                final Map<String, Object> level1Value3 = new LinkedHashMap<>();
                level1Value3.put("level2List2", List.of(10, 20, 30));
                value.put("level1Value1", level1Value1);
                value.put("level1Value2", level1Value2);
                value.put("level1Value3", level1Value3);
                break;
            case "009":
                value.put("test", 1.23123123123123);
                break;
            case "010":
                final Map<String, Object> item1Value = new LinkedHashMap<>();
                item1Value.put("item1-a", false);
                item1Value.put("item1-b", "abc");
                item1Value.put("item1-c", true);
                item1Value.put("item1-e", List.of("doo", "foo", "goo"));
                final List<String> item2Value = List.of("alpha", "beta", "gamma");
                final Map<String, Object> item3Entry1 = new LinkedHashMap<>();
                item3Entry1.put("item3-entry-a", "AAA");
                item3Entry1.put("item3-entry-b", List.of("A", "B"));
                item3Entry1.put("item3-entry-c", "BBB");
                final Map<String, Object> item3Entry2 = new LinkedHashMap<>();
                item3Entry2.put("item3-entry-a", "CCC");
                item3Entry2.put("item3-entry-b", List.of("C", "D", "E"));
                final Map<String, Object> item3Entry3 = new LinkedHashMap<>();
                item3Entry3.put("item3-entry-a", "FFF");
                item3Entry3.put("item3-entry-b", List.of("F", "G"));
                item3Entry3.put("item3-entry-c", "GGG");
                final List<Map<String, Object>> item3Value = List.of(item3Entry1, item3Entry2, item3Entry3);
                final Map<String, String> item4Value = new LinkedHashMap<>();
                item4Value.put("ooo", "pqrst");
                item4Value.put("ppp", "uvwxy");
                item4Value.put("qqq", "rstuv");
                final Map<String, String> item5Entry1 = new LinkedHashMap<>();
                item5Entry1.put("e1a", "aaa");
                item5Entry1.put("e1b", "bbb");
                final Map<String, String> item5Entry2 = new LinkedHashMap<>();
                item5Entry2.put("e2a", "ccc");
                item5Entry2.put("e2b", "ddd");
                item5Entry2.put("e2c", "eee");
                final Map<String, String> item5Entry3 = new LinkedHashMap<>();
                item5Entry3.put("e3a", "fff");
                final List<Map<String, String>> item5Value = List.of(item5Entry1, item5Entry2, item5Entry3);
                final Map<String, Object> item6Entry1 = new LinkedHashMap<>();
                item6Entry1.put("message", "hello");
                item6Entry1.put("num", 1000);
                item6Entry1.put("test", true);
                final Map<String, Object> item6Entry2 = new LinkedHashMap<>();
                item6Entry2.put("message", "world");
                item6Entry2.put("num", 2000);
                item6Entry2.put("test", false);
                final Map<String, Object> item6Entry3 = new LinkedHashMap<>();
                item6Entry3.put("message", "xxx");
                item6Entry3.put("num", 3000);
                item6Entry3.put("test", true);
                final Map<Integer, Map<String, Object>> item6Value = new LinkedHashMap<>();
                item6Value.put(2, item6Entry1);
                item6Value.put(5, item6Entry2);
                item6Value.put(9, item6Entry3);

                value.put("item1", item1Value);
                value.put("item2", item2Value);
                value.put("item3", item3Value);
                value.put("item4", item4Value);
                value.put("item5", item5Value);
                value.put("item6", item6Value);
                break;
            case "011":
                final Map<String, String> mapValue = new LinkedHashMap<>();
                mapValue.put("key1", "value1");
                mapValue.put("key2", "value2");
                mapValue.put("key3", "value3");
                value.put("the-map", mapValue);
                break;
            case "012": {
                final Map<String, String> item0 = new LinkedHashMap<>();
                item0.put("key1", "value1");
                item0.put("key2", "value2");
                item0.put("key3", "value3");
                final Map<String, String> item1 = new LinkedHashMap<>();
                item1.put("keyA", "valueA");
                item1.put("keyB", "valueB");
                value.put("one-of-the-maps", List.of(item0, item1));
                break;
            }
            case "013":
                final List<Integer> t1 = List.of(123, 234);
                final List<Integer> t2 = List.of(456, 678);
                final List<Integer> t3 = List.of(890, 901);
                final List<Integer> t4 = List.of(12, 23);
                final List<Integer> t5 = List.of(34, 45);
                final List<Integer> t6 = List.of(56, 67);
                final List<Integer> t7 = List.of(78, 89);
                final List<Integer> t8 = List.of(90, 11);
                final List<Integer> t9 = List.of(22, 33);
                final List<Integer> t10 = List.of(44, 55);
                final List<Integer> t11 = List.of(66, 77);
                final List<Integer> t12 = List.of(88, 99);
                final List<Integer> t13 = List.of(111, 222);
                final List<Integer> t14 = List.of(333, 444);
                final List<Integer> t15 = List.of(555, 666);
                final List<Integer> t16 = List.of(777, 888);

                final List<Object> i1 = List.of(t1, t2);
                final List<Object> i2 = List.of(t3, t4);
                final List<Object> i3 = List.of(t5, t6);
                final List<Object> i4 = List.of(t7, t8);
                final List<Object> i5 = List.of(t9, t10);
                final List<Object> i6 = List.of(t11, t12);
                final List<Object> i7 = List.of(t13, t14);
                final List<Object> i8 = List.of(t15, t16);

                final List<Object> m1 = List.of(i1, i2);
                final List<Object> m2 = List.of(i3, i4);
                final List<Object> m3 = List.of(i5, i6);
                final List<Object> m4 = List.of(i7, i8);

                final List<Object> o1 = List.of(m1, m2);
                final List<Object> o2 = List.of(m3, m4);

                value.put("test", List.of(o1, o2));
                break;
            case "014": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                final Boolean[] val0 = { true, false, true };
                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                final Boolean[] val1 = { false, true };

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "015": {
                final Map<String, Object> key0a = new LinkedHashMap<>();
                key0a.put("keyFloat", 7.8f);
                key0a.put("keyBool", true);
                final Map<String, Object> key0b = new LinkedHashMap<>();
                key0b.put("keyFloat", 17.3f);
                key0b.put("keyBool", false);
                final List<Map<String, Object>> key0 = List.of(key0a, key0b);

                final Map<String, Object> key1a = new LinkedHashMap<>();
                key1a.put("keyFloat", 3.9f);
                key1a.put("keyBool", false);
                final Map<String, Object> key1b = new LinkedHashMap<>();
                key1b.put("keyFloat", 6.2f);
                key1b.put("keyBool", true);
                final List<Map<String, Object>> key1 = List.of(key1a, key1b);

                value.put(key0, "value0");
                value.put(key1, "value1");
                break;
            }
            case "016": {
                final Map<String, Object> val0 = new LinkedHashMap<>();
                val0.put("bools", List.of(true, true));
                val0.put("score", 3.456d);
                final Map<String, Object> val1 = new LinkedHashMap<>();
                val1.put("bools", List.of(false, false));
                val1.put("score", 9.887d);
                final Map<String, Object> val2 = new LinkedHashMap<>();
                val2.put("bools", List.of(true, false));
                val2.put("score", 100.00001d);

                value.put(Long.valueOf(3), val0);
                value.put(Long.valueOf(9), val1);
                value.put(Long.valueOf(100), val2);
                break;
            }
            case "017": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyA", "AAAA");
                key0.put("keyB", "BBBB");
                final Map<String, String> val0 = new LinkedHashMap<>();
                val0.put("valA", "CCCC");
                val0.put("valB", "DDDD");

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyA", "EEEE");
                key1.put("keyB", "FFFF");
                final Map<String, String> val1 = new LinkedHashMap<>();
                val1.put("valA", "GGGG");
                val1.put("valB", "HHHH");

                final Map<String, String> key2 = new LinkedHashMap<>();
                key2.put("keyA", "IIII");
                key2.put("keyB", "JJJJ");
                final Map<String, String> val2 = new LinkedHashMap<>();
                val2.put("valA", "KKKK");
                val2.put("valB", "LLLL");

                final Map<Map<String, String>, Map<String, String>> firstMap = new LinkedHashMap<>();
                firstMap.put(key0, val0);
                firstMap.put(key1, val1);
                firstMap.put(key2, val2);

                final Map<String, String> key3 = new LinkedHashMap<>();
                key3.put("keyA", "MMMM");
                key3.put("keyB", "NNNN");
                final Map<String, String> val3 = new LinkedHashMap<>();
                val3.put("valA", "OOOO");
                val3.put("valB", "PPPP");

                final Map<String, String> key4 = new LinkedHashMap<>();
                key4.put("keyA", "QQQQ");
                key4.put("keyB", "RRRR");
                final Map<String, String> val4 = new LinkedHashMap<>();
                val4.put("valA", "SSSS");
                val4.put("valB", "TTTT");

                final Map<Map<String, String>, Map<String, String>> secondMap = new LinkedHashMap<>();
                secondMap.put(key3, val3);
                secondMap.put(key4, val4);

                value.put("maps", List.of(firstMap, secondMap));
                break;
            }
            case "018": {
                final Map<String, String> innerKey0 = new LinkedHashMap<>();
                innerKey0.put("inner", "theInnerKey0");
                final Map<String, String> innerKey1 = new LinkedHashMap<>();
                innerKey1.put("inner", "theInnerKey1");
                final Map<String, String> innerKey2 = new LinkedHashMap<>();
                innerKey2.put("inner", "theInnerKey2");
                final Map<Map<String, String>, List<Integer>> firstKey = new LinkedHashMap<>();
                firstKey.put(innerKey0, List.of(10, 11, 12));
                firstKey.put(innerKey1, List.of(20, 21, 22));
                firstKey.put(innerKey2, List.of(30, 31, 32));
                final Map<String, Float> firstValue = new LinkedHashMap<>();
                firstValue.put("innerValue1", 111.111f);
                firstValue.put("innerValue2", 222.222f);
                firstValue.put("innerValue3", 333.333f);

                final Map<String, String> innerKey3 = new LinkedHashMap<>();
                innerKey3.put("inner", "theInnerKey3");
                final Map<String, String> innerKey4 = new LinkedHashMap<>();
                innerKey4.put("inner", "theInnerKey4");
                final Map<Map<String, String>, List<Integer>> secondKey = new LinkedHashMap<>();
                secondKey.put(innerKey3, List.of(40, 41));
                secondKey.put(innerKey4, List.of(50, 51));
                final Map<String, Float> secondValue = new LinkedHashMap<>();
                secondValue.put("innerValue4", 444.444f);
                secondValue.put("innerValue5", 555.555f);
                secondValue.put("innerValue6", 666.666f);
                secondValue.put("innerValue7", 777.777f);

                value.put(firstKey, firstValue);
                value.put(secondKey, secondValue);
                break;
            }
            case "019": {
                final Map<String, Integer> letters1 = new LinkedHashMap<>();
                letters1.put("A", 1);
                letters1.put("B", 2);
                letters1.put("C", 3);
                final Map<String, Integer> letters2 = new LinkedHashMap<>();
                letters2.put("D", 4);
                letters2.put("E", 5);

                value.put(List.of("a", "b", "c"), letters1);
                value.put(List.of("d", "e"), letters2);
                break;
            }
            case "020": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Map<String, Object> value0 = new LinkedHashMap<>();
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);

                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Map<String, Object> value1 = new LinkedHashMap<>();
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);

                final Map<String, Object> key2 = new LinkedHashMap<>();
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);
                final Map<String, Object> value2 = new LinkedHashMap<>();
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);

                value.put(key0, value0);
                value.put(key1, value1);
                value.put(key2, value2);
                break;
            }
            case "021": {
                final Map<String, Object> value0 = new LinkedHashMap<>();
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);
                final Map<String, Object> value1 = new LinkedHashMap<>();
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);
                final Map<String, Object> value2 = new LinkedHashMap<>();
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);

                value.put("key0", value0);
                value.put("key1", value1);
                value.put("key2", value2);
                break;
            }
            case "022": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Map<String, Object> key2 = new LinkedHashMap<>();
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);

                value.put(key0, "value0");
                value.put(key1, "value1");
                value.put(key2, "value2");
                break;
            }
            case "023": {
                value.put(1, 10);
                value.put(2, 20);
                value.put(3, 30);
                value.put(4, 40);
                value.put(5, 50);
                break;
            }
            case "024": {
                value.put("string1", "ONE");
                value.put("optString2", "two");
                value.put("string2", "TWO");
                value.put("optString3", null);
                break;
            }
            case "025": {
                value.put("first", 1.1d);
                value.put(null, 2.2d);
                value.put("third", 3.3d);
                break;
            }
            case "026": {
                value.put("123", "unusual key 1");
                value.put("", "unusual key 2");
                value.put("Hello!", "unusual key 3");
                value.put("my-key", "valid key 1");
                value.put("12key", "unusual key 4");
                value.put("*HELLO*", "unusual key 5");
                value.put("my-key-2", "valid key 2");
                value.put("Hello World", "unusual key 6");
                break;
            }
            case "027": {
                value.put("mystring", "Hello World");
                value.put("onechar", "H");
                value.put("mybytes", "Hello World".getBytes());
                value.put("onebyte", "H".getBytes());
                break;
            }
            case "028": {
                final Map<byte[], byte[]> bytesMap = new LinkedHashMap<>();
                bytesMap.put(new byte[] { 0x10, 0x11, 0x12, 0x13 }, new byte[] { 0x60, 0x61, 0x62 });
                bytesMap.put(new byte[] { 0x14, 0x15 }, new byte[] { 0x70, 0x71, 0x72, 0x73, 0x74 });

                final Map<String, byte[]> strToBytesMap = new LinkedHashMap<>();
                strToBytesMap.put("Hello", "Hello".getBytes());
                strToBytesMap.put("World", "World".getBytes());
                strToBytesMap.put("This is a test", "This is a test".getBytes());

                final Map<byte[], String> bytesToStrMap = new LinkedHashMap<>();
                bytesToStrMap.put("one".getBytes(), "one");
                bytesToStrMap.put("two".getBytes(), "two");

                value.put("bytes", bytesMap);
                value.put("strToBytes", strToBytesMap);
                value.put("bytesToStr", bytesToStrMap);
                break;
            }
            case "029": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                key0.put("keyFloat", 1.23f);
                final List<Boolean> val0 = List.of(true, false, true);
                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                key1.put("keyFloat", 4.56f);
                final List<Boolean> val1 = List.of(false, true);

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "030": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                key0.put("keyFloat", 1.23f);
                final Map<String, Object> val0 = new LinkedHashMap<>();
                val0.put("valueLetter", "XXX");
                val0.put("valueNumber", 678);
                val0.put("valueFloat", 5.67f);

                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                key1.put("keyFloat", 4.56f);
                final Map<String, Object> val1 = new LinkedHashMap<>();
                val1.put("valueLetter", "YYY");
                val1.put("valueNumber", 789);
                val1.put("valueFloat", 7.89f);

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "031": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                final Map<String, String> val0 = new LinkedHashMap<>();
                val0.put("valueLetter", "B");
                val0.put("valueWord", "BANANA");
                val0.put("valueString", "STRING");

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                final Map<String, String> val1 = new LinkedHashMap<>();
                val1.put("valueLetter", "D");
                val1.put("valueWord", "DAMSON");
                val1.put("valueString", "OUTPUT");

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "032": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                final String val0 = "SIMPLE";

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                final String val1 = "SINGLE";

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "033": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                key0.put("keyOrdinal", "first");
                final String val0 = "SIMPLE";

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                key1.put("keyOrdinal", "second");
                final String val1 = "SINGLE";

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "034": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                key0.put("keyOrdinal", "first");
                final Map<String, String> val0 = new LinkedHashMap<>();
                val0.put("entry", "SIMPLE");
                val0.put("valueLetter", "a");

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                key1.put("keyOrdinal", "second");
                final Map<String, String> val1 = new LinkedHashMap<>();
                val1.put("entry", "SINGLE");
                val1.put("valueLetter", "b");

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "035": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("entry", "FIRST");
                final Map<String, String> val0 = new LinkedHashMap<>();
                val0.put("entry", "SIMPLE");
                val0.put("valueLetter", "a");

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("entry", "SECOND");
                final Map<String, String> val1 = new LinkedHashMap<>();
                val1.put("entry", "SINGLE");
                val1.put("valueLetter", "b");

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "036": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                key0.put("keyOrdinal", "first");
                final Map<String, String> val0 = new LinkedHashMap<>();
                val0.put("valueLetter", "a");
                val0.put("valueWord", "abacus");
                val0.put("valueString", "TESTING");
                val0.put("valueOrdinal", "FIRST");

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                key1.put("keyOrdinal", "second");
                final Map<String, String> val1 = new LinkedHashMap<>();
                val1.put("valueLetter", "c");
                val1.put("valueWord", "counting");
                val1.put("valueString", "INPUTTING");
                val1.put("valueOrdinal", "SECOND");

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "037": {
                final Map<String, String> value1 = new LinkedHashMap<>();
                value1.put("key3", "itemValue3");
                value1.put("key4", "itemValue4");
                final Map<String, String> value2 = new LinkedHashMap<>();
                value2.put("key5", "itemValue5");
                value2.put("key6", "itemValue6");
                value2.put("entry", "VALUE TWO");
                final Map<String, String> value3 = new LinkedHashMap<>();
                value3.put("key7", "itemValue7");
                value3.put("key8", "itemValue8");
                value3.put("inner", "INNER");
                final Map<String, Object> value4 = new LinkedHashMap<>();
                value4.put("key9", "itemValue9");
                value4.put("key10", "itemValue10");
                value4.put("inside", List.of("INSIDE", "OUT"));
                final Map<String, String> value5 = new LinkedHashMap<>();
                value5.put("key11", "itemValue11");
                value5.put("key12", "itemValue12");
                value5.put("key13", "itemValue13");
                value5.put("key14", "itemValue14");
                final Map<String, Object> value6 = new LinkedHashMap<>();
                value6.put("key15", "itemValue15");
                value6.put("key16", "itemValue16");
                value6.put("key17", List.of("list17ItemONE", "list17ItemTWO", "list17ItemTHREE"));
                value6.put("key18", "itemValue18");
                value6.put("key19", "itemValue19");
                final Map<String, Object> itm1 = new LinkedHashMap<>();
                itm1.put("key1", "itemKey1");
                itm1.put("key2", "itemKey2");
                itm1.put("value1", value1);
                itm1.put("value2", value2);
                itm1.put("value3", value3);
                itm1.put("value4", value4);
                itm1.put("value5", value5);
                itm1.put("value6", value6);

                value.put("item1", itm1);
                break;
            }
            case "038": {
                final Map<String, Integer> key0 = new LinkedHashMap<>();
                key0.put("keyNumber", 1);
                key0.put("keyCount", 10);
                key0.put("entry", 100);
                final Map<String, Integer> val0 = new LinkedHashMap<>();
                val0.put("entry", 10000);
                val0.put("valueNumber", 1000);

                final Map<String, Integer> key1 = new LinkedHashMap<>();
                key1.put("keyNumber", 2);
                key1.put("keyCount", 20);
                key1.put("entry", 200);
                final Map<String, Integer> val1 = new LinkedHashMap<>();
                val1.put("entry", 20000);
                val1.put("valueNumber", 2000);

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "039": {
                final Map<String, String> item1 = new LinkedHashMap<>();
                item1.put("key1", "itemKey1");
                item1.put("key2", "itemKey2");
                item1.put("entry", "FIRST");
                final Map<String, Integer> theMap = new LinkedHashMap<>();
                theMap.put("ten", 10);
                theMap.put("hundred", 100);
                final Map<String, Integer> theBigMap = new LinkedHashMap<>();
                theBigMap.put("twenty", 20);
                theBigMap.put("two-hundred", 200);

                value.put("item1", item1);
                value.put("themap", theMap);
                value.put("thebigmap", theBigMap);
                break;
            }
            case "040": {
                final Map<String, Integer> key0 = new LinkedHashMap<>();
                key0.put("keyNumber", 1);
                key0.put("keyCount", 10);
                key0.put("entry", 100);
                final Map<String, Integer> val0 = new LinkedHashMap<>();
                val0.put("entry", 10000);
                val0.put("valueNumber", 1000);

                final Map<String, Integer> key1 = new LinkedHashMap<>();
                key1.put("keyNumber", 2);
                key1.put("keyCount", 20);
                key1.put("entry", 200);
                final Map<String, Integer> val1 = new LinkedHashMap<>();
                val1.put("entry", 20000);
                val1.put("valueNumber", 2000);

                value.put(key0, val0);
                value.put(key1, val1);
                break;
            }
            case "043": {
                final Map<String, Float> map1 = new LinkedHashMap<>();
                map1.put("one point two", 1.2f);
                map1.put("two point three", 2.3f);
                map1.put("three point four", 3.4f);

                final Map<String, Float> map2 = new LinkedHashMap<>();
                map2.put("ten point five", 10.5f);
                map2.put("eleven point seven", 11.7f);

                final Map<String, Float> map3 = new LinkedHashMap<>();
                map3.put("pi", 3.14f);
                map3.put("half", 0.5f);

                final Map<String, Float> map4 = new LinkedHashMap<>();
                map4.put("first", 1.1f);
                map4.put("second", 2.2f);
                map4.put("third", 3.3f);
                map4.put("fourth", 4.4f);

                value.put("maps", List.of(List.of(map1, map2),
                                          List.of(map3, map4)));
                break;
            }
            case "044": {
                final Map<String, List<?>> key1 = new LinkedHashMap<>();
                key1.put("integers", List.of(1, 2, 3, 4));
                key1.put("floats", List.of(1.1f, 2.2f));
                final Map<Boolean, Integer> val1 = new LinkedHashMap<>();
                val1.put(true, 12);
                val1.put(false, 27);

                final Map<String, List<?>> key2 = new LinkedHashMap<>();
                key2.put("integers", List.of(10, 20, 30));
                key2.put("floats", List.of(1.23f, 4.56f));
                final Map<Boolean, Integer> val2 = new LinkedHashMap<>();
                val2.put(true, 3);
                val2.put(false, 7);

                final Map<String, List<?>> key3 = new LinkedHashMap<>();
                key3.put("integers", List.of(100, 200));
                key3.put("floats", List.of(12.3f, 45.6f, 78.9f));
                final Map<Boolean, Integer> val3 = new LinkedHashMap<>();
                val3.put(true, 1);
                val3.put(false, 1);

                final Map<Map<String, List<?>>, Map<Boolean, Integer>> map = new LinkedHashMap<>();
                map.put(key1, val1);
                map.put(key2, val2);
                map.put(key3, val3);

                value.put(map, "testing");
                break;
            }
            case "045": {
                value.put(new Integer[] { 2, 4, 6, 8, 10 }, "even numbers");
                value.put(new Integer[] { 1, 3, 5, 7, 9 }, "odd numbers");
                break;
            }
            case "046": {
                value.put("mostly-numbers", new Integer[] { 1, 4, 7, null, 13, 16 });
                break;
            }
            case "047": {
                value.put("optionalByte", new byte[] { 10 });
                value.put("oneByte", new byte[] { 20 });
                value.put("oneUnsignedByte", new byte[] { 40 });
                value.put("multipleBytes", List.of(new byte[] { 30 },
                                                   new byte[] { 31 },
                                                   new byte[] { 32 },
                                                   new byte[] { 33 },
                                                   new byte[] { 34 }));
                value.put("multipleUnsignedBytes", List.of(new byte[] { 60 },
                                                           new byte[] { 61 },
                                                           new byte[] { 62 }));
                value.put("base64EncodedBytes", "Secret message".getBytes());
                break;
            }
            case "049": {
                final Set<String> setOfStrings = new LinkedHashSet<>();
                setOfStrings.add("member");
                setOfStrings.add("of");
                setOfStrings.add("the");
                setOfStrings.add("set");

                final List<String> listOfStrings = new ArrayList<>();
                listOfStrings.add("list");
                listOfStrings.add("members");

                final String[] arrayOfStrings = new String[] { "string", "items" };

                final Map<String[], Integer[]> mapOfArrays = new LinkedHashMap<>();
                mapOfArrays.put(new String[] { "one", "two", "three" },
                                new Integer[] { 1, 2, 3 });
                mapOfArrays.put(new String[] { "ten", "twenty" },
                                new Integer[] { 10, 20 });

                value.put("set-of-strings", setOfStrings);
                value.put("list-of-strings", listOfStrings);
                value.put("array-of-strings", arrayOfStrings);
                value.put("map-of-arrays", mapOfArrays);
                break;
            }
            case "052": {
                value.put("myattr1", "testing");
                value.put("myattr2", 123);
                value.put("test1", "one");
                value.put("test2", "two");
                break;
            }
            case "053": {
                value.put("myattr1", "testing");
                value.put("myattr2", 123);
                value.put("myattr3", false);

                Map<String, Object> test1 = new LinkedHashMap<>();
                test1.put("myattr4", "inner");
                test1.put("entry", "one");
                value.put("test1", test1);

                Map<String, Object> test2 = new LinkedHashMap<>();
                test2.put("test2a", "alpha");
                Map<String, Object> test2b = new LinkedHashMap<>();
                test2b.put("myattr5", "deepinner");
                test2b.put("entry", "beta");
                test2.put("test2b", test2b);
                value.put("test2", test2);

                Map<String, Object> test3 = new LinkedHashMap<>();
                test3.put("myattr6", "middle");
                test3.put("test3a", 333);
                value.put("test3", test3);

                value.put("test4", "four");
                break;
            }
            case "054": {
                Map<String, Object> transaccion = new LinkedHashMap<>();
                transaccion.put("id", "PE80");
                transaccion.put("tecla", "00");
                transaccion.put("numclie", 51372133);
                Map<String, Object> datos = new LinkedHashMap<>();
                datos.put("transaccion", transaccion);
                Map<String, Object> xmlentrada = new LinkedHashMap<>();
                xmlentrada.put("datos", datos);
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("xml-entrada", xmlentrada);
                data.put("trama-entrada", "longer test string");
                data.put("mq-server", "MQ : SPIAWT99;;;SPIA.QC.QZT1;SPIA.QP.OUT");
                data.put("direccion-IP", "17.127.22.33");
                data.put("nombre-servidor", "Qpbxiaa");
                data.put("canal", "ABC");

                value.put("date", "2024-08-06 12:32:04");
                value.put("cr", 2246);
                value.put("tx", "PE23");
                value.put("user", "01888329");
                value.put("estatus-tx", 1);
                value.put("version", "7.0.1001.2");
                value.put("data", data);
                break;
            }
            case "055":{
                value.put("ProductID", 1);
                value.put("ProductName", "TestProductName");
                value.put("Price",new BigDecimal("10.000"));
                break;

            }
        }
        return value;
    }


    public static Map<?, ?> generic(String testCaseId) {
        switch (testCaseId) {
            case "003": {
                final Map<Object, Object> value = new LinkedHashMap<>();
                final List<String> strings = List.of("one", "two");
                final List<Double> floats = List.of(Double.valueOf(1.1), Double.valueOf(1.2));
                final List<Double> doubles = List.of(Double.valueOf(1.1), Double.valueOf(1.2));
                final List<Integer> shorts = List.of(Integer.valueOf(1), Integer.valueOf(2));
                final List<Integer> ints = List.of(Integer.valueOf(1), Integer.valueOf(2));
                final List<Integer> longs = List.of(Integer.valueOf(1), Integer.valueOf(2));
                final List<Boolean> booleans = List.of(true, false);
                final Map<String, List<?>> lists = new LinkedHashMap<>();
                lists.put("strings", strings);
                lists.put("floats", floats);
                lists.put("doubles", doubles);
                lists.put("shorts", shorts);
                lists.put("ints", ints);
                lists.put("longs", longs);
                lists.put("booleans", booleans);
                final Map<String, Object> items = new LinkedHashMap<>();
                items.put("string", "three");
                items.put("float", Double.valueOf(1.3));
                items.put("double", Double.valueOf(1.3));
                items.put("short", Integer.valueOf(3));
                items.put("int", Integer.valueOf(3));
                items.put("long", Integer.valueOf(3));
                items.put("boolean", false);
                final Map<String, Object> optionals = new LinkedHashMap<>();
                optionals.put("string", "four");
                optionals.put("float", Double.valueOf(1.4));
                optionals.put("double", Double.valueOf(1.4));
                optionals.put("short", Integer.valueOf(4));
                optionals.put("int", Integer.valueOf(4));
                optionals.put("long", Integer.valueOf(4));
                optionals.put("boolean", true);
                value.put("lists", lists);
                value.put("items", items);
                value.put("optionals", optionals);
                return value;
            }
            case "007": {
                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("type0", "string");
                value.put("type1", "https://www.ibm.com");
                value.put("type2", new String(Base64.getEncoder().encode("TEST VALUE".getBytes())));
                value.put("type3", false);
                value.put("type4", "1996-08-03");
                value.put("type5", "1971-02-07T22:24:55.93");
                value.put("type6", -3334129.7650942);
                value.put("type7", -3103999.81);
                value.put("type8", "P2Y8M26DT11H11M4.44S");
                value.put("type9", 60891.24);
                value.put("type10", "---15");
                value.put("type11", "--12");
                value.put("type12", "--03-10");
                value.put("type13", 2019);
                value.put("type14", "1980-01");
                value.put("type15", "212D32");
                value.put("type17", "string");
                value.put("type18", "14:45:47.61");
                value.put("type19", 937);
                value.put("type20", -1361);
                value.put("type21", -164);
                value.put("type22", -2851);
                value.put("type24", 1130);
                value.put("type25", 4759);
                value.put("type26", 8937);
                value.put("type27", 1319);
                value.put("type28", 398);
                value.put("type30", -2147483260);
                value.put("type31", -2147475003);
                value.put("type32", "string");
                value.put("optType0", "string");
                value.put("optType1", "https://ibm.com/products/event-automation");
                value.put("optType2", new String(Base64.getEncoder().encode("TEST VALUE".getBytes())));
                value.put("optType3", true);
                value.put("optType4", "2010-04-04");
                value.put("optType5", "2005-08-25T05:13:09.44");
                value.put("optType6", -4417739.7650942);
                value.put("optType7", 414280.2);
                value.put("optType8", "P3Y5M10DT9H18M1.61S");
                value.put("optType9", 32129.77);
                value.put("optType10", "---27");
                value.put("optType11", "--11");
                value.put("optType12", "--11-02");
                value.put("optType13", 1983);
                value.put("optType14", "1984-09");
                value.put("optType15", 21);
                value.put("optType17", "string");
                value.put("optType18", "06:19:58.75");
                value.put("optType19", -4104);
                value.put("optType20", 2642);
                value.put("optType21", 734);
                value.put("optType22", -2796);
                value.put("optType24", 2418);
                value.put("optType25", 4471);
                value.put("optType26", 9218);
                value.put("optType27", 2361);
                value.put("optType28", 3702);
                value.put("optType30", -2147475934);
                value.put("optType31", -2147475373);
                value.put("optType32", "string");
                return value;
            }
            case "010": {
                final Map<String, Object> item1Value = new LinkedHashMap<>();
                item1Value.put("item1-a", false);
                item1Value.put("item1-b", "abc");
                item1Value.put("item1-c", true);
                item1Value.put("item1-e", List.of("doo", "foo", "goo"));
                final List<String> item2Value = List.of("alpha", "beta", "gamma");
                final Map<String, Object> item3Entry1 = new LinkedHashMap<>();
                item3Entry1.put("item3-entry-a", "AAA");
                item3Entry1.put("item3-entry-b", List.of("A", "B"));
                item3Entry1.put("item3-entry-c", "BBB");
                final Map<String, Object> item3Entry2 = new LinkedHashMap<>();
                item3Entry2.put("item3-entry-a", "CCC");
                item3Entry2.put("item3-entry-b", List.of("C", "D", "E"));
                final Map<String, Object> item3Entry3 = new LinkedHashMap<>();
                item3Entry3.put("item3-entry-a", "FFF");
                item3Entry3.put("item3-entry-b", List.of("F", "G"));
                item3Entry3.put("item3-entry-c", "GGG");
                final List<Map<String, Object>> item3Value = List.of(item3Entry1, item3Entry2, item3Entry3);
                final Map<String, String> item4Value = new LinkedHashMap<>();
                item4Value.put("ooo", "pqrst");
                item4Value.put("ppp", "uvwxy");
                item4Value.put("qqq", "rstuv");
                final List<Map<String, Object>> item5Entry1 = new ArrayList<>();
                item5Entry1.add(representAsStructMap("e1a", "aaa"));
                item5Entry1.add(representAsStructMap("e1b", "bbb"));
                final List<Map<String, Object>> item5Entry2 = new ArrayList<>();
                item5Entry2.add(representAsStructMap("e2a", "ccc"));
                item5Entry2.add(representAsStructMap("e2b", "ddd"));
                item5Entry2.add(representAsStructMap("e2c", "eee"));
                final Map<String, String> item5Entry3 = new LinkedHashMap<>();
                item5Entry3.put("e3a", "fff");
                final List<Object> item5Value = List.of(
                        item5Entry1,
                        item5Entry2,
                        representAsStructMap(item5Entry3));
                final Map<String, Object> item6Entry1 = new LinkedHashMap<>();
                item6Entry1.put("message", "hello");
                item6Entry1.put("num", 1000);
                item6Entry1.put("test", true);
                final Map<String, Object> item6Entry2 = new LinkedHashMap<>();
                item6Entry2.put("message", "world");
                item6Entry2.put("num", 2000);
                item6Entry2.put("test", false);
                final Map<String, Object> item6Entry3 = new LinkedHashMap<>();
                item6Entry3.put("message", "xxx");
                item6Entry3.put("num", 3000);
                item6Entry3.put("test", true);
                final Map<Integer, Map<String, Object>> item6Value = new LinkedHashMap<>();
                item6Value.put(2, item6Entry1);
                item6Value.put(5, item6Entry2);
                item6Value.put(9, item6Entry3);

                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("item1", item1Value);
                value.put("item2", item2Value);
                value.put("item3", item3Value);
                value.put("item4", representAsStructMap(item4Value));
                value.put("item5", item5Value);
                value.put("item6", representAsStructMap(item6Value));
                return value;
            }
            case "011": {
                final Map<String, String> mapValue = new LinkedHashMap<>();
                mapValue.put("key1", "value1");
                mapValue.put("key2", "value2");
                mapValue.put("key3", "value3");
                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("the-map", representAsStructMap(mapValue));
                return value;
            }
            case "012": {
                final List<Map<String, Object>> item0 = new ArrayList<>();
                item0.add(representAsStructMap("key1", "value1"));
                item0.add(representAsStructMap("key2", "value2"));
                item0.add(representAsStructMap("key3", "value3"));
                final List<Map<String, Object>> item1 = new ArrayList<>();
                item1.add(representAsStructMap("keyA", "valueA"));
                item1.add(representAsStructMap("keyB", "valueB"));
                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("one-of-the-maps", List.of(item0, item1));
                return value;
            }
            case "014": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                final Map<Object, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", key0);
                entry0.put("value", List.of(true, false, true));

                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                final Map<Object, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", key1);
                entry1.put("value", List.of(false, true));

                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1));
                return value;
            }
            case "015": {
                final Map<String, Object> key0a = new LinkedHashMap<>();
                key0a.put("keyFloat", 7.8d);
                key0a.put("keyBool", true);
                final Map<String, Object> key0b = new LinkedHashMap<>();
                key0b.put("keyFloat", 17.3d);
                key0b.put("keyBool", false);
                final List<Map<String, Object>> key0 = List.of(key0a, key0b);
                final Map<String, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", key0);
                entry0.put("value", "value0");

                final Map<String, Object> key1a = new LinkedHashMap<>();
                key1a.put("keyFloat", 3.9d);
                key1a.put("keyBool", false);
                final Map<String, Object> key1b = new LinkedHashMap<>();
                key1b.put("keyFloat", 6.2d);
                key1b.put("keyBool", true);
                final List<Map<String, Object>> key1 = List.of(key1a, key1b);
                final Map<String, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", key1);
                entry1.put("value", "value1");

                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1));
                return value;
            }
            case "016": {
                final Map<String, Object> val0 = new LinkedHashMap<>();
                val0.put("bools", List.of(true, true));
                val0.put("score", 3.456d);
                final Map<String, Object> val1 = new LinkedHashMap<>();
                val1.put("bools", List.of(false, false));
                val1.put("score", 9.887d);
                final Map<String, Object> val2 = new LinkedHashMap<>();
                val2.put("bools", List.of(true, false));
                val2.put("score", 100.00001d);

                final Map<String, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", 3);
                entry0.put("value", val0);
                final Map<String, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", 9);
                entry1.put("value", val1);
                final Map<String, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", 100);
                entry2.put("value", val2);

                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1, entry2));
                return value;
            }
            case "017": {
                final Map<String, String> key0 = new LinkedHashMap<>();
                key0.put("keyA", "AAAA");
                key0.put("keyB", "BBBB");
                final Map<String, String> val0 = new LinkedHashMap<>();
                val0.put("valA", "CCCC");
                val0.put("valB", "DDDD");
                final Map<String, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Map<String, String> key1 = new LinkedHashMap<>();
                key1.put("keyA", "EEEE");
                key1.put("keyB", "FFFF");
                final Map<String, String> val1 = new LinkedHashMap<>();
                val1.put("valA", "GGGG");
                val1.put("valB", "HHHH");
                final Map<String, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Map<String, String> key2 = new LinkedHashMap<>();
                key2.put("keyA", "IIII");
                key2.put("keyB", "JJJJ");
                final Map<String, String> val2 = new LinkedHashMap<>();
                val2.put("valA", "KKKK");
                val2.put("valB", "LLLL");
                final Map<String, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", key2);
                entry2.put("value", val2);

                final List<Object> firstMap = List.of(entry0, entry1, entry2);

                final Map<String, String> key3 = new LinkedHashMap<>();
                key3.put("keyA", "MMMM");
                key3.put("keyB", "NNNN");
                final Map<String, String> val3 = new LinkedHashMap<>();
                val3.put("valA", "OOOO");
                val3.put("valB", "PPPP");
                final Map<String, Object> entry3 = new LinkedHashMap<>();
                entry3.put("key", key3);
                entry3.put("value", val3);

                final Map<String, String> key4 = new LinkedHashMap<>();
                key4.put("keyA", "QQQQ");
                key4.put("keyB", "RRRR");
                final Map<String, String> val4 = new LinkedHashMap<>();
                val4.put("valA", "SSSS");
                val4.put("valB", "TTTT");
                final Map<String, Object> entry4 = new LinkedHashMap<>();
                entry4.put("key", key4);
                entry4.put("value", val4);

                final List<Object> secondMap = List.of(entry3, entry4);

                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("maps", List.of(firstMap, secondMap));
                return value;
            }
            case "018": {
                final Map<Object, Object> innerKey0 = new LinkedHashMap<>();
                innerKey0.put("inner", "theInnerKey0");
                final Map<Object, Object> firstKeyEntry0 = new LinkedHashMap<>();
                firstKeyEntry0.put("key", innerKey0);
                firstKeyEntry0.put("value", List.of(10, 11, 12));

                final Map<Object, Object> innerKey1 = new LinkedHashMap<>();
                innerKey1.put("inner", "theInnerKey1");
                final Map<Object, Object> firstKeyEntry1 = new LinkedHashMap<>();
                firstKeyEntry1.put("key", innerKey1);
                firstKeyEntry1.put("value", List.of(20, 21, 22));

                final Map<Object, Object> innerKey2 = new LinkedHashMap<>();
                innerKey2.put("inner", "theInnerKey2");
                final Map<Object, Object> firstKeyEntry2 = new LinkedHashMap<>();
                firstKeyEntry2.put("key", innerKey2);
                firstKeyEntry2.put("value", List.of(30, 31, 32));

                final Map<Object, Object> firstKey = new LinkedHashMap<>();
                firstKey.put("entry", List.of(firstKeyEntry0, firstKeyEntry1, firstKeyEntry2));

                final Map<Object, Object> valueSchemaEntry1 = new LinkedHashMap<>();
                valueSchemaEntry1.put("key", "innerValue1");
                valueSchemaEntry1.put("value", 111.111d);
                final Map<Object, Object> valueSchemaEntry2 = new LinkedHashMap<>();
                valueSchemaEntry2.put("key", "innerValue2");
                valueSchemaEntry2.put("value", 222.222d);
                final Map<Object, Object> valueSchemaEntry3 = new LinkedHashMap<>();
                valueSchemaEntry3.put("key", "innerValue3");
                valueSchemaEntry3.put("value", 333.333d);

                final Map<Object, Object> value1 = new LinkedHashMap<>();
                value1.put("entry", List.of(valueSchemaEntry1, valueSchemaEntry2, valueSchemaEntry3));

                final Map<Object, Object> innerKey3 = new LinkedHashMap<>();
                innerKey3.put("inner", "theInnerKey3");
                final Map<Object, Object> secondKeyEntry0 = new LinkedHashMap<>();
                secondKeyEntry0.put("key", innerKey3);
                secondKeyEntry0.put("value", List.of(40, 41));

                final Map<Object, Object> innerKey4 = new LinkedHashMap<>();
                innerKey4.put("inner", "theInnerKey4");
                final Map<Object, Object> secondKeyEntry1 = new LinkedHashMap<>();
                secondKeyEntry1.put("key", innerKey4);
                secondKeyEntry1.put("value", List.of(50, 51));

                final Map<Object, Object> secondKey = new LinkedHashMap<>();
                secondKey.put("entry", List.of(secondKeyEntry0, secondKeyEntry1));

                final Map<Object, Object> valueSchemaEntry4 = new LinkedHashMap<>();
                valueSchemaEntry4.put("key", "innerValue4");
                valueSchemaEntry4.put("value", 444.444d);
                final Map<Object, Object> valueSchemaEntry5 = new LinkedHashMap<>();
                valueSchemaEntry5.put("key", "innerValue5");
                valueSchemaEntry5.put("value", 555.555d);
                final Map<Object, Object> valueSchemaEntry6 = new LinkedHashMap<>();
                valueSchemaEntry6.put("key", "innerValue6");
                valueSchemaEntry6.put("value", 666.666d);
                final Map<Object, Object> valueSchemaEntry7 = new LinkedHashMap<>();
                valueSchemaEntry7.put("key", "innerValue7");
                valueSchemaEntry7.put("value", 777.777d);

                final Map<Object, Object> value2 = new LinkedHashMap<>();
                value2.put("entry", List.of(valueSchemaEntry4, valueSchemaEntry5, valueSchemaEntry6, valueSchemaEntry7));

                final Map<Object, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", firstKey);
                entry1.put("value", value1);
                final Map<Object, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", secondKey);
                entry2.put("value", value2);

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry1, entry2));
                return value;
            }
            case "019": {
                final Map<String, Object> letters1_0 = new LinkedHashMap<>();
                letters1_0.put("key", "A");
                letters1_0.put("value", 1);
                final Map<String, Object> letters1_1 = new LinkedHashMap<>();
                letters1_1.put("key", "B");
                letters1_1.put("value", 2);
                final Map<String, Object> letters1_2 = new LinkedHashMap<>();
                letters1_2.put("key", "C");
                letters1_2.put("value", 3);
                final Map<String, Object> letters1 = new LinkedHashMap<>();
                letters1.put("entry", List.of(letters1_0, letters1_1, letters1_2));

                final Map<String, Object> letters2_0 = new LinkedHashMap<>();
                letters2_0.put("key", "D");
                letters2_0.put("value", 4);
                final Map<String, Object> letters2_1 = new LinkedHashMap<>();
                letters2_1.put("key", "E");
                letters2_1.put("value", 5);
                final Map<String, Object> letters2 = new LinkedHashMap<>();
                letters2.put("entry", List.of(letters2_0, letters2_1));

                final Map<String, Object> value_0 = new LinkedHashMap<>();
                value_0.put("key", List.of("a", "b", "c"));
                value_0.put("value", letters1);

                final Map<String, Object> value_1 = new LinkedHashMap<>();
                value_1.put("key", List.of("d", "e"));
                value_1.put("value", letters2);

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(value_0, value_1));
                return value;
            }
            case "020": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Map<String, Object> value0 = new LinkedHashMap<>();
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);
                final Map<Object, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", key0);
                entry0.put("value", value0);

                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Map<String, Object> value1 = new LinkedHashMap<>();
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);
                final Map<Object, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", key1);
                entry1.put("value", value1);

                final Map<String, Object> key2 = new LinkedHashMap<>();
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);
                final Map<String, Object> value2 = new LinkedHashMap<>();
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);
                final Map<Object, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", key2);
                entry2.put("value", value2);

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1, entry2));
                return value;
            }
            case "021": {
                final Map<String, Object> value0 = new LinkedHashMap<>();
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);
                final Map<String, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", "key0");
                entry0.put("value", value0);

                final Map<String, Object> value1 = new LinkedHashMap<>();
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);
                final Map<String, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", "key1");
                entry1.put("value", value1);

                final Map<String, Object> value2 = new LinkedHashMap<>();
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);
                final Map<String, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", "key2");
                entry2.put("value", value2);

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1, entry2));
                return value;
            }
            case "022": {
                final Map<String, Object> key0 = new LinkedHashMap<>();
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Map<Object, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", key0);
                entry0.put("value", "value0");

                final Map<String, Object> key1 = new LinkedHashMap<>();
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Map<Object, Object> entry1 = new LinkedHashMap<>();
                entry1.put("key", key1);
                entry1.put("value", "value1");

                final Map<String, Object> key2 = new LinkedHashMap<>();
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);
                final Map<Object, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", key2);
                entry2.put("value", "value2");

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1, entry2));
                return value;
            }
            case "023": {
                final Map<String, Integer> entry0 = new LinkedHashMap<>();
                entry0.put("key", 1);
                entry0.put("value", 10);
                final Map<String, Integer> entry1 = new LinkedHashMap<>();
                entry1.put("key", 2);
                entry1.put("value", 20);
                final Map<String, Integer> entry2 = new LinkedHashMap<>();
                entry2.put("key", 3);
                entry2.put("value", 30);
                final Map<String, Integer> entry3 = new LinkedHashMap<>();
                entry3.put("key", 4);
                entry3.put("value", 40);
                final Map<String, Integer> entry4 = new LinkedHashMap<>();
                entry4.put("key", 5);
                entry4.put("value", 50);

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1, entry2, entry3, entry4));
                return value;
            }
            case "024": {
                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("string1", "ONE");
                value.put("optString2", "two");
                value.put("string2", "TWO");
                value.put("optString3", "");
                return value;
            }
            case "025": {
                final Map<Object, Object> entry0 = new LinkedHashMap<>();
                entry0.put("key", "first");
                entry0.put("value", 1.1d);

                final Map<Object, Object> entry1 = new LinkedHashMap<>();
                entry1.put("value", 2.2d);

                final Map<Object, Object> entry2 = new LinkedHashMap<>();
                entry2.put("key", "third");
                entry2.put("value", 3.3d);

                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("entry", List.of(entry0, entry1, entry2));
                return value;
            }
            case "027": {
                final Map<Object, Object> value = new LinkedHashMap<>();
                value.put("mystring", "Hello World");
                value.put("onechar", "H");
                value.put("mybytes", new String(Base64.getEncoder().encode("Hello World".getBytes())));
                value.put("onebyte", new String(Base64.getEncoder().encode("H".getBytes())));
                return value;
            }
            case "028": {
                final Map<Object, Object> bytesEntry0 = new LinkedHashMap<>();
                bytesEntry0.put("key", new String(Base64.getEncoder().encode(new byte[] { 0x10, 0x11, 0x12, 0x13 })));
                bytesEntry0.put("value", new String(Base64.getEncoder().encode(new byte[] { 0x60, 0x61, 0x62 })));
                final Map<Object, Object> bytesEntry1 = new LinkedHashMap<>();
                bytesEntry1.put("key", new String(Base64.getEncoder().encode(new byte[] { 0x14, 0x15 })));
                bytesEntry1.put("value", new String(Base64.getEncoder().encode(new byte[] { 0x70, 0x71, 0x72, 0x73, 0x74 })));
                final Map<Object, Object> bytesMap = new LinkedHashMap<>();
                bytesMap.put("entry", List.of(bytesEntry0, bytesEntry1));

                final Map<Object, Object> strToBytesEntry0 = new LinkedHashMap<>();
                strToBytesEntry0.put("key", "Hello");
                strToBytesEntry0.put("value", new String(Base64.getEncoder().encode("Hello".getBytes())));
                final Map<Object, Object> strToBytesEntry1 = new LinkedHashMap<>();
                strToBytesEntry1.put("key", "World");
                strToBytesEntry1.put("value", new String(Base64.getEncoder().encode("World".getBytes())));
                final Map<Object, Object> strToBytesEntry2 = new LinkedHashMap<>();
                strToBytesEntry2.put("key", "This is a test");
                strToBytesEntry2.put("value", new String(Base64.getEncoder().encode("This is a test".getBytes())));
                final Map<Object, Object> strToBytesMap = new LinkedHashMap<>();
                strToBytesMap.put("entry", List.of(strToBytesEntry0, strToBytesEntry1, strToBytesEntry2));

                final Map<Object, Object> bytesToStrEntry0 = new LinkedHashMap<>();
                bytesToStrEntry0.put("key", new String(Base64.getEncoder().encode("one".getBytes())));
                bytesToStrEntry0.put("value", "one");
                final Map<Object, Object> bytesToStrEntry1 = new LinkedHashMap<>();
                bytesToStrEntry1.put("key", new String(Base64.getEncoder().encode("two".getBytes())));
                bytesToStrEntry1.put("value", "two");
                final Map<Object, Object> bytesToStrMap = new LinkedHashMap<>();
                bytesToStrMap.put("entry", List.of(bytesToStrEntry0, bytesToStrEntry1));

                final Map<String, Map<?, ?>> value = new LinkedHashMap<>();
                value.put("bytes", bytesMap);
                value.put("strToBytes", strToBytesMap);
                value.put("bytesToStr", bytesToStrMap);

                return value;
            }
            case "047": {
                final Map<String, Object> value = new LinkedHashMap<>();
                value.put("optionalByte", 10);
                value.put("oneByte", 20);
                value.put("oneUnsignedByte", 40);
                value.put("multipleBytes", List.of(30, 31, 32, 33, 34));
                value.put("multipleUnsignedBytes", List.of(60, 61, 62));
                value.put("base64EncodedBytes", "U2VjcmV0IG1lc3NhZ2U=");
                return value;
            }
            case "054": {
                final Map<String, Object> value = new LinkedHashMap<>();

                Map<String, Object> transaccion = new LinkedHashMap<>();
                transaccion.put("id", "PE80");
                transaccion.put("tecla", 0);
                transaccion.put("numclie", 51372133);
                Map<String, Object> datos = new LinkedHashMap<>();
                datos.put("transaccion", transaccion);
                Map<String, Object> xmlentrada = new LinkedHashMap<>();
                xmlentrada.put("datos", datos);
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("xml-entrada", xmlentrada);
                data.put("trama-entrada", "longer test string");
                data.put("mq-server", "MQ : SPIAWT99;;;SPIA.QC.QZT1;SPIA.QP.OUT");
                data.put("direccion-IP", "17.127.22.33");
                data.put("nombre-servidor", "Qpbxiaa");
                data.put("canal", "ABC");

                value.put("date", "2024-08-06 12:32:04");
                value.put("cr", 2246);
                value.put("tx", "PE23");
                value.put("user", 1888329);
                value.put("estatus-tx", 1);
                value.put("version", "7.0.1001.2");
                value.put("data", data);
                return value;
            }
            default:
                return get(testCaseId);
        }
    }

    private static Map<String, Object> representAsStructMap(Object key, Object value) {
        final Map<String, Object> output = new LinkedHashMap<>();
        output.put("key", key);
        output.put("value", value);
        return output;
    }

    private static Map<String, Object> representAsStructMap(Map<?, ?> input) {
        final Map<String, Object> output = new LinkedHashMap<>();
        if (input.size() > 1) {
            final List<Map<String, Object>> entries = new ArrayList<>();
            output.put("entry", entries);
            for (final Object key : input.keySet()) {
                final Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("key", key);
                entry.put("value", input.get(key));
                entries.add(entry);
            }
        }
        else {
            for (final Object key : input.keySet()) {
                output.put("key", key);
                output.put("value", input.get(key));
            }
        }
        return output;
    }
}
