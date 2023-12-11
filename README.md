# kafka-connect-xml-converter

A Kafka Connect plugin to make it easier to work with XML data in Kafka Connect pipelines.

## Contents

- `com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter`
    - a Kafka Connect converter for converting to/from XML strings
- `com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlTransformation`
    - a Kafka Connect transformation for converting Kafka Connect records to/from XML strings
- `com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlMQRecordBuilder`
    - an MQ Source Record builder for parsing MQ messages containing XML strings

## Configuration

Optional configuration that can be set when using the plugin to turn XML strings into Connect records

| **Option**             | **Default value** | **Notes**                                                        |
|------------------------|-------------------|------------------------------------------------------------------|
| `root.element.name`    | `root`            | The name of the root element in the XML document being parsed.   |
| `xsd.schema.path`      |                   | Location of a schema file to use to parse the XML string. |
| `xml.doc.flat.enable`  | `false`           | Set to true if the XML strings contain a single value (e.g. `<root>the message</root>` |)

Optional configuration that can be set when using the plugin to create XML strings from Connect records

| **Option**             | **Default value** | **Notes**                                                        |
|------------------------|-------------------|------------------------------------------------------------------|
| `root.element.name`    | `root`            | The name to use for the root element of the XML document being created. Only used when no name can be found within the schema of the Connect record. |

## Example uses

Use **`XmlConverter`** with Source Connectors to produce structured Connect records to Kafka topics as XML strings.

```
value.converter=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter
value.converter.schemas.enable=false
```

Use **`XmlConverter`** with Source Connectors to produce Connect records to Kafka topics as XML strings, with an embedded XSD schema. (requires structs)

```
value.converter=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter
value.converter.schemas.enable=true
```

Use **`XmlTransformation`** with Sink Connectors to convert a Connect record containing an XML string into a structured Connect record.

```
transforms=xmlconvert
transforms.xmlconvert.type=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlTransformation
transforms.xmlconvert.converter.type=value
```

Use **`XmlConverter`** with the MQ Sink Connector to send non-XML Kafka messages to MQ queues as XML strings.

```
mq.message.builder=com.ibm.eventstreams.connect.mqsink.builders.ConverterMessageBuilder
mq.message.builder.value.converter=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlConverter
```

Use **`XmlMQRecordBuilder`** with the MQ Source Connector to convert XML strings from MQ queues into Connect records.

```
mq.record.builder=com.ibm.eventstreams.kafkaconnect.plugins.xml.XmlMQRecordBuilder
mq.record.builder.schemas.enable=true
mq.record.builder.xsd.schema.path=/location/of/mq-message-schema.xsd
```
