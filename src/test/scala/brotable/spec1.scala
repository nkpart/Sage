package brotable

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.Stack

class ExampleSuite extends FunSuite with ShouldMatchers with BeforeAndAfterAll with DatastoreSuite {
  
  test("pop is invoked on a non-empty stack") {

    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    val oldSize = stack.size
    val result = stack.pop()
    result should equal (2)
    stack.size should equal (oldSize - 1)
  }

  test("pop is invoked on an empty stack") {

    val emptyStack = new Stack[String]
    evaluating { emptyStack.pop() } should produce [NoSuchElementException]
    emptyStack should be ('empty)
  }
}