package com.atlassian.labs.telamon.freemarker;

import freemarker.template.TransformControl;
import freemarker.template.TemplateModelException;

import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Map;

import com.atlassian.labs.telamon.api.Component;
import com.atlassian.labs.telamon.api.ContainerComponent;
import com.atlassian.labs.telamon.api.RenderOutput;

/**
 */
public class CallbackWriter extends Writer implements TransformControl {
    private final Component bean;
    private final boolean isContainer;
    private final RenderOutput writer;
    private final Map<String, ?> args;
    private StringWriter body;
    private boolean afterBody = false;

    public CallbackWriter(Component bean, RenderOutput writer, Map<String,?> args) {
        this.bean = bean;
        this.writer = writer;
        this.args = args;
        this.isContainer = (bean instanceof ContainerComponent);
        if (isContainer) {
            this.body = new StringWriter();
        }
    }

    public void close() throws IOException {
        if (isContainer) {
            body.close();
        }
    }

    public void flush() throws IOException {
        //writer.flush();

        if (isContainer) {
            body.flush();
        }
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        if (isContainer && !afterBody) {
            body.write(cbuf, off, len);
        } else {
            writer.write(new String(cbuf, off, len));
        }
    }

    public int onStart() throws TemplateModelException, IOException {
        bean.render(writer, args);

        if (isContainer) {
            return EVALUATE_BODY;
        } else {
            return SKIP_BODY;
        }
    }

    public int afterBody() throws TemplateModelException, IOException {
        afterBody = true;
        if (isContainer)
        {
            ((ContainerComponent)bean).renderEnd(writer, body.toString());
        }
        return END_EVALUATION;
    }

    public void onError(Throwable throwable) throws Throwable {
        throw throwable;
    }

    public Component getBean() {
        return bean;
    }
}

