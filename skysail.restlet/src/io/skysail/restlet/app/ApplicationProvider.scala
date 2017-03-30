package io.skysail.restlet.app

trait ScalaApplicationProvider extends Ordered[ScalaApplicationProvider] {
  def getSkysailApplication(): ScalaSkysailApplication
  def compare(that: ScalaApplicationProvider): Int = getSkysailApplication().getName.compareTo(that.getSkysailApplication().getName)
}