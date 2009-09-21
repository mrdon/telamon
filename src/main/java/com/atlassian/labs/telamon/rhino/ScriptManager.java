package com.atlassian.labs.telamon.rhino;

import org.mozilla.javascript.*;
import org.apache.commons.js2j.SugarWrapFactory;
import org.apache.commons.js2j.SugarContextFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.atlassian.labs.telamon.rhino.variable.JsGlobal;
import com.atlassian.labs.telamon.api.TelamonException;
import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.api.RenderOutput;

/**
 * Created by IntelliJ IDEA.
 * User: mrdon
 * Date: Dec 14, 2008
 * Time: 8:52:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptManager
{
    private Scriptable sharedScope;
    private final Map<String, Script> scripts = new ConcurrentHashMap<String, Script>();

    static
    {
        SugarWrapFactory factory = new SugarWrapFactory();
        ContextFactory.initGlobal(new SugarContextFactory(factory));
    }

    public ScriptManager()
    {
        resetSharedScope();
    }

    void resetSharedScope()
    {
        Context cx = Context.enter();
        try
        {
            JsGlobal scope = new JsGlobal();
            cx.initStandardObjects(scope);
            scope.defineFunctionProperties(new String[]{"print"}, JsGlobal.class, ScriptableObject.DONTENUM);
            sharedScope = scope;
        }
        finally
        {
            Context.exit();
        }
    }

    public void runInSharedScope(ScriptSource scriptSource)
    {
        Context cx = Context.enter();
        try
        {
            cx.evaluateReader(sharedScope, scriptSource.getReader(), scriptSource.getPath(), 1, null);
        }
        catch (IOException e)
        {
            throw new TelamonException("Unable to run shared script " + scriptSource.getPath(), e);
        }
        finally
        {
            Context.exit();
        }
    }

    public Component construct(String functionName, Object[] args, Map<String, Object> variables) throws IOException
    {
        Context cx = Context.enter();
        try
        {
            // Don't bother with a scope per thread since they should not be mutated
            Scriptable threadScope = sharedScope;

            Map<String, Object> vars = new HashMap<String, Object>(variables);

            for (Map.Entry<String, Object> var : vars.entrySet())
            {
                Object wrapped = Context.javaToJS(var.getValue(), threadScope);
                ScriptableObject.putProperty(threadScope, var.getKey(), wrapped);
            }

            Function func = (Function) threadScope.get(functionName, null);
            Object[] wrappedArgs = new Object[args.length];
            for (int x = 0; x < args.length; x++)
            {
                wrappedArgs[x] = Context.javaToJS(args[x], threadScope);
            }
            Object result = func.construct(cx, threadScope, wrappedArgs);
            return castResult(threadScope, result);
        }
        finally
        {
            Context.exit();
        }
    }

    private static Component castResult(Scriptable scope, Object result)
    {
        if (result instanceof NativeObject)
        {
            boolean isContainer = ((NativeObject)result).has("renderEnd", null);
            if (isContainer) {
                return new JsContainerComponent((NativeObject) result, scope);
            } else
            {
                return new JsComponent((NativeObject) result, scope);
            }
        }
        else if (result.getClass().isAssignableFrom(Component.class))
        {
            return (Component) result;
        }
        else
        {
            throw new IllegalArgumentException("Unknown type for result: " + result);
        }
    }

    private static class JsComponent implements Component
    {
        private final NativeObject jsObject;
        private final Scriptable scope;

        public JsComponent(NativeObject jsObject, Scriptable scope)
        {
            this.jsObject = jsObject;
            this.scope = scope;
        }

        public void render(RenderOutput writer, Map<String, ?> attributes)
        {
            exec("render", writer, attributes);
        }

        protected Object exec(String funcName, Object... args)
        {
            Context cx = Context.enter();
            try
            {
                Function func = (Function) jsObject.get(funcName, null);
                Object[] wrappedArgs = new Object[args.length];
                for (int x=0; x<args.length; x++)
                {
                    wrappedArgs[x] = Context.javaToJS(args[x], scope);
                }
                return func.call(cx, scope, jsObject, wrappedArgs);
            }
            finally
            {
                Context.exit();
            }
        }
    }

    private static class JsContainerComponent extends JsComponent implements ContainerComponent
    {
        private final Scriptable scope;
        private final NativeObject jsObject;
        public JsContainerComponent(NativeObject jsObject, Scriptable scope)
        {
            super(jsObject, scope);
            this.scope = scope;
            this.jsObject = jsObject;
        }

        public void renderEnd(RenderOutput writer, String content)
        {
            exec("renderEnd", writer, content);
        }

        @Override
        public String[] getChildNames()
        {
            Object result = jsObject.get("childrenNames", null);
            return (String[]) Context.jsToJava(result, String[].class);
        }

        @Override
        public Component get(String id)
        {
            Object result = exec("get", id);
            return castResult(scope, result);
        }
    }
}

