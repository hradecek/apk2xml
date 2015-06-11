/******************************************************************************
 * Copyright [2015] [Ivo Hradek]                                              *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 * http://www.apache.org/licenses/LICENSE-2.0                                 *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing,                 *
 * software distributed under the License is distributed on an                *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,               *
 * either express or implied. See the License for the specific language       *
 * governing permissions and limitations under the License                    *
 ******************************************************************************/
package com.hradek.androidxml;

import com.hradek.androidxml.resources.*;
import com.hradek.androidxml.annotation.*;
import com.hradek.androidxml.util.ByteUtils;
import com.hradek.androidxml.exception.ResReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;

import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Field;

/**
 * This class represents resource reader
 * Resource reader reads each chunk of binary xml
 *
 * @author Ivo Hradek
 */
public class ResReader {
    /* Private fields */
    private InputStream in;

    /**
     * Constructor
     *
     * @param in - Input stream
     */
    public ResReader(InputStream in) {
        this.in = new BufferedInputStream(in);
    }

    /**
     * Read ResValue
     *
     * @see ResValue
     * @return read resource value
     */
    public ResValue readResValue()  {
        ResValue rv = new ResValue();
        readAnnotations(rv, in);
        return rv;
    }

    /**
     * Read ResChunkHeader
     *
     * @see ResChunkHeader
     * @return read resource chunk header
     */
    public ResChunkHeader readResChunkHeader() {
        ResChunkHeader rchh = new ResChunkHeader();
        readAnnotations(rchh, in);
        return rchh;
    }

    /**
     * Read ResStringPoolHeader
     *
     * @see ResStringPoolHeader
     * @return read resource chunk header
     */
    public ResStringPoolHeader readResStringPoolHeader(ResChunkHeader header) {
        ResStringPoolHeader rsph = new ResStringPoolHeader();
        rsph.setHeader(header);
        readAnnotations(rsph, in);
        return rsph;
    }

    /**
     * Read ResStringPoolRef
     *
     * @see ResStringPoolRef
     * @return read resource string pool reference
     */
    public ResStringPoolRef readResStringPoolRef() {
        ResStringPoolRef rspr = new ResStringPoolRef();
        readAnnotations(rspr, in);
        return rspr;
    }

    /**
     * Read ResStringPoolSpan
     *
     * @see ResStringPoolSpan
     * @return read resource string pool span
     */
    public ResStringPoolSpan readResStringPoolSpan() {
        ResStringPoolSpan rsps = new ResStringPoolSpan();
        readAnnotations(rsps, in);
        return rsps;
    }

    /**
     * Read whole ResStringPool
     *
     * @see ResStringPool
     * @return read resource string pool
     */
    public ResStringPool readResStringPool(ResStringPoolHeader header) {
        ResStringPool rsp = new ResStringPool();
        rsp.setStringData(new ArrayList<>());
        rsp.setHeader(header);
        ResStringPoolHeader rsph = rsp.getHeader();

        /* Read string and styles indices to List */
        List<Long> idx = readStringPoolIds(rsph);

        /* Bytes left in string pool */
        long left = rsph.getHeader().getSize()
                    - rsph.getHeader().getHeaderSize()
                    - 4 * rsph.getStringCount()
                    - 4 * rsph.getStyleCount();

        /* Find out where whole section ends, depends on if styles presents */
        long stringEnd = rsph.getStyleCount() > 0 ? rsph.getStylesStart() : rsph.getHeader().getSize();
        int size = (int) (stringEnd - rsph.getStringStart());
        byte[] stringData = new byte[size];
        try {
            in.read(stringData);
        } catch (IOException e) {
            throw new ResReaderException(e);
        }

        left -= size;

        /* Are string encode as UTF-8? */
        boolean isUtf8 = (rsph.getFlags() & StringPoolFlags.UTF8_FLAG.getType()) == StringPoolFlags.UTF8_FLAG.getType();

        /* for all indices */
        for (long si : idx) {
            int pos = (int) si;
            if (isUtf8) {
                pos = readUtf8Strings(stringData, rsp, pos);
            } else {
                pos = readUtf16Strings(stringData, rsp, pos);
            }
        }

        return rsp;
    }

    private List<Long> readStringPoolIds(ResStringPoolHeader rsph) {
        List<Long> ret = new ArrayList<>();
        try {
            for (int i = 0; i < rsph.getStringCount(); ++i) {
                byte[] string = new byte[4];
                in.read(string);
                ret.add(ByteUtils.toUnsignedInt(string));
            }

            for (int i = 0; i < rsph.getStyleCount(); ++i) {
                byte[] string = new byte[4];
                in.read(string);
            }
        } catch (IOException e) {
            throw new ResReaderException(e);
        }
        return ret;
    }

    /* Read all utf8 strings and add them to string pool, and return position */
    private int readUtf8Strings(byte[] stringData, ResStringPool rsp, int pos) {
        /* Read character length */
        long charLen = stringData[pos++];
        if ((charLen & 0x080) != 0) {
            charLen = ((charLen & 0x7F) << 8) | stringData[pos++];
        }

        /* Read byte length */
        int byteLen = stringData[pos++];
        if ((charLen & 0x080) != 0) {
            byteLen = ((byteLen & 0x7F) << 8) | stringData[pos++];
        }

        /* Read string */
        String str = null;
        try {
            str = new String(stringData, pos, byteLen, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ResReaderException(e);
        }
        rsp.getStringData().add(str);
        return pos;
    }

    /* Read all utf16 strings and add them to string pool, and return position */
    private int readUtf16Strings(byte[] stringData, ResStringPool rsp, int pos) {
        /* Read length */
        int len = stringData[pos];
        pos += 2;
        if ((len & 0x8000) != 0) { /* handle string longer than 32768 characters */
            len = ((len & 0x07FFF) << 16) | stringData[pos];
            pos += 2;
        }

        String item = null;
        int byteLen = 2 * len;
        try {
            item = new String(stringData, pos, byteLen, "UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            throw new ResReaderException(e);
        }
        rsp.getStringData().add(item);
        return pos;
    }

    /**
     * Read xml tree attribute extension
     *
     * @return attribute extension
     */
    public ResXMLTreeAttExt readResXMLTreeAttExt() {
        ResXMLTreeAttExt rxtat = new ResXMLTreeAttExt();
        readAnnotations(rxtat, in);
        return rxtat;
    }

    /**
     * Read xml tree attribute
     *
     * @return attribute
     */
    public ResXMLTreeAttribute readResXMLTreeAttribute() {
        ResXMLTreeAttribute rxta = new ResXMLTreeAttribute();
        readAnnotations(rxta, in);
        return rxta;
    }

    /**
     * Read xml CDATA
     *
     * @return CDATA
     */
    public ResXMLCdataExt readResXMLTreeCDataExt() {
        ResXMLCdataExt rxcde = new ResXMLCdataExt();
        readAnnotations(rxcde, in);
        return rxcde;
    }

    /**
     * Read end element
     *
     * @return end element
     */
    public ResXMLTreeEndElementExt readResXMLTreeEndElementExt() {
        ResXMLTreeEndElementExt rxteee = new ResXMLTreeEndElementExt();
        readAnnotations(rxteee, in);
        return rxteee;
    }

    /**
     * Read xml header
     *
     * @return header
     */
    public ResXMLTreeHeader readResXMLTreeHeader() {
        ResXMLTreeHeader rxth = new ResXMLTreeHeader();
        readAnnotations(rxth, in);
        return rxth;
    }

    /**
     * Read namespace extension
     *
     * @return namespace extension
     */
    public ResXMLTreeNamespaceExt readResXMLTreeNamespaceExt() {
        ResXMLTreeNamespaceExt rxtne = new ResXMLTreeNamespaceExt();
        readAnnotations(rxtne, in);
        return rxtne;
    }

    /**
     * Read tree node
     *
     * @return tree node
     */
    public ResXMLTreeNode readResXMLTreeNode(ResChunkHeader header) {
        ResXMLTreeNode rxtn = new ResXMLTreeNode();
        rxtn.setHeader(header);
        readAnnotations(rxtn, in);
        return rxtn;
    }

    /**
     * Read tree node
     *
     * @return tree node
     */
    public ResResourceMap readResourceMap(ResChunkHeader header) {
        ResResourceMap rrm = new ResResourceMap();
        rrm.setHeader(header);
        readAnnotations(rrm, in);
        rrm.resourceIds = new ArrayList<>();
        for (int pos = rrm.getHeader().getHeaderSize(); pos < rrm.getHeader().getSize(); pos += 4) {
            byte[] b = new byte[4];
            try {
                in.read(b);
            } catch (IOException ex) {
                throw new ResReaderException(ex);
            }
            rrm.resourceIds.add(ByteUtils.toUnsignedInt(b));
        }

        return rrm;
    }

    /**
     * Read start element
     *
     * @param node
     * @return tree node
     */
    public ResXMLTreeStartElement readResXMLTreeStartelement(ResXMLTreeNode node) {
        ResXMLTreeStartElement rxtse = new ResXMLTreeStartElement();
        ResXMLTreeAttExt attrExt = readResXMLTreeAttExt();
        rxtse.setAtrributes(new ArrayList<>());
        int left = (int) node.getHeader().getSize() - 0x24;
        for (int i = 0; i < attrExt.getAttributeCount(); ++i) {
            rxtse.getAtrributes().add(readResXMLTreeAttribute());
            left -= 0x14;
        }
        return rxtse;
    }

    /* Read classes annotations */
    /* Note: Not all used due to parser REFACTOR */
    private void readAnnotations(Object object, InputStream dis) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(ChunkHeader.class)) {
                ResChunkHeader rchh = new ResChunkHeader();
                readAnnotationChunkHeader(rchh, dis);
                f.setAccessible(true);
                try {
                    f.set(object, rchh);
                } catch (IllegalAccessException ex) {
                    throw new ResReaderException(ex);
                }
            } else if (f.isAnnotationPresent(Size.class)) {
                readAnnotationSize(object, f, dis);
            } else if (f.isAnnotationPresent(StringPoolRef.class)) {
                ResStringPoolRef rspr = new ResStringPoolRef();
                readAnnotationStringPoolRef(rspr, dis);
                f.setAccessible(true);
                try {
                    f.set(object, rspr);
                } catch (IllegalAccessException ex) {
                    throw new ResReaderException(ex);
                }
            } else if (f.isAnnotationPresent(StringPoolHeader.class)) {
                ResStringPoolHeader rsh = new ResStringPoolHeader();
                readAnnotationStringPoolHeader(object, dis);
                f.setAccessible(true);
                try {
                    f.set(object, rsh);
                } catch (IllegalAccessException ex) {
                    throw new ResReaderException(ex);
                }
            } else if (f.isAnnotationPresent(Value.class)) {
                ResValue rv = new ResValue();
                readAnnotationValue(rv, dis);
                f.setAccessible(true);
                try {
                    f.set(object, rv);
                } catch (IllegalAccessException ex) {
                    throw new ResReaderException(ex);
                }
            } else if (f.isAnnotationPresent(Node.class)) {
                ResXMLTreeNode rxtn = new ResXMLTreeNode();
                readAnnotations(rxtn, dis);
                f.setAccessible(true);
                try {
                    f.set(object, rxtn);
                } catch (IllegalAccessException ex) {
                    throw new ResReaderException(ex);
                }
            }
        }
    }

    private void readAnnotationValue(Object object, InputStream dis) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            readAnnotationSize(object, f, dis);
        }
    }

    private void readAnnotationStringPoolHeader(Object object, InputStream dis) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(ChunkHeader.class)) {
                ResChunkHeader rchh = new ResChunkHeader();
                readAnnotationChunkHeader(rchh, dis);
                f.setAccessible(true);
                try {
                    f.set(object, rchh);
                } catch (IllegalAccessException ex) {
                    throw new ResReaderException(ex);
                }
            } else if (f.isAnnotationPresent(Size.class)) {
                readAnnotationSize(object, f, dis);
            }
        }
    }

    private void readAnnotationStringPoolRef(Object object, InputStream dis) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            readAnnotationSize(object, f, dis);
        }
    }

    private void readAnnotationChunkHeader(Object object, InputStream dis) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            readAnnotationSize(object, f, dis);
        }
    }

    private void readAnnotationSize(Object object, Field field, InputStream dis) {
        int size = field.getAnnotation(Size.class).value();
        byte[] b = new byte[size];
        try {
            dis.read(b);
            field.setAccessible(true);
            if (size == 1) {
                field.set(object, b[0]);
            } else {
                field.set(object, b);
            }
        } catch (IOException | IllegalAccessException ex) {
            throw new ResReaderException(ex);
        }
    }
}
