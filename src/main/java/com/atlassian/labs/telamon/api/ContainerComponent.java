package com.atlassian.labs.telamon.api;

import java.io.Writer;

public interface ContainerComponent extends Component
{
    void renderEnd(RenderOutput writer, String content);
    String[] getChildNames();
    Component get(String id);
}
