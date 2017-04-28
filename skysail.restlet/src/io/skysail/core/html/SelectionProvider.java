package io.skysail.core.html;

import java.util.Map;

public interface SelectionProvider {

    public Map<String, String> getSelections();

    public void setConfiguration(Object osgiServicesProvider);

    //public void setResource(Resource resource);

}
