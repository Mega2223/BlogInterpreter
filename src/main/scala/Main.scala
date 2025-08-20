package net.mega2223

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println("a" == "a")
    def a = "Oi meu nome eh julio 123"
    def b = "meu"

    val maybeInt = PatternMatcher.find(a, b)

    maybeInt match {
      case Some(value) => println(a.substring(value,value+b.length))
      case None => println("noval")
    }
  }
}