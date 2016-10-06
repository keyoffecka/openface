package com.riskpass.ofc

import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass

class ImagePanel(requestPanel: RequestPanel, previewImage: PreviewImage, otherPreviewImage: PreviewImage) : Container(newEl()) {
  val input: Input
  val openButtonPanel: Container
  val file: Control

  private var prevValue: String = ""

  init {
    el.addClass("riskpass-ofc-image_panel")

    file = Control(document.createElement("input"))

    input = Input()
    input.el.addClass("riskpass-ofc-url")
    input.el.addEventListener(DOMEvents.KEY_DOWN, { later {
      if (input.el.value != prevValue) {
        (file.el as HTMLInputElement).value = ""
        prevValue = input.el.value
        previewImage.setImage(prevValue)
        requestPanel.validate()
        otherPreviewImage.recalculateSize()
      }
    }})
    input.el.addEventListener(DOMEvents.PASTE, { later {
      if (input.el.value != prevValue) {
        (file.el as HTMLInputElement).value = ""
        prevValue = input.el.value
        previewImage.setImage(prevValue)
        requestPanel.validate()
        otherPreviewImage.recalculateSize()
      }
    }})
    add(input)

    file.el.setAttribute("type", "file")
    file.el.setAttribute("accept", "image/*")
    file.el.addEventListener(DOMEvents.CHANGE, {
      val fileEl = file.el as HTMLInputElement
      for (i in 0..fileEl.files!!.length-1) {
        val f = fileEl.files!!.item(i)!!
        input.el.value = f.name
        val reader = FileReader();
        reader.addEventListener(DOMEvents.LOAD, { e ->
          previewImage.setImage(cast<FileReader>(e.target).result)
        })
        reader.readAsDataURL(f)
      }
      requestPanel.validate()
    })

    val button = Button()
    button.el.addClass("riskpass-ofc-open_button")

    openButtonPanel = Container(newEl())
    openButtonPanel.el.addClass("riskpass-ofc-open_button_panel")
    openButtonPanel.add(button)
    openButtonPanel.add(file)
    add(openButtonPanel)
  }

  fun enable(enable: Boolean) {
    if (enable) {
      openButtonPanel.el.removeClass("riskpass-ofc-disabled")
      file.el.removeAttribute("disabled")
      input.el.removeAttribute("disabled")
    } else {
      openButtonPanel.el.addClass("riskpass-ofc-disabled")
      file.el.setAttribute("disabled", "disabled")
      input.el.setAttribute("disabled", "disabled")
    }
  }

  fun isValid(): Boolean {
    return input.el.value.isNotBlank()
  }
}