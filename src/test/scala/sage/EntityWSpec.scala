package sage

import scalaz._
import Scalaz._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import com.google.appengine.api.datastore._

import java.lang.{Long => JLong}

class EntityWSuite extends SageSuiteBase {
  test("Utils.typeCheck") {    
    Utils.typeCheck[Long](new java.lang.Long(50)) should equal (some(50))
    Utils.typeCheck[String]("") should equal (some(""))
  }
}
