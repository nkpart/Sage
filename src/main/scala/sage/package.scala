import sage.EntityImplicits
import scalaz._
import Scalaz._
import props._

import metascala.HLists._

package object sage extends 
  EntityImplicits 
  with FunctionWs 
  with FindDSLImplicits 
  with EntityProperties 
  with PropsBase {
    val End = props.End
}
