package io.skysail.app.wyt.it

import io.skysail.testsupport.BrowserTests2
import org.restlet.data.MediaType
import org.json.JSONObject
import org.junit._
import org.junit.Assert._
import org.hamcrest.CoreMatchers._

class WytIntegrationTestsBase extends BrowserTests2[WytBrowser] {

  protected var entityAsJson: JSONObject = _

  @Before def setUp() {
    browser = new WytBrowser(MediaType.APPLICATION_JSON, 2018 /*determinePort()*/ );
  }

  @Test def georgios_is_next() {
    val turn = browser.getNextTurn()
    assertThat(turn.nextTurn, is("Georgios is next"))
  }

  @Ignore
  @Test def confirming_yields_next_turn_is_carstens() {
    browser.postConfirmation()
    val turn = browser.getNextTurn()
    assertThat(turn.nextTurn, is("Carsten is next"))
  }
}