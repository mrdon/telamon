package com.atlassian.labs.telamon;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import static com.atlassian.labs.telamon.util.FileUtils.file;
import com.atlassian.labs.telamon.util.StringRenderOutput;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import com.atlassian.labs.telamon.rhino.WriterRenderOutput;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.ContainerComponent;

public class TestRhinoComponentFactory extends TestCase
{
    public void testSimpleComponent() throws IOException
    {
        RhinoComponentFactory factory = new RhinoComponentFactory(file("src", "test", "resources", "sampleLibrary"));
        Component comp = factory.create("TextField", "foo", Collections.singletonMap("value", "foobar"));

        StringWriter writer = new StringWriter();
        comp.render(new WriterRenderOutput(writer), Collections.singletonMap("label", "Some label"));

        System.out.println(writer.toString());
        assertTrue(writer.toString().contains("value=\"foobar\""));
        assertTrue(writer.toString().contains("title=\"Some label\""));
    }

    public void testSimpleContainerComponent() throws IOException
    {
        RhinoComponentFactory factory = new RhinoComponentFactory(file("src", "test", "resources", "sampleLibrary"));
        ContainerComponent comp = (ContainerComponent) factory.create("Form", "foo", Collections.singletonMap("action", "foo.bar"));

        StringRenderOutput rootOutput = new StringRenderOutput();
        comp.render(rootOutput, Collections.singletonMap("method", "PUT"));

        StringRenderOutput childOutput = new StringRenderOutput();
        comp.get(comp.getChildNames()[0]).render(childOutput, Collections.singletonMap("label", "Some label"));

        comp.renderEnd(rootOutput, childOutput.toString());

        String text = rootOutput.toString();
        System.out.println(text);
        assertTrue(text.contains("name=\"foo\""));
        assertTrue(text.contains("value=\"child\""));
        assertTrue(text.contains("method=\"PUT\""));
        assertTrue(text.contains("action=\"foo.bar\""));
        assertTrue(text.contains("title=\"Some label\""));
    }
}