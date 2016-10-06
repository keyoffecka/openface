package com.riskpass.ofc

import kotlin.browser.window

const val WS_URL = "wss://devrpcentral.riskpass.com:443/openface"

fun <T> cast(a: dynamic): T = a

fun <T> T?.hasProperty(propertyName: String): T? {
  val o = this
  val has = o !== null
  if (has) {
    js("if (typeof o[propertyName] === typeof {}[0]) has = false")
  }
  return if (has) cast<T>(o) else null
}

fun main() {
  window.addEventListener(DOMEvents.LOAD, {
    body.value.add(MainPanel())
  })
}