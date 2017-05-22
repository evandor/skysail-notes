package io.skysail.app.wyt.it

import io.skysail.testsupport.BrowserTests2
import org.restlet.data.MediaType
import org.json.JSONObject
import org.junit._
import org.assertj.core.api.Assertions._
import org.json4s._
import org.json4s.native.JsonMethods._
import io.skysail.app.wyt.domain.Pact

class WytIntegrationTestsBase extends BrowserTests2[WytBrowser] {

  protected var entityAsJson: JSONObject = _
  private var pactId: String = _
  private implicit val formats = DefaultFormats

  @Before def setUp() {
    browser = new WytBrowser(MediaType.APPLICATION_JSON, 2018)
  //  val pactRep = browser.createPact.getText
   // pactId = parse(pactRep).extract[Pact].id.get
  }

  @Test def georgios_is_next() {
   // val turn = browser.getNextTurn()
    //assertThat(turn.getText).contains("Georgios is next")
  }

//  @Test def confirming_yields_next_turn_is_carstens() {
//    browser.postConfirmation()
//    val turn = browser.getNextTurn()
//    assertThat(turn.getText).doesNotContain("Georgios is next")
//  }
}