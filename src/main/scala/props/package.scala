import metascala.HLists._

package props {
  import scalaz._
  import Scalaz._
  
  // Provides the DSL for composing properties
  trait PropsBase {
    def hlift[Src, T](p: Property[Src, T]): Property[Src, HCons[T, HNil]] = p.xmap(v => HCons(v, HNil), (xs: HCons[T, HNil]) => xs.head)(propertyInvariantFunctor[Src])
    
    implicit def pimpPropListThing[Src, L <: HList](p: Property[Src, L]) = new {
      def ::[V](property: Property[Src, V]) = Property[Src, HCons[V, L]](
        e => (property.get(e) <|*|> p.get(e)) map { case (v, xs) => HCons(v, xs) },
        (vls, e) => property.put(vls.head, p.put(vls.tail, e))
      )
    }
    
    implicit def pimpPropThing[Src, L](p: Property[Src, L]) = new {
      def ::[V](property: Property[Src, V]) = property :: hlift(p)
    }
  }
}

package object props extends PropsBase with MAs {
  def missing(s: String): PropertyError = Missing(s)
  def invalid(s: String): PropertyError = Invalid(s)
  
  import scalaz._
  import Scalaz._
  // Properties are invariant functors
  implicit def propertyInvariantFunctor[Src] = new InvariantFunctor[PartialApply1Of2[Property, Src]#Apply] {
    def xmap[T, U](prop: Property[Src, T], f: T => U, g: U => T): Property[Src, U] = Property[Src, U](
      e => prop.get(e) map f,
      (u, e) => prop.put(g(u), e)
    )
  }
}
