package io.skysail.restlet.utils;

import java.util.Collections;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

public abstract class ParamsUtils {

    private final String fieldname;

    public String getFieldname() {
        return fieldname;
    }

    private final Form originalForm;
    private final String hierarchicalPart;

    public ParamsUtils(String fieldname, Request request) {
        this.fieldname = fieldname;
        hierarchicalPart = request.getOriginalRef().getHierarchicalPart();
        this.originalForm = new Form(Collections.unmodifiableList(request.getOriginalRef().getQueryAsForm()));
    }

    protected abstract Form handleQueryForm(String format, String value);

    protected abstract Form reduceQueryForm(String format, String value);

    protected String toggleLink() {
        return this.toggleLink(null, null);
    }

    protected String toggleLink(String value, String format) {
        Form form = handleQueryForm(format, value);
        if (isEmpty(form)) {
            return emptyQueryRef();
        }
        form = stripEmptyParams(form);
        return isEmpty(form) ? hierarchicalPart : "?" + form.getQueryString();
    }

    protected String reduceLink(String value, String format) {
        Form form = reduceQueryForm(format, value);
        if (isEmpty(form)) {
            return emptyQueryRef();
        }
        form = stripEmptyParams(form);
        return isEmpty(form) ? "" : "?" + form.getQueryString();
    }

    protected String emptyQueryRef() {
        return "";//request.getOriginalRef().getHierarchicalPart();
    }

    protected boolean isEmpty(Form queryForm) {
        return "".equals(queryForm.getQueryString());
    }

    protected Form cloneForm() {
		Form result = new Form();
		originalForm.getValuesMap().keySet().forEach(key -> result.add(new Parameter(key, originalForm.getFirstValue(key))));
		return result;
	}

	protected Parameter getParameter(String filterParamKey) {
		return originalForm.getFirst(filterParamKey);
	}

    private Form stripEmptyParams(Form form) {
        Form result = new Form();
        form.forEach(param -> {
            if (param.getValue() != null && !"".equals(param.getValue().trim())) {
                result.add(param);
            }
        });
        return result;
    }

}
