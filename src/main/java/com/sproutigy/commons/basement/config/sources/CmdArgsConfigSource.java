package com.sproutigy.commons.basement.config.sources;

import java.util.LinkedHashMap;
import java.util.Map;

public class CmdArgsConfigSource extends AbstractConfigSource {
    private String[] args;
    private Map<String, String> parsed = new LinkedHashMap<>();

    public CmdArgsConfigSource(String... args) {
        super(false);
        this.args = args;
        parse();
    }

    private void parse() {
        for (String arg : args) {
            boolean escaped = false;

            StringBuilder key = new StringBuilder();
            StringBuilder value = null;
            char prev = 0;

            for (char c : arg.toCharArray()) {
                if (c == '"') {
                    if (prev != '\"') {
                        escaped = !escaped;
                    }
                }

                if (c == '=' && value == null) {
                    value = new StringBuilder();
                } else {
                    if (value != null) {
                        value.append(c);
                    } else {
                        key.append(c);
                    }
                }

                prev = c;
            }

            String valueString = value != null ? value.toString().replace("\\\"", "\"") : "";
            parsed.put(key.toString().replace("\\\"", "\""), valueString);
        }
    }

    @Override
    public Object load(String key) {
        return parsed.get(key);
    }

    @Override
    public Iterable<String> keys() {
        return parsed.keySet();
    }
}
