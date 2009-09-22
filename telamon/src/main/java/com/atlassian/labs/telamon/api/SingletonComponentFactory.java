package com.atlassian.labs.telamon.api;

public class SingletonComponentFactory
{
    private static volatile ComponentFactory componentFactory;

    public static ComponentFactory getInstance()
    {
        return componentFactory;
    }

    public static void setInstance(ComponentFactory componentFactory)
    {
        SingletonComponentFactory.componentFactory = componentFactory;
    }
}
