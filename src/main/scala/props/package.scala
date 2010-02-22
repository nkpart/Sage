import metascala.HLists._

package object props extends PropsDSL with MAs with Instances {
  def missing(s: String): PropertyError = Missing(s)
  def invalid(s: String): PropertyError = Invalid(s)
}
