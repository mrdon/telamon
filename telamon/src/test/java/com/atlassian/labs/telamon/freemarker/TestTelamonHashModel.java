package com.atlassian.labs.telamon.freemarker;

import junit.framework.TestCase;
import static com.atlassian.labs.telamon.Helper.buildComponentFactory;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Collections;

/**
 *
 */
public class TestTelamonHashModel extends TestCase {
    public void testRenderComponent() throws Exception
    {
        buildComponentFactory();

        Configuration cfg = new Configuration();
        // Specify the data source where the template files come from.
        // Here I set a file directory for it:
        cfg.setClassForTemplateLoading(TestTelamonHashModel.class, "/freemarker");
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template template = cfg.getTemplate("simple.ftl");
        StringWriter writer = new StringWriter();
        template.process(Collections.singletonMap("test", new TelamonHashModel()), writer);

        String result = writer.toString();
        System.out.println("result:"+result);
        assertTrue(result.contains("id=\"bar\""));
    }

    public void testRenderComponentWithArgs() throws Exception
    {
        buildComponentFactory();

        Configuration cfg = new Configuration();
        // Specify the data source where the template files come from.
        // Here I set a file directory for it:
        cfg.setClassForTemplateLoading(TestTelamonHashModel.class, "/freemarker");
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template template = cfg.getTemplate("simple-with-args.ftl");
        StringWriter writer = new StringWriter();
        template.process(Collections.singletonMap("test", new TelamonHashModel()), writer);

        String result = writer.toString();
        System.out.println("result:"+result);
        assertTrue(result.contains("id=\"bar\""));
        assertTrue(result.contains("title=\"jim\""));
    }
}
