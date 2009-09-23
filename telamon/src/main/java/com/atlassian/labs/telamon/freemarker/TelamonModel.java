package com.atlassian.labs.telamon.freemarker;

import freemarker.template.TemplateTransformModel;
import freemarker.template.TemplateModelException;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.RenderOutput;
import com.atlassian.labs.telamon.api.ComponentFactory;
import com.atlassian.labs.telamon.util.WriterRenderOutput;

/**
 *
 */
public class TelamonModel implements TemplateTransformModel {
    private final ComponentFactory componentFactory;
    private final String type;
    private static final DefaultObjectWrapper wrapper = new DefaultObjectWrapper();

    public TelamonModel(ComponentFactory componentFactory, String type) {
        this.componentFactory = componentFactory;
        this.type = type;
    }


    public Writer getWriter(Writer writer, Map map) throws TemplateModelException, IOException {
        Map<String, Object> args = unwrap(map);
        String id = (String) args.get("id");
        if (id == null)
        {
            throw new TemplateModelException("Missing id parameter for " + type);
        }
        Component comp = componentFactory.create(type, id, Collections.<String,Object> emptyMap());
        return new CallbackWriter(comp, new WriterRenderOutput(writer), args);
    }

    private static Map<String, Object> unwrap(Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Object entry : map.entrySet())
        {
            String key = (String) ((Map.Entry)entry).getKey();
            Object value = ((Map.Entry)entry).getValue();
            // the value should ALWAYS be a decendant of TemplateModel
            if (value instanceof TemplateModel) {
                try {
                    result.put(key, wrapper.unwrap((TemplateModel) value));
                } catch (TemplateModelException e) {
                    //LOG.error("failed to unwrap [" + value
                    //    + "] it will be ignored", e);
                    e.printStackTrace();
                }
            }
            // if it doesn't, we'll do it the old way by just returning the toString() representation
            else {
                result.put(key, value.toString());
            }
        }
        return result;
    }
}
