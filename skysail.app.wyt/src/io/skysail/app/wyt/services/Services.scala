package io.skysail.app.wyt.services

import io.skysail.app.wyt.WytApplication

object Services {
  def pacts = org.restlet.Application.getCurrent().asInstanceOf[WytApplication].pactService
  def confirmations = org.restlet.Application.getCurrent().asInstanceOf[WytApplication].confirmationService
  //def turns = org.restlet.Application.getCurrent().asInstanceOf[WytApplication].turnService
  def cars = org.restlet.Application.getCurrent().asInstanceOf[WytApplication].carService
}

