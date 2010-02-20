package sage

import metascala.HLists._

import props._
import scalaz._
import Scalaz._

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class RequestSpec extends SageSuiteBase {
  import sage.http._
  import scalaz.http._
  import request._
  import StringW._
  
  def buildRequest(qs: String) = {
    val line = Line.line(GET, Uri.uri("/abc".charsNel.get, some(qs.toList)), Version.version11)    
    Request.request(line, Nil, Stream.empty)
  }
  
  case class Name(name: String)
  test("a") {
    val nameProp = hlift("name".as[String]) >< (Name <-> Name.unapply _)
    val nameAndAgeProp = "name".as[String] :: "age".as[String] :: "song".as[String]
    
    val request = buildRequest("name=nick")
    
    nameProp.get(request).success.map(_.name) should equal (some("nick"))
    nameAndAgeProp.get(request).failure.map(_.list) should equal (some(Missing("age") :: Missing("song") :: Nil))
  }
  
  test("missing vs. invalid data") {
    val nameAndAgeProp = "name".as[String] :: "age".as[Int]
    
    val nameOnly = buildRequest("name=nick")
    val invalidAge = buildRequest("name=nick&age=five")
    
    // Values can be missing, or they can be unparsable.
    
  }
}