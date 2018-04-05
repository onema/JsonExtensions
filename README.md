JSON Core
=========
![Code Build](https://codebuild.us-east-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoibTVHQWhoZ2NZWWk2cmFEbG10M0VKRlo5YklMRU1xWnZaQWdJZndRUE91dk9MN0V3cEVMeTNNemNUU1NVVXZtR2VrSDBJSlFSUlNBV3BBMEZDYUh6NHRzPSIsIml2UGFyYW1ldGVyU3BlYyI6ImRWbnp2QkRvUmRqWmNPWC8iLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ad23ac0f208c4c0988f16f4f1e800c8f)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=onema/JsonCore&amp;utm_campaign=Badge_Grade)

Simple wrapper [json4s](https://github.com/json4s/json4s) to simplify de/serialization of JSON.

Usage
-----
### Deserialize JSON to a Scala Case Class
```scala
import onema.core.json.Implicits._

case class TestJsonFoo(name: String, value: String, id: Int = 0)

val fooJson = "{\"name\": \"test\", \"value\": \"foo\"}"

val fooObject = fooJson.jsonParse[TestJsonFoo]

fooObject.name should be("test")
fooObject.value should be("foo")
fooObject.id should be(0)
```

### Serialize Scala Class to JSON string
```scala
import onema.core.json.Implicits._

case class Message(data: Seq[String])
val result = "{\"data\":[\"http://foo.com\",\"http://bar.com\",\"http://baz.com\",\"http://blah.org\"]}"
val message = Message(Seq("http://foo.com", "http://bar.com", "http://baz.com", "http://blah.org"))

val jsonValue = message.toJson

jsonValue should be(result)
```

### Deserialize to Java POJO

```scala
import onema.core.json.Implicits._

val fooJson = "{\"name\": \"test\", \"value\": \"foo\"}"

val fooObject = fooJson.jsonParseToJavaClass[TestJsonPojo]

fooObject.getName should be("test")
fooObject.getValue should be("foo")
fooObject.getId should be(0)
```

### Serialize java POJO to JSON string
```scala
import onema.core.json.Implicits._

val result = "{\"data\":[\"http://foo.com\",\"http://bar.com\",\"http://baz.com\",\"http://blah.org\"]}"
val message = new TestJsonPojo()

val jsonValue = message.javaClassToJson

jsonValue should be(result)
```