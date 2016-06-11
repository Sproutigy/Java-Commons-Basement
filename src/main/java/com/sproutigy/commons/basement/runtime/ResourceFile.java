package com.sproutigy.commons.basement.runtime;

import java.io.*;
import java.net.URL;

public class ResourceFile implements AutoCloseable {

    private File tempFile = null;
    private Class ownerClass;
    private ClassLoader ownerClassLoader;
    private String resourceName;

    public ResourceFile(String name) {
        this.resourceName = name;
    }

    public ResourceFile(ClassLoader owner, String name) {
        this.ownerClassLoader = owner;
        this.resourceName = name;
    }

    public ResourceFile(Class owner, String name) {
        this.ownerClass = owner;
        this.resourceName = name;
    }

    public synchronized ResourceFile load() {
        if (tempFile == null || !tempFile.exists()) {
            try {
                InputStream stream = getURL().openStream();
                String name = resourceName;
                String ext = "";
                int lastDot = resourceName.lastIndexOf(".");
                if (lastDot > 0) {
                    name = resourceName.substring(0, lastDot);
                    ext = resourceName.substring(lastDot + 1);
                }

                String suffix = null;
                if (!ext.isEmpty()) {
                    suffix = "." + ext;
                }

                tempFile = File.createTempFile(name, suffix);
                FileOutputStream out = new FileOutputStream(tempFile);
                try {
                    byte[] temp = new byte[32768];
                    int rc;
                    while ((rc = stream.read(temp)) > 0)
                        out.write(temp, 0, rc);
                } finally {
                    out.close();
                }
                stream.close();
                tempFile.deleteOnExit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public URL getURL() throws IOException {
        URL url;
        if (ownerClass != null) {
            url = ownerClass.getResource(resourceName);
        } else if (ownerClassLoader != null) {
            url = ownerClassLoader.getResource(resourceName);
        } else {
            url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        }

        if (url == null) {
            throw new IOException("Could not find resource: " + toString());
        }
        return url;
    }

    public boolean isLoaded() {
        return (tempFile != null);
    }

    public InputStream openStream() throws IOException {
        if (tempFile == null || !tempFile.exists())
            return getURL().openStream();
        else {
            try {
                return new FileInputStream(tempFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public File getFile() throws IOException {
        if (!isLoaded()) load();
        return tempFile;
    }

    public String getPath() throws IOException {
        if (!isLoaded()) load();
        return tempFile.getAbsolutePath();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (ownerClass != null) {
            builder.append(ownerClass.toString());
        } else if (ownerClassLoader != null) {
            builder.append(ownerClassLoader.toString());
        }

        if (builder.length() > 0) {
            builder.append(":");
        }

        builder.append(resourceName);

        if (tempFile != null) {
            builder.append(" <").append(tempFile.getAbsolutePath()).append(">");
        }

        return builder.toString();
    }

    @Override
    public synchronized void close() throws IOException {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
            tempFile = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } catch (Throwable ignore) {
        }

        super.finalize();
    }
}
