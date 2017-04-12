package io.skysail.restlet

import org.restlet.Component
import org.slf4j.LoggerFactory
import org.restlet.data.Protocol
import org.restlet.engine.Engine
import org.restlet.ext.slf4j.Slf4jLoggerFacade

class ScalaSkysailComponent extends Component {

  var log = LoggerFactory.getLogger(this.getClass())

  log.debug("Creating Restlet Component: {}", this.getClass().getName())

  getClients().add(Protocol.CLAP)

  val httpClient = getClients().add(Protocol.HTTP)
  httpClient.getContext().getParameters().add("requestBufferSize", "16384000")
  httpClient.getContext().getParameters().add("responseBufferSize", "16384000")

  val httpsClient = getClients().add(Protocol.HTTPS)
  httpsClient.getContext().getParameters().add("requestBufferSize", "16384000")
  httpsClient.getContext().getParameters().add("responseBufferSize", "16384000")

  getClients().add(Protocol.FILE)

  //  {ES} {es} {hh} {cig} {fi}
  getLogService().setResponseLogFormat("{ciua} {cri} {ra} {m} {rp} {rq} {S}")

  Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade())
}