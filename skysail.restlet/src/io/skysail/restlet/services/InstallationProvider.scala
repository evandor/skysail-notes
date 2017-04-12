package io.skysail.restlet.services

trait ScalaInstallationProvider {
  def getPort(): Int
  def getProductName(): String
}