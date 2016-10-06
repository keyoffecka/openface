package com.riskpass.ofc

import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document
import kotlin.dom.addClass

fun newEl(): Element { return document.createElement("div") }
fun newInput(): HTMLInputElement { return document.createElement("input") as HTMLInputElement }

open class Control(open val el: Element) {
  internal var _parent: Container? = null
  val parent: Container? get() = _parent

  open fun onControlAdded() {
  }

  open fun onControlRemoved() {
  }
}

open class Input: Control(newInput()) {
  override val el: HTMLInputElement = super.el as HTMLInputElement

  init {
    el.addClass("riskpass-ofc-input")
  }

  fun focus() {
    later {
      el.focus()
    }
  }
}

open class Button: Control(newEl()){
  init {
    el.addClass("riskpass-ofc-button")
  }
}

open class Container(el: Element): Control(el) {
  fun add(control: Control) {
    el.appendChild(control.el)
    control._parent = this
    control.onControlAdded()
  }

  fun remove(control: Control) {
    el.removeChild(control.el)
    control._parent = null
    control.onControlRemoved()
  }
}