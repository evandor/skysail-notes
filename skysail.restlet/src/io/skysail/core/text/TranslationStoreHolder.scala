package io.skysail.core.text

import io.skysail.api.text.TranslationStore
import java.lang.ref.WeakReference
import org.slf4j.LoggerFactory
import org.osgi.framework.Constants

class ScalaTranslationStoreHolder(store: TranslationStore, props: Map[String,String]) {
  
  val log = LoggerFactory.getLogger(this.getClass)

  val storeRef = new WeakReference[TranslationStore](store);
  val name = store.getClass().getName();

  def serviceRanking(): Int = {
    val sr = props.get(Constants.SERVICE_RANKING);
    try {
      return Integer.valueOf(sr.get);
    } catch {
      case e: Throwable => log.error(e.getMessage(), e)
    }
    0
  }
}