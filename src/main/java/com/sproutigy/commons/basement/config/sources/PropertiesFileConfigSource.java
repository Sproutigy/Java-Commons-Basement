package com.sproutigy.commons.basement.config.sources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileConfigSource extends PropertiesConfigSource {

    private String path;

    public PropertiesFileConfigSource(String path, boolean modifiable) throws IOException {
        super(new Properties(), modifiable);
        this.path = path;
        try {
            refresh();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() throws Exception {
        super.refresh();
        try {
            try (FileInputStream stream = new FileInputStream(path)) {
                properties.load(stream);
            }
        } catch (FileNotFoundException ignore) {
        }
    }

    @Override
    public synchronized void store(String key, Object value) throws Exception {
        super.store(key, value);
        try (FileOutputStream stream = new FileOutputStream(path)) {
            properties.store(stream, "config");
        }
    }
}
