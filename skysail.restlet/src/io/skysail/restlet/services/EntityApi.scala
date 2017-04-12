package io.skysail.restlet.services

trait EntityApi {
  def getEntityClass(): Class[_]
  def  create[T]()
  def persist[T](entity: T):Unit
}