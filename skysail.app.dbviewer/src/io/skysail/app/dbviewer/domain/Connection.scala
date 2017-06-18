package io.skysail.app.dbviewer.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.core.domain.ScalaEntity

@BeanProperty
case class Connection (
    @(Field @field)(description = "Connection name/identifier") 
    var id: Option[String] = None,
    
    @(Field @field)(description = "Connection name") 
    @BeanProperty var name: String = "",

    @(Field @field)(description = "Connection url") 
    @BeanProperty var url: String = "",

    @(Field @field)(description = "Connection username") 
    @BeanProperty var username: String = "",

    @(Field @field)(description = "Connection password") 
    @BeanProperty var password: String = ""

) extends ScalaEntity[String] {}