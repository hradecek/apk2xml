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

/* Imports */
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/* MAIN CLASS */
public class AndroidXml {
    /* Constant */
    private static final String usage = String.format
            ( "Usage: apk2xml [OPTION] <FILE>\n"
            + "Convert android xml to human-readable xml\n\n"
            + "%-35s\t%s\n%-35s\t%s\n%-35s\t%s\n%-35s\t%s\n%-35s\t%s\n"
            , " --apk <file.apk>"               , "Prints all xml files from 'file.apk'"
            , " --apk <file.apk> -d <directory>", "Put all transformed xml files from 'file.apk' do 'directory'"
            , " --apk <file.apk> -f <entry.xml>", "Prints 'entry.xml' file from 'file.apk'"
            , " --axml <file.xml>"              , "Prints single file"
            , " --help"                         , "Very useful help"
            );

    /* Privates */
    private static AndroidXmlParser parser = null;

    /* MAIN */
    public static void main(String[] args) {
        /* Prevents Null pointer exception */
        if(args.length == 0) {
            printAndExit(usage, 1);
        }

        /* Handle command line arguments */
        if (args[0].equals("--apk") && (args.length == 2 || args.length == 4)) { // APK argument
            File file = getFile(args[1]);
            try(ZipFile apk = new ZipFile(file)) {
                processApk(args, apk);
            } catch (IOException ioex) {
                printAndExit("Error: " + ioex.getMessage(), 2);
            }
        } else if (args[0].equals("--axml") && args.length == 2) {
            File file = getFile(args[1]);
            try {
                if (isBinaryXML(new FileInputStream(file))) {
                    printAndExit("Error: File " + args[1] + " is not valid Android XML.", 1);
                }
            } catch (IOException e) {
                printAndExit("Error: " + e.getMessage(), 2);
            }
        } else if (args[0].equals("--help")) {
            printAndExit(usage, 0);
        } else {
            printAndExit(usage, -1);
        }
    }

    /* Refactored, just print message and exit with specified code */
    private static void printAndExit(String msg, int exitCode) {
        System.out.println(msg);
        System.exit(exitCode);
    }

    /* Process apk command line arg */
    private static void processApk(String[] args, ZipFile apk) {
        if (2 == args.length) {
            createForAll(apk);
        } else if (4 == args.length && args[2].equals("-f")) {
            createForSpecific(apk, args[3]);
        } else if (4 == args.length && args[2].equals("-d")) {
            createForAll(apk, args[3]);
        } else {
            printAndExit("Not valid option\n" + usage, 0);
        }
    }

    /* Parse out specific entry from apk and print it to stdout */
    private static void createForSpecific(ZipFile apk, String entry) {
        Enumeration<?> entries = apk.entries();
        boolean found = false;
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            try {
                if (isBinaryXML(apk.getInputStream(zipEntry)) && zipEntry.getName().endsWith(entry)) {
                    parser = new AndroidXmlParser(apk.getInputStream(zipEntry));
                    parser.parse();
                    System.out.println(getXmlString(parser));
                    found = true;
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
        if (!found) {
            System.err.println("Error: File is not Android xml or it was not found in apk.");
            System.exit(2);
        }
    }

    /* Parse apk and put all xml files to dirname */
    private static void createForAll(ZipFile apk, String dirname) {
        createDir(dirname);
        Enumeration<?> entries = apk.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            try {
                if (isBinaryXML(apk.getInputStream(zipEntry))) {
                    parser = new AndroidXmlParser(apk.getInputStream(zipEntry));
                    parser.parse();
                    printToFile(parser, dirname, zipEntry.getName());
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    /* Parse apk and print to stdout */
    private static void createForAll(ZipFile apk) {
        Enumeration<?> entries = apk.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            try {
                if (isBinaryXML(apk.getInputStream(zipEntry))) {
                    parser = new AndroidXmlParser(apk.getInputStream(zipEntry));
                    parser.parse();
                    System.out.println("-- File name: " + zipEntry.getName());
                    System.out.println(getXmlString(parser));
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }

    }

    /* Create directory if does not exists */
    private static void createDir(String dirname) {
        File theDir = new File(dirname);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    /* Return file */
    private static File getFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.format("Error: File %s does not exists!", file.getAbsoluteFile());
            System.exit(2);
        } else if (!file.isFile()) {
            System.err.format("Error: %s is not a file!\n", file.getAbsoluteFile());
            System.exit(2);
        } else if (!file.canRead()) {
            System.err.format("Error: Can't read the file %s!", file.getAbsoluteFile());
            System.exit(3);
        }
        return file;
    }

    /* Print to file with specified directory */
    private static void printToFile(AndroidXmlParser parser, String dir, String name) throws IOException {
        File file = new File(dir + "/" + name);
        file.getParentFile().mkdirs(); // create missing parents
        file.createNewFile();

        PrintWriter out = new PrintWriter(file);
        String xml = getXmlString(parser);
        out.write(xml);
        out.close();
    }

    /* Get string in form of xml from Document */
    private static String getXmlString(AndroidXmlParser parser) {
        Document doc = parser.getDocument();
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            System.err.println("Can't create xml transformer: " + e.getMessage());
            System.exit(1);
        }

        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); /* Nice indentation, for 4 spaces */
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            System.err.println("Can't transform xml: " + e.getMessage());
            System.exit(1);
        }
        return result.getWriter().toString();
    }

    /* Find out if file is really binary xml
     * Reads "magic" number */
    private static boolean isBinaryXML(InputStream stream) throws IOException {
        byte[] expect = new byte[]{0x03, 0x00, 0x08, 0x00};
        byte[] magic = new byte[4];
        stream.read(magic);
        return Arrays.equals(magic, expect);
    }

    /* Creates input stream from ZIP FILE */
    private static InputStream getInputStreamFromZip(File zip, String entry) throws IOException {
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zip));
        for (ZipEntry e; (e = zin.getNextEntry()) != null; ) {
            if (e.getName().equals(entry)) {
                return zin;
            }
        }
        throw new EOFException("Cannot find " + entry);
    }
}