package com.n.a.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.n.a.util.sequences.NumberGenerator;

import java.util.Objects;

/**
 * Plays one song continuously until {@link #playNextSong()} or {@link #playNextSong(String)} is
 * invoked. Will fade out the old song, then fade in the new song.
 */
public class MusicPlayer {

    // TODO add configurable playlists to shuffle from

    // Fade state
    private final int STATE_FADEIN = 0;
    private final int STATE_NORMAL = 1;
    private final int STATE_FADEOUT = 2;
    private final int STATE_FINISHED = 3;

    // Play mode
    /** Value: 0. Play songs in order. */
    public final int MODE_LINEAR = 0;
    /** Value: 1. Play songs in shuffled order. */
    public final int MODE_SHUFFLE = 1;

    // has its own number generator with its own seed
    private NumberGenerator generator = new NumberGenerator( this.hashCode() );

    private int mode = MODE_SHUFFLE;
    private int state = STATE_FADEIN; // 0 = fadeIn, 1 = normal, 2 = fadeout, 3 = finished
    // TODO music config
    // private Map<String, String> songs = new LinkedHashMap<>();
    private PlayList playList = new PlayList();
    private String nextSong = null;
    private float maxVolume = 1f;
    private Song currentSong;

    private class Song {
        private Music music;
        private float volume = 0;
        private String songKey = "";
        private String songFile = "";

        public Song(float volume, String songKey, String songFile) {
            this.volume = volume;
            this.songKey = songKey;
            this.songFile = songFile;

            this.music = Gdx.audio.newMusic(Gdx.files.internal(songFile));
            this.music.setVolume(this.volume);
            this.music.setLooping(true);
            this.music.play();
        }

        public void update() {
            if( this.music.isPlaying() || this.music.isLooping() ) {
                this.music.setVolume(this.volume);
            }
        }
    }

    public MusicPlayer() {
        this.playList.addSong("song1", "Cave", "b5cully", "mus/cave.ogg");
        this.playList.addSong("song2", "Submarine", "b5cully", "mus/sub.wav");
        this.playList.addSong("song3", "Far Away", "b5cully", "mus/far away.wav");
        this.playList.addSong("song4", "Pianometric", "b5cully", "mus/pianometric.wav");
        this.playList.addSong("main", "Solar Disturbance", "b5cully", "mus/solar disturbance.wav");
    }

    /**
     * Sets the playlist to use for this music player.
     * @param playList
     */
    public void setPlayList(PlayList playList) {
        this.playList = playList;
        this.stopCurrentSong();
    }

    /**
     * Pauses the music in the player
     */
    public void pause() {
        this.currentSong.music.pause();
    }

    /**
     * Use this method to get the Music Player running for the first time.
     * If the player already is actively playing songs, {@link #playNextSong()} is
     * being invoked instead.
     */
    public void play() {
        if( this.currentSong == null) {
            String nextSongKey = this.getNextSongKey();
            this.currentSong = new Song(0, nextSongKey, this.getSongFile(nextSongKey));
            this.state = STATE_FADEIN;
        } else {
            this.playNextSong();
        }
    }
    /**
     * Plays the next song determined by the playmode of the music player.
     */
    public void playNextSong() {
        this.stopCurrentSong();
        this.nextSong = this.getNextSongKey();
    }

    /**
     * Gets the next song as key determined by the playmode of the music player.
     * @return a key to the next song.
     */
    private String getNextSongKey() {
        switch ( mode ) {
            case MODE_LINEAR: {
                // obtain next song in the map
                Object[] keys = playList.getKeySet();
                if( this.currentSong == null) return (String) keys[0];
                int i = 0;
                String nextSongId = null;
                for( Object value : keys) {
                    if ( Objects.equals(value, this.currentSong.songKey)) {
                        boolean playlistEndReached = i+1 >= keys.length;
                        if( playlistEndReached ) {
                            // back to first song in playlist
                            nextSongId = (String) keys[0];
                        } else {
                            // next song
                            nextSongId = (String) keys[i+1];
                        }

                    }
                    i++;
                }
                return nextSongId;
            }
            case MODE_SHUFFLE: {
                Object[] keys = playList.getKeySet();
                return (String) this.generator.getRandomEntry( keys);
            }
        }
        return null;
    }

    private String getSongFile( String songKey) {
        PlayList.SongInfo songInfo = this.playList.getSongInfoByKey(songKey);
        if( songInfo != null) return songInfo.file;
        return null;
    }

    /**
     * Fades out the current song, then fades in the next song
     * @param id the id of the next song
     */
    public void playNextSong(String id) {
        this.stopCurrentSong();
        this.nextSong = id;
    }

    /**
     * Gets the current fade state of the music player
     * @return
     */
    public int getState() {
        return this.state;
    }

    /**
     *
     * @return the playmode {@link #MODE_LINEAR} or {@link #MODE_SHUFFLE}
     */
    public int getPlayMode() {
        return mode;
    }

    /**
     *
     * @param mode playmode {@link #MODE_LINEAR} or {@link #MODE_SHUFFLE}
     */
    public void setPlayMode(int mode) {
        this.mode = mode;
    }

    /**
     * Gets the current set volume of the player
     * @return
     */
    public float getCurrentVolume() {
        return this.currentSong.volume;
    }

    /**
     * Gets the maximum volume used for all songs.
     * @return
     */
    public float getMaxVolume() {
        return this.maxVolume;
    }

    /**
     * Sets the maximum volume used for all songs.
     * @param maxVolume
     */
    public void setMaxVolume(float maxVolume) {
        if( maxVolume < 0) return;
        this.maxVolume = maxVolume;
        if( this.currentSong != null ) {
            this.currentSong.volume = MathUtils.clamp(this.currentSong.volume, 0, this.maxVolume);
        }
    }

    /**
     * Stops the current song and fades it out.
     */
    public void stopCurrentSong() {
        this.state = STATE_FADEOUT;
    }

    private void playSong(String id) {
        float oldVolume = this.currentSong.volume; // pass current volume
        String songFile = this.getSongFile(id);
        this.currentSong.music.dispose();
        this.currentSong = new Song(oldVolume, id, songFile);
        this.state = STATE_FADEIN;
    }

    private void fadeInSong(float delta) {
        this.currentSong.volume = this.currentSong.volume + (delta * 0.4f);
        if( this.currentSong.volume  > maxVolume) {
            this.currentSong.volume  = maxVolume;
            this.state = STATE_NORMAL;
        }
    }

    private void fadeOutSong(float delta) {
        this.currentSong.volume  = this.currentSong.volume  - (delta * 0.4f);
        if( this.currentSong.volume  < 0 ) {
            this.currentSong.volume  = 0;
            this.state = STATE_FINISHED;
            this.currentSong.music.stop();
        }
    }

    /**
     * Updates the music player for fading songs in and out
     * @param delta
     */
    public void update(float delta) {
        if( this.currentSong == null) return;
        this.currentSong.update();

        // TODO I can move these states to Song class
        switch( state ) {
            case STATE_FADEIN: {
                this.fadeInSong(delta);
                break;
            }
            case STATE_NORMAL: break; // do nothing
            case STATE_FADEOUT: {
                this.fadeOutSong(delta);
                break;
            }
            case STATE_FINISHED: {
                // next song
                this.currentSong.volume = 0;
                if( this.nextSong != null) {
                    this.playSong(this.nextSong);
                    this.nextSong = null;
                }
                break;
            }
        }
    }

    public void dispose() {
        if( this.currentSong != null) {
            this.currentSong.music.dispose();
        }
    }
}
