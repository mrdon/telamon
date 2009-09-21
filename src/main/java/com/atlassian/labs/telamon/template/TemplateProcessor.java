package com.atlassian.labs.telamon.template;

import org.xml.sax.InputSource;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.*;
import java.util.Map;
import java.util.HashMap;

import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.api.RenderOutput;
import com.atlassian.labs.telamon.util.StringRenderOutput;

public class TemplateProcessor
{
    public void run(Reader reader, Writer writer, ContainerComponent root)
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new InputSource(reader), new TagParser(root, writer));
            reader.close();
            writer.close();
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }

    }

}
