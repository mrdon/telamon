package com.atlassian.labs.telamon.rhino;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.Reader;

public interface ScriptSource
{
    long getLastModified();
    String getPath();
    Reader getReader();
}
