package com.riskpass.ofc

import kotlin.dom.addClass

class ResultPanel: Control(newEl()) {
  private val grad = arrayOf("#54FF54", "#affa52", "#cafb54", "#ddfd57", "#ecfb57", "#f1e751", "#f7d34b", "#fbc547", "#fbc547", "#ff5554")

  init {
    el.addClass("riskpass-ofc-result_panel")
  }

  var result: String get() = el.innerHTML ; set(value: String) {

    if (value.isBlank()) {
      el.innerHTML = ""
      el.removeAttribute("style")
    } else {
      val ratio = safeParseDouble(value)!! / 4.0
      val index = Math.round(24.0 * ratio)
      val colour = if (index <= 8) {
        grad[index]
      } else {
        grad[grad.size - 1]
      }
      el.setAttribute("style", "background-color: $colour;")
      el.innerHTML = Math.round(100.0 * (1 - ratio)).toString() + "% match"
    }
  }
}