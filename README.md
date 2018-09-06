JSON Extensions
===============
![Code Build](https://codebuild.us-east-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiNnVFUnZXVXdaVGRJN2VvY1BITk82R0pEYjdISDVMa0pVOWttejNySUIrd2VJeTVEUzVjYm10YURPS1NLNDcyZmVJS0tWbG8yUDlNMXdkQ3pHbEI4azlzPSIsIml2UGFyYW1ldGVyU3BlYyI6IjNKMTJNRGh0Q0k2cUNRaU8iLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/99ec645be864498c96a17dab1ec01d15)](https://www.codacy.com/app/onema/JsonExtensions?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=onema/JsonExtensions&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/99ec645be864498c96a17dab1ec01d15)](https://www.codacy.com?utm_source=github.com&utm_medium=referral&utm_content=onema/JsonExtensions&utm_campaign=Badge_Coverage)
[![LICENSE](https://img.shields.io/badge/license-Apache--2.0-blue.svg?longCache=true&style=flat-square)](LICENSE)

Simple wrapper for [json4s](https://github.com/json4s/json4s) to simplify de/serialization of JSON.

Usage
-----
### Deserialize JSON to a Scala Case Class
```scala
import io.onema.json.Extensions._

case class TestJsonFoo(name: String, value: String, id: Int = 0)

val fooJson = "{\"name\": \"test\", \"value\": \"foo\"}"

val fooObject = fooJson.jsonDecode[TestJsonFoo]

fooObject.name should be("test")
fooObject.value should be("foo")
fooObject.id should be(0)
```

### Serialize Scala Class to JSON string
```scala
import io.onema.json.Extensions._

case class Message(data: Seq[String])
val result = "{\"data\":[\"http://foo.com\",\"http://bar.com\",\"http://baz.com\",\"http://blah.org\"]}"
val message = Message(Seq("http://foo.com", "http://bar.com", "http://baz.com", "http://blah.org"))

val jsonValue = message.asJson

jsonValue should be(result)
```
### Serialize using a custom serializer

Case class and custom types
```scala
case class TestWithTypes(name: String, testType: TestType)
object TestTypes {
  sealed abstract class TestType(val name: String)
  case object TEST_1 extends TestType("work-request")
  case object TEST_2 extends TestType("process-request")
}
```

Custom Serializer
```scala
object TestTypeSerializer extends CustomSerializer[TestType](format => ({
  case JString("test-1") => TEST_1
  case JString("test-2") => TEST_2
}, {
  case TEST_1 => JString("test-1")
  case TEST_2 => JString("test-2")
}
))
```

Using the custom serializer
```scala

val result = "{\"name\":\"foo\",\"testType\":\"test-1\"}"
val obj = TestWithTypes("foo", TEST_1)
val jsonValue = obj.asJson(TestTypeSerializer)
jsonValue should be(result)
```

### Deserialize to Java POJO

```scala
import io.onema.json.JavaExtensions._

val fooJson = "{\"name\": \"test\", \"value\": \"foo\"}"

val fooObject = fooJson.jsonDecode[TestJsonPojo]

fooObject.getName should be("test")
fooObject.getValue should be("foo")
fooObject.getId should be(0)
```

### Serialize java POJO to JSON string
```scala
import io.onema.json.JavaExtensions._

val result = "{\"data\":[\"http://foo.com\",\"http://bar.com\",\"http://baz.com\",\"http://blah.org\"]}"
val message = new TestJsonPojo()

val jsonValue = message.asJson

jsonValue should be(result)
```

### Using a Custom Object Mapper for POJOs
```scala
import io.onema.json.JavaExtensions._
import com.fasterxml.jackson.databind.ObjectMapper
val mapper: ObjectMapper = new ObjectMapper()
                              .registerModule(new JodaModule)
                              .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
val fooObject = json.jsonDecode[TestJsonPojo](mapper)
```