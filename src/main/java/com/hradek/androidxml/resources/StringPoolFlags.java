package com.hradek.androidxml.resources;


/**
 * This enum represents string pool flags
 *
 * @author Ivo Hradek
 */
public enum StringPoolFlags {
    SORTED_FLAG(1 << 0),
    UTF8_FLAG(1 << 8); // Signalize UTF-8 coding

    private int flag;

    StringPoolFlags(int flag) {
        this.flag = flag;
    }

    public int getType() {
        return flag;
    }
}
