package sage

import hprops._
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
    abProp.get(e).failure.map(_.list) should equal (some(missing("a") :: missing("b") :: nil))
  
    e.setProperty("a", "aValue")
    abProp.get(e).failure.map(_.list) should equal (some(missing("b") :: Nil))
  }
  
  test("properties type check") {
    val abProp = "a".prop[String]
    val e = new Entity("someKind")
    
    e.setProperty("a", 50l)
    abProp.get(e).failure.map(_.list) should equal (some((missing("a") :: Nil)))
    
    e.setProperty("a", "value")
    abProp.get(e).failure should equal (none)
  }
}
