package net.mega2223

import scala.annotation.tailrec

object PatternMatcher {
  @tailrec
  def find(data: String, matched: String, start: Int = 0): Option[Int] = {
    //println(f"-:$str")
    if(data.substring(start, start + matched.length) == matched){
      Some(start)
    } else {
      if (start + matched.length < data.length) {
        find(data, matched, start + 1)
      } else {
        None
      }
    }
  }

  def mapPattern(data: String, pattern: String, mapFun: Function[Int,Int]): Unit = {

  }
}
