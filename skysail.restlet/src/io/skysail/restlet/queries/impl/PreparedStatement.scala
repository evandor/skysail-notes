package io.skysail.restlet.queries.impl

case class PreparedStatement(joinWith: String, statements: List[PreparedStatement]) {

  var sb = new StringBuilder()
  var params = Map[String,String]()

  if (statements.length == 1 && "NOT".equals(joinWith)) {
    sb = new StringBuilder(joinWith).append(" (").append(statements(0).getSql()).append(")");
  } else {
    sb = new StringBuilder(statements.map(s => { s.getSql() }).mkString(" " + joinWith + " "))
  }
  statements
    .map { s => s.getParams() }  
    .flatten { params => params.iterator }
    .foreach(m => { params.+(m._1 -> m._2)})

  def getSql(): String = return sb.toString()
  def getParams() = params
}