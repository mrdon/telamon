package com.atlassian.labs.telamon;

import junit.framework.TestCase;
import static com.atlassian.labs.telamon.Helper.buildComponentFactory;
import com.atlassian.labs.telamon.freemarker.TelamonHashModel;
import com.atlassian.labs.telamon.jsp.TelamonTag;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentCaptor;

/**
 *
 */
public class TestTelamonTag extends TestCase {
    public void testRenderComponent() throws Exception
    {
        buildComponentFactory();

        TelamonTag tag = new TelamonTag("TextField") {};
        PageContext ctx = mock(PageContext.class);
        JspWriter jspWriter = mock(JspWriter.class);
        when(ctx.getOut()).thenReturn(jspWriter);
        tag.setPageContext(ctx);
        tag.setDynamicAttribute("", "id", "bar");
        tag.doStartTag();
        tag.doEndTag();

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(jspWriter).write(argument.capture());
        assertTrue(argument.getValue().contains("id=\"bar\""));
    }

    public void testRenderComponentWithArgs() throws Exception
    {
        buildComponentFactory();

        TelamonTag tag = new TelamonTag("TextField") {};
        PageContext ctx = mock(PageContext.class);
        JspWriter jspWriter = mock(JspWriter.class);
        when(ctx.getOut()).thenReturn(jspWriter);
        tag.setPageContext(ctx);
        tag.setDynamicAttribute("", "id", "bar");
        tag.setDynamicAttribute("", "label", "baz");
        tag.doStartTag();
        tag.doEndTag();

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(jspWriter).write(argument.capture());
        assertTrue(argument.getValue().contains("id=\"bar\""));
        assertTrue(argument.getValue().contains("title=\"baz\""));
    }
}
