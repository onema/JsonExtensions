JSON Core
=========

Simple wrapper [json4s](https://github.com/json4s/json4s) to simplify
de/serialization of JSON.

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