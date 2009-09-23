package com.atlassian.labs.telamon.rhino;

import java.io.Reader;

public interface ScriptSource
{
    long getLastModified();
    String getPath();
    Reader getReader();
}
