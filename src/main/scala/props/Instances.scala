package props

trait Instances {
  import scalaz._
  import Scalaz._
  
  // Properties are invariant functors
  implicit def ReadWriteInvariantFunctor[Src]: InvariantFunctor[PartialApply1Of2[ReadWrite, Src]#Apply] = 
    new scalaz.InvariantFunctor[PartialApply1Of2[ReadWrite, Src]#Apply] {
      def xmap[T, U](prop: ReadWrite[Src, T], f: T => U, g: U => T): ReadWrite[Src, U] = ReadWrite[Src, U](
        e => prop.get(e) map f,
        (u, e) => prop.put(g(u), e)
      )
    }
  
    // Properties are invariant functors
  implicit def ReadUpdateInvariantFunctor[Src]: InvariantFunctor[PartialApply1Of2[ReadUpdate, Src]#Apply] = 
    new scalaz.InvariantFunctor[PartialApply1Of2[ReadUpdate, Src]#Apply] {
      def xmap[T, U](prop: ReadUpdate[Src, T], f: T => U, g: U => T): ReadUpdate[Src, U] = new ReadUpdate[Src, U] {
        def get(s: Src) = prop.get(s) map f

        def put(s: Src, u: U): Result[U] = prop.put(s, g(u)) map f
      }
    }
  
  implicit def AttrReaderFunctor[Src] = new scalaz.Functor[PartialApply1Of2[AttrReader, Src]#Apply] {
    def fmap[A,B](reader: AttrReader[Src, A], f: A => B) = new AttrReader[Src, B] {
      def get(s: Src): Validation[NonEmptyList[PropertyError], B] = reader.get(s) map f
    }
  }
  
  implicit def AttrWriterCofunctor[Src] = new scalaz.Cofunctor[PartialApply1Of2[AttrWriter, Src]#Apply] {
    def comap[A,B](writer: AttrWriter[Src, A], f: B => A) = new AttrWriter[Src, B] {
      def put(b: B, s: Src) = writer.put(f(b), s)
    }
  }
}