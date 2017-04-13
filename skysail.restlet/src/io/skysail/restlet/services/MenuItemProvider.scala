package io.skysail.restlet.services

import org.osgi.annotation.versioning.ProviderType
import io.skysail.restlet.menu.MenuItem

@ProviderType
trait MenuItemProvider {

    /**
     * Example implementation
     *
     * <pre>
     * <code>
     *    public List&lt;MenuItem&gt; getMenuEntries() {
     *         MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
     *         appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
     *         return Arrays.asList(appMenu);
     *    }
     * </code>
     * </pre>
     */
   def getMenuEntries(): List[MenuItem]
  
}