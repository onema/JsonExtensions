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

package onema.core.json

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write

import scala.reflect._


/**
  * Implicit class that provides a toJson and jsonParse methods. These will turn a class into
  * json, and a json string into a class.
  */
object Implicits {

  implicit class AnyClassToJsonString(anyClass: AnyRef) {

    // --- Methods ---
    /**
      * Converts a class into a json string.
      */
    def toJson: String = {
      implicit val formats = Serialization.formats(NoTypeHints)
      write(anyClass)
    }
  }

  implicit class JsonStringToCaseClass(json: String) {

    //--- Methods ---
    /**
      * Parses a json string to a class of the given type parameter.
      *
      * @tparam T type to serialize json into
      * @return
      */
    def jsonParse[T: Manifest]: T = {
      implicit val formats = Serialization.formats(NoTypeHints)
      parse(json).extract[T]
    }
  }
}
