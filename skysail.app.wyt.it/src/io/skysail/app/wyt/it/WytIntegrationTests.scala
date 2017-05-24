package io.skysail.app.wyt.it

import io.skysail.testsupport.BrowserTests2
import org.restlet.data.MediaType
import org.json.JSONObject
import org.junit._
import org.assertj.core.api.Assertions._
import org.json4s._
import org.json4s.native.JsonMethods._
import io.skysail.app.wyt.domain.Pact
import io.skysail.app.wyt.resources.PactsResource

class WytIntegrationTests extends BrowserTests2[WytBrowser] {

  protected var entityAsJson: JSONObject = _
  private var pactId: String = _
  private implicit val formats = DefaultFormats

  @Before def setUp() {
    browser = new WytBrowser(2018)
  }

  @Test def pacts_are_available_in_various_formats() {
    var rep = browser.getPacts().getText
    assertThat(rep).startsWith("{")
    assertThat(rep).endsWith("}")
    
    rep = browser.getPacts(MediaType.TEXT_XML).getText
    assertThat(rep).startsWith("<")
    assertThat(rep).endsWith(">")
    
    rep = browser.getPacts(MediaType.TEXT_HTML).getText
    assertThat(rep).contains("<!DOCTYPE html>")
  }

  @Test def get_request_on_PostPactEndpoint_with_html_media_type_returns_HTML() {
    val rep = browser.getPostPacts(MediaType.TEXT_HTML).getText
    assertThat(rep).contains("<!DOCTYPE html>")
  }

  @Test def post_request_on_PostPactEndpoint_with_json_media_type_returns_JSON() {
    val rep = browser.postPostPacts(MediaType.APPLICATION_JSON).getText
    assertThat(rep).contains("<!DOCTYPE html>")
  }

  @Ignore
  @Test def georgios_is_next() {
    val pactRep = browser.createPact.getText
    pactId = parse(pactRep).extract[Pact].id.get
    val turn = browser.getNextTurn()
    assertThat(turn.getText).contains("Georgios is next")
  }

  //  @Test def confirming_yields_next_turn_is_carstens() {
  //    browser.postConfirmation()
  //    val turn = browser.getNextTurn()
  //    assertThat(turn.getText).doesNotContain("Georgios is next")
  //  }
}