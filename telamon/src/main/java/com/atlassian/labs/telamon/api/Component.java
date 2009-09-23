package com.atlassian.labs.telamon.api;

import java.io.Writer;
import java.util.Map;

public interface Component
{
    boolean render(RenderOutput writer, Map<String, ?> attributes);
}
