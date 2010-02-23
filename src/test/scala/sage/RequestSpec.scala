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
  
  def r(qs: String) = {
    val line = Line.line(GET, Uri.uri("/abc".charsNel.get, some(qs.toList)), Version.version11)    
    Request.request(line, Nil, Stream.empty)
  }
  
  case class Name(name: String)
  case class Person(name: String, age: Int)
  case class Record(name: String, age: String, song: String)
  
  val personProp = "name".as[String] :: "age".as[Int] >< (Person <-> Person.unapply _)
  val nameProp = "name".as[String].hlift >< (Name <-> Name.unapply _)
  val nameAndAgeProp = "name".as[String] :: "age".as[String] :: "song".as[String] >< (Record <-> Record.unapply _)
  
  test("reading") {
    val request = r("name=nick")
    nameProp.get(request).success should equal (some(Name("nick")))
  }
  
  test ("missing") {
    val request = r("name=nick")
    nameAndAgeProp.get(request).failure.map(_.list) should equal (some(Missing("age") :: Missing("song") :: Nil))
  }
  
  test ("invalid") {
    // TODO
    val request = r("name=nick&age=five")
    personProp.get(request).failure.map(_.list) should equal {
      some(Invalid("age") :: Nil)
    }
  }
  
  test("updating") {
    val start = Record("nick", "four", "song1")
    
    nameAndAgeProp.put(r("song=song2"), start) should equal {
      success(Record("nick", "four", "song2"))
    }
  }
  
  case class Rect(x: Int, y: Int, width: Int, height: Int)
  val RectProp = "x".as[Int] :: "y".as[Int] :: "width".as[Int] :: "height".as[Int] >< (Rect <-> Rect.unapply _)
  
  test("updating invalid") {
    val start = Rect(0, 0, 25, 25)
    
    RectProp.put(r("width=fifteen&height=eighteen"), start).fail.map(_.list).validation should equal {
      failure(Invalid("width") :: Invalid("height") :: Nil)
    }
  }
}