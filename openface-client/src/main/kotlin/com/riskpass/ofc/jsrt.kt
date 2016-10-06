package com.riskpass.ofc

import org.w3c.dom.Element
import org.w3c.dom.css.CSSStyleDeclaration
import kotlin.browser.document
import kotlin.browser.window

object DOMEvents {
  const val LOAD = "load"
  const val RESIZE = "resize"
  const val KEY_PRESS = "keypress"
  const val KEY_DOWN = "keydown"
  const val MOUSE_DOWN = "mousedown"
  const val CHANGE = "change"
  const val PASTE = "paste"
  const val CLICK = "click"
  const val OPEN = "open"
  const val MESSAGE = "message"
  const val CLOSE = "close"
  const val ERROR = "error"
}

val body = lazy { Container(document.querySelector("body")!!) }

fun Double.toFixed(p: Int): String {
  val d = this
  return js("return d.toFixed(2);")
}

fun later(f: () -> Unit) {
  window.setTimeout(f, 0)
}

fun escape(text: String): String {
  return text.replace(Regex("&"), "&amp")
    .replace(Regex("<"), "&lt;")
    .replace(Regex(">"), "&gt;")
    .replace(Regex("\""), "&quot;")
    .replace(Regex("'"), "&#039;")
}

val Element.style: CSSStyleDeclaration get() = window.getComputedStyle(this, null)