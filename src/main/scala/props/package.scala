import metascala.HLists._

package object props extends PropsDSL with MAs with Instances {
  import scalaz._
  import Scalaz._
  
  def missing(s: String): PropertyError = Missing(s)
  def invalid(s: String): PropertyError = Invalid(s)
  
  type Result[T] = Validation[NonEmptyList[PropertyError], T]
}
