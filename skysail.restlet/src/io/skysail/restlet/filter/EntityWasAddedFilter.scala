package io.skysail.restlet.filter

import org.slf4j.LoggerFactory

import io.skysail.restlet.ScalaAbstractResourceFilter
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.Wrapper3
import io.skysail.restlet.ScalaResponseWrapper

class EntityWasAddedFilter[T:Manifest](entity: T) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[EntityWasAddedFilter[T]])

  override def doHandle(resource: SkysailServerResource, responseWrapper:  Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    val infoMessage = resource.getClass().getSimpleName() + ".saved.success";
    //responseWrapper.addInfo(infoMessage);

//    if (application instanceof MessageQueueProvider) {
//      MessageQueueHandler messageQueueHandler = ((MessageQueueProvider) application)
//        .getMessageQueueHandler();
//      if (messageQueueHandler != null) {
//        Object currentEntity = resource.getCurrentEntity();
//        try {
//          String serialized = mapper.writeValueAsString(currentEntity);
//          messageQueueHandler.send("topic://entity." + currentEntity.getClass().getName().replace(".", "_") + ".post", serialized);
//        } catch (IOException e) {
//          log.error(e.getMessage(), e);
//        }
//      }
//    }
    super.doHandle(resource, responseWrapper);
    FilterResult.CONTINUE;
  }
}