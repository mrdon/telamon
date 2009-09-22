package com.atlassian.labs.telamon.servlet;

import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import com.atlassian.labs.telamon.rhino.ScriptManager;
import com.atlassian.labs.telamon.util.RequestUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class TelamonFilter implements Filter {

    private ScriptManager scriptManager;
    private RhinoComponentFactory componentFactory;
    private ServletContext servletContext;

    public void init(FilterConfig filterConfig) throws ServletException {

        File baseDir = new File(filterConfig.getServletContext().getRealPath("/WEB-INF/components"));
        scriptManager = new ScriptManager();
        componentFactory = new RhinoComponentFactory(scriptManager, baseDir);
        this.servletContext = filterConfig.getServletContext();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = RequestUtils.getUri(req);
        TelemonHttpServletResponseWrapper wrapper = null;
        if (uri.endsWith(".html")) {
            wrapper = new TelemonHttpServletResponseWrapper(servletContext, res, scriptManager);
            response = wrapper;
        }
        chain.doFilter(request, response);

        if (wrapper != null)
        {
            wrapper.close();
        }
    }

    public void destroy() {

    }


}
