package com.atlassian.labs.telamon;

import junit.framework.TestCase;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import com.atlassian.labs.telamon.util.WriterRenderOutput;
import static com.atlassian.labs.telamon.util.FileUtils.file;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.RenderOutput;
import static com.atlassian.labs.telamon.Helper.buildComponentFactory;

import java.util.Collections;
import java.util.Map;
import java.io.StringWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class TestPerformance extends TestCase
{
    public void testSimpleComponent() throws IOException, URISyntaxException
    {
        long jsTime = buildAndRenderJs(10000);
        long javaTime = buildAndRenderJava(10000);

        System.out.println("js:" + jsTime + " javatime:" + javaTime);
    }

    private long buildAndRenderJs(int runs)
            throws IOException, URISyntaxException
    {
        RhinoComponentFactory factory = buildComponentFactory();
        long start = System.currentTimeMillis();

        StringWriter writer = new StringWriter();
        WriterRenderOutput output = new WriterRenderOutput(writer);
        for (int x=0; x<runs; x++)
        {
            Component comp = factory.create("TextField", "foo", Collections.singletonMap("value", "foobar"));
            comp.render(output, Collections.singletonMap("label", "Some label"));
            writer.getBuffer().setLength(0);
        }


        return System.currentTimeMillis() - start;
    }

    private long buildAndRenderJava(int runs)
            throws IOException
    {
        //RhinoComponentFactory factory = new RhinoComponentFactory(file("src", "test", "resources", "sampleLibrary"));
        long start = System.currentTimeMillis();

        StringWriter writer = new StringWriter();
        for (int x=0; x<runs; x++)
        {
            Component comp = new TextFieldComponent("foo", Collections.<String, Object>singletonMap("value", "foobar"));
            comp.render(new WriterRenderOutput(writer), Collections.singletonMap("label", "Some label"));
            writer.getBuffer().setLength(0);
        }

        return System.currentTimeMillis() - start;
    }

    private static class TextFieldComponent implements Component
    {
        private final String id;
        private final Map<String, Object> params;

        public TextFieldComponent(String id, Map<String, Object> params)
        {
            this.id = id;
            this.params = params;
        }

        public boolean render(RenderOutput writer, Map<String, ?> attributes)
        {
            StringBuilder sb = new StringBuilder();
            String xml = "<div> \n'" +
                        "  <label for=\"" + id + "\"/> \n" +
                        "  <input type=\"text\" id=\"" + id + "\" name=\"" + id + "\" title=\"" + attributes.get("label") + "\" \n" +
                        "         value=\"" + params.get("value") + "\"/> \n" +
                        "</div>";
            writer.write(xml);
            return true;
        }
    }
}
