package net.mega2223

import java.io.PrintStream
import scala.collection.mutable

object Context {
  var properties: mutable.Map[String,String] = mutable.Map()
  var output: Option[PrintStream] = Some(System.out)

  object Constants {
    val HTML_ENTRIES = "HTML_ENTRIES"
    val MARKDOWN_ENTRIES = "MARKDOWN_ENTRIES"
    val STYLESHEETS = "STYLESHEETS"
    val MEDIA = "MEDIA"
    val HTML_DYNAMIC_TEMPLATES = "TEMPLATES"
  }
}

