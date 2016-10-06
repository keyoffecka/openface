package com.riskpass.ofc

import org.w3c.dom.Element
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

class PreviewImage(private val resultPanel: ResultPanel, private val errorPanel: ErrorPanel) : Container(newEl()) {
  private var img: HTMLImageElement? = null
  private val div: Element
  val url: String get() = img!!.src

  init {
    el.addClass("riskpass-ofc-preview_image")

    div = newEl();
    el.appendChild(div)

    window.addEventListener(DOMEvents.RESIZE, {
      recalculateSize()
    })
  }

  fun setImage(src: String) {
    errorPanel.label = ""
    resultPanel.result = ""

    if (img != null) {
      div.removeChild(img!!)
    }
    img = document.createElement("img") as HTMLImageElement
    img!!.setAttribute("src", src)
    div.appendChild(img!!)

    recalculateSize()
  }

  override fun onControlAdded() {
    recalculateSize()
  }

  fun recalculateSize() {
    later {
      if (img != null) {
        val r = el.getBoundingClientRect()
        var w = r.width -
          (safeParseDouble(el.style.paddingLeft) ?: 0.0) -
          (safeParseDouble(el.style.paddingRight) ?: 0.0) -
          (safeParseDouble(el.style.borderLeftWidth) ?: 0.0) -
          (safeParseDouble(el.style.borderRightWidth) ?: 0.0)
        w = if (w < 20) 0.0 else w
        var h = r.height -
          (safeParseDouble(el.style.paddingTop) ?: 0.0) -
          (safeParseDouble(el.style.paddingBottom) ?: 0.0) -
          (safeParseDouble(el.style.borderTopWidth) ?: 0.0) -
          (safeParseDouble(el.style.borderBottomWidth) ?: 0.0)
        h = if (h < 20) 0.0 else h
        img!!.setAttribute("style", "width: ${w}px; height: ${h}px;")
      }
    }
  }
}