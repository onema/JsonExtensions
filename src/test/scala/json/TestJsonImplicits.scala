/**
  * This file is part of the ONEMA SScraper Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2017, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <kinojman@gmail.com>
  */
package json

import onema.core.json.Implicits._
import org.scalatest.{FlatSpec, Matchers}

class TestJsonImplicits  extends FlatSpec with Matchers {
  "A simple json object" should "be converted to an object" in {

    // Arrange
    val fooJson =
      """{
        |   "name": "test",
        |   "value": "foo",
        |   "id": 123
        |}
      """.stripMargin

    // Act
    val fooObject = fooJson.jsonParse[TestJsonFoo]

    // Assert
    fooObject.name should be("test")
    fooObject.value should be("foo")
    fooObject.id should be(123)
  }

  "A simple json object with missing element" should "be converted to an object" in {

    // Arrange
    val fooJson = "{\"name\": \"test\", \"value\": \"foo\"}"

    // Act
    val fooObject = fooJson.jsonParse[TestJsonFoo]

    // Assert
    fooObject.name should be("test")
    fooObject.value should be("foo")
    fooObject.id should be(0)
  }

  "A nested json object" should "be converted to an object" in {

    // Arrange
    val barJson =
      """{
        |   "id": 321,
        |   "testFoo": {"name": "test", "value": "foo", "id": 123}
        |}
      """.stripMargin

    // Act
    val barObject = barJson.jsonParse[TestJsonBar]

    // Assert
    barObject.id should be(321)
    barObject.testFoo.name should be("test")
    barObject.testFoo.value should be("foo")
    barObject.testFoo.id should be(123)
  }

  "A nested array of json objects" should "be converted to an object" in {

    // Arrange
    val bazJson =
      """{
        |   "id": 321,
        |   "array": [{"name": "test", "value": "foo", "id": 123}]
        |}
      """.stripMargin

    // Act
    val barObject = bazJson.jsonParse[TestJsonBaz]

    // Assert
    barObject.id should be(321)
    barObject.array.head.name should be("test")
    barObject.array.head.value should be("foo")
    barObject.array.head.id should be(123)
  }

  "A nested sequence of json objects" should "be converted to an object" in {

    // Arrange
    val bazJson =
      """{
        |   "id": 321,
        |   "array": [{"name": "test", "value": "foo", "id": 123}]
        |}
      """.stripMargin

    // Act
    val barObject = bazJson.jsonParse[TestJsonBazSeq]

    // Assert
    barObject.id should be(321)
    barObject.array.head.name should be("test")
    barObject.array.head.value should be("foo")
    barObject.array.head.id should be(123)
  }

  "A message with multiple rows of data" should "be converted to json" in {
    // Arrange
    val result = "{\"data\":[\"http://foo.com\",\"http://bar.com\",\"http://baz.com\",\"http://blah.org\"]}"
    val message = Message(Seq("http://foo.com", "http://bar.com", "http://baz.com", "http://blah.org"))

    // Act
    val jsonValue = message.toJson

    // Assert
    jsonValue should be(result)
  }
}

case class TestJsonFoo(name: String, value: String, id: Int = 0)
case class TestJsonBaz(id: Int, array: Array[TestJsonFoo])
case class TestJsonBazSeq(id: Int, array: Seq[TestJsonFoo])
case class TestJsonBar(id: Int, testFoo: TestJsonFoo)
case class Message(data: Seq[String])
