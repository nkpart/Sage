import metascala.HLists._

package props {
  import scalaz._
  import Scalaz._
  
  trait PropsBase {
    def hlift[Src, T](p: Property[Src, T]): Property[Src, HCons[T, HNil]] = p.xmap(v => HCons(v, HNil), (xs: HCons[T, HNil]) => xs.head)(propertyInvariantF[Src])
    
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
  import scalaz._
  import Scalaz._
  implicit def propertyInvariantF[Src] = new InvariantFunctor[PartialApply1Of2[Property, Src]#Apply] {
    // Properties are invariant functors
    def xmap[T, U](prop: Property[Src, T], f: T => U, g: U => T): Property[Src, U] = Property[Src, U](
      e => prop.get(e) map f,
      (u, e) => prop.put(g(u), e)
    )
  }
}

package props {
  // DSL Helper, terminates a defined list of properties
  case object End {
    def ::[M[_] : InvariantFunctor, T](mt: M[T]): M[HCons[T, HNil]] = 
      (mt: MA[M, T]).xmap(v => HCons(v, HNil), (xs: HCons[T, HNil]) => xs.head)
  }
}