package props

import scalaz._
import Scalaz._

trait MAs {
  implicit def readWriteMA[Src, T](p: ReadWrite[Src,T]) = ma[PartialApply1Of2[ReadWrite, Src]#Apply, T](p)
  implicit def readUpdateMA[Src, T](p: ReadUpdate[Src,T]) = ma[PartialApply1Of2[ReadUpdate, Src]#Apply, T](p)
}
