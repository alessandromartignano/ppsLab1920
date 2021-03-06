package AlessandroMartignano.u06

/** Implement trait Functions with an object FunctionsImpl such that the code
  * in TryFunctions works correctly. To apply DRY principle at the best,
  * note the three methods in Functions do something similar.
  * Use the following approach:
  * - find three implementations of Combiner that tell (for sum,concat and max) how
  *   to combine two elements, and what to return when the input list is empty
  * - implement in FunctionsImpl a single method combiner that, other than
  *   the collection of A, takes a Combiner as input
  * - implement the three methods by simply calling combiner
  *
  * When all works, note we completely avoided duplications..
 */

trait Functions {
  def sum(a: List[Double]): Double
  def concat(a: Seq[String]): String
  def max(a: List[Int]): Int // gives Int.MinValue if a is empty
}

trait Combiner[A] {
  def unit: A
  def combine(a: A, b: A): A
}

case class CombinerImpl[A](override val unit: A, _combine: (A,A)=>A) extends Combiner[A] {

  override def combine(a: A, b: A): A = _combine(a,b)
}

object FunctionsImpl extends Functions {

  override def sum(a: List[Double]): Double = combine(a)(CombinerImpl(0, _+_))

  override def concat(a: Seq[String]): String = combine(a)(CombinerImpl("", _+_))

  override def max(a: List[Int]): Int = combine(a)(CombinerImpl(Int.MinValue, (a,b)=> if (a>b) a else b))

  def combine[A](a: Iterable[A])(c: Combiner[A]): A = a.foldLeft(c.unit)(c.combine)
}

object TryFunctions extends App {
  val f: Functions = FunctionsImpl
  println(f.sum(List(10.0,20.0,30.1))) // 60.1
  println(f.sum(List()))                // 0.0
  println(f.concat(Seq("a","b","c")))   // abc
  println(f.concat(Seq()))              // ""
  println(f.max(List(-10,3,-5,0)))      // 3
  println(f.max(List()))                // -2147483648
}