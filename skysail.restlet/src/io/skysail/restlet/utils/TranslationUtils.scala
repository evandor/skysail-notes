package io.skysail.restlet.utils

import org.restlet.resource.ServerResource
import io.skysail.core.text.TranslationStoreHolder
import io.skysail.api.text.Translation
import io.skysail.api.text.I18nArgumentsProvider
import java.util.Locale
import io.skysail.api.text.TranslationStore
import io.skysail.server.app.TranslationRenderServiceHolder

object ScalaTranslationUtils {

  def getBestTranslation(stores: Set[TranslationStoreHolder], key: String, resource: ServerResource): Option[Translation] = {

    var storeIsNotNull = (store: TranslationStoreHolder) => store.getStore.get != null
    var translationIsNotNull = (t: Translation) => t != null

    getSortedTranslationStores(stores)
      .filter { storeIsNotNull(_) }
      .map { createTranslationFromStore(key, resource, _, stores) }
      .filter(translationIsNotNull(_))
      .headOption
  }
  //
  //    public static List<Translation> getAllTranslations(Set<TranslationStoreHolder> stores, String key, SkysailServerResource<?> resource) {
  //        List<TranslationStoreHolder> sortedTranslationStores = getSortedTranslationStores(stores);
  //        return sortedTranslationStores.stream()
  //                .filter(store -> store.getStore().get() != null)
  //                .map(store -> createFromStore(key, resource, store, stores))
  //                .filter(t -> t != null)
  //                .collect(Collectors.toList());
  //    }
  //
  //    public static Translation getTranslation(String key, Set<TranslationStoreHolder> stores, String selectedStore,
  //            SkysailServerResource<?> resource) {
  //        Optional<TranslationStoreHolder> storeToUse = stores.stream()
  //                .filter(s -> s.getStore().get().getClass().getName().equals(selectedStore))
  //                .findFirst();
  //        if (storeToUse.isPresent()) {
  //            return createFromStore(key, resource, storeToUse.get(), stores);
  //        }
  //        return null;
  //    }
  //
  def render(translationRenderServices: Set[TranslationRenderServiceHolder], translation: Translation): Translation = {
    getSortedTranslationRenderServices(translationRenderServices)
      .filter(_.getService.get.applicable(translation.getValue()))
      .map(renderService => {
        translation.setValue(renderService.getService().get().render(translation));
        translation.setRenderer(renderService.getService().get().getClass().getSimpleName());
        translation;
      })
      .headOption.getOrElse(translation);
  }

  private def getSortedTranslationRenderServices(services: Set[TranslationRenderServiceHolder]) = services.toSeq.sortWith(_.getServiceRanking > _.getServiceRanking)

  private def getSortedTranslationStores(stores: Set[TranslationStoreHolder]) = {
    stores.toSeq.sortWith(_.getServiceRanking < _.getServiceRanking)
  }
  //
  private def createTranslationFromStore(
    key: String,
    resource: ServerResource,
    store: TranslationStoreHolder,
    stores: Set[TranslationStoreHolder]): Translation = {

    val result = store.getStore().get().get(key, resource.getClass().getClassLoader(), resource.getRequest())
      .orElse(null);
    if (result == null) {
      /*if (key.endsWith(".desc") || key.endsWith(".placeholder") || key.endsWith(".message")) {
                  return null;
              } else {
                  String[] split = key.split("\\.");
                  result = split[split.length-1];
              }*/
      return null;
    }
    if (resource.isInstanceOf[I18nArgumentsProvider]) {
      val messageArguments = resource.asInstanceOf[I18nArgumentsProvider].getMessageArguments();
      return new Translation(
        result,
        store.getStore().get(),
        Locale.getDefault(),
        messageArguments.get(key));
    }
    return new Translation(
      result,
      store.getStore().get(),
      Locale.getDefault());
  }

}