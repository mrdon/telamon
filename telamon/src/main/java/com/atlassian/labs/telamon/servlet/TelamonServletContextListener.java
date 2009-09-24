package com.atlassian.labs.telamon.servlet;

import com.atlassian.labs.telamon.api.SingletonComponentFactory;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import com.atlassian.labs.telamon.script.DirectoryScriptSourceLoader;
import com.atlassian.labs.telamon.script.ScriptSourceLoader;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class TelamonServletContextListener implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        ServletContext ctx = servletContextEvent.getServletContext();

        List<ScriptSourceLoader> loaders = new ArrayList<ScriptSourceLoader>();
        for (File baseDir : parseBaseDirs(ctx, "/include/components"))
        {
            loaders.add(new DirectoryScriptSourceLoader(baseDir));
        }

        RhinoComponentFactory factory = new RhinoComponentFactory(loaders.toArray(new ScriptSourceLoader[loaders.size()]));
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
