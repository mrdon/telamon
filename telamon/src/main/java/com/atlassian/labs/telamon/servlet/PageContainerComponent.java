package com.atlassian.labs.telamon.servlet;

import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.api.RenderOutput;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.rhino.ScriptManager;
import com.atlassian.labs.telamon.rhino.variable.JsGlobal;

import java.util.Map;
import java.util.LinkedHashMap;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Scriptable;

/**
 *
 */
public class PageContainerComponent implements ContainerComponent {

    private LinkedHashMap<String, Component> children = new LinkedHashMap<String, Component>();
    private final Scriptable scope;

    public PageContainerComponent(Scriptable scope) {
        this.scope = scope;
    }

    public PageContainerComponent add(String id, Object comp)
    {
        children.put(id, ScriptManager.castResult(scope, comp));
        return this;
    }

    public void renderEnd(RenderOutput writer, String content) {
    }

    public String[] getChildNames() {
        return children.keySet().toArray(new String[children.size()]);
    }

    public Component get(String id) {
        return children.get(id);
    }

    public boolean render(RenderOutput writer, Map<String, ?> attributes) {
        return true;
    }
}
