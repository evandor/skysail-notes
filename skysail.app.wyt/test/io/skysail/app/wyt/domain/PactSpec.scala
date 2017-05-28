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
import java.math.BigInteger
import java.security.SecureRandom
import org.scalatest.FlatSpec
import org.assertj.core.api.Assertions._

//@RunWith(classOf[JUnitRunner])
class PactSpec extends FlatSpec {

  private implicit val formats = DefaultFormats

  //  // Als User Georgios möchte ich im Browser die Url ... angeben, um den Pact abzufragen
  //  "User Georgios" should "be able to retrieve a Pact identified by its name" in {
  //    //browser.login("georgios")
  //   // val pact = ...
  //   // browser.open(theUrl)
  //  
  //    //assert(pact.title == "")
  //  }
  //  
  val random = new SecureRandom();

  "simple Pact" should "should be serialized as JSON" in {
    val pact = Pact(None, "pactTitle_" + new BigInteger(130, random).toString(32))
    val extr = Extraction.decompose(pact)
    println(extr)
    println(pretty(render(extr)))
  }

  "the JSON representation of a simple Pact" should "should be deserialized" in {
    val json = """ { "title" : "pactTitle_98qgoq2cb9vj6p9v7p7jno3qpn",
                     "turn" : {
                       "nextTurn" : "test",
                       "lastConfirmation" : ""
                     }
                   } """
    val pact = parse(json).extract[Pact]
    assertThat(pact.title).isEqualTo("pactTitle_98qgoq2cb9vj6p9v7p7jno3qpn")
  }

  "the JSON representation of a Pact with missing turn" should "should be deserialized" in {
    val json = """ { "title" : "pactTitle_4g0j7pimsj8j5mm554h03u6f7t" } """
    val pact = parse(json).extract[Pact]
    assertThat(pact.title).isEqualTo("pactTitle_4g0j7pimsj8j5mm554h03u6f7t")
  }

}