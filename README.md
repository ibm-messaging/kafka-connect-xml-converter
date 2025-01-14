# Kafka Connect XML converter and transformation plug-ins

The Kafka Connect XML converter and transformation plug-ins make it easier to work with XML data in Kafka Connect pipelines.

## Contents

- `com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter`
  A Kafka Connect converter for converting between structured objects (Kafka Connect's internal data format, and Java Maps and Lists) and XML strings.
- `com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlTransformation`
  A Single Message Transform (SMT) that takes a Kafka Connect record containing an XML string and transforms it into a structured Connect record.
- `com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlMQRecordBuilder`
  An MQ Source Record builder for parsing MQ messages containing XML strings.

## Configuration

The following table lists optional configuration that can be set when turning XML strings into Connect records by using the plug-ins (XML string to Connect record).

| **Option**            | **Default value** | **Notes**                                                                                         |
| --------------------- | ----------------- | ------------------------------------------------------------------------------------------------- |
| `root.element.name`   | `root`            | The name of the root element in the XML document that is being parsed.                            |
| `xsd.schema.path`     |                   | The location of the schema file to use when parsing the XML string.                               |
| `xml.doc.flat.enable` | `false`           | Set to `true` if the XML strings contain a single value (for example, `<root>the message</root>`) |

The following table lists optional configuration that can be set when turning Connect records into XML strings by using the plug-ins (Connect Record to XML string)

| **Option**          | **Default value** | **Notes**                                                                                                                                            |
| ------------------- | ----------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| `root.element.name` | `root`            | The name to use for the root element of the XML document being created. Only used when no name can be found within the schema of the Connect record. |

## Example uses

Use **`XmlConverter`** with source connectors to produce structured Connect records to Kafka topics as XML strings.

```properties
value.converter=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter
value.converter.schemas.enable=false
```

Use **`XmlConverter`** with source connectors to produce Connect records to Kafka topics as XML strings, with an embedded XSD schema (requires structured objects).

```properties
value.converter=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter
value.converter.schemas.enable=true
```

Use **`XmlTransformation`** with sink connectors to convert a Connect record containing an XML string into a structured Connect record.

```properties
transforms=xmlconvert
transforms.xmlconvert.type=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlTransformation
transforms.xmlconvert.converter.type=value
```

Use **`XmlConverter`** with the IBM MQ sink connector to send non-XML Kafka messages to MQ queues as XML strings.

```properties
mq.message.builder=com.ibm.eventstreams.connect.mqsink.builders.ConverterMessageBuilder
mq.message.builder.value.converter=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter
```

Use **`XmlMQRecordBuilder`** with the IBM MQ source connector to convert XML strings from MQ queues into Connect records.

```properties
mq.record.builder=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlMQRecordBuilder
mq.record.builder.schemas.enable=true
mq.record.builder.xsd.schema.path=/location/of/mq-message-schema.xsd
```

## Adding the IBM MQ Source Connector JAR to the Project

To build the project, you need to pull and install the latest IBM MQ source connector JAR. Follow these steps:

1. **Download the MQ Source Connector JAR**

   Download the latest version of the IBM MQ source connector JAR from the official IBM GitHub releases page:

   ```bash
   curl -L -o kafka-connect-mq-source-<VERSION>.jar https://github.com/ibm-messaging/kafka-connect-mq-source/releases/download/v<VERSION>/kafka-connect-mq-source-<VERSION>.jar
   ```

2. **Install the JAR in Your Local Maven Repository**

   Install the downloaded JAR file into your local Maven repository:

   ```bash
   mvn install:install-file \
     -Dfile=kafka-connect-mq-source-<VERSION>.jar \
     -DgroupId=com.ibm.eventstreams.connect \
     -DartifactId=kafka-connect-mq-source \
     -Dversion=<VERSION> \
     -Dpackaging=jar
   ```

Replace `<VERSION>` with the actual version number of the JAR you downloaded.
