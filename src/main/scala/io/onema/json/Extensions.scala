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

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.dslplatform.json._

import scala.io.Source
import scala.reflect.runtime.universe._


/**
  * Implicit class that provides a toJson and jsonParse methods. These will turn a class into
  * json, and a json string into a class.
  */
object Extensions {
  implicit class CaseClassToJson(anyClass: Any) {

    // --- Methods ---
    /**
      * Converts a class into a json string.
      */
    def asJson: String = {
      val dslJson = new DslJson[Any]()
      implicit val encoder = dslJson.encoder[Any]
      val os = new ByteArrayOutputStream
      dslJson.encode(anyClass, os)
      Source.fromInputStream(new ByteArrayInputStream(os.toByteArray)).mkString
    }

    /**
      * Converts a class into a json string using a custom serializer
      * @param serializer custom serializer
      * @tparam TEnum Some types cannot be easily serialized like enums , use custom serializer for these types
      * @return
      */
//    def asJson[TEnum](serializer: CustomSerializer[TEnum]): String = {
//      implicit val formats = Serialization.formats(NoTypeHints) + serializer
//      write(anyClass)
//    }
  }

  implicit class JsonStringToCaseClass(json: String) {

    //--- Methods ---
    /**
      * Parses a json string to a case class of the given type parameter.
      *
      * @tparam T type to serialize json into
      * @return object of type T
      */
    def jsonDecode[T: TypeTag]: T = {
      val is = new ByteArrayInputStream(json.getBytes())
      val dslJson = new DslJson[T]()
      implicit val decoder = dslJson.decoder[T]
      dslJson.decode[T](is)
    }

//    def jsonDecode[T: Manifest, TEnum](serializer: CustomSerializer[TEnum]): T = {
//      implicit val formats = Serialization.formats(NoTypeHints) + serializer
//      parse(json).extract[T]
//    }
  }
}
