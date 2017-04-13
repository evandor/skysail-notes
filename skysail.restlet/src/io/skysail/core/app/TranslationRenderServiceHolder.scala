package io.skysail.core.app

import io.skysail.api.text.TranslationRenderService
import io.skysail.core.Constants
import org.slf4j.LoggerFactory

case class ScalaTranslationRenderServiceHolder(val service: TranslationRenderService, props: Map[String, String]) {

  val log = LoggerFactory.getLogger(this.getClass())

  def getServiceRanking(): Int = {
    val serviceRanking = props.get(org.osgi.framework.Constants.SERVICE_RANKING)
    if (serviceRanking.isDefined) serviceRankingAsInt(serviceRanking) else 0
  }

  def serviceRankingAsInt(sr: Option[String]): Int = {
    try {
      return Integer.valueOf(sr.get);
    } catch {
      case e: Throwable => log.error(e.getMessage(), e)
    }
    0
  }
}