package io.skysail.testsupport;

public class BrowserTests2<T extends ScalaApplicationBrowser>  extends TestsupportTestBase {

    // doesnt seem to work in integration tests
    //@Rule
    //public ExpectedException thrown = ExpectedException.none();

    protected T browser;

}
