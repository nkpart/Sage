package sage

import metascala.HLists._

import scalaz._
import Scalaz._

import props._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class ClassSuite extends SageSuiteBase {
  
  case class FreeHat(name: String, style: String)
  test("Saving simple class") {
    object hats extends Base[FreeHat]("hats") {
      def * = "name".prop[String] :: "style".prop[String] >< (FreeHat <-> FreeHat.unapply _)
    }
    
    val hat = FreeHat("barry","fedora")
    val r = hats << hat

    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
    
  }
}


class ExampleSuite extends SageSuiteBase {
  
  test("It should look a little bit like this") {    
    val hats = new Base[String]("hats") {
      def * = "type".prop[String]
    }
    
    val r: Keyed[String] = hats << ("slouch")
    
    val e: Entity = datastoreService.get(r.key)
    e.getProperty("type") should equal ("slouch")
  }

  test("Multiple properties like this") {

    object hats extends Base[String :: JLong :: HNil]("hats") {
      def * = "type".prop[String] :: "price".prop[JLong] 
    }
    
    val r = hats << "flatcap" :: new java.lang.Long(25) :: HNil
    
    val e: Entity = datastoreService.get(r.key)
    e.getProperty("type") should equal ("flatcap")
    e.getProperty("price") should equal (25)
  }

  case class Name(value: String) extends NewType[String]
  case class Price(value: JLong) extends NewType[JLong]
    
  test("Newtyped properties like this") {

    object hats extends Base[Name :: Price :: HNil]("hats") {
      def * = "type".typedProp(Name) :: "price".typedProp(Price) 
    }
    
    val hat = Name("bowler") :: Price(50) :: HNil
    val r = hats << hat
 
    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
  }
  
  
  case class Hat(name: String, price: Price)
  test("Saving classes like this") {
    object hats extends Base[Hat]("hats") {
      def * = "type".prop[String] :: "price".typedProp(Price)  >< (Hat <-> Hat.unapply _)
    }
    
    val hat = Hat("fedora", Price(65))
    val r = hats << hat

    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
  }
  
  test("newtypes as props like this") {
    object hats extends Base[Hat]("hats") {
      def * = "type".prop[String] :: "price".typedProp(Price)  >< (Hat <-> Hat.unapply _)
    }
    
    val hat = Hat("fedora", Price(65))
    val r = hats << hat

    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
  }
  
  test("write many") {
    object hats extends Base[Hat]("hats") {
      def * =  "type".prop[String] :: "price".typedProp(Price)  >< (Hat <-> Hat.unapply _)
    }
    val newHats = List(Hat("a", Price(1)), Hat("b", Price(2)))
    val keys = hats <<++ newHats map (_.key)
    keys map (k => hats.lookup(k.getId).get.value) should equal (newHats)
  }
}

class AnyValSuite extends SageSuiteBase {
  import metascala.HLists._
  
  object Numbers extends Base[Long]("hats") {
    def * = "number".prop[Long]
  }

  test("longs can be stored and retrieved") {
    val longProp = "number".prop[Long] 
    
    val r = Numbers << (50l)
    
    Numbers.lookup(r.key.getId) map (_.value) should equal (Some(50l))
  }
}