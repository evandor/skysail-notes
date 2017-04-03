package io.skysail.restlet.utils

import io.skysail.restlet.forms.ScalaFormField

object ScalaMessagesUtils {
  def getBaseKey(entityClass: Class[_], f: ScalaFormField) = f.getId()
  def getSimpleName(f: ScalaFormField) = f.getId()
}