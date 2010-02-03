package sage

import scalaz._
import Scalaz._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class ChildrenOfSuite extends FunSuite with ShouldMatchers with BeforeAndAfterAll with DatastoreSuite {
  
  case class Hat(name: String, price: JLong)
  
  object Racks extends Base[String]("rack") {
    def * = "name".prop[String]
  }
  
  object Hats extends Base[Hat]("hats") {
    def * =  "type".prop[String] ~ "price".prop[JLong] <> (Hat, Hat.unapply _)
  }
  
  test("can insert with parent") {
    import dsl._
    val Keyed(rackKey, rack) = Racks << "rack0"
    val Keyed(randomKey, hat0) = Hats << Hat("random", 1)
    val Keyed(childKey, hat2) = Hats parentedSave (Hat("childKey", 1), rackKey)
    
    val hatsNamedA = Hats.find.query(qry => qry.setAncestor(rackKey)).iterable
    hatsNamedA map (_.value) should equal (List(Hat("childKey", 1)))
  }
  
  test("can request children") {
    val Keyed(rackKey, rack) = Racks << "rack0"
    val Keyed(randomKey, hat0) = Hats << Hat("random", 1)
    val Keyed(childKey, hat2) = Hats parentedSave (Hat("childKey", 1), rackKey)
    
    Hats.childrenOf(rackKey) map (_.value) should equal (List(Hat("childKey", 1)))
  }
}