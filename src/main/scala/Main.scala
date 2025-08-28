package net.mega2223

import java.util.regex.Pattern
import scala.util.matching.Regex

object Main {
  def main(args: Array[String]): Unit = {
    args.map((arg: String) =>{
      val pattern: Regex = "--(.+)=(.+)".r
      arg match {
        case pattern(name,option) => Some((name,option))
        case _ => None
      }
    }).filter(_.nonEmpty).foreach(Context.properties += _.get)
    val tuple: (String, String) = ("TESTE", "TESTE")
    Context.properties += tuple
    println(Context.properties)
  }
}