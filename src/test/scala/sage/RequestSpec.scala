package sage

import metascala.HLists._

import scalaz._
import Scalaz._

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class RequestSpec extends SageSuiteBase {
  import sage.http._
  import scalaz.http._
  import request._
  import StringW._
  
  case class Name(name: String)
  test("a") {
    val nameProp = hlift("name".as[String]) >< (Name <-> Name.unapply _)
    val nameAndAgeProp = "name".as[String] :: "age".as[String] :: "song".as[String]
    
    val line = Line.line(GET, Uri.uri("/abc".charsNel.get, some("name=nick".toList)), Version.version11)    
    val request = Request.request(line, Nil, Stream.empty)
    
    nameProp.get(request).success.map(_.name) assert_≟ (some("nick"))
    nameAndAgeProp.get(request).failure assert_≟ (some(nel("age", "song" :: Nil)))
    
  }
  
  test("missing vs. invalid data") {
    val nameAndAgeProp = "name".as[String] :: "age".as[Int]
  }
}