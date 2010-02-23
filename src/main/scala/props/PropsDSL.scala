package props

import metascala.HLists._

import scalaz._
import Scalaz._

// Provides the DSL for composing properties
trait PropsDSL {
  def hlift[T, P[_]: InvariantFunctor](p: P[T]): P[HCons[T, HNil]] = p.xmap(v => HCons(v, HNil), (xs: HCons[T, HNil]) => xs.head)
  
  implicit def pimpPropListThing[Src, L <: HList](p: ReadWrite[Src, L]) = new {
    def ::[V](property: ReadWrite[Src, V]): ReadWrite[Src, HCons[V, L]] = ReadWrite[Src, HCons[V, L]](
      e => (property.get(e) <|*|> p.get(e)) map { case (v, xs) => HCons(v, xs) },
      (vls, e) => { 
        p.put(vls.tail, e) >>= (x => property.put(vls.head, x))
      }
    )
  }
  
  implicit def pimpPropThing[Src, L](p: ReadWrite[Src, L]) = new {
    def ::[V](property: ReadWrite[Src, V]): ReadWrite[Src, HCons[V, HCons[L, HNil]]] = 
      property :: PropsDSL.this.hlift[L, PartialApply1Of2[ReadWrite, Src]#Apply](p)

    def hlift = PropsDSL.this.hlift[L, PartialApply1Of2[ReadWrite, Src]#Apply](p)
  }
  
  implicit def pimpReadUpdateList[Src, L <: HList](p: ReadUpdate[Src, L]) = new {
    def ::[V](property: ReadUpdate[Src, V]): ReadUpdate[Src, HCons[V, L]] = new ReadUpdate[Src, HCons[V, L]] {
      def get(s: Src) = (property.get(s) <|*|> p.get(s)) map { case (v, xs) => HCons(v, xs) }
      def put(s: Src, vls: HCons[V, L]) = {
        val ell = p.put(s, vls.tail)
        val v = property.put(s, vls.head)
        (v <|*|> ell) map { case (v, ell) => HCons(v, ell) }
      }
    }
  }

  implicit def pimpReadUpdateProp[Src, L](p: ReadUpdate[Src, L]) = new {
    def ::[V](property: ReadUpdate[Src, V]): ReadUpdate[Src, HCons[V, HCons[L, HNil]]] = 
      property :: PropsDSL.this.hlift[L, PartialApply1Of2[ReadUpdate, Src]#Apply](p)
      
    def hlift = PropsDSL.this.hlift[L, PartialApply1Of2[ReadUpdate, Src]#Apply](p)
  }
}
