package io.skysail.domain.model



case class ResourceModel(
    val path: String,
    val targetClass: Class[_]
    //, val applicationModel: ApplicationModel = null
    ) {
  
  require(path != null, "A ResourceModel's path must not be null")
  require(path.trim().length() > 0, "A ResourceModel's path must not be empty")
  
}