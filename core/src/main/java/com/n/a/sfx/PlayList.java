package com.n.a.sfx;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayList {
    /** the name of the playlist */
    private String name;
    /** A map of song info objects to use as music. */
    private Map<String, SongInfo> songs = new LinkedHashMap<>();

    public class SongInfo{
        /**The display name of the song.*/
        public String name;
        /** The composer/artist of this song.*/
        public String composer;
        /** The sound file. */
        public String file;
    }

    public void addSong(String songkey, String name, String composer, String file){
        SongInfo songInfo = new SongInfo();
        songInfo.composer = composer;
        songInfo.file = file;
        songInfo.name = name;
        this.songs.put(songkey, songInfo );
    }

    public SongInfo getSongInfoByKey(String songkey) {
        return this.songs.get(songkey);
    }

    public Object[] getKeySet() {
        return songs.keySet().toArray();
    }
}
