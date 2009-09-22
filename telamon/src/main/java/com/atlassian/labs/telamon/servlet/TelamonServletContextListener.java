package com.atlassian.labs.telamon.servlet;

import com.atlassian.labs.telamon.api.SingletonComponentFactory;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import java.io.File;

public class TelamonServletContextListener implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        ServletContext ctx = servletContextEvent.getServletContext();

        File[] componentDirs = parseBaseDirs(ctx, "/include/components");
        RhinoComponentFactory factory = new RhinoComponentFactory(componentDirs);
        SingletonComponentFactory.setInstance(factory);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        SingletonComponentFactory.setInstance(null);
    }

    private static File[] parseBaseDirs(ServletContext ctx, String... def)
    {
        String[] parsed = def;
        String toParse = ctx.getInitParameter("componentDirectories");
        if (toParse != null)
        {
            toParse = toParse.trim();
            if (toParse.length() > 0)
            {
                parsed = toParse.split(",");
            }
        }
        File[] baseDirs = new File[parsed.length];
        for (int x=0; x<baseDirs.length; x++)
        {
            baseDirs[x] = new File(ctx.getRealPath(parsed[x]));
        }
        return baseDirs;
    }
}
