package io.skysail.restlet.menu;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.restlet.forms.ListView;

public class MenuItemDescriptor implements Entity {

    @Field
    @ListView
    private String url;

    private String name;

    public MenuItemDescriptor(MenuItem menuItem) {
//        name = menuItem.get;
//        String link = menuItem.getLink();
//        url = "<a href='"+link+"'>"+name+"</a>";
    }

    @Override
    public String getId() {
        return url;
    }


}
