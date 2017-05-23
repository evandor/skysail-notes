package io.skysail.app.wyt.it

import org.restlet.data.MediaType

//sealed trait Path {
//  def -->(linkTitle: String):Path
//  def get(mediaType: MediaType) = { ???  }
//}
//case object Root extends Path {
//  def -->(linkTitle: String):Path = Root
//}

object Path {

  sealed trait Method
  case object ANY extends Method
  case object GET extends Method
  case object POST extends Method

  sealed trait PathElem
  case class Static(name: String) extends PathElem
  //case object * extends PathElem

  sealed trait RouteDef[Self] {
    //def withMethod(method: Method): Self
    //def method: Method
    def elems: List[PathElem]
  }

  implicit def stringToRouteDef0(name: String) = RouteDef0(Static(name) :: Nil)

  case class RouteDef0(elems: List[PathElem]) extends RouteDef[RouteDef0]
  case class RouteDef1(elems: List[PathElem]) extends RouteDef[RouteDef1]
  case class RouteDef2(elems: List[PathElem]) extends RouteDef[RouteDef2]
}