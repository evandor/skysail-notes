package io.skysail.testsupport;

import org.restlet.data.Header;
import org.restlet.util.Series;

import io.skysail.api.links.Link;

public class LinkTitlePredicate extends LinkPredicate {

    private String title;

    public LinkTitlePredicate(String title, Series<Header> series) {
        super(series);
        this.title = title;
    }

    @Override
    public boolean test(Link l) {
        return l.getTitle().equals(title);
    }

    @Override
    public String toString() {
        return new StringBuilder("LinkTitlePredicate(title='").append(title).append("', available links: \n")
                .append(super.toString()).toString();
    }

}
