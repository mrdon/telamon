package com.atlassian.labs.telamon.velocity;

import junit.framework.TestCase;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import static com.atlassian.labs.telamon.util.FileUtils.file;
import com.atlassian.labs.telamon.api.SingletonComponentFactory;
import static com.atlassian.labs.telamon.Helper.buildComponentFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;

public class TestAbstractDirective extends TestCase
{
    public void testRenderComponent() throws Exception
    {
        buildComponentFactory();

        VelocityEngine ve = new VelocityEngine();

        ve.setProperty("userdirective", TextFieldDirective.class.getName());
        ve.init();
        StringWriter writer = new StringWriter();
        ve.evaluate(new VelocityContext(), writer, "ff", "foo:\n#TextField('bar')\n:baz");

        String result = writer.toString();
        System.out.println("result:"+result);
        assertTrue(result.contains("id=\"bar\""));
    }

    public void testRenderComponentWithArgs() throws Exception
    {
        buildComponentFactory();

        VelocityEngine ve = new VelocityEngine();

        ve.setProperty("userdirective", TextFieldDirective.class.getName());
        ve.init();
        StringWriter writer = new StringWriter();
        ve.evaluate(new VelocityContext(), writer, "ff", "foo:\n#TextField('bar', {'label' : 'jim'})\n:baz");

        String result = writer.toString();
        System.out.println("result:"+result);
        assertTrue(result.contains("id=\"bar\""));
        assertTrue(result.contains("title=\"jim\""));
    }
}
