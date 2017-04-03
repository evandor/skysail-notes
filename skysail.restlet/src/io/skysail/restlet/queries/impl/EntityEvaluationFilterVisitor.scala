package io.skysail.restlet.queries.impl

class EntityEvaluationFilterVisitor extends FilterVisitor {
  def visit(node: ExprNode): Any = node.evaluateEntity(this)
}