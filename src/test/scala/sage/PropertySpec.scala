package sage

import scalaz._
import Scalaz._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class PropertySuite extends FunSuite with ShouldMatchers with BeforeAndAfterAll with DatastoreSuite {
  
  case class Hat(name: String, price: JLong)
  object Hats extends Base[Hat]("hats") {
    def * =  "type".prop[String] ~ "price".prop[JLong] <> (Hat, Hat.unapply _)
  }
  
  test("properties report missing fields on read") {
    val abProp = "a".prop[String] ~ "b".prop[String]
      
    val e = new Entity("some_kind")
    abProp.get(e) assert_≟ failure("a" +>: nel1("b"))
  
    e.setProperty("a", "aValue")
    abProp.get(e) assert_≟ failure("b".wrapNel)    
  }
}