import sage.EntityImplicits
import scalaz._
import Scalaz._
import hprops._

import metascala.HLists._

package object sage extends 
  EntityImplicits 
  with FunctionWs 
  with FindDSLImplicits 
  with EntityProperties 
  with HPropsDSL 
