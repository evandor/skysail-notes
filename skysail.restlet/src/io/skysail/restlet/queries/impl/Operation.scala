package io.skysail.restlet.queries.impl

object Operation {
  sealed trait EnumVal
  case object EQUAL extends EnumVal
  case object APPROX extends EnumVal
  case object GREATER extends EnumVal
  case object LESS extends EnumVal
  case object PRESENT extends EnumVal
  case object SUBSTRING extends EnumVal
  case object AND extends EnumVal
  case object OR extends EnumVal
  case object NOT extends EnumVal
  case object IN extends EnumVal
  case object NONE extends EnumVal
}