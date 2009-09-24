package com.atlassian.labs.telamon.script;

import java.io.Reader;

public interface ScriptSource
{
    long getLastModified();
    String getName();
    String getPath();
    Reader getReader();
}
