package com.atlassian.labs.telamon.script;

import com.atlassian.labs.telamon.api.TelamonException;
import com.atlassian.labs.telamon.script.ScriptSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.FileReader;

public class FileScriptSource implements ScriptSource
{
    private final File file;
    private final String name;

    public FileScriptSource(File file)
    {
        this.file = file;
        this.name = file.getName().substring(0, file.getName().length() - 3);
    }

    public long getLastModified()
    {
        return file.lastModified();
    }

    public String getPath()
    {
        return file.getPath();
    }

    public String getName()
    {
        return this.name;
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