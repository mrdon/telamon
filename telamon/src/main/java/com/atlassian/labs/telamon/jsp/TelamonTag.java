package com.atlassian.labs.telamon.jsp;

import com.atlassian.labs.telamon.api.*;
import com.atlassian.labs.telamon.util.WriterRenderOutput;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.JspException;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.io.IOException;

/**
 *
 */
public abstract class TelamonTag extends BodyTagSupport implements DynamicAttributes {

    private static final ComponentFactory componentFactory;
    private final String type;
    private final Map<String, Object> args = new HashMap<String, Object>();

    private Component component;
    private RenderOutput output;
    static {
        componentFactory = SingletonComponentFactory.getInstance();
    }
    public TelamonTag(String type) {
        this.type = type;
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        args.put(localName, value);
    }

    @Override
    public int doStartTag() throws JspException {
        output = new WriterRenderOutput(pageContext.getOut());
        String id = (String) args.get("id");
        try {
            component = componentFactory.create(type, id, Collections.<String, Object> emptyMap());

        } catch (IOException e) {
            throw new JspException(e);
        }
        boolean shouldContinue = component.render(output, args);
        return shouldContinue ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        if (component instanceof ContainerComponent)
        {
            ((ContainerComponent)component).renderEnd(output, getBodyContent().getString());
        }

        args.clear();
        component = null;
        output = null;
        return EVAL_PAGE;
    }
}
