package io.skysail.restlet.queries.impl


/**
 * ExprNodes are used to model, parse and render LDAP-style filter expressions
 * like (&(A=a)(B=b)).
 *
 * There are leaf-nodes (without children) and nodes with children, and each
 * type of node has one associated operation (Like "EQUAL", "AND", "OR", etc).
 *
 */
trait ExprNode {
  /**
   * Traverse the node by applying the provided filter to each subnode.
   *
   * @param visitor
   * @return the result.
   */
  def accept(visitor: FilterVisitor): Any

  /**
   * @return true if this is a type of node which cannot have children.
   */
  def isLeaf(): Boolean

  /**
   * @return the (root) operation of this node (like "AND", "OR", "LESS", ...)
   */
  // def getOperation(): Operation 

  /**
   * @return a LDAP-style string representation of this node (like "(&(a=b)(x=y))");
   */
  def asLdapString(): String

  def getSelected(lines: Map[String, String]): Set[String]

  def getKeys(): Set[String]

  def reduce(value: String, format: String): ExprNode

  /**
   * @param sqlFilterVisitor
   * @return a PreparedStatement representation of this node, which can be rendered as a SQL Where Statement;
   */
  def createPreparedStatement(sqlFilterVisitor: SqlFilterVisitor): PreparedStatement

  def evaluateEntity(entityEvaluationVisitor: EntityEvaluationFilterVisitor): Boolean

  def evaluateValue(gotten: Any): Boolean
}