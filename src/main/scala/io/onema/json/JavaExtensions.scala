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

import com.fasterxml.jackson.databind.{DeserializationFeature, MapperFeature, ObjectMapper}
import com.fasterxml.jackson.datatype.joda.JodaModule

import scala.reflect._
import scala.util.{Failure, Success, Try}


/**
  * Implicit class that provides extension methods to simplify serializing and de-serializing json
  */
object JavaExtensions {
  implicit class JavaPojoToJson(objectClass: Any) {

    /**
      * Some java types are not properly serialized by json4s
      *
      * @return
      */
    def asJson: String = {
      val mapper = new ObjectMapper
      mapper.writeValueAsString(objectClass)
    }
  }

  implicit class JsonStringToJavaPojo(json: String) {

    /**
      * Parses a json string to a java class of the given type parameter.
      * @tparam T type to serialize json into
      * @return object of type T
      */
    def jsonDecode[T: ClassTag]: T = {
      val mapper = Mapper.default
      jsonDecode(mapper)
    }

    /**
      * Parse a json string to a java class of the given type parameter
      * @param mapper a custom object mapper defining any rules on how the string should be parsed
      * @tparam T type to serialize json into
      * @return object of type T
      */
    def jsonDecode[T: ClassTag](mapper: ObjectMapper): T = {
      val classType: Class[_] = implicitly[ClassTag[T]].runtimeClass
      Try(mapper.readValue(json, classType)) match {
        case Success(result) =>
          result.asInstanceOf[T]
        case Failure(e) =>
          throw e
      }
    }
  }
}

object Mapper {
  val default: ObjectMapper = new ObjectMapper()
    .registerModule(new JodaModule)
    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
    .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)

  val allowUnknownPropertiesMapper: ObjectMapper = new ObjectMapper()
    .registerModule(new JodaModule)
    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
}
