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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.FileGenerators;
import com.ibm.eventstreams.kafkaconnect.plugins.xml.testutils.StructGenerators;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

@RunWith(Parameterized.class)
public class XStreamStructConverterTest {

    private final String currentTestCase;
    private final boolean ambiguous;

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
            { "000", false },
            { "001", false },
            { "002", false },
            { "003", false },
            { "004", false },
            { "005", false },
            { "006", false },
            { "007", false },
            { "008", false },
            { "009", false },
            { "010", true },
            { "011", true },
            { "012", true },
            { "013", false },
            { "014", false },
            { "015", false },
            { "016", false },
            { "017", false },
            { "018", true },
            { "019", true },
            { "020", true },
            { "021", true },
            { "022", true },
            { "023", true },
            { "024", true },
            { "025", true },
//            { "026", true }
            { "027", false },
            { "028", true },
            { "029", false },
            { "030", false },
            { "031", false },
            { "032", false },
            { "033", false },
            { "034", false },
            { "035", false },
            { "036", false },
            { "037", false },
            { "038", false },
            { "039", false },
            { "040", false },
            { "042", false },
            { "043", false },
            { "044", false },
            { "052", false },
            { "053", false },
            { "054", false }
        });
    }

    public XStreamStructConverterTest(String testCase, boolean ambiguous) {
        this.currentTestCase = testCase;
        this.ambiguous = ambiguous;
    }

    @Test
    public void runTestCase() {
        final XStreamStructConverter converter = new XStreamStructConverter();

        final String ROOT = "root";

        final XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
        xstream.allowTypes(new Class[] { Struct.class });
        xstream.registerConverter(converter);
        xstream.alias(ROOT, Struct.class);

        final SchemaAndValue expected = ambiguous ? StructGenerators.generic(currentTestCase) : StructGenerators.get(currentTestCase);
        converter.registerSchema(expected.schema());

        final File input = FileGenerators.getXml(currentTestCase);
        final Object output = xstream.fromXML(input);

//        Comparisons.compareStruct((Struct)expected.value(), (Struct)output);
        assertEquals(expected.value(), output);
    }
}
