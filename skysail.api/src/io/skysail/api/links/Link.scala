//package io.skysail.api.links
//
//import org.restlet.data.Method
//
//case class Link(
//  val uri: String,
//  val title:String = "unknown",
//  val alt:String = "",
//  val relation:LinkRelation = LinkRelation.CANONICAL,
//  val verbs:Set[Method] = Set(),
//  val needsAuth:Boolean = false,
//  val linkRole:LinkRole = LinkRole.DEFAULT,
//  val refId:String = null,
//  val cls:Class[_] = null
//) 