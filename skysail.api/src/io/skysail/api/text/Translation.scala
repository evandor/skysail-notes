package io.skysail.api.text

import java.util.Locale

class Translation(
    text: String,
    store: TranslationStore = null,
    val locale: Locale = Locale.getDefault,
    messageArguments: Seq[Any] = Seq()) {

  def getStoreName() = if (store != null) store.getClass().getSimpleName() else "-"

  var value = text
  def setValue(v: String) = this.value = v

  var renderer: String = null
  def setRenderer(v: String) = this.renderer = v

}