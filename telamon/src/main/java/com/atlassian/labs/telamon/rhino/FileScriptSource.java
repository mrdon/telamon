package com.atlassian.labs.telamon.rhino;

import com.atlassian.labs.telamon.api.TelamonException;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.FileReader;

public class FileScriptSource implements ScriptSource
{
    private final File file;

    public FileScriptSource(File file)
    {
        this.file = file;
    }

    public long getLastModified()
    {
        return file.lastModified();
    }

    public String getPath()
    {
        return file.getPath();
    }

    public Reader getReader()
    {
        try
        {
            return new FileReader(file);
        }
        catch (FileNotFoundException e)
        {
            throw new TelamonException(e);
        }
    }

}