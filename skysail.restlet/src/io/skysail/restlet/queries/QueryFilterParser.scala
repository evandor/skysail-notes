package io.skysail.restlet.queries

import io.skysail.restlet.queries.impl.ExprNode

trait QueryFilterParser {
  def getSelected(lines: Map[String, String], value: String): Set[String]
  def parse(filterstring: String): ExprNode
}