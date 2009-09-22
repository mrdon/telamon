package com.atlassian.labs.telamon.util;

import com.atlassian.labs.telamon.api.RenderOutput;

public class StringRenderOutput implements RenderOutput
{
    private final StringBuffer sb = new StringBuffer();
    public RenderOutput write(String output)
    {
        sb.append(output);
        return this;
    }

    public String toString()
    {
        return sb.toString();
    }
}
