package sage

import scalaz._
import Scalaz._

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.Stack

import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class ExampleSuite extends FunSuite with ShouldMatchers with BeforeAndAfterAll with DatastoreSuite {
  
  test("It should look a little bit like this") {    
    val hats = new Base[String]("hats") {
      def * = "type".prop[String]
    }
    
    val r: Keyed[String] = hats << ("slouch")
    
    val e: Entity = datastoreService.get(r.key)
    e.getProperty("type") should equal ("slouch")
  }

  test("Multiple properties like this") {

    object hats extends Base[(String, JLong)]("hats") {
      def * = "type".prop[String] ~ "price".prop[JLong]
    }
    
    val r = hats << ("flatcap", 25)
    
    val e: Entity = datastoreService.get(r.key)
    e.getProperty("type") should equal ("flatcap")
    e.getProperty("price") should equal (25)
  }

  case class Name(value: String) extends NewType[String]
  case class Price(value: JLong) extends NewType[JLong]
    
  test("Newtyped properties like this") {

    object hats extends Base[(Name, Price)]("hats") {
      def * = "type".typedProp(Name) ~ "price".typedProp(Price)
    }
    
    val hat = (Name("bowler"), Price(50))
    val r = hats << hat
 
    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
  }
  
  case class Hat(name: String, price: Price)
  test("Saving classes like this") {
    object hats extends Base[Hat]("hats") {
      def * = "type".prop[String] ~ "price".typedProp(Price) <> (Hat, Hat.unapply _)
    }
    
    val hat = Hat("fedora", Price(65))
    val r = hats << hat

    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
  }
  
  test("newtypes as props like this") {
    object hats extends Base[Hat]("hats") {
      def * = "type".prop[String] ~ Price <> (Hat, Hat.unapply _)
    }
    
    val hat = Hat("fedora", Price(65))
    val r = hats << hat

    hats.lookup(r.key.getId) map (_.value) should equal (Some(hat))
  }
  
  test("write many") {
    object hats extends Base[Hat]("hats") {
      def * =  "type".prop[String] ~ Price <> (Hat, Hat.unapply _)
    }
    val newHats = List(Hat("a", Price(1)), Hat("b", Price(2)))
    val keys = hats <<++ newHats map (_.key)
    keys map (k => hats.lookup(k.getId).get.value) should equal (newHats)
  }
}