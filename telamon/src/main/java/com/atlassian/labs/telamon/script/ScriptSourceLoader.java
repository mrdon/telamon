package com.atlassian.labs.telamon.script;

import com.atlassian.labs.telamon.script.ScriptSource;

import java.util.List;

/**
 *
 */
public interface ScriptSourceLoader {
    List<ScriptSource> getSources();

    ScriptSource getSource(String name);

    boolean getSupportsReload();
}
