package com.n.a.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;

public interface FileHandler {

    enum FileLocation {
        ABSOLUTE,
        INTERNAL,
        LOCAL,
        CLASSPATH,
        EXTERNAL;
    }


    /**
     * Reads the contents of a text-based file
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return
     */
    StringBuilder readFileContents(String path, FileLocation location) throws IOException, GdxRuntimeException;

    /**
     * Reads the contents of a binary file
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return
     */
    byte[] readFileBinary( String path, FileLocation location) throws IOException;

    /**
     * Saves text content to a text-based file
     * @param data String data
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     */
    void saveFileContents(String data, String path, FileLocation location) throws IOException;

    /**
     * Saves bytes to a binary file
     * @param data binary data
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     */
    void saveFileBinary(byte[] data, String path, FileLocation location) throws IOException;
}
