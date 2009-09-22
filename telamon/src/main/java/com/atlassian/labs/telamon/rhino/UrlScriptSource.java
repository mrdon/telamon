package com.atlassian.labs.telamon.rhino;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class UrlScriptSource implements ScriptSource {
    private final URL url;

    public UrlScriptSource(URL url) {
        this.url = url;
    }

    public long getLastModified() {
        return 0;
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
