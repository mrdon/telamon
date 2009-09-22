package com.atlassian.labs.telamon.rhino.variable;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.WrappedException;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.JavaScriptException;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class JsGlobal extends ScriptableObject
{
    /**
     * Print the string values of its arguments.
     * <p/>
     * This method is defined as a JavaScript function.
     * Note that its arguments are of the "varargs" form, which
     * allows it to handle an arbitrary number of arguments
     * supplied to the JavaScript function.
     */
    public static void print(Context cx, Scriptable thisObj,
                             Object[] args, Function funObj)
    {
        for (int i = 0; i < args.length; i++)
        {
            if (i > 0)
            {
                System.out.print(" ");
            }

            // Convert the arbitrary JavaScript value into a string form.
            String s = Context.toString(args[i]);

            System.out.print(s);
        }
        System.out.println();
    }

    public String getClassName()
    {
        return "global";
    }
}
