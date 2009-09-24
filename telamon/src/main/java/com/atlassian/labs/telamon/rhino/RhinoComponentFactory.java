package com.atlassian.labs.telamon.rhino;

import com.atlassian.labs.telamon.api.ComponentFactory;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.script.ScriptSource;
import com.atlassian.labs.telamon.script.ScriptSourceLoader;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

public class RhinoComponentFactory implements ComponentFactory
{
    private final ScriptManager scriptManager;
    private final Map<String, LoadedScriptSource> sources;
    private final List<ScriptSourceLoader> sourceLoaders;

    public RhinoComponentFactory(ScriptSourceLoader... loaders)
    {
        this(new ScriptManager(), loaders);
    }
    public RhinoComponentFactory(ScriptManager scriptManager, ScriptSourceLoader... loaders)
    {
        this.sources = new ConcurrentHashMap<String, LoadedScriptSource>();
        this.scriptManager = scriptManager;
        this.sourceLoaders = Arrays.asList(loaders);
        for (ScriptSourceLoader loader : loaders)
        {
            for (ScriptSource source : loader.getSources())
            {
                loadSource(loader, source);
            }
        }

    }

    public Component create(String type, String id, Map<String, ?> args) throws IOException
    {
        LoadedScriptSource loadedSource = sources.get(type);
        if (loadedSource == null) {
            for (ScriptSourceLoader loader : sourceLoaders)
            {
                ScriptSource source = loader.getSource(type);
                if (source != null)
                {
                    loadSource(loader, source);
                }
                else {
                    return null;
                }
            }
        } else if (loadedSource.loader.getSupportsReload() && loadedSource.isOutDated()) {
            loadSource(loadedSource.loader, loadedSource.source);
        }


        return scriptManager.construct(type, new Object[] {id, args});
    }

    private void loadSource(ScriptSourceLoader loader, ScriptSource source) {
        scriptManager.runInSharedScope(source);
        sources.put(source.getName(), new LoadedScriptSource(loader, source));
    }

    private static class LoadedScriptSource {
        final ScriptSource source;
        final long dateLoaded;
        final ScriptSourceLoader loader;

        public LoadedScriptSource(ScriptSourceLoader loader, ScriptSource source) {
            this.loader = loader;
            this.source = source;
            this.dateLoaded = source.getLastModified();
        }

        public boolean isOutDated() {
            return (dateLoaded < source.getLastModified());
        }
    }
}
