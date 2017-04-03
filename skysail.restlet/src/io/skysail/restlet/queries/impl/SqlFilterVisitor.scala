package io.skysail.restlet.queries.impl

class SqlFilterVisitor extends FilterVisitor {
  override def visit(exprNode: ExprNode): PreparedStatement = exprNode.createPreparedStatement(this)
}