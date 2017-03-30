//package io.skysail.restlet
//
//object ScalaEntityFactory {
//  public static <T extends Entity> SkysailEntityModel<T> createFrom(SkysailApplication skysailApplication, @NonNull Class<T> identifiable, SkysailServerResource<?> resourceInstance) {
//        return new SkysailEntityModel<>(skysailApplication.getFacetsProvider(), identifiable, resourceInstance);
//    }
//}