/**
  * This file is part of the ONEMA onema Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2017, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <kinojman@gmail.com>
  */

package onema.json

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

import scala.reflect._


/**
  * Implicit class that provides a toJson and jsonParse methods. These will turn a class into
  * json, and a json string into a class.
  */
object Extensions {
  implicit class CaseClassToJson(anyClass: AnyRef) {

    // --- Methods ---
    /**
      * Converts a class into a json string.
      */
    def asJson: String = {
      implicit val formats = Serialization.formats(NoTypeHints)
      write(anyClass)
    }

    /**
      * Converts a class into a json string using a custom serializer
      * @param serializer
      * @tparam TEnum
      * @return
      */
    def asJson[TEnum](serializer: CustomSerializer[TEnum]): String = {
      implicit val formats = Serialization.formats(NoTypeHints) + serializer
      write(anyClass)
    }
  }

  implicit class JsonStringToCaseClass(json: String) {

    //--- Methods ---
    /**
      * Parses a json string to a case class of the given type parameter.
      *
      * @tparam T type to serialize json into
      * @return object of type T
      */
    def jsonDecode[T: Manifest]: T = {
      implicit val formats = Serialization.formats(NoTypeHints)
      parse(json).extract[T]
    }

    def jsonDecode[T: Manifest, TEnum](serializer: CustomSerializer[TEnum]): T = {
      implicit val formats = Serialization.formats(NoTypeHints) + serializer
      parse(json).extract[T]
    }
  }
}
