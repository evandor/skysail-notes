package io.skysail.restlet.menu;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;

public class MenuItemDescriptorJava implements Entity {

    @Field
    //@ListView
    private String url;

    private String name;

    public MenuItemDescriptorJava(MenuItem menuItem) {
//        name = menuItem.get;
//        String link = menuItem.getLink();
//        url = "<a href='"+link+"'>"+name+"</a>";
    }

    @Override
    public String getId() {
        return url;
    }


}