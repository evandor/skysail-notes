package io.skysail.core.um.domain

case class SkysailUser(val username: String, val password: String, val id: String) {
      
  val roles = Set[SkysailRole]()
  def getRoles = roles
      
}