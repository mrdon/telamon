package com.atlassian.labs.telamon.rhino;

import com.atlassian.labs.telamon.api.ComponentFactory;
import com.atlassian.labs.telamon.api.Component;

import java.util.Map;
import java.util.Collections;
import java.io.File;
import java.io.IOException;

public class RhinoComponentFactory implements ComponentFactory
{
    private final ScriptManager scriptManager;

    public RhinoComponentFactory(File... baseDirs)
    {
        this(new ScriptManager(), baseDirs);
    }
    public RhinoComponentFactory(ScriptManager scriptManager, File... baseDirs)
    {
        this.scriptManager = scriptManager;
        for (File baseDir : baseDirs)
        {
            for (File jsfile : baseDir.listFiles())
            {
                if (jsfile.getName().endsWith(".js"))
                {
                    scriptManager.runInSharedScope(new FileScriptSource(jsfile));
                }
            }
        }

    }

    public Component create(String type, String id, Map<String, ?> args) throws IOException
    {
        return scriptManager.construct(type, new Object[] {id, args});
    }
}
