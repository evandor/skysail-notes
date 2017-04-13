package io.skysail.restlet.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Entity;
import io.skysail.restlet.ScalaSkysailServerResource;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListView {

    public static final class DEFAULT extends ScalaSkysailServerResource {

        @Override
        public Entity getEntity() {
            return null;
        }

        @Override
        public LinkRelation getLinkRelation() {
            return null;
        }

        @Override
        public ApiMetadata getApiMetadata() {
            return null;
        }

    }

    Class<? extends ScalaSkysailServerResource> link() default DEFAULT.class;

    int truncate() default -1;

    boolean hide() default false;

    String colorize() default "";

    String prefix() default "";

    String format() default "";
}
