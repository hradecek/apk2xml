package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Size;
import com.hradek.androidxml.util.ByteUtils;

public class ResStringPoolRef {
    @Size(4)
    private byte[] index;

    public long getIndex() {
        return ByteUtils.toUnsignedInt(index);
    }

    public void setIndex(byte[] index) {
        this.index = index;
    }
}
