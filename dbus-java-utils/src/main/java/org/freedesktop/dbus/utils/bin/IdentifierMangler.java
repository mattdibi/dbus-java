package org.freedesktop.dbus.utils.bin;

import java.util.Arrays;

/**
 * Checks identifiers for keywords etc and mangles them if so.
 */
public final class IdentifierMangler {
    private static String[] keywords;
    static {
        keywords = new String[] {
                "true", "false", "null", "abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof",
                "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"
        };
        Arrays.sort(keywords);
    }

    public static String mangle(String _name) {
        if (Arrays.binarySearch(keywords, _name) >= 0) {
            _name = "_" + _name;
        }
        return _name;
    }

    private IdentifierMangler() {

    }
}
