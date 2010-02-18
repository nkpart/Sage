package sage

import scalaz._
import Scalaz._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class PropertySuite extends SageSuiteBase {
  test("properties report missing fields on read") {
    val abProp = "a".prop[String] :: "b".prop[String] 
    
    val e = new Entity("some_kind")
    abProp.get(e).failure assert_≟ Some("a" +>: nel1("b"))
  
    e.setProperty("a", "aValue")
    abProp.get(e).failure assert_≟ Some("b".wrapNel)
  }
  
  test("properties type check") {
    val abProp = "a".prop[String]
    val e = new Entity("someKind")
    
    e.setProperty("a", 50l)
    abProp.get(e).failure assert_≟ Some("a".wrapNel)
    
    e.setProperty("a", "value")
    abProp.get(e).failure assert_≟ none
  }
}
