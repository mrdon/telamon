package com.atlassian.labs.telamon.rhino;

import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.TelamonException;
import com.atlassian.labs.telamon.rhino.variable.JsGlobal;
import com.atlassian.labs.telamon.rhino.variable.JsComponent;
import com.atlassian.labs.telamon.rhino.variable.JsContainerComponent;
import com.atlassian.labs.telamon.util.IOUtils;
import com.atlassian.labs.telamon.script.ScriptSource;
import org.apache.commons.js2j.SugarContextFactory;
import org.apache.commons.js2j.SugarWrapFactory;
import org.mozilla.javascript.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        SugarWrapFactory factory = new SugarWrapFactory(Collections.<Class>emptyList());
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
        Reader reader = null;
        try
        {
            reader = scriptSource.getReader();
            cx.evaluateReader(sharedScope, reader, scriptSource.getPath(), 1, null);
        }
        catch (IOException e)
        {
            throw new TelamonException("Unable to run shared script " + scriptSource.getPath(), e);
        }
        finally
        {
            Context.exit();
            IOUtils.closeQuietly(reader);
        }
    }

    public Component construct(String functionName, Object[] args) throws IOException
    {
        Context cx = Context.enter();
        try
        {
            // Don't bother with a scope per thread since they should not be mutated
            Scriptable threadScope = sharedScope;
            Scriptable tm = (Scriptable) ScriptableObject.getProperty(threadScope, "TM");
            Function func = (Function) ScriptableObject.getProperty(tm, functionName);
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


    void ensureCompiled(ScriptSource source) throws IOException
    {
        if (!scripts.containsKey(source.getPath()))
        {
            Context cx = Context.enter();
            Reader reader = null;
            try
            {
                reader = source.getReader();
                Script script = cx.compileReader(reader, source.getPath(), 1, null);
                scripts.put(source.getPath(), script);
            }
            finally
            {
                Context.exit();
                IOUtils.closeQuietly(reader);
            }
        }
    }

    public void run(ScriptSource scriptSource, Map<String, Object> variables) throws IOException
    {
        Context cx = Context.enter();
        try
        {
            // We can share the scope.
            Scriptable threadScope = cx.newObject(sharedScope);
            threadScope.setPrototype(sharedScope);

            // We want "threadScope" to be a new top-level
            // scope, so set its parent scope to null. This
            // means that any variables created by assignments
            // will be properties of "threadScope".
            threadScope.setParentScope(null);


            String path = scriptSource.getPath();
            System.out.println("really running " + path);
            ensureCompiled(scriptSource);
            Script script = scripts.get(path);

            Map<String, Object> vars = new HashMap<String, Object>(variables);

            for (Map.Entry<String, Object> var : vars.entrySet())
            {
                Object wrapped = Context.javaToJS(var.getValue(), threadScope);
                ScriptableObject.putProperty(threadScope, var.getKey(), wrapped);
            }

            script.exec(cx, threadScope);
        }
        finally
        {
            Context.exit();
        }
    }

    public static Component castResult(Scriptable scope, Object result)
    {
        if (result instanceof NativeObject)
        {
            boolean isContainer = ScriptableObject.hasProperty((NativeObject)result, "renderEnd");
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

    public Scriptable getSharedScope() {
        return sharedScope;
    }

}

