package io.skysail.app.wyt.domain

import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import io.skysail.core.ApiVersion
import io.skysail.core.app.SkysailApplication
import org.mockito._
import org.mockito.Mockito.{ mock, when }
import io.skysail.core.app.ScalaServiceListProvider
import io.skysail.api.um._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{ read, write }
import org.restlet._
import scala.util.Try
import com.tinkerpop.blueprints.impls.orient._
import org.scalatest.fixture.FlatSpec

//@RunWith(classOf[JUnitRunner])
abstract class PactSpec extends FlatSpec {

//  // Als User Georgios möchte ich im Browser die Url ... angeben, um den Pact abzufragen
//  "User Georgios" should "be able to retrieve a Pact identified by its name" in {
//    //browser.login("georgios")
//   // val pact = ...
//   // browser.open(theUrl)
//  
//    //assert(pact.title == "")
//  }
//  
}