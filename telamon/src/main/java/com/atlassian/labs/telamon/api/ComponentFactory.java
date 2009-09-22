package com.atlassian.labs.telamon.api;

import java.util.Map;
import java.io.IOException;

public interface ComponentFactory
{
    Component create(String type, String id, Map<String, ?> args) throws IOException;
}
