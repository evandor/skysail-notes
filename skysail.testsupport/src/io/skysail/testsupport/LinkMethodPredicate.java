package io.skysail.testsupport;

import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.util.Series;

import io.skysail.api.links.Link;
import lombok.ToString;

@ToString(callSuper = true)
public class LinkMethodPredicate extends LinkPredicate {

    private Method method;

    public LinkMethodPredicate(Method method, Series<Header> series) {
        super(series);
        this.method = method;

    }

    @Override
    public boolean test(Link l) {
        return l.getVerbs().contains(method);
    }

}
