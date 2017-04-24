package io.skysail.restlet.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.skysail.restlet.SkysailServerResource;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListView3 {

//    public static final class DEFAULT extends SkysailServerResource<Entity> {
//
//        @Override
//        public Entity getEntity() {
//            return null;
//        }
//
//        @Override
//        public LinkRelation getLinkRelation() {
//            return null;
//        }
//
//        @Override
//        public ApiMetadata getApiMetadata() {
//            return null;
//        }
//
//    }

    Class<? extends SkysailServerResource> link() default SkysailServerResource.class; //default DEFAULT.class;

    int truncate() default -1;

    boolean hide() default false;

    String colorize() default "";

    String prefix() default "";

    String format() default "";
}
