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

import io.onema.json.Extensions._
import com.fasterxml.jackson.annotation.JsonProperty
import json.TestTypes.{TEST_1, TEST_2, TestType}
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString
import org.scalatest.{FlatSpec, Matchers}

class TestJsonExtensions  extends FlatSpec with Matchers {
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
    val fooObject = fooJson.jsonDecode[TestJsonFoo]

    // Assert
    fooObject.name should be("test")
    fooObject.value should be("foo")
    fooObject.id should be(123)
  }

  "A simple json object with missing element" should "be converted to an object" in {

    // Arrange
    val fooJson = "{\"name\": \"test\", \"value\": \"foo\"}"

    // Act
    val fooObject = fooJson.jsonDecode[TestJsonFoo]

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
    val barObject = barJson.jsonDecode[TestJsonBar]

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
    val barObject = bazJson.jsonDecode[TestJsonBaz]

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
    val barObject = bazJson.jsonDecode[TestJsonBazSeq]

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
    val jsonValue = message.asJson

    // Assert
    jsonValue should be(result)
  }

  "A case class with an enum using a custom serializer " should "be converted to json" in {
    // Arrange
    val result = "{\"name\":\"foo\",\"testType\":\"test-1\"}"
    val obj = TestWithTypes("foo", TEST_1)

    // Act
    val jsonValue = obj.asJson(TestTypeSerializer)

    // Assert
    jsonValue should be(result)
  }

  "A json string with an enum using a custom serializer " should "be converted to a case class" in {
    // Arrange
    val jsonString = "{\"name\":\"foo\",\"testType\":\"test-2\"}"

    // Act
    val obj = jsonString.jsonDecode[TestWithTypes, TestType](TestTypeSerializer)

    // Assert
    obj.name should be("foo")
    obj.testType should be (TEST_2)
  }
}

case class TestJsonFoo(name: String, value: String, id: Int = 0)
case class TestJsonBaz(id: Int, array: Array[TestJsonFoo])
case class TestJsonBazSeq(id: Int, array: Seq[TestJsonFoo])
case class TestJsonBar(id: Int, testFoo: TestJsonFoo)
case class Message(data: Seq[String])
case class TestWithAnnotation(@JsonProperty("test_one") testOne: String)
case class ScalaExample(age: Int, name: String, blog: String, messages: Seq[String])
case class TestWithTypes(name: String, testType: TestType)
object TestTypes {
  sealed abstract class TestType(val name: String)
  case object TEST_1 extends TestType("work-request")
  case object TEST_2 extends TestType("process-request")
}

object TestTypeSerializer extends CustomSerializer[TestType](format => ({
  case JString("test-1") => TEST_1
  case JString("test-2") => TEST_2
}, {
  case TEST_1 => JString("test-1")
  case TEST_2 => JString("test-2")
}
))
