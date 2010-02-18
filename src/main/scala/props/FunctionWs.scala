package props

import Function._

import metascala.HLists._
import metascala.Nats._

trait FunctionWs {
  implicit def pimp1[A, Z](f: A => Z) = new {
    def m: A :: HNil => Z = list => f(list.nth[_0])
  }
  
  implicit def pimp2[A, B, Z](f: (A, B) => Z) = new {
    def m: A :: B :: HNil => Z = list => f(list.nth[_0], list.nth[_1])
    
    def <->(unapply: Z => Option[(A,B)]) = (m, (z: Z) => unapply(z).get match { case (a,b) => a :: b :: HNil })
  }
  
  implicit def pimp3[A, B, C, Z](f: (A, B, C) => Z) = new {
    def m: A :: B :: C :: HNil => Z = list => f(list.nth[_0], list.nth[_1], list.nth[_2])
    
    def <->(unapply: Z => Option[(A,B,C)]) = 
      (m, (z: Z) => unapply(z).get match { case (a,b,c) => a :: b :: c :: HNil })
  }
  
  implicit def pimp4[A, B, C, D, Z](f: (A, B, C, D) => Z) = new {
    def m: A :: B :: C :: D :: HNil => Z = list => f(list.nth[_0], list.nth[_1], list.nth[_2], list.nth[_3])
    
    def <->(unapply: Z => Option[(A,B,C,D)]) = 
      (m, (z: Z) => unapply(z).get match { case (a,b,c,d) => a :: b :: c :: d :: HNil })
  }
  
  implicit def pimp5[A, B, C, D, E, Z](f: (A, B, C, D, E) => Z) = new {
    def m: A :: B :: C :: D :: E :: HNil => Z = list => f(list.nth[_0], list.nth[_1], list.nth[_2], list.nth[_3], list.nth[_4])
    
    def <->(unapply: Z => Option[(A,B,C,D,E)]) = 
       (m, (z: Z) => unapply(z).get match { case (a,b,c,d,e) => a :: b :: c :: d :: e :: HNil })
  }
  
  implicit def pimp6[A, B, C, D, E, F, Z](f: (A, B, C, D, E, F) => Z) = new {
    def m: A :: B :: C :: D :: E :: F :: HNil => Z = list => f(list.nth[_0], list.nth[_1], list.nth[_2], list.nth[_3], list.nth[_4], list.nth[_5])
    
    def <->(unapply: Z => Option[(A,B,C,D,E,F)]) = 
       (m, (z: Z) => unapply(z).get match { case (a,b,c,d,e,f) => a :: b :: c :: d :: e :: f :: HNil })
  }
  
  implicit def pimp7[A, B, C, D, E, F, G, Z](f: (A, B, C, D, E, F, G) => Z) = new {
    def m: A :: B :: C :: D :: E :: F :: G :: HNil => Z = list => f(list.nth[_0], list.nth[_1], list.nth[_2], list.nth[_3], list.nth[_4], list.nth[_5], list.nth[_6])
    
    def <->(unapply: Z => Option[(A,B,C,D,E,F,G)]) = 
       (m, (z: Z) => unapply(z).get match { case (a,b,c,d,e,f,g) => a :: b :: c :: d :: e :: f :: g :: HNil })
  }
  
  implicit def pimp8[A, B, C, D, E, F, G, H, Z](f: (A, B, C, D, E, F, G, H) => Z) = new {
    def m: A :: B :: C :: D :: E :: F :: G :: H :: HNil => Z = list => f(list.nth[_0], list.nth[_1], list.nth[_2], list.nth[_3], list.nth[_4], list.nth[_5], list.nth[_6], list.nth[_7])
    
    def <->(unapply: Z => Option[(A,B,C,D,E,F,G,H)]) = 
       (m, (z: Z) => unapply(z).get match { case (a,b,c,d,e,f,g,h) => a :: b :: c :: d :: e :: f :: g :: h :: HNil })
  }
}