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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.exceptions.NotImplementedException;

public class StructGenerators {

    public static SchemaAndValue get(String testCaseId) {
        switch (testCaseId) {
            case "000-customroot": {
                final Schema schema = SchemaBuilder.struct()
                    .name("mydoc")
                    .field("test-1", Schema.INT32_SCHEMA)
                    .field("test-2", Schema.FLOAT32_SCHEMA)
                    .field("test-3", Schema.STRING_SCHEMA)
                    .field("test-4", Schema.BOOLEAN_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("test-1", Integer.valueOf(123));
                value.put("test-2", Float.valueOf(1.23f));
                value.put("test-3", "xyz");
                value.put("test-4", Boolean.valueOf(true));

                return new SchemaAndValue(schema, value);
            }
            case "000": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test-1", Schema.INT32_SCHEMA)
                    .field("test-2", Schema.FLOAT32_SCHEMA)
                    .field("test-3", Schema.STRING_SCHEMA)
                    .field("test-4", Schema.BOOLEAN_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("test-1", Integer.valueOf(123));
                value.put("test-2", Float.valueOf(1.23f));
                value.put("test-3", "xyz");
                value.put("test-4", Boolean.valueOf(true));

                return new SchemaAndValue(schema, value);
            }
            case "001": {
                final Schema phoneSchema = SchemaBuilder.struct()
                    .field("type", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("number", Schema.STRING_SCHEMA)
                    .build();
                final Schema customerSchema = SchemaBuilder.struct()
                    .field("name", Schema.STRING_SCHEMA)
                    .field("phone", phoneSchema)
                    .field("email", Schema.STRING_SCHEMA)
                    .field("address", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("postalZip", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("region", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Schema productSchema = SchemaBuilder.struct()
                    .field("brand", Schema.STRING_SCHEMA)
                    .field("item", Schema.STRING_SCHEMA)
                    .field("quantity", Schema.INT32_SCHEMA)
                    .build();
                final Schema orderSchema = SchemaBuilder.struct()
                    .field("date", Schema.STRING_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("customer", customerSchema)
                    .field("product", SchemaBuilder.array(productSchema).build())
                    .field("order", orderSchema)
                    .build();

                final Struct phoneValue = new Struct(phoneSchema);
                phoneValue.put("type", "landline");
                phoneValue.put("number", "0911 910 5491");
                final Struct customerValue = new Struct(customerSchema);
                customerValue.put("name", "Helen Velazquez");
                customerValue.put("phone", phoneValue);
                customerValue.put("email", "mus.donec.dignissim@yahoo.ca");
                customerValue.put("address", "3249 Hendrerit Av.");
                customerValue.put("postalZip", "F2 1IX");
                customerValue.put("region", "Dunbartonshire");
                final Struct product0 = new Struct(productSchema);
                product0.put("brand", "Acme Inc");
                product0.put("item", "Awesome-ivator");
                product0.put("quantity", 1);
                final Struct product1 = new Struct(productSchema);
                product1.put("brand", "Globex");
                product1.put("item", "Widget");
                product1.put("quantity", 2);
                final Struct orderValue = new Struct(orderSchema);
                orderValue.put("date", "2023-11-05 22:11:00");
                final Struct value = new Struct(schema);
                value.put("customer", customerValue);
                value.put("product", List.of(product0, product1));
                value.put("order", orderValue);

                return new SchemaAndValue(schema, value);
            }
            case "002": {
                final Schema test1aSchema = SchemaBuilder.struct()
                    .field("test1aa", SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .field("test1ab", SchemaBuilder.array(Schema.FLOAT64_SCHEMA).build())
                    .field("test1ac", SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build())
                    .field("test1ad", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .build();
                final Schema test1Schema = SchemaBuilder.struct()
                    .field("test1a", test1aSchema)
                    .field("test1b", Schema.STRING_SCHEMA)
                    .field("test1c", Schema.STRING_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test1", test1Schema)
                    .build();

                final Struct test1aValue = new Struct(test1aSchema);
                test1aValue.put("test1aa", List.of(Integer.valueOf(10), Integer.valueOf(20), Integer.valueOf(30)));
                test1aValue.put("test1ab", List.of(1.1d, 1.2d));
                test1aValue.put("test1ac", List.of(true, false, true));
                test1aValue.put("test1ad", List.of("abc", "def", "ghi", "jkl"));
                final Struct test1Value = new Struct(test1Schema);
                test1Value.put("test1a", test1aValue);
                test1Value.put("test1b", "ppp");
                test1Value.put("test1c", "qqq");
                final Struct value = new Struct(schema);
                value.put("test1", test1Value);

                return new SchemaAndValue(schema, value);
            }
            case "003": {
                final Schema listsSchema = SchemaBuilder.struct()
                    .field("strings", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("floats", SchemaBuilder.array(Schema.FLOAT32_SCHEMA).build())
                    .field("doubles", SchemaBuilder.array(Schema.FLOAT64_SCHEMA).build())
                    .field("shorts", SchemaBuilder.array(Schema.INT16_SCHEMA).build())
                    .field("ints", SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .field("longs", SchemaBuilder.array(Schema.INT64_SCHEMA).build())
                    .field("booleans", SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build())
                    .build();
                final Schema itemsSchema = SchemaBuilder.struct()
                    .field("string", Schema.STRING_SCHEMA)
                    .field("float", Schema.FLOAT32_SCHEMA)
                    .field("double", Schema.FLOAT64_SCHEMA)
                    .field("short", Schema.INT16_SCHEMA)
                    .field("int", Schema.INT32_SCHEMA)
                    .field("long", Schema.INT64_SCHEMA)
                    .field("boolean", Schema.BOOLEAN_SCHEMA)
                    .build();
                final Schema optionalsSchema = SchemaBuilder.struct()
                    .field("string", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("float", Schema.OPTIONAL_FLOAT32_SCHEMA)
                    .field("double", Schema.OPTIONAL_FLOAT64_SCHEMA)
                    .field("short", Schema.OPTIONAL_INT16_SCHEMA)
                    .field("int", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("long", Schema.OPTIONAL_INT64_SCHEMA)
                    .field("boolean", Schema.OPTIONAL_BOOLEAN_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("lists", listsSchema)
                    .field("items", itemsSchema)
                    .field("optionals", optionalsSchema)
                    .build();

                final Struct listsValue = new Struct(listsSchema);
                listsValue.put("strings", List.of("one", "two"));
                listsValue.put("floats", List.of(Float.valueOf(1.1f), Float.valueOf(1.2f)));
                listsValue.put("doubles", List.of(Double.valueOf(1.1), Double.valueOf(1.2)));
                listsValue.put("shorts", List.of(Short.valueOf((short) 1), Short.valueOf((short) 2)));
                listsValue.put("ints", List.of(Integer.valueOf(1), Integer.valueOf(2)));
                listsValue.put("longs", List.of(Long.valueOf(1), Long.valueOf(2)));
                listsValue.put("booleans", List.of(true, false));
                final Struct itemsValue = new Struct(itemsSchema);
                itemsValue.put("string", "three");
                itemsValue.put("float", Float.valueOf(1.3f));
                itemsValue.put("double", Double.valueOf(1.3));
                itemsValue.put("short", Short.valueOf((short) 3));
                itemsValue.put("int", Integer.valueOf(3));
                itemsValue.put("long", Long.valueOf(3));
                itemsValue.put("boolean", false);
                final Struct optionalsValue = new Struct(optionalsSchema);
                optionalsValue.put("string", "four");
                optionalsValue.put("float", Float.valueOf(1.4f));
                optionalsValue.put("double", Double.valueOf(1.4));
                optionalsValue.put("short", Short.valueOf((short) 4));
                optionalsValue.put("int", Integer.valueOf(4));
                optionalsValue.put("long", Long.valueOf(4));
                optionalsValue.put("boolean", true);
                final Struct value = new Struct(schema);
                value.put("lists", listsValue);
                value.put("items", itemsValue);
                value.put("optionals", optionalsValue);

                return new SchemaAndValue(schema, value);
            }
            case "004": {
                final Schema schema = SchemaBuilder.array(Schema.STRING_SCHEMA).build();
                final List<String> value = List.of("list", "of", "strings");

                return new SchemaAndValue(schema, value);
            }
            case "005": {
                final Schema itemSchema = SchemaBuilder.struct()
                    .field("message", Schema.STRING_SCHEMA)
                    .field("count", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.array(itemSchema).build();

                final Struct item0 = new Struct(itemSchema);
                item0.put("message", "list of objects");
                item0.put("count", 123);
                final Struct item1 = new Struct(itemSchema);
                item1.put("message", "another object");
                item1.put("count", 456);
                final List<Struct> value = List.of(item0, item1);

                return new SchemaAndValue(schema, value);
            }
            case "006": {
                final Schema test1Schema = SchemaBuilder.struct()
                    .field("test2", Schema.STRING_SCHEMA)
                    .build();
                final Schema test5Schema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("test6", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("test7", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Schema test3Schema = SchemaBuilder.struct()
                    .field("test5", SchemaBuilder.array(test5Schema).build())
                    .field("test4", Schema.STRING_SCHEMA)
                    .build();
                final Schema outerSchema = SchemaBuilder.struct()
                    .field("test3", test3Schema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test1", test1Schema)
                    .field("outer", outerSchema)
                    .build();

                final Struct test1Value = new Struct(test1Schema);
                test1Value.put("test2", "one");
                final Struct test5_0 = new Struct(test5Schema);
                test5_0.put("test6", "three");
                test5_0.put("entry", "test message");
                final Struct test5_1 = new Struct(test5Schema);
                test5_1.put("test6", "four");
                test5_1.put("entry", "next item");
                final Struct test5_2 = new Struct(test5Schema);
                test5_2.put("test7", "five");
                test5_2.put("entry", "final item");
                final Struct test3Value = new Struct(test3Schema);
                test3Value.put("test4", "two");
                test3Value.put("test5", List.of(test5_0, test5_1, test5_2));
                final Struct outerValue = new Struct(outerSchema);
                outerValue.put("test3", test3Value);
                final Struct value = new Struct(schema);
                value.put("test1", test1Value);
                value.put("outer", outerValue);

                return new SchemaAndValue(schema, value);
            }
            case "007": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("type0", Schema.STRING_SCHEMA)
                    .field("type1", Schema.STRING_SCHEMA)
                    .field("type2", Schema.BYTES_SCHEMA)
                    .field("type3", Schema.BOOLEAN_SCHEMA)
                    .field("type4", Schema.STRING_SCHEMA)
                    .field("type5", Schema.STRING_SCHEMA)
                    .field("type6", Schema.FLOAT64_SCHEMA)
                    .field("type7", Schema.FLOAT64_SCHEMA)
                    .field("type8", Schema.STRING_SCHEMA)
                    .field("type9", Schema.FLOAT32_SCHEMA)
                    .field("type10", Schema.STRING_SCHEMA)
                    .field("type11", Schema.STRING_SCHEMA)
                    .field("type12", Schema.STRING_SCHEMA)
                    .field("type13", Schema.STRING_SCHEMA)
                    .field("type14", Schema.STRING_SCHEMA)
                    .field("type15", Schema.STRING_SCHEMA)
                    .field("type17", Schema.STRING_SCHEMA)
                    .field("type18", Schema.STRING_SCHEMA)
                    .field("type19", Schema.INT32_SCHEMA)
                    .field("type20", Schema.INT64_SCHEMA)
                    .field("type21", Schema.INT32_SCHEMA)
                    .field("type22", Schema.INT16_SCHEMA)
                    .field("type24", Schema.INT32_SCHEMA)
                    .field("type25", Schema.INT32_SCHEMA)
                    .field("type26", Schema.INT64_SCHEMA)
                    .field("type27", Schema.INT32_SCHEMA)
                    .field("type28", Schema.INT16_SCHEMA)
                    .field("type30", Schema.INT32_SCHEMA)
                    .field("type31", Schema.INT32_SCHEMA)
                    .field("type32", Schema.STRING_SCHEMA)
                    .field("optType0", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType1", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType2", Schema.OPTIONAL_BYTES_SCHEMA)
                    .field("optType3", Schema.OPTIONAL_BOOLEAN_SCHEMA)
                    .field("optType4", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType5", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType6", Schema.OPTIONAL_FLOAT64_SCHEMA)
                    .field("optType7", Schema.OPTIONAL_FLOAT64_SCHEMA)
                    .field("optType8", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType9", Schema.OPTIONAL_FLOAT32_SCHEMA)
                    .field("optType10", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType11", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType12", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType13", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType14", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType15", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType17", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType18", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("optType19", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType20", Schema.OPTIONAL_INT64_SCHEMA)
                    .field("optType21", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType22", Schema.OPTIONAL_INT16_SCHEMA)
                    .field("optType24", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType25", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType26", Schema.OPTIONAL_INT64_SCHEMA)
                    .field("optType27", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType28", Schema.OPTIONAL_INT16_SCHEMA)
                    .field("optType30", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType31", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("optType32", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
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

                return new SchemaAndValue(schema, value);
            }
            case "008": {
                final Schema value3Schema = SchemaBuilder.struct()
                    .field("level2List1", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("level2List2", SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .build();
                final Schema repeatingItemSchema = SchemaBuilder.struct()
                    .field("level3Value6", Schema.STRING_SCHEMA)
                    .field("level3Value7", Schema.STRING_SCHEMA)
                    .build();
                final Schema repeatingItemSchemaOptional = SchemaBuilder.struct()
                    .field("level3Value6", Schema.STRING_SCHEMA)
                    .field("level3Value7", Schema.STRING_SCHEMA)
                    .optional()
                    .build();
                final Schema value2Schema = SchemaBuilder.struct()
                    .field("level2RepeatingItem1", repeatingItemSchemaOptional)
                    .field("level2RepeatingItem2", repeatingItemSchema)
                    .field("level2RepeatingItem3", repeatingItemSchema)
                    .optional()
                    .build();
                final Schema option2Schema = SchemaBuilder.struct()
                    .field("level3Value4", Schema.BOOLEAN_SCHEMA)
                    .field("level3Value5", Schema.FLOAT64_SCHEMA)
                    .optional()
                    .build();
                final Schema option1Schema = SchemaBuilder.struct()
                    .field("level3Value1", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("level3Value2", Schema.STRING_SCHEMA)
                    .field("level3Value3", Schema.INT32_SCHEMA)
                    .optional()
                    .build();
                final Schema value1Schema = SchemaBuilder.struct()
                    .field("level2Option1", option1Schema)
                    .field("level2Option2", option2Schema)
                    .field("level2Option3", SchemaBuilder.array(repeatingItemSchema).optional().build())
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("level1Value1", value1Schema)
                    .field("level1Value2", value2Schema)
                    .field("level1Value3", value3Schema)
                    .build();

                final Struct value3Value = new Struct(value3Schema);
                value3Value.put("level2List1", Collections.EMPTY_LIST);
                value3Value.put("level2List2", List.of(10, 20, 30));
                final Struct value2Item0 = new Struct(repeatingItemSchema);
                value2Item0.put("level3Value6", "g");
                value2Item0.put("level3Value7", "h");
                final Struct value2Item1 = new Struct(repeatingItemSchema);
                value2Item1.put("level3Value6", "i");
                value2Item1.put("level3Value7", "j");
                final Struct value2Value = new Struct(value2Schema);
                value2Value.put("level2RepeatingItem2", value2Item0);
                value2Value.put("level2RepeatingItem3", value2Item1);
                final Struct value1Item = new Struct(repeatingItemSchema);
                value1Item.put("level3Value6", "third");
                value1Item.put("level3Value7", "option");
                final Struct value1Value = new Struct(value1Schema);
                value1Value.put("level2Option3", List.of(value1Item));
                final Struct value = new Struct(schema);
                value.put("level1Value1", value1Value);
                value.put("level1Value2", value2Value);
                value.put("level1Value3", value3Value);

                return new SchemaAndValue(schema, value);
            }
            case "009": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test", Schema.FLOAT64_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("test", 1.23123123123123);

                return new SchemaAndValue(schema, value);
            }
            case "010": {
                final Schema item1 = SchemaBuilder.struct()
                    .field("item1-a", Schema.BOOLEAN_SCHEMA)
                    .field("item1-b", Schema.STRING_SCHEMA)
                    .field("item1-c", Schema.OPTIONAL_BOOLEAN_SCHEMA)
                    .field("item1-d", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("item1-e", SchemaBuilder.array(Schema.STRING_SCHEMA))
                    .build();
                final Schema item2 = SchemaBuilder.array(Schema.STRING_SCHEMA).build();
                final Schema item3_entry = SchemaBuilder.struct()
                    .field("item3-entry-a", Schema.STRING_SCHEMA)
                    .field("item3-entry-b", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("item3-entry-c", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Schema item3 = SchemaBuilder.array(item3_entry).build();
                final Schema item4 = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema item5_entry = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema item5 = SchemaBuilder.array(item5_entry).build();
                final Schema item6_entry = SchemaBuilder.struct()
                    .field("message", Schema.STRING_SCHEMA)
                    .field("num", Schema.INT32_SCHEMA)
                    .field("test", Schema.BOOLEAN_SCHEMA)
                    .build();
                final Schema item6 = SchemaBuilder.map(Schema.INT32_SCHEMA, item6_entry).build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("item1", item1)
                    .field("item2", item2)
                    .field("item3", item3)
                    .field("item4", item4)
                    .field("item5", item5)
                    .field("item6", item6)
                    .build();

                final Struct item1Value = new Struct(item1);
                item1Value.put("item1-a", false);
                item1Value.put("item1-b", "abc");
                item1Value.put("item1-c", true);
                item1Value.put("item1-e", List.of("doo", "foo", "goo"));
                final List<String> item2Value = List.of("alpha", "beta", "gamma");
                final Struct item3Entry1 = new Struct(item3_entry);
                item3Entry1.put("item3-entry-a", "AAA");
                item3Entry1.put("item3-entry-b", List.of("A", "B"));
                item3Entry1.put("item3-entry-c", "BBB");
                final Struct item3Entry2 = new Struct(item3_entry);
                item3Entry2.put("item3-entry-a", "CCC");
                item3Entry2.put("item3-entry-b", List.of("C", "D", "E"));
                final Struct item3Entry3 = new Struct(item3_entry);
                item3Entry3.put("item3-entry-a", "FFF");
                item3Entry3.put("item3-entry-b", List.of("F", "G"));
                item3Entry3.put("item3-entry-c", "GGG");
                final List<Struct> item3Value = List.of(item3Entry1, item3Entry2, item3Entry3);
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
                final Struct item6Entry1 = new Struct(item6_entry);
                item6Entry1.put("message", "hello");
                item6Entry1.put("num", 1000);
                item6Entry1.put("test", true);
                final Struct item6Entry2 = new Struct(item6_entry);
                item6Entry2.put("message", "world");
                item6Entry2.put("num", 2000);
                item6Entry2.put("test", false);
                final Struct item6Entry3 = new Struct(item6_entry);
                item6Entry3.put("message", "xxx");
                item6Entry3.put("num", 3000);
                item6Entry3.put("test", true);
                final Map<Integer, Struct> item6Value = new LinkedHashMap<>();
                item6Value.put(2, item6Entry1);
                item6Value.put(5, item6Entry2);
                item6Value.put(9, item6Entry3);

                final Struct value = new Struct(schema);
                value.put("item1", item1Value);
                value.put("item2", item2Value);
                value.put("item3", item3Value);
                value.put("item4", item4Value);
                value.put("item5", item5Value);
                value.put("item6", item6Value);

                return new SchemaAndValue(schema, value);
            }
            case "011": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("the-map", SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build())
                    .build();

                final Map<String, String> mapValue = new LinkedHashMap<>();
                mapValue.put("key1", "value1");
                mapValue.put("key2", "value2");
                mapValue.put("key3", "value3");

                final Struct value = new Struct(schema);
                value.put("the-map", mapValue);

                return new SchemaAndValue(schema, value);
            }
            case "012": {
                final Schema schema = SchemaBuilder.struct()
                    .field("one-of-the-maps",
                           SchemaBuilder.array(
                               SchemaBuilder.map(
                                   Schema.STRING_SCHEMA, Schema.STRING_SCHEMA
                               ).build()
                           ).build());

                final Map<String, String> item0 = new LinkedHashMap<>();
                item0.put("key1", "value1");
                item0.put("key2", "value2");
                item0.put("key3", "value3");
                final Map<String, String> item1 = new LinkedHashMap<>();
                item1.put("keyA", "valueA");
                item1.put("keyB", "valueB");

                final Struct value = new Struct(schema);
                value.put("one-of-the-maps", List.of(item0, item1));

                return new SchemaAndValue(schema, value);
            }
            case "013": {
                final Schema testValueSchema = SchemaBuilder.array(Schema.INT32_SCHEMA).build();
                final Schema innerSchema = SchemaBuilder.array(testValueSchema).build();
                final Schema middleSchema = SchemaBuilder.array(innerSchema).build();
                final Schema outerSchema = SchemaBuilder.array(middleSchema).build();
                final Schema schema = SchemaBuilder.struct()
                        .field("test", SchemaBuilder.array(outerSchema).build())
                        .build();

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

                final Struct value = new Struct(schema);
                value.put("test", List.of(o1, o2));

                return new SchemaAndValue(schema, value);
            }
            case "014": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build();

                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                final List<Boolean> val0 = List.of(true, false, true);
                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                final List<Boolean> val1 = List.of(false, true);

                final Map<Struct, List<Boolean>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "015": {
                final Schema keyItem = SchemaBuilder.struct()
                    .field("keyFloat", Schema.FLOAT32_SCHEMA)
                    .field("keyBool", Schema.BOOLEAN_SCHEMA)
                    .build();
                final Schema keySchema = SchemaBuilder.array(keyItem).build();
                final Schema valueSchema = Schema.STRING_SCHEMA;

                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct key0a = new Struct(keyItem);
                key0a.put("keyFloat", 7.8f);
                key0a.put("keyBool", true);
                final Struct key0b = new Struct(keyItem);
                key0b.put("keyFloat", 17.3f);
                key0b.put("keyBool", false);
                final List<Struct> key0 = List.of(key0a, key0b);

                final Struct key1a = new Struct(keyItem);
                key1a.put("keyFloat", 3.9f);
                key1a.put("keyBool", false);
                final Struct key1b = new Struct(keyItem);
                key1b.put("keyFloat", 6.2f);
                key1b.put("keyBool", true);
                final List<Struct> key1 = List.of(key1a, key1b);

                final Map<List<Struct>, String> value = new LinkedHashMap<>();
                value.put(key0, "value0");
                value.put(key1, "value1");

                return new SchemaAndValue(schema, value);
            }
            case "016": {
                final Schema keySchema = Schema.INT64_SCHEMA;
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("bools", SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build())
                    .field("score", Schema.FLOAT64_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct val0 = new Struct(valueSchema);
                val0.put("bools", List.of(true, true));
                val0.put("score", 3.456d);
                final Struct val1 = new Struct(valueSchema);
                val1.put("bools", List.of(false, false));
                val1.put("score", 9.887d);
                final Struct val2 = new Struct(valueSchema);
                val2.put("bools", List.of(true, false));
                val2.put("score", 100.00001d);

                final Map<Long, Struct> value = new LinkedHashMap<>();
                value.put(Long.valueOf(3), val0);
                value.put(Long.valueOf(9), val1);
                value.put(Long.valueOf(100), val2);

                return new SchemaAndValue(schema, value);
            }
            case "017": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyA", Schema.STRING_SCHEMA)
                    .field("keyB", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valA", Schema.STRING_SCHEMA)
                    .field("valB", Schema.STRING_SCHEMA)
                    .build();
                final Schema mapSchema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("maps", SchemaBuilder.array(mapSchema).build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyA", "AAAA");
                key0.put("keyB", "BBBB");
                final Struct val0 = new Struct(valueSchema);
                val0.put("valA", "CCCC");
                val0.put("valB", "DDDD");

                final Struct key1 = new Struct(keySchema);
                key1.put("keyA", "EEEE");
                key1.put("keyB", "FFFF");
                final Struct val1 = new Struct(valueSchema);
                val1.put("valA", "GGGG");
                val1.put("valB", "HHHH");

                final Struct key2 = new Struct(keySchema);
                key2.put("keyA", "IIII");
                key2.put("keyB", "JJJJ");
                final Struct val2 = new Struct(valueSchema);
                val2.put("valA", "KKKK");
                val2.put("valB", "LLLL");

                final Map<Struct, Struct> firstMap = new LinkedHashMap<>();
                firstMap.put(key0, val0);
                firstMap.put(key1, val1);
                firstMap.put(key2, val2);

                final Struct key3 = new Struct(keySchema);
                key3.put("keyA", "MMMM");
                key3.put("keyB", "NNNN");
                final Struct val3 = new Struct(valueSchema);
                val3.put("valA", "OOOO");
                val3.put("valB", "PPPP");

                final Struct key4 = new Struct(keySchema);
                key4.put("keyA", "QQQQ");
                key4.put("keyB", "RRRR");
                final Struct val4 = new Struct(valueSchema);
                val4.put("valA", "SSSS");
                val4.put("valB", "TTTT");

                final Map<Struct, Struct> secondMap = new LinkedHashMap<>();
                secondMap.put(key3, val3);
                secondMap.put(key4, val4);

                final Struct value = new Struct(schema);
                value.put("maps", List.of(firstMap, secondMap));

                return new SchemaAndValue(schema, value);
            }
            case "018": {
                final Schema innerKeySchema = SchemaBuilder.struct()
                    .field("inner", Schema.STRING_SCHEMA)
                    .build();
                final Schema keySchema = SchemaBuilder
                    .map(innerKeySchema,
                         SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .build();
                final Schema valueSchema = SchemaBuilder
                    .map(Schema.STRING_SCHEMA, Schema.FLOAT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct innerKey0 = new Struct(innerKeySchema);
                innerKey0.put("inner", "theInnerKey0");
                final Struct innerKey1 = new Struct(innerKeySchema);
                innerKey1.put("inner", "theInnerKey1");
                final Struct innerKey2 = new Struct(innerKeySchema);
                innerKey2.put("inner", "theInnerKey2");
                final Map<Struct, List<Integer>> firstKey = new LinkedHashMap<>();
                firstKey.put(innerKey0, List.of(10, 11, 12));
                firstKey.put(innerKey1, List.of(20, 21, 22));
                firstKey.put(innerKey2, List.of(30, 31, 32));
                final Map<String, Float> firstValue = new LinkedHashMap<>();
                firstValue.put("innerValue1", 111.111f);
                firstValue.put("innerValue2", 222.222f);
                firstValue.put("innerValue3", 333.333f);

                final Struct innerKey3 = new Struct(innerKeySchema);
                innerKey3.put("inner", "theInnerKey3");
                final Struct innerKey4 = new Struct(innerKeySchema);
                innerKey4.put("inner", "theInnerKey4");
                final Map<Struct, List<Integer>> secondKey = new LinkedHashMap<>();
                secondKey.put(innerKey3, List.of(40, 41));
                secondKey.put(innerKey4, List.of(50, 51));
                final Map<String, Float> secondValue = new LinkedHashMap<>();
                secondValue.put("innerValue4", 444.444f);
                secondValue.put("innerValue5", 555.555f);
                secondValue.put("innerValue6", 666.666f);
                secondValue.put("innerValue7", 777.777f);

                final Map<Map<Struct, List<Integer>>, Map<String, Float>> value = new LinkedHashMap<>();
                value.put(firstKey, firstValue);
                value.put(secondKey, secondValue);

                return new SchemaAndValue(schema, value);
            }
            case "019": {
                final Schema mapKeySchema = SchemaBuilder.array(Schema.STRING_SCHEMA).build();
                final Schema mapValueSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.INT32_SCHEMA).build();
                final Schema schema = SchemaBuilder.map(mapKeySchema, mapValueSchema).build();

                final Map<String, Integer> letters1 = new LinkedHashMap<>();
                letters1.put("A", 1);
                letters1.put("B", 2);
                letters1.put("C", 3);
                final Map<String, Integer> letters2 = new LinkedHashMap<>();
                letters2.put("D", 4);
                letters2.put("E", 5);

                final Map<List<String>, Map<String, Integer>> value = new LinkedHashMap<>();
                value.put(List.of("a", "b", "c"), letters1);
                value.put(List.of("d", "e"), letters2);

                return new SchemaAndValue(schema, value);
            }
            case "020": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Struct value0 = new Struct(valueSchema);
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Struct value1 = new Struct(valueSchema);
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);

                final Struct key2 = new Struct(keySchema);
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);
                final Struct value2 = new Struct(valueSchema);
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);

                final Map<Struct, Struct> value = new LinkedHashMap<>();
                value.put(key0, value0);
                value.put(key1, value1);
                value.put(key2, value2);

                return new SchemaAndValue(schema, value);
            }
            case "021": {
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.map(Schema.STRING_SCHEMA, valueSchema).build();

                final Struct value0 = new Struct(valueSchema);
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);
                final Struct value1 = new Struct(valueSchema);
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);
                final Struct value2 = new Struct(valueSchema);
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);

                final Map<String, Struct> value = new LinkedHashMap<>();
                value.put("key0", value0);
                value.put("key1", value1);
                value.put("key2", value2);

                return new SchemaAndValue(schema, value);
            }
            case "022": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.map(keySchema, Schema.STRING_SCHEMA).build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Struct key2 = new Struct(keySchema);
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);

                final Map<Struct, String> value = new LinkedHashMap<>();
                value.put(key0, "value0");
                value.put(key1, "value1");
                value.put(key2, "value2");

                return new SchemaAndValue(schema, value);
            }
            case "023": {
                final Schema schema = SchemaBuilder.map(Schema.INT32_SCHEMA, Schema.INT32_SCHEMA).build();

                final Map<Integer, Integer> value = new LinkedHashMap<>();
                value.put(1, 10);
                value.put(2, 20);
                value.put(3, 30);
                value.put(4, 40);
                value.put(5, 50);

                return new SchemaAndValue(schema, value);
            }
            case "024": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("optString1", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("string1", Schema.STRING_SCHEMA)
                    .field("optString2", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("string2", Schema.STRING_SCHEMA)
                    .field("optString3", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("string1", "ONE");
                value.put("optString2", "two");
                value.put("string2", "TWO");
                value.put("optString3", null);

                return new SchemaAndValue(schema, value);
            }
            case "025": {
                final Schema schema = SchemaBuilder.map(Schema.OPTIONAL_STRING_SCHEMA, Schema.FLOAT64_SCHEMA).build();

                final Map<Object, Double> value = new LinkedHashMap<>();
                value.put("first", 1.1d);
                value.put(null, 2.2d);
                value.put("third", 3.3d);

                return new SchemaAndValue(schema, value);
            }
            case "026": {
                final Schema schema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();

                final Map<String, String> value = new LinkedHashMap<>();
                value.put("123", "unusual key 1");
                value.put("", "unusual key 2");
                value.put("Hello!", "unusual key 3");
                value.put("my-key", "valid key 1");
                value.put("12key", "unusual key 4");
                value.put("*HELLO*", "unusual key 5");
                value.put("my-key-2", "valid key 2");
                value.put("Hello World", "unusual key 6");

                return new SchemaAndValue(schema, value);
            }
            case "027": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("mystring", Schema.STRING_SCHEMA)
                    .field("onechar", Schema.STRING_SCHEMA)
                    .field("mybytes", Schema.BYTES_SCHEMA)
                    .field("onebyte", Schema.BYTES_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("mystring", "Hello World");
                value.put("onechar", "H");
                value.put("mybytes", "Hello World".getBytes());
                value.put("onebyte", "H".getBytes());

                return new SchemaAndValue(schema, value);
            }
            case "028": {
                final Schema byteMapSchema = SchemaBuilder.map(Schema.BYTES_SCHEMA, Schema.BYTES_SCHEMA).build();
                final Schema strToByteMapSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.BYTES_SCHEMA).build();
                final Schema byteToStrMapSchema = SchemaBuilder.map(Schema.BYTES_SCHEMA, Schema.STRING_SCHEMA).build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("bytes", byteMapSchema)
                    .field("strToBytes", strToByteMapSchema)
                    .field("bytesToStr", byteToStrMapSchema)
                    .build();

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

                final Struct value = new Struct(schema);
                value.put("bytes", bytesMap);
                value.put("strToBytes", strToBytesMap);
                value.put("bytesToStr", bytesToStrMap);

                return new SchemaAndValue(schema, value);
            }
            case "029": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .field("keyFloat", Schema.FLOAT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build();

                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                key0.put("keyFloat", 1.23f);
                final List<Boolean> val0 = List.of(true, false, true);
                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                key1.put("keyFloat", 4.56f);
                final List<Boolean> val1 = List.of(false, true);

                final Map<Struct, List<Boolean>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "030": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .field("keyFloat", Schema.FLOAT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .field("valueFloat", Schema.FLOAT32_SCHEMA)
                    .build();

                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                key0.put("keyFloat", 1.23f);
                final Struct val0 = new Struct(valueSchema);
                val0.put("valueLetter", "XXX");
                val0.put("valueNumber", 678);
                val0.put("valueFloat", 5.67f);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                key1.put("keyFloat", 4.56f);
                final Struct val1 = new Struct(valueSchema);
                val1.put("valueLetter", "YYY");
                val1.put("valueNumber", 789);
                val1.put("valueFloat", 7.89f);

                final Map<Struct, Struct> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "031": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema valueSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, String>, Map<String, String>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "032": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema valueSchema = Schema.STRING_SCHEMA;
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, String>, String> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "033": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema valueSchema = Schema.STRING_SCHEMA;
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, String>, String> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "034": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema valueSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, String>, Map<String, String>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "035": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema valueSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, String>, Map<String, String>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "036": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema valueSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, String>, Map<String, String>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "037": {
                final Schema value1Schema = SchemaBuilder.struct()
                    .field("key3", Schema.STRING_SCHEMA)
                    .field("key4", Schema.STRING_SCHEMA)
                    .build();
                final Schema value2Schema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("key5", Schema.STRING_SCHEMA)
                    .field("key6", Schema.STRING_SCHEMA)
                    .build();
                final Schema value3Schema = SchemaBuilder.struct()
                    .field("inner", Schema.STRING_SCHEMA)
                    .field("key7", Schema.STRING_SCHEMA)
                    .field("key8", Schema.STRING_SCHEMA)
                    .build();
                final Schema value4Schema = SchemaBuilder.struct()
                    .field("inside", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("key9", Schema.STRING_SCHEMA)
                    .field("key10", Schema.STRING_SCHEMA)
                    .build();
                final Schema value5Schema = SchemaBuilder.struct()
                    .field("key13", Schema.STRING_SCHEMA)
                    .field("key14", Schema.STRING_SCHEMA)
                    .field("key11", Schema.STRING_SCHEMA)
                    .field("key12", Schema.STRING_SCHEMA)
                    .build();
                final Schema value6Schema = SchemaBuilder.struct()
                    .field("key17", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("key18", Schema.STRING_SCHEMA)
                    .field("key19", Schema.STRING_SCHEMA)
                    .field("key15", Schema.STRING_SCHEMA)
                    .field("key16", Schema.STRING_SCHEMA)
                    .build();
                final Schema item1Schema = SchemaBuilder.struct()
                    .field("value1", value1Schema)
                    .field("value2", value2Schema)
                    .field("value3", value3Schema)
                    .field("value4", value4Schema)
                    .field("value5", value5Schema)
                    .field("value6", value6Schema)
                    .field("key1", Schema.STRING_SCHEMA)
                    .field("key2", Schema.STRING_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct().name("root").field("item1", item1Schema).build();

                final Struct value1 = new Struct(value1Schema);
                value1.put("key3", "itemValue3");
                value1.put("key4", "itemValue4");
                final Struct value2 = new Struct(value2Schema);
                value2.put("key5", "itemValue5");
                value2.put("key6", "itemValue6");
                value2.put("entry", "VALUE TWO");
                final Struct value3 = new Struct(value3Schema);
                value3.put("key7", "itemValue7");
                value3.put("key8", "itemValue8");
                value3.put("inner", "INNER");
                final Struct value4 = new Struct(value4Schema);
                value4.put("key9", "itemValue9");
                value4.put("key10", "itemValue10");
                value4.put("inside", List.of("INSIDE", "OUT"));
                final Struct value5 = new Struct(value5Schema);
                value5.put("key11", "itemValue11");
                value5.put("key12", "itemValue12");
                value5.put("key13", "itemValue13");
                value5.put("key14", "itemValue14");
                final Struct value6 = new Struct(value6Schema);
                value6.put("key15", "itemValue15");
                value6.put("key16", "itemValue16");
                value6.put("key17", List.of("list17ItemONE", "list17ItemTWO", "list17ItemTHREE"));
                value6.put("key18", "itemValue18");
                value6.put("key19", "itemValue19");
                final Struct item1 = new Struct(item1Schema);
                item1.put("key1", "itemKey1");
                item1.put("key2", "itemKey2");
                item1.put("value1", value1);
                item1.put("value2", value2);
                item1.put("value3", value3);
                item1.put("value4", value4);
                item1.put("value5", value5);
                item1.put("value6", value6);

                final Struct value = new Struct(schema);
                value.put("item1", item1);

                return new SchemaAndValue(schema, value);
            }
            case "038": {
                final Schema keySchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.INT32_SCHEMA).build();
                final Schema valueSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.INT32_SCHEMA).build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

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

                final Map<Map<String, Integer>, Map<String, Integer>> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "039": {
                final Schema item1Schema = SchemaBuilder.struct()
                    .field("key1", Schema.STRING_SCHEMA)
                    .field("key2", Schema.STRING_SCHEMA)
                    .field("entry", Schema.STRING_SCHEMA)
                    .build();
                final Schema mapSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.INT32_SCHEMA).build();
                final Schema schema = SchemaBuilder.struct()
                    .field("item1", item1Schema)
                    .field("themap", mapSchema)
                    .field("thebigmap", mapSchema)
                    .build();

                final Struct item1 = new Struct(item1Schema);
                item1.put("key1", "itemKey1");
                item1.put("key2", "itemKey2");
                item1.put("entry", "FIRST");
                final Map<String, Integer> theMap = new LinkedHashMap<>();
                theMap.put("ten", 10);
                theMap.put("hundred", 100);
                final Map<String, Integer> theBigMap = new LinkedHashMap<>();
                theBigMap.put("twenty", 20);
                theBigMap.put("two-hundred", 200);

                final Struct value = new Struct(schema);
                value.put("item1", item1);
                value.put("themap", theMap);
                value.put("thebigmap", theBigMap);

                return new SchemaAndValue(schema, value);
            }
            case "040": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .field("keyCount", Schema.INT32_SCHEMA)
                    .field("entry", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .field("entry", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.map(keySchema, valueSchema).build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyNumber", 1);
                key0.put("keyCount", 10);
                key0.put("entry", 100);
                final Struct val0 = new Struct(valueSchema);
                val0.put("entry", 10000);
                val0.put("valueNumber", 1000);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyNumber", 2);
                key1.put("keyCount", 20);
                key1.put("entry", 200);
                final Struct val1 = new Struct(valueSchema);
                val1.put("entry", 20000);
                val1.put("valueNumber", 2000);

                final Map<Struct, Struct> value = new LinkedHashMap<>();
                value.put(key0, val0);
                value.put(key1, val1);

                return new SchemaAndValue(schema, value);
            }
            case "041": {
                final Schema schema = SchemaBuilder.struct()
                    .name("custom-name")
                    .field("myint", Schema.INT32_SCHEMA)
                    .field("mystring", Schema.STRING_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("myint", 123);
                value.put("mystring", "hello world");

                return new SchemaAndValue(schema, value);
            }
            case "042": {
                final Schema schema = SchemaBuilder.struct()
                    .name("This Is Not A Good Tag Name")
                    .field("myint", Schema.INT32_SCHEMA)
                    .field("mystring", Schema.STRING_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("myint", 123);
                value.put("mystring", "hello world");

                return new SchemaAndValue(schema, value);
            }
            case "043": {
                final Schema mapSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.FLOAT32_SCHEMA).build();
                final Schema listOfMapsSchema = SchemaBuilder.array(mapSchema).build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("maps", SchemaBuilder.array(listOfMapsSchema).build())
                    .build();

                final Map<String, Float> map1 = new LinkedHashMap<>();
                map1.put("one point two", 1.2f);
                map1.put("two point three", 2.3f);
                map1.put("three point four", 3.4f);

                final Map<String, Float> map2 = new LinkedHashMap<>();
                map2.put("ten point five", 10.5f);
                map2.put("eleven point seven", 11.7f);

                final List<Map<String, Float>> listOfMaps1 = List.of(map1, map2);

                final Map<String, Float> map3 = new LinkedHashMap<>();
                map3.put("pi", 3.14f);
                map3.put("half", 0.5f);

                final Map<String, Float> map4 = new LinkedHashMap<>();
                map4.put("first", 1.1f);
                map4.put("second", 2.2f);
                map4.put("third", 3.3f);
                map4.put("fourth", 4.4f);

                final List<Map<String, Float>> listOfMaps2 = List.of(map3, map4);

                final Struct value = new Struct(schema);
                value.put("maps", List.of(listOfMaps1, listOfMaps2));

                return new SchemaAndValue(schema, value);
            }
            case "044": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("integers", SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .field("floats", SchemaBuilder.array(Schema.FLOAT32_SCHEMA).build())
                    .build();
                final Schema valueSchema = SchemaBuilder.map(Schema.BOOLEAN_SCHEMA, Schema.INT32_SCHEMA).build();
                final Schema mapSchema = SchemaBuilder.map(keySchema, valueSchema).build();
                final Schema schema = SchemaBuilder.map(mapSchema, Schema.STRING_SCHEMA).build();

                final Struct key1 = new Struct(keySchema);
                key1.put("integers", List.of(1, 2, 3, 4 ));
                key1.put("floats", List.of(1.1f, 2.2f));
                final Map<Boolean, Integer> val1 = new LinkedHashMap<>();
                val1.put(true, 12);
                val1.put(false, 27);

                final Struct key2 = new Struct(keySchema);
                key2.put("integers", List.of(10, 20, 30));
                key2.put("floats", List.of(1.23f, 4.56f));
                final Map<Boolean, Integer> val2 = new LinkedHashMap<>();
                val2.put(true, 3);
                val2.put(false, 7);

                final Struct key3 = new Struct(keySchema);
                key3.put("integers", List.of(100, 200));
                key3.put("floats", List.of(12.3f, 45.6f, 78.9f));
                final Map<Boolean, Integer> val3 = new LinkedHashMap<>();
                val3.put(true, 1);
                val3.put(false, 1);

                final Map<Struct, Map<Boolean, Integer>> map = new LinkedHashMap<>();
                map.put(key1, val1);
                map.put(key2, val2);
                map.put(key3, val3);

                final Map<Map<Struct, Map<Boolean, Integer>>, String> value = new LinkedHashMap<>();
                value.put(map, "testing");

                return new SchemaAndValue(schema, value);
            }
            case "046": {
                final Schema schema = SchemaBuilder.struct()
                    .field("mostly-numbers", SchemaBuilder.array(Schema.OPTIONAL_INT32_SCHEMA).build())
                    .build();

                final Struct value = new Struct(schema);
                final List<Integer> mostlyNumbers = new ArrayList<>();
                mostlyNumbers.add(1);
                mostlyNumbers.add(4);
                mostlyNumbers.add(7);
                mostlyNumbers.add(null);
                mostlyNumbers.add(13);
                mostlyNumbers.add(16);
                value.put("mostly-numbers", mostlyNumbers);

                return new SchemaAndValue(schema, value);
            }
            case "047": {
                final Schema bytesSchemaWithAnnotation = SchemaBuilder.bytes().doc("xs:byte").build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("optionalByte",          SchemaBuilder.bytes().doc("xs:byte").optional().build())
                    .field("oneByte",               bytesSchemaWithAnnotation)
                    .field("oneUnsignedByte",       bytesSchemaWithAnnotation)
                    .field("multipleBytes",         SchemaBuilder.array(bytesSchemaWithAnnotation).build())
                    .field("multipleUnsignedBytes", SchemaBuilder.array(bytesSchemaWithAnnotation).build())
                    .field("base64EncodedBytes",    Schema.BYTES_SCHEMA)
                    .build();

                final Struct value = new Struct(schema);
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

                return new SchemaAndValue(schema, value);
            }
            case "048": {
                final Schema schema = SchemaBuilder.struct()
                    .name("byte-data")
                    .field("oneByte",               Schema.BYTES_SCHEMA)
                    .field("oneUnsignedByte",       Schema.BYTES_SCHEMA)
                    .field("multipleBytes",         Schema.BYTES_SCHEMA)
                    .field("multipleUnsignedBytes", Schema.BYTES_SCHEMA)
                    .field("base64EncodedBytes",    Schema.BYTES_SCHEMA)
                    .build();

                final Struct value = new Struct(schema);
                value.put("oneByte", new byte[] { 20 });
                value.put("oneUnsignedByte", new byte[] { 40 });
                value.put("multipleBytes", new byte[] { 30, 31, 32, 33, 34 });
                value.put("multipleUnsignedBytes", new byte[] { 60, 61, 62 });
                value.put("base64EncodedBytes", "Secret message".getBytes());

                return new SchemaAndValue(schema, value);
            }
            case "049": {
                final Schema mapSchema = SchemaBuilder.map(
                        SchemaBuilder.array(Schema.STRING_SCHEMA).build(),
                        SchemaBuilder.array(Schema.INT32_SCHEMA).build()
                    ).build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("set-of-strings", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("list-of-strings", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("array-of-strings", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("map-of-arrays", mapSchema)
                    .build();

                final List<String> setOfStrings = new ArrayList<>();
                setOfStrings.add("member");
                setOfStrings.add("of");
                setOfStrings.add("the");
                setOfStrings.add("set");

                final List<String> listOfStrings = new ArrayList<>();
                listOfStrings.add("list");
                listOfStrings.add("members");

                final List<String> arrayOfStrings = List.of("string", "items");

                final Map<List<String>, List<Integer>> mapOfArrays = new LinkedHashMap<>();
                mapOfArrays.put(List.of("one", "two", "three"),
                                List.of(1, 2, 3));
                mapOfArrays.put(List.of("ten", "twenty"),
                                List.of(10, 20));

                final Struct value = new Struct(schema);
                value.put("set-of-strings", setOfStrings);
                value.put("list-of-strings", listOfStrings);
                value.put("array-of-strings", arrayOfStrings);
                value.put("map-of-arrays", mapOfArrays);

                return new SchemaAndValue(schema, value);
            }
            case "052": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test1", Schema.STRING_SCHEMA)
                    .field("test2", Schema.STRING_SCHEMA)
                    .field("myattr1", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("myattr2", Schema.OPTIONAL_INT32_SCHEMA)
                    .build();

                final Struct value = new Struct(schema);
                value.put("test1", "one");
                value.put("test2", "two");
                value.put("myattr1", "testing");
                value.put("myattr2", 123);

                return new SchemaAndValue(schema, value);
            }
            case "053": {
                final Schema test1Schema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("myattr4", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Schema test2bSchema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("myattr5", Schema.STRING_SCHEMA)
                    .build();
                final Schema test2Schema = SchemaBuilder.struct()
                    .field("test2a", Schema.STRING_SCHEMA)
                    .field("test2b", test2bSchema)
                    .build();
                final Schema test3Schema = SchemaBuilder.struct()
                    .field("test3a", Schema.INT32_SCHEMA)
                    .field("myattr6", Schema.STRING_SCHEMA)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test1", test1Schema)
                    .field("test2", test2Schema)
                    .field("test3", test3Schema)
                    .field("test4", Schema.STRING_SCHEMA)
                    .field("myattr1", Schema.STRING_SCHEMA)
                    .field("myattr2", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("myattr3", Schema.BOOLEAN_SCHEMA)
                    .build();

                final Struct test1 = new Struct(test1Schema);
                test1.put("myattr4", "inner");
                test1.put("entry", "one");
                final Struct test2b = new Struct(test2bSchema);
                test2b.put("myattr5", "deepinner");
                test2b.put("entry", "beta");
                final Struct test2 = new Struct(test2Schema);
                test2.put("test2a", "alpha");
                test2.put("test2b", test2b);
                final Struct test3 = new Struct(test3Schema);
                test3.put("myattr6", "middle");
                test3.put("test3a", 333);

                final Struct value = new Struct(schema);
                value.put("test1", test1);
                value.put("test2", test2);
                value.put("test3", test3);
                value.put("test4", "four");
                value.put("myattr1", "testing");
                value.put("myattr2", 123);
                value.put("myattr3", false);

                return new SchemaAndValue(schema, value);
            }
            case "054": {
                final Schema transaccionSchema = SchemaBuilder.struct()
                    .field("numclie", Schema.INT32_SCHEMA)
                    .field("id", Schema.STRING_SCHEMA)
                    .field("tecla", Schema.STRING_SCHEMA)
                    .build();
                final Schema datosSchema = SchemaBuilder.struct()
                    .field("transaccion", transaccionSchema)
                    .build();
                final Schema xmlentradaSchema = SchemaBuilder.struct()
                    .field("datos", datosSchema)
                    .build();
                final Schema dataSchema = SchemaBuilder.struct()
                    .field("xml-entrada", xmlentradaSchema)
                    .field("trama-entrada", Schema.STRING_SCHEMA)
                    .field("mq-server", Schema.STRING_SCHEMA)
                    .field("direccion-IP", Schema.STRING_SCHEMA)
                    .field("nombre-servidor", Schema.STRING_SCHEMA)
                    .field("canal", Schema.STRING_SCHEMA)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("data", dataSchema)
                    .field("date", Schema.STRING_SCHEMA)
                    .field("cr", Schema.STRING_SCHEMA)
                    .field("tx", Schema.STRING_SCHEMA)
                    .field("user", Schema.STRING_SCHEMA)
                    .field("estatus-tx", Schema.STRING_SCHEMA)
                    .field("version", Schema.STRING_SCHEMA)
                    .build();

                final Struct transaccion = new Struct(transaccionSchema);
                transaccion.put("id", "PE80");
                transaccion.put("tecla", "00");
                transaccion.put("numclie", 51372133);
                final Struct datos = new Struct(datosSchema);
                datos.put("transaccion", transaccion);
                final Struct xmlentrada = new Struct(xmlentradaSchema);
                xmlentrada.put("datos", datos);
                final Struct data = new Struct(dataSchema);
                data.put("xml-entrada", xmlentrada);
                data.put("trama-entrada", "longer test string");
                data.put("mq-server", "MQ : SPIAWT99;;;SPIA.QC.QZT1;SPIA.QP.OUT");
                data.put("direccion-IP", "17.127.22.33");
                data.put("nombre-servidor", "Qpbxiaa");
                data.put("canal", "ABC");

                final Struct value = new Struct(schema);
                value.put("date", "2024-08-06 12:32:04");
                value.put("cr", "2246");
                value.put("tx", "PE23");
                value.put("user", "01888329");
                value.put("estatus-tx", "1");
                value.put("version", "7.0.1001.2");
                value.put("data", data);

                return new SchemaAndValue(schema, value);
            }
            case "055":{

                final Schema decimalSchema = Decimal.builder(3).build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("ProductID", Schema.INT32_SCHEMA)
                    .field("ProductName", Schema.STRING_SCHEMA)
                    .field("Price", decimalSchema)
                    .build();
                final Struct value = new Struct(schema);

                BigDecimal bigDecimal = new BigDecimal(10).setScale(3);

                value.put("ProductID", 1);
                value.put("ProductName", "TestProductName");
                value.put("Price", bigDecimal);

                return new SchemaAndValue(schema, value);
            }
        }
        throw new NotImplementedException("Unrecognised test " + testCaseId);
    }


    public static SchemaAndValue generic(String testCaseId) {
        switch (testCaseId) {
            case "004": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .build();

                final Struct value = new Struct(schema);
                value.put("entry", List.of("list", "of", "strings"));

                return new SchemaAndValue(schema, value);
            }
            case "005": {
                final Schema itemSchema = SchemaBuilder.struct()
                    .field("message", Schema.STRING_SCHEMA)
                    .field("count", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(itemSchema).build())
                    .build();

                final Struct item0 = new Struct(itemSchema);
                item0.put("message", "list of objects");
                item0.put("count", 123);
                final Struct item1 = new Struct(itemSchema);
                item1.put("message", "another object");
                item1.put("count", 456);
                final Struct value = new Struct(schema);
                value.put("entry", List.of(item0, item1));

                return new SchemaAndValue(schema, value);
            }
            case "010": {
                final Schema item1 = SchemaBuilder.struct()
                    .field("item1-a", Schema.BOOLEAN_SCHEMA)
                    .field("item1-b", Schema.STRING_SCHEMA)
                    .field("item1-c", Schema.OPTIONAL_BOOLEAN_SCHEMA)
                    .field("item1-d", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("item1-e", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .build();
                final Schema item2 = SchemaBuilder.array(Schema.STRING_SCHEMA).build();
                final Schema item3_entry = SchemaBuilder.struct()
                    .field("item3-entry-a", Schema.STRING_SCHEMA)
                    .field("item3-entry-b", SchemaBuilder.array(Schema.STRING_SCHEMA).build())
                    .field("item3-entry-c", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Schema item3 = SchemaBuilder.array(item3_entry).build();
                final Schema item4_entry = SchemaBuilder.struct()
                        .field("key", Schema.STRING_SCHEMA)
                        .field("value", Schema.STRING_SCHEMA)
                        .build();
                final Schema item4 = SchemaBuilder.struct()
                        .field("entry", SchemaBuilder.array(item4_entry).optional().build())
                        .build();
                final Schema item5_entry = SchemaBuilder.struct()
                        .field("key", Schema.STRING_SCHEMA)
                        .field("value", Schema.STRING_SCHEMA)
                        .build();
                final Schema item5_item = SchemaBuilder.struct()
                        .field("entry", SchemaBuilder.array(item5_entry).optional().build())
                    .build();
                final Schema item5 = SchemaBuilder.array(item5_item).build();
                final Schema item6_value = SchemaBuilder.struct()
                    .field("message", Schema.STRING_SCHEMA)
                    .field("num", Schema.INT32_SCHEMA)
                    .field("test", Schema.BOOLEAN_SCHEMA)
                    .build();
                final Schema item6_entry = SchemaBuilder.struct()
                    .field("key", Schema.INT32_SCHEMA)
                    .field("value", item6_value)
                    .build();
                final Schema item6 = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(item6_entry).optional().build())
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("item1", item1)
                    .field("item2", item2)
                    .field("item3", item3)
                    .field("item4", item4)
                    .field("item5", item5)
                    .field("item6", item6)
                    .build();

                final Struct item1Value = new Struct(item1);
                item1Value.put("item1-a", false);
                item1Value.put("item1-b", "abc");
                item1Value.put("item1-c", true);
                item1Value.put("item1-e", List.of("doo", "foo", "goo"));
                final List<String> item2Value = List.of("alpha", "beta", "gamma");
                final Struct item3Entry1 = new Struct(item3_entry);
                item3Entry1.put("item3-entry-a", "AAA");
                item3Entry1.put("item3-entry-b", List.of("A", "B"));
                item3Entry1.put("item3-entry-c", "BBB");
                final Struct item3Entry2 = new Struct(item3_entry);
                item3Entry2.put("item3-entry-a", "CCC");
                item3Entry2.put("item3-entry-b", List.of("C", "D", "E"));
                final Struct item3Entry3 = new Struct(item3_entry);
                item3Entry3.put("item3-entry-a", "FFF");
                item3Entry3.put("item3-entry-b", List.of("F", "G"));
                item3Entry3.put("item3-entry-c", "GGG");
                final List<Struct> item3Value = List.of(item3Entry1, item3Entry2, item3Entry3);
                final Struct item4_entry0 = new Struct(item4_entry);
                item4_entry0.put("key", "ooo");
                item4_entry0.put("value", "pqrst");
                final Struct item4_entry1 = new Struct(item4_entry);
                item4_entry1.put("key", "ppp");
                item4_entry1.put("value", "uvwxy");
                final Struct item4_entry2 = new Struct(item4_entry);
                item4_entry2.put("key", "qqq");
                item4_entry2.put("value", "rstuv");
                final Struct item4Value = new Struct(item4);
                item4Value.put("entry", List.of(item4_entry0, item4_entry1, item4_entry2));
                final Struct item5_0_entry0 = new Struct(item5_entry);
                item5_0_entry0.put("key", "e1a");
                item5_0_entry0.put("value", "aaa");
                final Struct item5_0_entry1 = new Struct(item5_entry);
                item5_0_entry1.put("key", "e1b");
                item5_0_entry1.put("value", "bbb");
                final Struct item5_0 = new Struct(item5_item);
                item5_0.put("entry", List.of(item5_0_entry0, item5_0_entry1));
                final Struct item5_1_entry0 = new Struct(item5_entry);
                item5_1_entry0.put("key", "e2a");
                item5_1_entry0.put("value", "ccc");
                final Struct item5_1_entry1 = new Struct(item5_entry);
                item5_1_entry1.put("key", "e2b");
                item5_1_entry1.put("value", "ddd");
                final Struct item5_1_entry2 = new Struct(item5_entry);
                item5_1_entry2.put("key", "e2c");
                item5_1_entry2.put("value", "eee");
                final Struct item5_1 = new Struct(item5_item);
                item5_1.put("entry", List.of(item5_1_entry0, item5_1_entry1, item5_1_entry2));
                final Struct item5_2_entry0 = new Struct(item5_entry);
                item5_2_entry0.put("key", "e3a");
                item5_2_entry0.put("value", "fff");
                final Struct item5_2 = new Struct(item5_item);
                item5_2.put("entry", List.of(item5_2_entry0));
                final List<Struct> item5Value = List.of(item5_0, item5_1, item5_2);
                final Struct item6Entry1 = new Struct(item6_value);
                item6Entry1.put("message", "hello");
                item6Entry1.put("num", 1000);
                item6Entry1.put("test", true);
                final Struct item6Value1 = new Struct(item6_entry);
                item6Value1.put("key", 2);
                item6Value1.put("value", item6Entry1);
                final Struct item6Entry2 = new Struct(item6_value);
                item6Entry2.put("message", "world");
                item6Entry2.put("num", 2000);
                item6Entry2.put("test", false);
                final Struct item6Value2 = new Struct(item6_entry);
                item6Value2.put("key", 5);
                item6Value2.put("value", item6Entry2);
                final Struct item6Entry3 = new Struct(item6_value);
                item6Entry3.put("message", "xxx");
                item6Entry3.put("num", 3000);
                item6Entry3.put("test", true);
                final Struct item6Value3 = new Struct(item6_entry);
                item6Value3.put("key", 9);
                item6Value3.put("value", item6Entry3);
                final Struct item6Value = new Struct(item6);
                item6Value.put("entry", List.of(item6Value1, item6Value2, item6Value3));

                final Struct value = new Struct(schema);
                value.put("item1", item1Value);
                value.put("item2", item2Value);
                value.put("item3", item3Value);
                value.put("item4", item4Value);
                value.put("item5", item5Value);
                value.put("item6", item6Value);

                return new SchemaAndValue(schema, value);
            }
            case "011": {
                final Schema entry = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.STRING_SCHEMA)
                    .build();
                final Schema themap = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(entry).optional().build())
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("the-map", themap)
                    .build();

                final Struct item1 = new Struct(entry);
                item1.put("key", "key1");
                item1.put("value", "value1");
                final Struct item2 = new Struct(entry);
                item2.put("key", "key2");
                item2.put("value", "value2");
                final Struct item3 = new Struct(entry);
                item3.put("key", "key3");
                item3.put("value", "value3");

                final Struct mapValue = new Struct(themap);
                mapValue.put("entry", List.of(item1, item2, item3));

                final Struct value = new Struct(schema);
                value.put("the-map", mapValue);

                return new SchemaAndValue(schema, value);
            }
            case "012": {
                final Schema entry = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.STRING_SCHEMA)
                    .build();
                final Schema themap = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(entry).optional().build())
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("one-of-the-maps", SchemaBuilder.array(themap).build())
                    .build();

                final Struct item1 = new Struct(entry);
                item1.put("key", "key1");
                item1.put("value", "value1");
                final Struct item2 = new Struct(entry);
                item2.put("key", "key2");
                item2.put("value", "value2");
                final Struct item3 = new Struct(entry);
                item3.put("key", "key3");
                item3.put("value", "value3");

                final Struct mapValueA = new Struct(themap);
                mapValueA.put("entry", List.of(item1, item2, item3));

                final Struct item4 = new Struct(entry);
                item4.put("key", "keyA");
                item4.put("value", "valueA");
                final Struct item5 = new Struct(entry);
                item5.put("key", "keyB");
                item5.put("value", "valueB");

                final Struct mapValueB = new Struct(themap);
                mapValueB.put("entry", List.of(item4, item5));

                final Struct value = new Struct(schema);
                value.put("one-of-the-maps", List.of(mapValueA, mapValueB));

                return new SchemaAndValue(schema, value);
            }
            case "013": {
                final Schema level1Schema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .build();
                final Schema level2Schema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(level1Schema).build())
                    .build();
                final Schema level3Schema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(level2Schema).build())
                    .build();
                final Schema level4Schema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(level3Schema).build())
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("test", SchemaBuilder.array(level4Schema).build())
                    .build();

                final Struct t1 = new Struct(level1Schema);
                t1.put("entry", List.of(123, 234));
                final Struct t2 = new Struct(level1Schema);
                t2.put("entry", List.of(456, 678));
                final Struct t3 = new Struct(level1Schema);
                t3.put("entry", List.of(890, 901));
                final Struct t4 = new Struct(level1Schema);
                t4.put("entry", List.of(12, 23));
                final Struct t5 = new Struct(level1Schema);
                t5.put("entry", List.of(34, 45));
                final Struct t6 = new Struct(level1Schema);
                t6.put("entry", List.of(56, 67));
                final Struct t7 = new Struct(level1Schema);
                t7.put("entry", List.of(78, 89));
                final Struct t8 = new Struct(level1Schema);
                t8.put("entry", List.of(90, 11));
                final Struct t9 = new Struct(level1Schema);
                t9.put("entry", List.of(22, 33));
                final Struct t10 = new Struct(level1Schema);
                t10.put("entry", List.of(44, 55));
                final Struct t11 = new Struct(level1Schema);
                t11.put("entry", List.of(66, 77));
                final Struct t12 = new Struct(level1Schema);
                t12.put("entry", List.of(88, 99));
                final Struct t13 = new Struct(level1Schema);
                t13.put("entry", List.of(111, 222));
                final Struct t14 = new Struct(level1Schema);
                t14.put("entry", List.of(333, 444));
                final Struct t15 = new Struct(level1Schema);
                t15.put("entry", List.of(555, 666));
                final Struct t16 = new Struct(level1Schema);
                t16.put("entry", List.of(777, 888));

                final Struct i1 = new Struct(level2Schema);
                i1.put("entry", List.of(t1, t2));
                final Struct i2 = new Struct(level2Schema);
                i2.put("entry", List.of(t3, t4));
                final Struct i3 = new Struct(level2Schema);
                i3.put("entry", List.of(t5, t6));
                final Struct i4 = new Struct(level2Schema);
                i4.put("entry", List.of(t7, t8));
                final Struct i5 = new Struct(level2Schema);
                i5.put("entry", List.of(t9, t10));
                final Struct i6 = new Struct(level2Schema);
                i6.put("entry", List.of(t11, t12));
                final Struct i7 = new Struct(level2Schema);
                i7.put("entry", List.of(t13, t14));
                final Struct i8 = new Struct(level2Schema);
                i8.put("entry", List.of(t15, t16));

                final Struct m1 = new Struct(level3Schema);
                m1.put("entry", List.of(i1, i2));
                final Struct m2 = new Struct(level3Schema);
                m2.put("entry", List.of(i3, i4));
                final Struct m3 = new Struct(level3Schema);
                m3.put("entry", List.of(i5, i6));
                final Struct m4 = new Struct(level3Schema);
                m4.put("entry", List.of(i7, i8));

                final Struct o1 = new Struct(level4Schema);
                o1.put("entry", List.of(m1, m2));
                final Struct o2 = new Struct(level4Schema);
                o2.put("entry", List.of(m3, m4));

                final Struct value = new Struct(schema);
                value.put("test", List.of(o1, o2));

                return new SchemaAndValue(schema, value);
            }
            case "014": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", List.of(true, false, true));

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", List.of(false, true));

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "015": {
                final Schema keyItem = SchemaBuilder.struct()
                        .field("keyFloat", Schema.FLOAT32_SCHEMA)
                        .field("keyBool", Schema.BOOLEAN_SCHEMA)
                        .build();
                final Schema keySchema = SchemaBuilder.array(keyItem).build();
                final Schema valueSchema = Schema.STRING_SCHEMA;

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0a = new Struct(keyItem);
                key0a.put("keyFloat", 7.8f);
                key0a.put("keyBool", true);
                final Struct key0b = new Struct(keyItem);
                key0b.put("keyFloat", 17.3f);
                key0b.put("keyBool", false);
                final List<Struct> key0 = List.of(key0a, key0b);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", "value0");

                final Struct key1a = new Struct(keyItem);
                key1a.put("keyFloat", 3.9f);
                key1a.put("keyBool", false);
                final Struct key1b = new Struct(keyItem);
                key1b.put("keyFloat", 6.2f);
                key1b.put("keyBool", true);
                final List<Struct> key1 = List.of(key1a, key1b);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", "value1");

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "016": {
                final Schema keySchema = Schema.INT64_SCHEMA;
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("bools", SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build())
                    .field("score", Schema.FLOAT64_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct val0 = new Struct(valueSchema);
                val0.put("bools", List.of(true, true));
                val0.put("score", 3.456d);
                final Struct val1 = new Struct(valueSchema);
                val1.put("bools", List.of(false, false));
                val1.put("score", 9.887d);
                final Struct val2 = new Struct(valueSchema);
                val2.put("bools", List.of(true, false));
                val2.put("score", 100.00001d);

                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", Long.valueOf(3));
                entry0.put("value", val0);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", Long.valueOf(9));
                entry1.put("value", val1);
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", Long.valueOf(100));
                entry2.put("value", val2);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2));

                return new SchemaAndValue(schema, value);
            }
            case "017": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyA", Schema.STRING_SCHEMA)
                    .field("keyB", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valA", Schema.STRING_SCHEMA)
                    .field("valB", Schema.STRING_SCHEMA)
                    .build();
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();

                final Schema mapSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("maps", SchemaBuilder.array(mapSchema).build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyA", "AAAA");
                key0.put("keyB", "BBBB");
                final Struct val0 = new Struct(valueSchema);
                val0.put("valA", "CCCC");
                val0.put("valB", "DDDD");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyA", "EEEE");
                key1.put("keyB", "FFFF");
                final Struct val1 = new Struct(valueSchema);
                val1.put("valA", "GGGG");
                val1.put("valB", "HHHH");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct key2 = new Struct(keySchema);
                key2.put("keyA", "IIII");
                key2.put("keyB", "JJJJ");
                final Struct val2 = new Struct(valueSchema);
                val2.put("valA", "KKKK");
                val2.put("valB", "LLLL");
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", key2);
                entry2.put("value", val2);

                final Struct firstMap = new Struct(mapSchema);
                firstMap.put("entry", List.of(entry0, entry1, entry2));

                final Struct key3 = new Struct(keySchema);
                key3.put("keyA", "MMMM");
                key3.put("keyB", "NNNN");
                final Struct val3 = new Struct(valueSchema);
                val3.put("valA", "OOOO");
                val3.put("valB", "PPPP");
                final Struct entry3 = new Struct(entrySchema);
                entry3.put("key", key3);
                entry3.put("value", val3);

                final Struct key4 = new Struct(keySchema);
                key4.put("keyA", "QQQQ");
                key4.put("keyB", "RRRR");
                final Struct val4 = new Struct(valueSchema);
                val4.put("valA", "SSSS");
                val4.put("valB", "TTTT");
                final Struct entry4 = new Struct(entrySchema);
                entry4.put("key", key4);
                entry4.put("value", val4);

                final Struct secondMap = new Struct(mapSchema);
                secondMap.put("entry", List.of(entry3, entry4));

                final Struct value = new Struct(schema);
                value.put("maps", List.of(firstMap, secondMap));

                return new SchemaAndValue(schema, value);
            }
            case "018": {
                final Schema innerKeySchema = SchemaBuilder.struct()
                    .field("inner", Schema.STRING_SCHEMA)
                    .build();
                final Schema keySchemaEntrySchema = SchemaBuilder.struct()
                    .field("key", innerKeySchema)
                    .field("value", SchemaBuilder.array(Schema.INT32_SCHEMA).build())
                    .build();
                final Schema keySchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(keySchemaEntrySchema).optional().build())
                    .build();

                final Schema valueSchemaEntrySchema = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.FLOAT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(valueSchemaEntrySchema).optional().build())
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct innerKey0 = new Struct(innerKeySchema);
                innerKey0.put("inner", "theInnerKey0");
                final Struct firstKeyEntry0 = new Struct(keySchemaEntrySchema);
                firstKeyEntry0.put("key", innerKey0);
                firstKeyEntry0.put("value", List.of(10, 11, 12));

                final Struct innerKey1 = new Struct(innerKeySchema);
                innerKey1.put("inner", "theInnerKey1");
                final Struct firstKeyEntry1 = new Struct(keySchemaEntrySchema);
                firstKeyEntry1.put("key", innerKey1);
                firstKeyEntry1.put("value", List.of(20, 21, 22));

                final Struct innerKey2 = new Struct(innerKeySchema);
                innerKey2.put("inner", "theInnerKey2");
                final Struct firstKeyEntry2 = new Struct(keySchemaEntrySchema);
                firstKeyEntry2.put("key", innerKey2);
                firstKeyEntry2.put("value", List.of(30, 31, 32));

                final Struct firstKey = new Struct(keySchema);
                firstKey.put("entry", List.of(firstKeyEntry0, firstKeyEntry1, firstKeyEntry2));

                final Struct valueSchemaEntry1 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry1.put("key", "innerValue1");
                valueSchemaEntry1.put("value", 111.111f);
                final Struct valueSchemaEntry2 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry2.put("key", "innerValue2");
                valueSchemaEntry2.put("value", 222.222f);
                final Struct valueSchemaEntry3 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry3.put("key", "innerValue3");
                valueSchemaEntry3.put("value", 333.333f);

                final Struct value1 = new Struct(valueSchema);
                value1.put("entry", List.of(valueSchemaEntry1, valueSchemaEntry2, valueSchemaEntry3));

                final Struct innerKey3 = new Struct(innerKeySchema);
                innerKey3.put("inner", "theInnerKey3");
                final Struct secondKeyEntry0 = new Struct(keySchemaEntrySchema);
                secondKeyEntry0.put("key", innerKey3);
                secondKeyEntry0.put("value", List.of(40, 41));

                final Struct innerKey4 = new Struct(innerKeySchema);
                innerKey4.put("inner", "theInnerKey4");
                final Struct secondKeyEntry1 = new Struct(keySchemaEntrySchema);
                secondKeyEntry1.put("key", innerKey4);
                secondKeyEntry1.put("value", List.of(50, 51));

                final Struct secondKey = new Struct(keySchema);
                secondKey.put("entry", List.of(secondKeyEntry0, secondKeyEntry1));

                final Struct valueSchemaEntry4 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry4.put("key", "innerValue4");
                valueSchemaEntry4.put("value", 444.444f);
                final Struct valueSchemaEntry5 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry5.put("key", "innerValue5");
                valueSchemaEntry5.put("value", 555.555f);
                final Struct valueSchemaEntry6 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry6.put("key", "innerValue6");
                valueSchemaEntry6.put("value", 666.666f);
                final Struct valueSchemaEntry7 = new Struct(valueSchemaEntrySchema);
                valueSchemaEntry7.put("key", "innerValue7");
                valueSchemaEntry7.put("value", 777.777f);

                final Struct value2 = new Struct(valueSchema);
                value2.put("entry", List.of(valueSchemaEntry4, valueSchemaEntry5, valueSchemaEntry6, valueSchemaEntry7));

                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", firstKey);
                entry1.put("value", value1);
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", secondKey);
                entry2.put("value", value2);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry1, entry2));

                return new SchemaAndValue(schema, value);
            }
            case "019": {
                final Schema mapKeySchema = SchemaBuilder.array(Schema.STRING_SCHEMA).build();
                final Schema mapValueEntry = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.INT32_SCHEMA)
                    .build();
                final Schema mapValueSchema = SchemaBuilder.struct()
                        .field("entry", SchemaBuilder.array(mapValueEntry).optional().build())
                        .build();

                final Schema mapEntry = SchemaBuilder.struct()
                    .field("key", mapKeySchema)
                    .field("value", mapValueSchema)
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(mapEntry).optional().build())
                    .build();

                final Struct letters1_0 = new Struct(mapValueEntry);
                letters1_0.put("key", "A");
                letters1_0.put("value", 1);
                final Struct letters1_1 = new Struct(mapValueEntry);
                letters1_1.put("key", "B");
                letters1_1.put("value", 2);
                final Struct letters1_2 = new Struct(mapValueEntry);
                letters1_2.put("key", "C");
                letters1_2.put("value", 3);
                final Struct letters1 = new Struct(mapValueSchema);
                letters1.put("entry", List.of(letters1_0, letters1_1, letters1_2));

                final Struct letters2_0 = new Struct(mapValueEntry);
                letters2_0.put("key", "D");
                letters2_0.put("value", 4);
                final Struct letters2_1 = new Struct(mapValueEntry);
                letters2_1.put("key", "E");
                letters2_1.put("value", 5);
                final Struct letters2 = new Struct(mapValueSchema);
                letters2.put("entry", List.of(letters2_0, letters2_1));

                final Struct value_0 = new Struct(mapEntry);
                value_0.put("key", List.of("a", "b", "c"));
                value_0.put("value", letters1);

                final Struct value_1 = new Struct(mapEntry);
                value_1.put("key", List.of("d", "e"));
                value_1.put("value", letters2);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(value_0, value_1));

                return new SchemaAndValue(schema, value);
            }
            case "020": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Struct value0 = new Struct(valueSchema);
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", value0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Struct value1 = new Struct(valueSchema);
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", value1);

                final Struct key2 = new Struct(keySchema);
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);
                final Struct value2 = new Struct(valueSchema);
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", key2);
                entry2.put("value", value2);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2));

                return new SchemaAndValue(schema, value);
            }
            case "021": {
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct value0 = new Struct(valueSchema);
                value0.put("valueLetter", "aa");
                value0.put("valueNumber", 1);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", "key0");
                entry0.put("value", value0);

                final Struct value1 = new Struct(valueSchema);
                value1.put("valueLetter", "bb");
                value1.put("valueNumber", 2);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", "key1");
                entry1.put("value", value1);

                final Struct value2 = new Struct(valueSchema);
                value2.put("valueLetter", "cc");
                value2.put("valueNumber", 3);
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", "key2");
                entry2.put("value", value2);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2));

                return new SchemaAndValue(schema, value);
            }
            case "022": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", Schema.STRING_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AA");
                key0.put("keyNumber", 12);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", "value0");

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BB");
                key1.put("keyNumber", 23);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", "value1");

                final Struct key2 = new Struct(keySchema);
                key2.put("keyLetter", "CC");
                key2.put("keyNumber", 34);
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", key2);
                entry2.put("value", "value2");

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2));

                return new SchemaAndValue(schema, value);
            }
            case "023": {
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", Schema.INT32_SCHEMA)
                    .field("value", Schema.INT32_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();


                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", 1);
                entry0.put("value", 10);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", 2);
                entry1.put("value", 20);
                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", 3);
                entry2.put("value", 30);
                final Struct entry3 = new Struct(entrySchema);
                entry3.put("key", 4);
                entry3.put("value", 40);
                final Struct entry4 = new Struct(entrySchema);
                entry4.put("key", 5);
                entry4.put("value", 50);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2, entry3, entry4));

                return new SchemaAndValue(schema, value);
            }
            case "024": {
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("optString1", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("string1", Schema.STRING_SCHEMA)
                    .field("optString2", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("string2", Schema.STRING_SCHEMA)
                    .field("optString3", Schema.OPTIONAL_STRING_SCHEMA)
                    .build();
                final Struct value = new Struct(schema);
                value.put("string1", "ONE");
                value.put("optString2", "two");
                value.put("string2", "TWO");
                value.put("optString3", "");

                return new SchemaAndValue(schema, value);
            }
            case "025": {
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", Schema.OPTIONAL_STRING_SCHEMA)
                    .field("value", Schema.FLOAT64_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", "first");
                entry0.put("value", 1.1d);

                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", null);
                entry1.put("value", 2.2d);

                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", "third");
                entry2.put("value", 3.3d);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2));

                return new SchemaAndValue(schema, value);
            }
            case "026": {
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.STRING_SCHEMA)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", "123");
                entry0.put("value", "unusual key 1");

                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", "");
                entry1.put("value", "unusual key 2");

                final Struct entry2 = new Struct(entrySchema);
                entry2.put("key", "Hello!");
                entry2.put("value", "unusual key 3");

                final Struct entry3 = new Struct(entrySchema);
                entry3.put("key", "my-key");
                entry3.put("value", "valid key 1");

                final Struct entry4 = new Struct(entrySchema);
                entry4.put("key", "12key");
                entry4.put("value", "unusual key 4");

                final Struct entry5 = new Struct(entrySchema);
                entry5.put("key", "*HELLO*");
                entry5.put("value", "unusual key 5");

                final Struct entry6 = new Struct(entrySchema);
                entry6.put("key", "my-key-2");
                entry6.put("value", "valid key 2");

                final Struct entry7 = new Struct(entrySchema);
                entry7.put("key", "Hello World");
                entry7.put("value", "unusual key 6");

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7));

                return new SchemaAndValue(schema, value);
            }
            case "028": {
                final Schema byteMapEntry = SchemaBuilder.struct()
                    .field("key", Schema.BYTES_SCHEMA)
                    .field("value", Schema.BYTES_SCHEMA)
                    .build();
                final Schema byteMapSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(byteMapEntry).optional().build())
                    .build();

                final Schema strToByteMapEntry = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.BYTES_SCHEMA)
                    .build();
                final Schema strToByteMapSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(strToByteMapEntry).optional().build())
                    .build();

                final Schema byteToStrMapEntry = SchemaBuilder.struct()
                    .field("key", Schema.BYTES_SCHEMA)
                    .field("value", Schema.STRING_SCHEMA)
                    .build();
                final Schema byteToStrMapSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(byteToStrMapEntry).optional().build())
                    .build();

                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("bytes", byteMapSchema)
                    .field("strToBytes", strToByteMapSchema)
                    .field("bytesToStr", byteToStrMapSchema)
                    .build();

                final Struct bytesEntry0 = new Struct(byteMapEntry);
                bytesEntry0.put("key", new byte[] { 0x10, 0x11, 0x12, 0x13 });
                bytesEntry0.put("value", new byte[] { 0x60, 0x61, 0x62 });
                final Struct bytesEntry1 = new Struct(byteMapEntry);
                bytesEntry1.put("key", new byte[] { 0x14, 0x15 });
                bytesEntry1.put("value", new byte[] { 0x70, 0x71, 0x72, 0x73, 0x74 });
                final Struct bytesMap = new Struct(byteMapSchema);
                bytesMap.put("entry", List.of(bytesEntry0, bytesEntry1));

                final Struct strToBytesEntry0 = new Struct(strToByteMapEntry);
                strToBytesEntry0.put("key", "Hello");
                strToBytesEntry0.put("value", "Hello".getBytes());
                final Struct strToBytesEntry1 = new Struct(strToByteMapEntry);
                strToBytesEntry1.put("key", "World");
                strToBytesEntry1.put("value", "World".getBytes());
                final Struct strToBytesEntry2 = new Struct(strToByteMapEntry);
                strToBytesEntry2.put("key", "This is a test");
                strToBytesEntry2.put("value", "This is a test".getBytes());
                final Struct strToBytesMap = new Struct(strToByteMapSchema);
                strToBytesMap.put("entry", List.of(strToBytesEntry0, strToBytesEntry1, strToBytesEntry2));

                final Struct bytesToStrEntry0 = new Struct(byteToStrMapEntry);
                bytesToStrEntry0.put("key", "one".getBytes());
                bytesToStrEntry0.put("value", "one");
                final Struct bytesToStrEntry1 = new Struct(byteToStrMapEntry);
                bytesToStrEntry1.put("key", "two".getBytes());
                bytesToStrEntry1.put("value", "two");
                final Struct bytesToStrMap = new Struct(byteToStrMapSchema);
                bytesToStrMap.put("entry", List.of(bytesToStrEntry0, bytesToStrEntry1));

                final Struct value = new Struct(schema);
                value.put("bytes", bytesMap);
                value.put("strToBytes", strToBytesMap);
                value.put("bytesToStr", bytesToStrMap);

                return new SchemaAndValue(schema, value);
            }
            case "029": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyFloat", Schema.FLOAT32_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.array(Schema.BOOLEAN_SCHEMA).build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                key0.put("keyFloat", 1.23f);
                final List<Boolean> val0 = List.of(true, false, true);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                key1.put("keyFloat", 4.56f);
                final List<Boolean> val1 = List.of(false, true);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "030": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyFloat", Schema.FLOAT32_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueFloat", Schema.FLOAT32_SCHEMA)
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "AAA");
                key0.put("keyNumber", 123);
                key0.put("keyFloat", 1.23f);
                final Struct val0 = new Struct(valueSchema);
                val0.put("valueLetter", "XXX");
                val0.put("valueNumber", 678);
                val0.put("valueFloat", 5.67f);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "BBB");
                key1.put("keyNumber", 456);
                key1.put("keyFloat", 4.56f);
                final Struct val1 = new Struct(valueSchema);
                val1.put("valueLetter", "YYY");
                val1.put("valueNumber", 789);
                val1.put("valueFloat", 7.89f);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "031": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyString", Schema.STRING_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyWord", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueString", Schema.STRING_SCHEMA)
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueWord", Schema.STRING_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                final Struct val0 = new Struct(valueSchema);
                val0.put("valueLetter", "B");
                val0.put("valueWord", "BANANA");
                val0.put("valueString", "STRING");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                final Struct val1 = new Struct(valueSchema);
                val1.put("valueLetter", "D");
                val1.put("valueWord", "DAMSON");
                val1.put("valueString", "OUTPUT");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "032": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyString", Schema.STRING_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyWord", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = Schema.STRING_SCHEMA;

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", "SIMPLE");

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", "SINGLE");

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "033": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyString", Schema.STRING_SCHEMA)
                    .field("keyOrdinal", Schema.STRING_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyWord", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = Schema.STRING_SCHEMA;

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                key0.put("keyOrdinal", "first");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", "SIMPLE");

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                key1.put("keyOrdinal", "second");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", "SINGLE");

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "034": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyString", Schema.STRING_SCHEMA)
                    .field("keyOrdinal", Schema.STRING_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyWord", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                key0.put("keyOrdinal", "first");
                final Struct val0 = new Struct(valueSchema);
                val0.put("valueLetter", "a");
                val0.put("entry", "SIMPLE");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                key1.put("keyOrdinal", "second");
                final Struct val1 = new Struct(valueSchema);
                val1.put("valueLetter", "b");
                val1.put("entry", "SINGLE");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "035": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyWord", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("entry", Schema.STRING_SCHEMA)
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("entry", "FIRST");
                final Struct val0 = new Struct(valueSchema);
                val0.put("valueLetter", "a");
                val0.put("entry", "SIMPLE");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("entry", "SECOND");
                final Struct val1 = new Struct(valueSchema);
                val1.put("valueLetter", "b");
                val1.put("entry", "SINGLE");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "036": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("keyString", Schema.STRING_SCHEMA)
                    .field("keyOrdinal", Schema.STRING_SCHEMA)
                    .field("keyLetter", Schema.STRING_SCHEMA)
                    .field("keyWord", Schema.STRING_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("valueString", Schema.STRING_SCHEMA)
                    .field("valueOrdinal", Schema.STRING_SCHEMA)
                    .field("valueLetter", Schema.STRING_SCHEMA)
                    .field("valueWord", Schema.STRING_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyLetter", "A");
                key0.put("keyWord", "APPLE");
                key0.put("keyString", "TEST");
                key0.put("keyOrdinal", "first");
                final Struct val0 = new Struct(valueSchema);
                val0.put("valueLetter", "a");
                val0.put("valueWord", "abacus");
                val0.put("valueString", "TESTING");
                val0.put("valueOrdinal", "FIRST");
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyLetter", "C");
                key1.put("keyWord", "CABBAGE");
                key1.put("keyString", "INPUT");
                key1.put("keyOrdinal", "second");
                final Struct val1 = new Struct(valueSchema);
                val1.put("valueLetter", "c");
                val1.put("valueWord", "counting");
                val1.put("valueString", "INPUTTING");
                val1.put("valueOrdinal", "SECOND");
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "038": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("entry", Schema.INT32_SCHEMA)
                    .field("keyNumber", Schema.OPTIONAL_INT32_SCHEMA)
                    .field("keyCount", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("entry", Schema.INT32_SCHEMA)
                    .field("valueNumber", Schema.INT32_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyNumber", 1);
                key0.put("keyCount", 10);
                key0.put("entry", 100);
                final Struct val0 = new Struct(valueSchema);
                val0.put("entry", 10000);
                val0.put("valueNumber", 1000);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyNumber", 2);
                key1.put("keyCount", 20);
                key1.put("entry", 200);
                final Struct val1 = new Struct(valueSchema);
                val1.put("entry", 20000);
                val1.put("valueNumber", 2000);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "040": {
                final Schema keySchema = SchemaBuilder.struct()
                    .field("entry", Schema.INT32_SCHEMA)
                    .field("keyNumber", Schema.INT32_SCHEMA)
                    .field("keyCount", Schema.INT32_SCHEMA)
                    .build();
                final Schema valueSchema = SchemaBuilder.struct()
                    .field("entry", Schema.INT32_SCHEMA)
                    .field("valueNumber", Schema.OPTIONAL_INT32_SCHEMA)
                    .build();

                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", keySchema)
                    .field("value", valueSchema)
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Struct key0 = new Struct(keySchema);
                key0.put("keyNumber", 1);
                key0.put("keyCount", 10);
                key0.put("entry", 100);
                final Struct val0 = new Struct(valueSchema);
                val0.put("entry", 10000);
                val0.put("valueNumber", 1000);
                final Struct entry0 = new Struct(entrySchema);
                entry0.put("key", key0);
                entry0.put("value", val0);

                final Struct key1 = new Struct(keySchema);
                key1.put("keyNumber", 2);
                key1.put("keyCount", 20);
                key1.put("entry", 200);
                final Struct val1 = new Struct(valueSchema);
                val1.put("entry", 20000);
                val1.put("valueNumber", 2000);
                final Struct entry1 = new Struct(entrySchema);
                entry1.put("key", key1);
                entry1.put("value", val1);

                final Struct value = new Struct(schema);
                value.put("entry", List.of(entry0, entry1));

                return new SchemaAndValue(schema, value);
            }
            case "043": {
                final Schema entrySchema = SchemaBuilder.struct()
                    .field("key", Schema.STRING_SCHEMA)
                    .field("value", Schema.FLOAT32_SCHEMA)
                    .build();
                final Schema mapSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(entrySchema).optional().build())
                    .build();

                final Schema listOfMapsSchema = SchemaBuilder.struct()
                    .field("entry", SchemaBuilder.array(mapSchema).build())
                    .build();
                final Schema schema = SchemaBuilder.struct()
                    .name("root")
                    .field("maps", SchemaBuilder.array(listOfMapsSchema).build())
                    .build();

                final Struct map1Entry0 = new Struct(entrySchema);
                map1Entry0.put("key", "one point two");
                map1Entry0.put("value", 1.2f);
                final Struct map1Entry1 = new Struct(entrySchema);
                map1Entry1.put("key", "two point three");
                map1Entry1.put("value", 2.3f);
                final Struct map1Entry2 = new Struct(entrySchema);
                map1Entry2.put("key", "three point four");
                map1Entry2.put("value", 3.4f);
                final Struct map1 = new Struct(mapSchema);
                map1.put("entry", List.of(map1Entry0, map1Entry1, map1Entry2));

                final Struct map2Entry0 = new Struct(entrySchema);
                map2Entry0.put("key", "ten point five");
                map2Entry0.put("value", 10.5f);
                final Struct map2Entry1 = new Struct(entrySchema);
                map2Entry1.put("key", "eleven point seven");
                map2Entry1.put("value", 11.7f);
                final Struct map2 = new Struct(mapSchema);
                map2.put("entry", List.of(map2Entry0, map2Entry1));

                final Struct listOfMaps1 = new Struct(listOfMapsSchema);
                listOfMaps1.put("entry", List.of(map1, map2));

                final Struct map3Entry0 = new Struct(entrySchema);
                map3Entry0.put("key", "pi");
                map3Entry0.put("value", 3.14f);
                final Struct map3Entry1 = new Struct(entrySchema);
                map3Entry1.put("key", "half");
                map3Entry1.put("value", 0.5f);
                final Struct map3 = new Struct(mapSchema);
                map3.put("entry", List.of(map3Entry0, map3Entry1));

                final Struct map4Entry0 = new Struct(entrySchema);
                map4Entry0.put("key", "first");
                map4Entry0.put("value", 1.1f);
                final Struct map4Entry1 = new Struct(entrySchema);
                map4Entry1.put("key", "second");
                map4Entry1.put("value", 2.2f);
                final Struct map4Entry2 = new Struct(entrySchema);
                map4Entry2.put("key", "third");
                map4Entry2.put("value", 3.3f);
                final Struct map4Entry3 = new Struct(entrySchema);
                map4Entry3.put("key", "fourth");
                map4Entry3.put("value", 4.4f);
                final Struct map4 = new Struct(mapSchema);
                map4.put("entry", List.of(map4Entry0, map4Entry1, map4Entry2, map4Entry3));

                final Struct listOfMaps2 = new Struct(listOfMapsSchema);
                listOfMaps2.put("entry", List.of(map3, map4));

                final Struct value = new Struct(schema);
                value.put("maps", List.of(listOfMaps1, listOfMaps2));

                return new SchemaAndValue(schema, value);
            }
            default:
                return get(testCaseId);
        }
    }
}
