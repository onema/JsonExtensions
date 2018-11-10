/**
  * This file is part of the ONEMA onema Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2017, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.json

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

import scala.reflect.Manifest


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
      * @param serializer custom serializer
      * @tparam TEnum Some types cannot be easily serialized like enums , use custom serializer for these types
      * @return
      */
    def asJson[TEnum](serializer: CustomSerializer[TEnum]): String = {
      val formats = Serialization.formats(NoTypeHints) + serializer
      anyClass.asJson(formats)
    }

    def asJson[TEnum](serializer: FieldSerializer[TEnum]): String = {
      val formats = Serialization.formats(NoTypeHints) + serializer
      anyClass.asJson(formats)
    }

    def asJson[TEnum](formats: Formats): String = {
      write(anyClass)(formats)
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
      val formats = Serialization.formats(NoTypeHints) + serializer
      json.jsonDecode(formats)
    }

    def jsonDecode[T: Manifest](serializer: FieldSerializer[T]): T = {
      val formats = Serialization.formats(NoTypeHints) + serializer
      json.jsonDecode(formats)
    }

    def jsonDecode[T: Manifest](formats: Formats): T = {
      implicit val f: Formats = formats
      parse(json).extract[T]
    }
  }
}
