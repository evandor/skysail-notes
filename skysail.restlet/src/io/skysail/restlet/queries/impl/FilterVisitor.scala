package io.skysail.restlet.queries.impl

trait FilterVisitor {
  def visit(node: ExprNode): Any
}