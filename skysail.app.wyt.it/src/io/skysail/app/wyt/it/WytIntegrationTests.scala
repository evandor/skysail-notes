//package io.skysail.app.wyt.it
//
//import io.skysail.testsupport.BrowserTests2
//import org.junit.Before
//import org.restlet.data.MediaType
//import org.junit.Test
//import org.json.JSONObject
//import io.skysail.app.wyt.it.WytBrowser
//
//class WytIntegrationTestsBase extends BrowserTests2[WytBrowser] {
//
//  protected var entityAsJson: JSONObject = _
//
//  @Before
//  def setUp() {
//    browser = new WytBrowser(MediaType.APPLICATION_JSON, 2018/*determinePort()*/);
// //   browser.setUser("admin");
//    entityAsJson = browser.createRandomEntity();
//  }
//
//  //    @Test(expected = IllegalStateException.class)
//  //    @Ignore
//  //    public void cannot_create_entity_if_not_logged_in() { // NOSONAR
//  //        browser // no login here!
//  //            .create(entityAsJson);
//  //    }
//
//  @Test
//  def create_and_read_entity() {
//    browser
//      .loginAs("admin", "skysail")
//      .create(entityAsJson);
//    //        String html = browser.getEntities().getText();
//    //        System.out.println(html);
//    //        assertFalse(html.contains("\"created\":null"));
//    //        assertTrue(html.contains("\"name\":\"account_"));
//  }
//}