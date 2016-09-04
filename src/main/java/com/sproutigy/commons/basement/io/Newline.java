package com.sproutigy.commons.basement.io;

public enum Newline {
    CR_LF("\r\n"), //Windows style
    LF_CR("\n\r"),
    LF("\n"), //UNIX style
    CR("\r")
    ;

    public static final Newline WINDOWS = Newline.CR_LF;
    public static final Newline UNIX = Newline.LF;

    public static final Newline SYSTEM = detect(System.lineSeparator());


    private String string;

    Newline(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public static Newline detect(String s) {
        if (s.contains(CR_LF.getString())) {
            return CR_LF;
        }
        if (s.contains(LF_CR.getString())) {
            return LF_CR;
        }
        if (s.contains(LF.getString())) {
            return LF;
        }
        if (s.contains(CR.getString())) {
            return CR;
        }

        return null;
    }

    @Override
    public String toString() {
        return getString();
    }
}
