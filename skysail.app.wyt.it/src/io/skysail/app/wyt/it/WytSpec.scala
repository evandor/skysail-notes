package io.skysail.app.wyt.it

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfterEach
import org.restlet.data.MediaType

class WytSpec extends FlatSpec with BeforeAndAfterEach {

  var browser: WytBrowser = _
  
  override def beforeEach() {
    browser = new WytBrowser(MediaType.APPLICATION_JSON, 2018 /*determinePort()*/ );
    //   browser.setUser("admin");
   // entityAsJson = browser.createRandomEntity();
  }

  "confirming a turn" should "yield the next turn" in {
    //    turn = getCurrentTurn
    //    assert(turn.next == Georgios)
    //    confirm(turn)
    //    trun = getCurrentTurn()
    //     assert(turn.next == Carsten)
//    assert(false)

  }

}