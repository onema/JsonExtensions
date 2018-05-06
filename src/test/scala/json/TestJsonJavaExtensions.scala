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

import io.onema.json.JavaExtensions._
import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConverters._

class TestJsonJavaExtensions  extends FlatSpec with Matchers {

  "A java class " should "be converted to json string using javaClassToJson" in {
    // Arrange
    val javaCls = new Example
    javaCls.setName("foobar")
    javaCls.setAge(34)
    javaCls.setBlog("http://blog.test.com")
    javaCls.setMessages(List("msg1", "msg2", "msg3").asJava)
    val expectedJson = "{\"age\":34,\"name\":\"foobar\",\"blog\":\"http://blog.test.com\",\"messages\":[\"msg1\",\"msg2\",\"msg3\"]}"

    // Act
    val result = javaCls.asJson

    // Assert
    result should be (expectedJson)
  }

  "A json string " should "be converted to java object using the parseToJavaClass" in {
    // Arrange
    val json =
      """
        |  {
        |    "age":23,
        |    "name":"foobar",
        |    "blog":"http://blog.test.com",
        |    "messages":["msg1","msg2","msg3"]
        |  }
      """.stripMargin

    // Act
    val javaClass = json.jsonDecode[Example]

    // Assert
    javaClass.getAge should be(23)
    javaClass.getName should be("foobar")
    javaClass.getBlog should be("http://blog.test.com")
  }
}
