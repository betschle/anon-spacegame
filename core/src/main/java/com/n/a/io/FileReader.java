package com.n.a.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {

    public StringBuilder loadFile(String path, boolean internal) {
        // TODO cant write tests for this because of this line
        FileHandle internalFile = internal ? Gdx.files.internal(path) : Gdx.files.local(path);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader( internalFile.reader() );
        String currentLine = "";

        try {
            while ((currentLine = reader.readLine()) != null) {
                builder.append(currentLine);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder;
    }
}
