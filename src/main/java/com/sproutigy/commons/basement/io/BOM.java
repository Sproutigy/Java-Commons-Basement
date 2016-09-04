package com.sproutigy.commons.basement.io;

import java.nio.charset.Charset;

public enum BOM {
    UTF_32BE(Charset.forName("UTF-32BE"), 0, 0, 254, 255),
    UTF_32LE(Charset.forName("UTF-32LE"), 255, 254, 0, 0),
    UTF_16BE(Charset.forName("UTF-16BE"), 254, 255),
    UTF_16LE(Charset.forName("UTF-16LE"), 255, 254),
    UTF_8(Charset.forName("UTF-8"), 239, 187, 191),
    ;

    public static final int MAX_LENGTH = 4;

    private Charset charset;
    private byte[] bytes;

    BOM(Charset charset, int... bytes) {
        this.charset = charset;
        this.bytes = new byte[bytes.length];
        for (int i=0; i<bytes.length; i++) {
            this.bytes[i] = (byte)bytes[i];
        }
    }

    public Charset getCharset() {
        return charset;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean matches(byte[] data) {
        if (data.length < bytes.length) {
            return false;
        }

        for (int i=0; i < bytes.length; i++) {
            if (data[i] != bytes[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return charset.toString();
    }

    public static BOM forCharset(Charset charset) {
        for (BOM bom : values()) {
            if (bom.getCharset().equals(charset)) {
                return bom;
            }
        }
        return null;
    }

    public static BOM forBytes(byte[] data) {
        for (BOM bom : values()) {
            if (bom.matches(data)) {
                return bom;
            }
        }
        return null;
    }
}
