package com.atlassian.labs.telamon.util;

import com.atlassian.labs.telamon.script.ScriptSource;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class UrlScriptSource implements ScriptSource {
    private final URL url;
    private final String name;

    public UrlScriptSource(URL url) {
        this.url = url;
        this.name = url.getFile().substring(0, url.getFile().length() - 3);
    }

    public long getLastModified() {
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return url.toExternalForm();
    }

    public Reader getReader() {
        try {
            return new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
