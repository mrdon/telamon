package com.atlassian.labs.telamon.util;

import com.atlassian.labs.telamon.api.RenderOutput;

import java.io.Writer;
import java.io.IOException;

public class WriterRenderOutput implements RenderOutput
{
    private final Writer writer;

    public WriterRenderOutput(Writer writer)
    {
        this.writer = writer;
    }

    public RenderOutput write(String output)
    {
        try
        {
            writer.write(output);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
}
