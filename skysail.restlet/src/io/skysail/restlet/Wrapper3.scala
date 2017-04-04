package io.skysail.restlet

import org.restlet.Response

trait Wrapper3 {
  def getResponse(): Response

  //    Object getEntity();
  //
  //    void setEntity(Object entity);
  //
  //    //void setConstraintViolationResponse(ConstraintViolationsResponse<T> reponse);
  //
  //    void addError(String msg);
  //    void addInfo(String msg);
  //    void addWarning(String msg);
  //
  //    List<Long> getMessageIds();

}