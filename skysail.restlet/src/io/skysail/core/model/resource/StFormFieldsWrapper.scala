package io.skysail.core.model.resource

import io.skysail.restlet.forms.ScalaFormField
import org.restlet.Request
import io.skysail.api.text.Translation
import scala.collection.JavaConverters._

case class StFormFieldsWrapper(scalaformfields: Iterable[ScalaFormField], request: Request, messages: Map[String, Translation]) {

  class ScalaFormFieldAdapter(ff: ScalaFormField) {
    def getLabel() = determineText(ff, ff.getId());
    def getId() = ff.getHtmlId()
    def getName() = ff.getHtmlName();
    def getInputType() = ff.getInputType().name();
    //            type = ff.getType();
    //            nestedTable = ff.getNestedTable();
    //            tab = ff.getTab();
    //            fieldRelation = ff.getFieldRelation();
    //            eventsCode = ff.getEventsCode();
    //            dynamicAttributes = ff.getDynamicAttributes();
    //
    //            fieldAttributes = ff.getFieldAttributes();
    //            currentEntity = ff.getCurrentEntity();
    //
    //            helpMsg = determineText(ff, ff.getId() + ".desc");
    //            infoMsg = determineText(ff, ff.getId() + ".info");
    //            placeholder = determineText(ff, ff.getId() + ".placeholder");
    //
    //            cssClass = ff.getCssClass();
    //            cssStyle = ff.getCssStyle();
    //
    //            violationMessage = ff.getViolationMessage();
    //
    //            mandatory = ff.isMandatory();

    private def determineText(ff: ScalaFormField, key: String): String = {
      val translation = StFormFieldsWrapper.this.messages.get(key);
      if (translation.isEmpty) { // || translation.value == null) {
        val split = key.split("\\|")
        return split(1)
      }
      return translation.get.value
    }

    override def toString() = {
      s"label: $getLabel, id: $getId, name: $getName, inputType: $getInputType"
    }

  }

  val formfields = scalaformfields.map(new ScalaFormFieldAdapter(_)).toList.asJava
  def getFormfields() = formfields

}