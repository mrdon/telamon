package com.atlassian.labs.telamon.template;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.Writer;
import java.io.IOException;

import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.RenderOutput;
import com.atlassian.labs.telamon.rhino.WriterRenderOutput;
import com.atlassian.labs.telamon.util.StringRenderOutput;

/**
 *
 */
public class TagParser extends DefaultHandler
{
    private Frame currentFrame;
    private List<Frame> frameStack = new ArrayList<Frame>();
    private int tagLevel;

    public TagParser(ContainerComponent page, Writer writer)
    {
        currentFrame = new Frame(page, 0);
        currentFrame.buffer = new WriterRenderOutput(writer);
    }

    @Override
    public void startElement(String uri, String local, String qname, Attributes attributes) throws SAXException
    {
        tagLevel++;
        //System.out.println("tag start " + qname + " lvl " + tagLevel);
        String id = attributes.getValue("jsid");
        if (id != null)
        {
            Component comp = currentFrame.component.get(id);
            comp.render(currentFrame.buffer, readAttributes(attributes));
            if (comp instanceof ContainerComponent)
            {
                Frame frame = new Frame((ContainerComponent) comp, tagLevel);
                frameStack.add(currentFrame);
                currentFrame = frame;
            }
        }
        else
        {
            currentFrame.buffer.write("<" + qname);
            for (int x = 0; x < attributes.getLength(); x++)
            {
                currentFrame.buffer.write(" " + attributes.getQName(x) + "=\"" + attributes.getValue(x) + "\"");
            }
            currentFrame.buffer.write(">");
        }
    }

    @Override
    public void endElement(String uri, String local, String qname) throws SAXException
    {
        if (tagLevel == currentFrame.tagLevel)
        {
            Frame prevFrame = frameStack.remove(frameStack.size() - 1);
            prevFrame.component.renderEnd(prevFrame.buffer, currentFrame.buffer.toString());
            currentFrame = prevFrame;
        }
        else
        {
            if (local.equalsIgnoreCase("AREA"))
            {
                return;
            }
            if (local.equalsIgnoreCase("BASE"))
            {
                return;
            }
            if (local.equalsIgnoreCase("BASEFONT"))
            {
                return;
            }
            if (local.equalsIgnoreCase("BR"))
            {
                return;
            }
            if (local.equalsIgnoreCase("COL"))
            {
                return;
            }
            if (local.equalsIgnoreCase("FRAME"))
            {
                return;
            }
            if (local.equalsIgnoreCase("HR"))
            {
                return;
            }
            if (local.equalsIgnoreCase("IMG"))
            {
                return;
            }
            if (local.equalsIgnoreCase("INPUT"))
            {
                return;
            }
            if (local.equalsIgnoreCase("ISINDEX"))
            {
                return;
            }
            if (local.equalsIgnoreCase("LINK"))
            {
                return;
            }
            if (local.equalsIgnoreCase("META"))
            {
                return;
            }
            if (local.equalsIgnoreCase("PARAM"))
            {
                return;
            }

            currentFrame.buffer.write("</" + qname + ">");
        }
        tagLevel--;
    }

    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException
    {
        currentFrame.buffer.write(new String(chars, i, i1));
    }

    private static Map<String, Object> readAttributes(Attributes attrs)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int x=0; x<attrs.getLength(); x++)
        {
            map.put(attrs.getQName(x), attrs.getValue(x));
        }
        return map;
    }

    private static class Frame
    {

        final int tagLevel;
        RenderOutput buffer = new StringRenderOutput();
        final ContainerComponent component;

        public Frame(ContainerComponent component, int tagLevel)
        {
            this.component = component;
            this.tagLevel = tagLevel;
        }

    }

    public static class NoOpContainerComponent implements ContainerComponent
    {

        public void renderEnd(RenderOutput writer, String content) {
        }

        public String[] getChildNames() {
            return new String[0];
        }

        public Component get(String id) {
            return null;
        }

        public void render(RenderOutput writer, Map<String, ?> attributes) {
        }
    }
}
