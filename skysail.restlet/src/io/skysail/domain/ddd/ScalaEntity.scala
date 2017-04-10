package io.skysail.domain.ddd

trait ScalaEntity[IdType] {
  var id:Option[IdType]
  //def setId(id: Option[IdType])
}