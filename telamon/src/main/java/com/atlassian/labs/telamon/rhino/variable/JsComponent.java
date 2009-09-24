package com.atlassian.labs.telamon.rhino.variable;

import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.RenderOutput;
import org.mozilla.javascript.*;

import java.util.Map;

/**
 *
 */
public class JsComponent implements Component
{
    private final NativeObject jsObject;
    private final Scriptable scope;

    public JsComponent(NativeObject jsObject, Scriptable scope)
    {
        this.jsObject = jsObject;
        this.scope = scope;
    }

    public boolean render(RenderOutput writer, Map<String, ?> attributes)
    {
        Boolean result = (Boolean) exec("render", writer, attributes);
        return (result == null ? false : result);
    }

    protected Object exec(String funcName, Object... args)
    {
        Context cx = Context.enter();
        try
        {
            Function func = (Function) ScriptableObject.getProperty(jsObject, funcName);
            Object[] wrappedArgs = new Object[args.length];
            for (int x=0; x<args.length; x++)
            {
                wrappedArgs[x] = Context.javaToJS(args[x], scope);
            }
            Object result = func.call(cx, scope, jsObject, wrappedArgs);
            if (result == Undefined.instance)
            {
                return null;
            } else
            {
                return result;
            }
        }
        finally
        {
            Context.exit();
        }
    }
}
