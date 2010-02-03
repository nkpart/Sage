package sage

import scalaz._
import Scalaz._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class UpdatesSuite extends FunSuite with ShouldMatchers with BeforeAndAfterAll with DatastoreSuite {
  
  case class Hat(name: String, price: JLong)
  object Hats extends Base[Hat]("hats") {
    def * =  "type".prop[String] ~ "price".prop[JLong] <> (Hat, Hat.unapply _)
  }
  
  test("update without duping") {
    import dsl._
    val Keyed(key, hat) = Hats << Hat("a", 1)
    val Keyed(key2, hat2) = Hats << Keyed(key, Hat("a", 2))
    
    val hatsNamedA = Hats.find.query("type" ?== "a").iterable
    hatsNamedA map (_.value) should equal (List(Hat("a", 2)))
  }
}