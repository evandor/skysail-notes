package io.skysail.app.wyt.it

import io.skysail.app.wyt.domain.Pact
import io.skysail.app.wyt.domain.Pact$
import io.skysail.app.wyt.resources.PactsResource
import io.skysail.testsupport.BrowserTests2
import java.math.BigInteger
import org.assertj.core.api.Assertions._
import org.restlet.data._
import org.json.JSONObject
import org.json4s._
import org.json4s.native.JsonMethods._
import org.junit._
import io.skysail.app.wyt.domain.Car

class WytIntegrationTests extends BrowserTests2[WytBrowser] {

  private implicit val formats = DefaultFormats

  @Before def setUp() {
    browser = new WytBrowser(2018)
  }

  //@Test
  def postPact_returns_created_pact_with_id() {
    val title = "pactTitle_" + new BigInteger(130, random).toString(32)
    val pact = browser.postToPostPacts(Pact(None, title))
    assertThat(pact.id.get).isNotNull()
    assertThat(pact.title).isEqualTo(title)
  }

  @Test
  def postCar_returns_created_car_with_id() {
    val model = "carModel_" + new BigInteger(130, random).toString(32)
    val car = browser.postToPostCars(Car(model, "20", None))
    //assertThat(car.id.get).isNotNull()
    assertThat(car.model).isEqualTo(model)
  }

  //@Test
  def postPact_returns_created_pact_with_default_turn() {
    val pact = browser.postToPostPacts(Pact(None, "pactTitle_" + new BigInteger(130, random).toString(32)))
    assertThat(pact.getTurn()).isNotNull()
    assertThat(pact.getTurn().nextTurn).isEqualTo("test")
  }

  //@Test
  def pacts_are_available_in_various_formats() {
    var rep = browser.getPacts().getText
    assertThat(rep).startsWith("{")
    assertThat(rep).endsWith("}")

    rep = browser.getPacts(MediaType.TEXT_XML).getText
    assertThat(rep).startsWith("<")
    assertThat(rep).endsWith(">")

    rep = browser.getPacts(MediaType.TEXT_HTML).getText
    assertThat(rep).contains("<!DOCTYPE html>")
  }

  //@Test
  def get_request_on_PostPactEndpoint_with_html_media_type_returns_HTML() {
    val rep = browser.getPostPacts(MediaType.TEXT_HTML).getText
    assertThat(rep).contains("<!DOCTYPE html>")
  }

  //    @Test
  //    def post_request_on_PostPactEndpoint_with_json_media_type_returns_JSON() {
  //      val title = "pactTitle_" + new BigInteger(130, random).toString(32)
  //      val rep = browser.postToPostPacts(createForm(Pact(None, title)), MediaType.APPLICATION_JSON).getText
  //      val persistedPact = parse(rep).extract[Pact]
  //      assertThat(persistedPact.id.get).isNotNull()
  //      assertThat(persistedPact.title).isEqualTo(title)
  //    }
  //
  //  @Test
  //  def georgios_is_next_for_default_pact() {
  //    val title = "pactTitle_" + new BigInteger(130, random).toString(32)
  //    val rep = browser.postToPostPacts(createForm(Pact(None, title)), MediaType.APPLICATION_JSON).getText
  //    val persistedPact = parse(rep).extract[Pact]
  //    val pactId = persistedPact.id.get.replace("#","")
  //    
  //    val pactRep = browser.createPact.getText
  //    pactId = parse(pactRep).extract[Pact].id.get
  //    val turn = browser.getNextTurn()
  //    assertThat(turn.getText).contains("Georgios is next")
  //  }

  //  @Test def confirming_yields_next_turn_is_carstens() {
  //    browser.postConfirmation()
  //    val turn = browser.getNextTurn()
  //    assertThat(turn.getText).doesNotContain("Georgios is next")
  //  }
}