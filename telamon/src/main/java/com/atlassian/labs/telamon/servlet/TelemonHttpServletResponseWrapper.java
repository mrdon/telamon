package com.atlassian.labs.telamon.servlet;

import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.*;

import com.atlassian.labs.telamon.template.TagParser;
import com.atlassian.labs.telamon.rhino.ScriptManager;

/**
 *
 */
public class TelemonHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final ServletContext servletContext;
    private final HttpServletResponse response;
    private final ScriptManager scriptManager;

    private ServletOutputStream sout;
    private PrintWriter swriter;
    private ByteArrayOutputStream bout = new ByteArrayOutputStream();

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @throws IllegalArgumentException if the response is null
     */
    public TelemonHttpServletResponseWrapper(ServletContext servletContext, HttpServletResponse response, ScriptManager scriptManager) {
        super(response);
        this.servletContext = servletContext;
        this.response = response;
        this.scriptManager = scriptManager;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (sout == null)
        {
            start();
        }
        return sout;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (swriter == null)
        {
            start();
        }
        return swriter;
    }

    private void start() {
        sout = new ServletOutputStream()
        {
            public void write(int b) throws IOException {
                bout.write(b);
            }
        };
        swriter = new PrintWriter(bout);
    }

    public void close() {
        try {
            if (sout != null)
            sout.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (swriter != null)
        swriter.flush();
        if (bout.size() > 0)
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                factory.setValidating(false);
                SAXParser saxParser = factory.newSAXParser();
                PageContainerComponent page = new PageContainerComponent(scriptManager.getSharedScope());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                OutputStreamWriter bufferWriter = new OutputStreamWriter(buffer);
                TagParser tagParser = new TagParser(page, bufferWriter);
                RunAtServerHandler runAtServerHandler = new RunAtServerHandler(scriptManager, servletContext, page, tagParser);

                System.out.println("data:"+new String(bout.toByteArray()));
                saxParser.parse(new InputSource(new ByteArrayInputStream(bout.toByteArray())), runAtServerHandler);

                bufferWriter.close();
                response.setContentLength(buffer.size());
                response.getOutputStream().write(buffer.toByteArray());
                response.getOutputStream().close();
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}
