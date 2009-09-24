package com.atlassian.labs.telamon.rhino.variable;

import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.api.RenderOutput;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.rhino.ScriptManager;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Context;

/**
 *
 */
public class JsContainerComponent extends JsComponent implements ContainerComponent
{
    private final Scriptable scope;
    public JsContainerComponent(NativeObject jsObject, Scriptable scope)
    {
        super(jsObject, scope);
        this.scope = scope;
    }

    public void renderEnd(RenderOutput writer, String content)
    {
        exec("renderEnd", writer, content);
    }

    public String[] getChildNames()
    {
        Object result = exec("childNames");
        return (String[]) Context.jsToJava(result, String[].class);
    }

    public Component get(String id)
    {
        Object result = exec("get", id);
        return ScriptManager.castResult(scope, result);
    }
}
