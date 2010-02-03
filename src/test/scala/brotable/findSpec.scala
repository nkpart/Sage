package sage

import scalaz._
import Scalaz._

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.Stack

import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class FindSuite extends FunSuite with ShouldMatchers with BeforeAndAfterAll with DatastoreSuite {
  
  case class Hat(name: String, price: JLong)
  
  object Hats extends Base[Hat]("hats") {
    def * =  "type".prop[String] ~ "price".prop[JLong] <> (Hat, Hat.unapply _)
  }
  
  test("find something") {
    import dsl._
    val newHats = List(Hat("a", 1), Hat("b", 2))
    val keys = Hats <<++ newHats map (_._1)
    
    val hatsNamedA = Hats.find.query("type" ?== "a").iterable
    
    hatsNamedA map (_._2) should equal (List(Hat("a", 1)))
//    keys map (k => hats.lookup(k.getId).get) should equal (newHats)
  }
}