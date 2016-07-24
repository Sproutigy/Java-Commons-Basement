package com.sproutigy.commons.basement.config.sources;

public abstract class AbstractConfigSource implements ConfigSource {
    protected boolean modifiable;

    public AbstractConfigSource(boolean modifiable) {
        this.modifiable = modifiable;
    }

    @Override
    public boolean isModifiable() {
        return modifiable;
    }

    @Override
    public void store(String key, Object value) throws Exception {
        if (!isModifiable()) {
            throw new IllegalStateException("Configuration not modifiable");
        }
    }

    @Override
    public void refresh() throws Exception {

    }

    @Override
    public void flush() {

    }
}
