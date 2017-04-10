package io.skysail.restlet.utils

import org.apache.commons.beanutils.BeanUtilsBean
import java.util.Locale
import org.apache.commons.beanutils.ConvertUtilsBean

class ScalaSkysailBeanUtilsBean(bean: Any, locale: Locale) 
  extends BeanUtilsBean(new ConvertUtilsBean() {}) {
  
  
  
}