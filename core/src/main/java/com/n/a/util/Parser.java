package com.n.a.util;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {

    static String readString(String line) throws IOException {
        return line.substring(line.indexOf(":") + 1).trim();
    }

    static String readString(BufferedReader reader, String name) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Missing value: " + name);
        }
        return readString(line);
    }

    static boolean readBoolean(String line) throws IOException {
        return Boolean.parseBoolean(readString(line));
    }

    static boolean readBoolean(BufferedReader reader, String name) throws IOException {
        return Boolean.parseBoolean(readString(reader, name));
    }

    static int readInt(BufferedReader reader, String name) throws IOException {
        return Integer.parseInt(readString(reader, name));
    }

    static float readFloat(BufferedReader reader, String name) throws IOException {
        return Float.parseFloat(readString(reader, name));
    }
}
