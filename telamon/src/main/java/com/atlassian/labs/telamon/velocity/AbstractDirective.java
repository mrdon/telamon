package com.atlassian.labs.telamon.velocity;

import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.MethodInvocationException;

import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Collections;

import com.atlassian.labs.telamon.api.ComponentFactory;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.SingletonComponentFactory;
import com.atlassian.labs.telamon.api.RenderOutput;
import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import com.atlassian.labs.telamon.rhino.WriterRenderOutput;

public abstract class AbstractDirective extends Directive
{
    private final String name;
    private final int type;

    public AbstractDirective(String name, int type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException
    {
        ComponentFactory componentFactory = getComponentFactory();

        String id = (String) node.jjtGetChild(0).value(internalContextAdapter);
        Map<String, Object> args;
        if (node.jjtGetNumChildren() == 2)
        {
            args = (Map<String, Object>) node.jjtGetChild(1).value(internalContextAdapter);
        } else
        {
            args = Collections.emptyMap();
        }
        Component comp = componentFactory.create(name, id, Collections.<String,Object> emptyMap());

        RenderOutput output = new WriterRenderOutput(writer);
        comp.render(output, args);

        Writer bodyWriter = writer;
        if (getType() == BLOCK) {
            bodyWriter = new StringWriter();
        }

        Node body = node.jjtGetChild(node.jjtGetNumChildren() - 1);
        body.render(internalContextAdapter, bodyWriter);

        if (getType() == BLOCK)
        {
            ((ContainerComponent)comp).renderEnd(output, bodyWriter.toString());
        }
        return true;
    }

    protected ComponentFactory getComponentFactory()
    {
        return SingletonComponentFactory.getInstance();
    }
}
