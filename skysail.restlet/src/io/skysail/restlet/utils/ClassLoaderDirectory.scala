package io.skysail.restlet.utils

import org.restlet.Response
import org.restlet.Request
import org.restlet.Context
import org.restlet.data.Reference
import org.restlet.resource.Directory

class ClassLoaderDirectory(context: Context, rootLocalReference: Reference, cl: ClassLoader) extends Directory(context, rootLocalReference) {

    override def handle(request:Request, response: Response ) {
        val saveCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        super.handle(request, response);
        Thread.currentThread().setContextClassLoader(saveCL);
    }
    
    override def toString() = super.toString()// + ": " + if (getRootRef() != null) getRootRef().toString() else ""
  
}