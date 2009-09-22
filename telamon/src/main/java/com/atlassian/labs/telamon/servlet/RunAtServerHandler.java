package com.atlassian.labs.telamon.servlet;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import com.atlassian.labs.telamon.rhino.ScriptManager;
import com.atlassian.labs.telamon.rhino.UrlScriptSource;
import com.atlassian.labs.telamon.rhino.ScriptSource;
import com.atlassian.labs.telamon.api.ContainerComponent;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

/**
 *
 */
public class RunAtServerHandler extends DefaultHandler {
    private final ScriptManager scriptManager;
    private final ServletContext servletContext;
    private final ContainerComponent root;
    private final DefaultHandler nextHandler;
    private boolean ignoreNext = false;

    public RunAtServerHandler(ScriptManager scriptManager, ServletContext servletContext, ContainerComponent root, DefaultHandler nextHandler) {
        this.scriptManager = scriptManager;
        this.servletContext = servletContext;
        this.root = root;
        this.nextHandler = nextHandler;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("script".equals(qName) && "server".equals(attributes.getValue("runat")))
        {
            String src = attributes.getValue("src");
            try {
                ScriptSource script = new UrlScriptSource(servletContext.getResource(src));
                scriptManager.run(script, Collections.<String, Object>singletonMap("page", root));
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            ignoreNext = true;
        } else
        {
            nextHandler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        return nextHandler.resolveEntity(publicId, systemId);
    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        nextHandler.notationDecl(name, publicId, systemId);
    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        nextHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        nextHandler.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        nextHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        nextHandler.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        nextHandler.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        nextHandler.endPrefixMapping(prefix);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (ignoreNext)
        {
            ignoreNext = false;
        }
        else
        {
            nextHandler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        nextHandler.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        nextHandler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        nextHandler.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        nextHandler.skippedEntity(name);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        nextHandler.warning(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        nextHandler.error(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        nextHandler.fatalError(e);
    }
}
