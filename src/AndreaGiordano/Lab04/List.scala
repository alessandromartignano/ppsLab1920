package AndreaGiordano.Lab04

import scala.annotation.tailrec

object Lists extends App {

  // A generic linkedlist
  sealed trait List[E]

  // a companion object (i.e., module) for List
  object List {
    case class Cons[E](head: E, tail: List[E]) extends List[E]
    case class Nil[E]() extends List[E]

    def nil[A]: List[A] = Nil() // smart constructor

    def genList[A](elems:A*):List[A] = {
      var l = nil[A]
      for (e <- elems)
        l = append(l,Cons(e,nil[A]))
      l
    }

    def sum(l: List[Int]): Int = l match {
      case Cons(h, t) => h + sum(t)
      case _ => 0
    }

    def append[A](l1: List[A], l2: List[A]): List[A] = (l1, l2) match {
      case (Cons(h, t), l2) => Cons(h, append(t, l2))
      case _ => l2
    }

    def contains[A](l:List[A],item:A):Boolean = l match {
      case Cons(h,t) => if (h==item) true else contains(t,item)
      case _ => false
    }

    def drop[A](l: List[A], n: Int): List[A] = l match {
      case _ if n<=0 || l==Nil() => l
      case Cons(h,t) => drop(t,n-1)
    }

    def map[A,B](l: List[A])(f: A => B): List[B] = l match {
      case Cons(h,t) => Cons(f(h), map(t)(f))
      case Nil() => Nil()
    }

    def filter[A](l: List[A])(f: A => Boolean): List[A] = l match {
      case Cons(h,t) if f(h) => Cons(h, filter(t)(f))
      case Cons(h,t) => filter(t)(f)
      case Nil() => Nil()
    }

    def flatMap[A,B](l: List[A])(f: A => List[B]): List[B] = l match {
      case Cons(h,t) => append(f(h),flatMap(t)(f))
      case Nil() => Nil()
    }

    @tailrec
    def foldLeft[A,B](l: List[A])(acc: B)(f: (B,A)=>B): B = l match {
      case Cons(h,t) => foldLeft(t)(f(acc,h))(f)
      case Nil() => acc
    }

    def foldRightNonTailRec[A,B](l: List[A])(acc: B)(f: (A,B)=>B): B = l match {
      case Cons(h,t) => f(h, foldRightNonTailRec(t)(acc)(f))
      case Nil() => acc
    }

    def reverse[A](l: List[A]) : List[A] =
      foldLeft(l)(nil[A])((acc,elem) => Cons(elem,acc))

    def foldRightViaFoldleft[A,B](l: List[A])(acc: B)(f: (A,B)=>B): B =
      foldLeft(reverse(l))(acc)((acc,elem) => f(elem,acc))

    def foldRight[A,B](l: List[A])(acc: B)(f: (A,B)=>B): B =
      foldRightViaFoldleft(l)(acc)(f)

    def filterByFlatmap[A](l: List[A])(f: A => Boolean): List[A] = ???

    def appendByFold[A](l1: List[A], l2: List[A]): List[A] = ???

    def length(l: List[_]): Int = ???
  }

  object sameTeacher {
    def unapply(list: List[Course]): Option[String] = {
      def checkSameTeacher(list: List[Course],teacher:String):Option[String] = list match {
        case List.Cons(h,t) => if (h.teacher==teacher) checkSameTeacher(t,teacher) else Option.empty
        case _ => Option(teacher)
      }
      list match {
        case List.Cons(h,t) => checkSameTeacher(t,h.teacher)
        case _ => Option.empty
      }
    }
  }

  /*
  // Note "List." qualification
  println(List.sum(List.Cons(10, List.Cons(20, List.Cons(30, List.Nil()))))) // 60
  import List._
  println(append(Cons("10", Nil()), Cons("1", Cons("2", Nil())))) // "10","1","2"

  println(drop(Cons(10, Cons(20, Cons(30, Nil()))),2)) // Cons(30, Nil())
  println(drop(Cons(10, Cons(20, Cons(30, Nil()))),5)) // Nil()
  println(drop(Nil(), 5)) // Nil()

  println(map(Cons(10, Cons(20, Nil())))(_+1))       // Cons(11, Cons(21, Nil()))
  println(map(Cons(10, Cons(20, Nil())))(":"+_+":")) // Cons(":10:", Cons(":20:",Nil()))
  println(filter(Cons(10, Cons(20, Nil())))(_>15)) // Cons(20, Nil())
  println(filter(Cons("a", Cons("bb", Cons("ccc", Nil()))))( _.length <=2)) // Cons("a",Cons("bb", Nil()))

  val lst = Cons(3,Cons(7,Cons(1,Cons(5, Nil()))))
  println(foldLeft(lst)(0)(_-_)) // -16
  println(reverse(lst)) // Cons(5,Cons(1,Cons(7,Cons(3,Nil()))))
  println(foldRightNonTailRec(lst)(0)(_-_)) // -8
  println(foldRightViaFoldleft(lst)(0)(_-_)) // -8

  // EXERCISES:
  println(filterByFlatmap(Cons(10, Cons(20, Nil())))(_>15)) // Cons(20, Nil())
  println(filterByFlatmap(Cons("a", Cons("bb", Cons("ccc", Nil()))))( _.length <=2)) // Cons("a",Cons("bb", Nil()))
  println(appendByFold(Cons(3,Cons(7,Nil())), Cons(1,Cons(5,Nil())))) // Cons(3,Cons(7,Cons(1,Cons(5, Nil()))))
  println(length(Nil())) // 0
  println(length(Cons(3,Cons(7,Cons(1,Cons(5, Nil())))))) // 4
  */

  //test genList
  import List._
  val l = genList(1,2,3,4,5,6)
  println(l)

  val c1 = Course("C1","Viroli")
  val c2 = Course("C2","Viroli")
  val c3 = Course("C3","Viroli")
  val c4 = Course("C4","Viroli")
  val c5 = Course("C5","Ricci")
  val c6 = Course("C6","Omicini")
  val c7 = Course("C7","Bravetti")
  val cListEq = genList(c1,c2,c3,c4)
  val cListNotEq = genList(c4,c5,c6,c7)
  val testList = cListEq
  testList match {
    case sameTeacher(t) => println ( s"$testList have same teacher $t")
    case _ => println("NOPE")
  }
}