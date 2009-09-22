package com.atlassian.labs.telamon;

import com.atlassian.labs.telamon.api.ComponentFactory;
import com.atlassian.labs.telamon.api.SingletonComponentFactory;
import com.atlassian.labs.telamon.rhino.RhinoComponentFactory;
import static com.atlassian.labs.telamon.util.FileUtils.file;

import java.net.URL;
import java.net.URISyntaxException;
import java.io.File;

public class Helper
{

    public static RhinoComponentFactory buildComponentFactory() throws URISyntaxException
    {
        URL url = Helper.class.getResource("/sampleLibrary/");
        File dir = new File(url.toURI());
        RhinoComponentFactory factory = new RhinoComponentFactory(dir);
        SingletonComponentFactory.setInstance(factory);
        return factory;
    }
}
