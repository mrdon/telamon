package com.atlassian.labs.telamon.freemarker;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import com.atlassian.labs.telamon.api.ComponentFactory;
import com.atlassian.labs.telamon.api.SingletonComponentFactory;

/**
 *
 */
public class TelamonHashModel extends SimpleHash {

    private final ComponentFactory componentFactory;

    public TelamonHashModel() {
        this(SingletonComponentFactory.getInstance());
    }

    public TelamonHashModel(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    @Override
    public TemplateModel get(String type) throws TemplateModelException {
        return new TelamonModel(componentFactory, type);
    }
}
