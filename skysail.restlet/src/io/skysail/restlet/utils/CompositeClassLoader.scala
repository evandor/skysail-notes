package io.skysail.restlet.utils

import java.net.URL

class CompositeClassLoader extends ClassLoader {

  val classLoaders = scala.collection.mutable.ListBuffer[ClassLoader]()

  override def getResource(name: String): URL = {
    for (cl <- classLoaders) {
      val resource = cl.getResource(name);
      if (resource != null) {
        return resource;
      }
    }
    return null;
  }

  override def loadClass(name: String, resolve: Boolean): Class[_] = {
    for (cl <- classLoaders) {
      try {
        return cl.loadClass(name);
      } catch {
        case e: Throwable =>
      }
    }
    throw new ClassNotFoundException(name);

  }

  def addClassLoader(cl: ClassLoader) = classLoaders += cl
}