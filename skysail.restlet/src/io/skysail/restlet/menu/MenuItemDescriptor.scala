package io.skysail.restlet.menu

import io.skysail.domain.ddd.ScalaEntity
import io.skysail.core.html.Field
import io.skysail.restlet.forms.ListView

class MenuItemDescriptor(menuItem: MenuItem) extends ScalaEntity[String] {

  @Field
  @ListView(hide = true)
  val url = "<a href='" + link + "'>" + name + "</a>";

  val name = menuItem.name
  val link = menuItem.link

  var id: Option[String] = Some(url)

}
