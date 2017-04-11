package io.skysail.restlet.filter

import org.slf4j.LoggerFactory
import io.skysail.restlet.ScalaAbstractListResourceFilter
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.ScalaListResponseWrapper
import io.skysail.server.restlet.filter.FilterResult
import io.skysail.restlet.utils.ScalaCookiesUtils
import io.skysail.domain.ddd.ScalaEntity
import io.skysail.restlet.Wrapper3
import io.skysail.restlet.ScalaListResponseWrapper

class ScalaDataExtractingListFilter[T:Manifest] extends ScalaAbstractListResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[ScalaExceptionCatchingListFilter[T]])

  override def doHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());

    //val installation = ScalaCookiesUtils.getInstallationFromCookie(resource.getRequest()).orElse(null);
    val entity = resource.getEntity()
    if (entity.isInstanceOf[List[T]]) {
      val data = entity.asInstanceOf[List[T]]
      //sanitizeIds(data);

      responseWrapper.asInstanceOf[ScalaListResponseWrapper[T]].setEntity(data);
//      resource.setCurrentEntity(data); // TODO why both wrapper AND
      // resource?
    } else {
      sanitizeIds(entity);

//      responseWrapper.setEntity(entity);
//      resource.setCurrentEntity(entity); // TODO why both wrapper AND
      // resource?

    }
    super.doHandle(resource, responseWrapper);
    return FilterResult.CONTINUE;
  }

  private def sanitizeIds(data: List[T]) = {
    data.foreach(element => {
      if (element.isInstanceOf[ScalaEntity[_]]) {
        replaceHash(element);
      }
    });
  }
  
  private def sanitizeIds( data: Any) = {
//		if (data.isInstanceOf[List[_]]) {
//			data.asInstanceOf[List[_]].foreach(element => {
//				if (element instanceof Entity) {
//					replaceHash(element);
//				}
//			});
//		} else if (data instanceof Entity) {
//			replaceHash(data);
//		}
	}

  private def replaceHash(element: Any) = {
    val identifiable = element.asInstanceOf[ScalaEntity[_]]
    if (identifiable.id != null) {
      try {
        val field = identifiable.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(identifiable, identifiable.id.get.toString().replace("#", ""));
      } catch {
        case e: Throwable => {
          val getIdMethod = identifiable.getClass().getDeclaredMethod("setId", classOf[String])
          getIdMethod.invoke(identifiable, identifiable.id.get.toString().replace("#", ""))
        }
      }

    }
  }

}