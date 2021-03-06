/**
  * This file is part of the ONEMA SScraper Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2017, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */
package json

import io.onema.json.Extensions._
import com.fasterxml.jackson.annotation.JsonProperty
import json.TestTypes.{TEST_1, TEST_2, TestType}
import org.json4s.FieldSerializer._
import org.json4s.{CustomSerializer, FieldSerializer, NoTypeHints}
import org.json4s.JsonAST.JString
import org.json4s.jackson.Serialization
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
    val fooJson = """{"name": "test", "value": "foo"}"""

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
    val result = """{"data":["http://foo.com","http://bar.com","http://baz.com","http://blah.org"]}"""
    val message = Message(Seq("http://foo.com", "http://bar.com", "http://baz.com", "http://blah.org"))

    // Act
    val jsonValue = message.asJson

    // Assert
    jsonValue should be(result)
  }

  "A case class with an enum using a custom serializer " should "be converted to json" in {
    // Arrange
    val result = """{"name":"foo","testType":"test-1"}"""
    val obj = TestWithTypes("foo", TEST_1)

    // Act
    val jsonValue = obj.asJson(TestTypeSerializer)

    // Assert
    jsonValue should be(result)
  }

  "A json string with an enum using a custom serializer " should "be converted to a case class" in {
    // Arrange
    val jsonString = """{"name":"foo","testType":"test-2"}"""

    // Act
    val obj = jsonString.jsonDecode[TestWithTypes, TestType](TestTypeSerializer)

    // Assert
    obj.name should be("foo")
    obj.testType should be (TEST_2)
  }

  "A case class with custom rename serializer " should "be serialized to json " in {
    // Arrange
    val obj = RenameObject("test", "one")
    val result = """{"bar":"test","@baz":"one"}"""

    // Act
    val jsonString = obj.asJson(RenameObject.renames)

    // Assert
    jsonString should be(result)
  }

  "A json string " should "be deserialized to a case class with custom rename serializer " in {
    // Arrange
    val jsonString = """{"bar":"test","@baz":"one"}"""

    // Act
    val obj = jsonString.jsonDecode(RenameObject.renames)

    // Assert
    obj.foo should be("test")
    obj.baz should be("one")
  }

  "A nested case class with custom rename serializer " should "be serialized to json " in {
    // Arrange
    val obj = TestWithAnnotation(NestedObject("foo"))
    val result = """{"test_one":{"foo_bar":"foo"}}"""


    // Act
    val jsonString = obj.asJson(RenameObject.nestedFormats)

    // Assert
    jsonString should be(result)
  }

  "A nested json string " should "be deserialized to a case class with custom rename serializer " in {
    // Arrange
    val jsonString = """{"test_one":{"foo_bar":"foo"}}"""

    // Act
    val obj = jsonString.jsonDecode[TestWithAnnotation](RenameObject.nestedFormats)

    // Assert
    obj.testOne.fooBar should be("foo")
  }
}

case class TestJsonFoo(name: String, value: String, id: Int = 0)
case class TestJsonBaz(id: Int, array: Array[TestJsonFoo])
case class TestJsonBazSeq(id: Int, array: Seq[TestJsonFoo])
case class TestJsonBar(id: Int, testFoo: TestJsonFoo)
case class Message(data: Seq[String])
case class TestWithAnnotation(testOne: NestedObject)
case class NestedObject(fooBar: String)
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

case class RenameObject(foo: String, baz: String)
object RenameObject {
  val renames = FieldSerializer[RenameObject](
    renameTo("foo", "bar") orElse renameTo("baz", "@baz"),
    renameFrom("bar", "foo") orElse renameFrom("@baz", "baz"))
  val testWithAnnotationRename = FieldSerializer[TestWithAnnotation](
    renameTo("testOne", "test_one"),
    renameFrom("test_one", "testOne")
  )
  val nestedObjectRename = FieldSerializer[NestedObject](
    renameTo("fooBar", "foo_bar"),
    renameFrom("foo_bar", "fooBar")
  )
  val nestedFormats = Serialization.formats(NoTypeHints) + testWithAnnotationRename + nestedObjectRename
}
