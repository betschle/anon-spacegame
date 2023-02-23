package com.n.a.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.n.a.XYZException;

import java.io.*;

public class XYZFiles implements FileHandler {

    public static FileHandle toGdxFileHandle(String path, FileLocation fileLocation) {
        if (Gdx.files == null) {
            throw new XYZException(XYZException.ErrorCode.E0001, "LibGdx");
        }
        switch (fileLocation) {
            case INTERNAL: return Gdx.files.internal(path);
            case CLASSPATH: return Gdx.files.classpath(path);
            case EXTERNAL: return Gdx.files.external(path);
            case ABSOLUTE: return Gdx.files.absolute(path);
            case LOCAL: return Gdx.files.local(path);
        }
        return null;
    }
    /**
     *
     * @param path the path to file
     * @param location defines the location. Only matters if GDX was initialized.
     * @return when GDX was not initialized, returns an InputStream from source root
     */
    protected InputStream getReader(String path, FileLocation location) throws GdxRuntimeException {
        System.out.println("get reader for path: " + path);
        if( Gdx.files == null) { // fallback
            try { // TODO clumsy
                return new FileInputStream( new File(path.replace('\\', '/')) );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            return toGdxFileHandle(path, location).read();
        }
        return null;
    }

    @Override
    public StringBuilder readFileContents(String path, FileLocation location) throws IOException, GdxRuntimeException {
        InputStream stream = getReader(path, location);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {
            builder.append(currentLine);
        }
        return builder;
    }

    @Override
    public byte[] readFileBinary(String path, FileLocation location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveFileContents(String data, String path, FileLocation location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveFileBinary(byte[] data, String path, FileLocation location) {
        throw new UnsupportedOperationException();
    }
}
