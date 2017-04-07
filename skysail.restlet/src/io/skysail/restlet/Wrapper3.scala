package io.skysail.restlet

import org.restlet.Response

trait Wrapper3 {
  def getResponse(): Response

  def getEntity(): Any
  def setEntity(entity: Any)
  //
  //    //void setConstraintViolationResponse(ConstraintViolationsResponse<T> reponse)
  //
  //    void addError(String msg)
  //    void addInfo(String msg)
  //    void addWarning(String msg)
  //
  //    List<Long> getMessageIds()

}