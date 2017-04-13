package io.skysail.restlet.services

trait StringTemplateProvider {
  def getTemplates(): Map[String, String]
  def getNamespace(): String
  def getShortName(): String
}