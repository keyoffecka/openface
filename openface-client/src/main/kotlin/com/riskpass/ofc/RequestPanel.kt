package com.riskpass.ofc

import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import kotlin.dom.addClass
import kotlin.dom.hasClass
import kotlin.dom.removeClass

class RequestPanel(resultPanel: ResultPanel, previewPanel: PreviewPanel, errorPanel: ErrorPanel): Container(newEl()) {
  private val left: ImagePanel
  private val right: ImagePanel
  private val compareButton: Button
  private val waitPanel: Container

  init {
    el.addClass("riskpass-ofc-request_panel")

    val editPanel = Container(newEl())
    editPanel.el.addClass("riskpass-ofc-edit_panel")
    add(editPanel)

    val runPanel = Container(newEl())
    runPanel.el.addClass("riskpass-ofc-compare_panel")
    add(runPanel)

    add(resultPanel)

    left = ImagePanel(this, previewPanel.leftPreviewImage, previewPanel.rightPreviewImage)
    left.input.focus()

    right = ImagePanel(this, previewPanel.rightPreviewImage, previewPanel.leftPreviewImage)

    editPanel.add(left)
    editPanel.add(right)

    waitPanel = Container(newEl())
    waitPanel.el.addClass("riskapss-ofc-wait_panel")
    waitPanel.el.addClass("riskapss-ofc-disabled")
    runPanel.add(waitPanel)

    compareButton = Button()
    compareButton.el.addClass("riskpass-ofc-compare_button")
    runPanel.add(compareButton)
    compareButton.el.addEventListener(DOMEvents.CLICK, {
      if (!(compareButton.el.hasClass("riskpass-ofc-disabled") || compareButton.el.hasClass("riskpass-ofc-invalid"))) {
        waitPanel.el.removeClass("riskapss-ofc-disabled")
        left.enable(false)
        right.enable(false)
        compareButton.el.addClass("riskpass-ofc-disabled")
        errorPanel.label = ""
        resultPanel.result = ""
        previewPanel.leftPreviewImage.recalculateSize()
        previewPanel.rightPreviewImage.recalculateSize()
        try {
          val ws = WebSocket(WS_URL)
          ws.addEventListener(DOMEvents.OPEN, {
            ws.send("{\"type\": \"COMPARE\", \"img1\": \"${previewPanel.leftPreviewImage.url}\", \"img2\": \"${previewPanel.rightPreviewImage.url}\"}")
          })
          ws.addEventListener(DOMEvents.CLOSE, { e ->
            left.enable(true)
            right.enable(true)
            compareButton.el.removeClass("riskpass-ofc-disabled")
            waitPanel.el.addClass("riskapss-ofc-disabled")
          })
          ws.addEventListener(DOMEvents.ERROR, { e ->
            errorPanel.label = "Unexpected error"
            previewPanel.leftPreviewImage.recalculateSize()
            previewPanel.rightPreviewImage.recalculateSize()
            ws.close()
          })
          ws.addEventListener(DOMEvents.MESSAGE, { e ->
            try {
              val me = e as MessageEvent
              val d = JSON.parse<Distance>(me.data as String).hasProperty("distance")
              if (d == null) {
                errorPanel.label = "${(JSON.parse<Error>(me.data as String)).error}"
              } else {
                resultPanel.result = "${d.distance}"
              }
              previewPanel.leftPreviewImage.recalculateSize()
              previewPanel.rightPreviewImage.recalculateSize()
            } finally {
              ws.close()
            }
          })
        } catch (ex: Exception) {
          left.enable(true)
          right.enable(true)
          compareButton.el.removeClass("riskpass-ofc-disabled")
          waitPanel.el.addClass("riskapss-ofc-disabled")

          errorPanel.label = "${ex.message}"
          previewPanel.leftPreviewImage.recalculateSize()
          previewPanel.rightPreviewImage.recalculateSize()
        }
      }
    })

    validate()
  }

  fun validate() {
    if (left.isValid() && right.isValid()) {
      compareButton.el.removeClass("riskpass-ofc-invalid")
    } else {
      compareButton.el.addClass("riskpass-ofc-invalid")
    }
  }
}

data class Distance(
  val distance: Double
)

data class Error(
  val error: String
)