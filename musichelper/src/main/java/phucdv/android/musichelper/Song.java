package phucdv.android.musichelper;

import android.net.Uri;

public class Song {
    private long mId;
    private String mSongTitle;
    private String mArtist;
    private String mAlbumTitle;
    private Uri mAlbumUri;
    private long mTimeStamp;

    public Song() {
    }

    public Song(long id, String title, String artist, String albumTitle, Uri albumUri, long times) {
        this.mId = id;
        this.mSongTitle = title;
        this.mArtist = artist;
        this.mAlbumTitle = albumTitle;
        this.mAlbumUri = albumUri;
        this.mTimeStamp = times;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return mSongTitle;
    }

    public void setTitle(String title) {
        this.mSongTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    public String getAlbumTitle() {
        return mAlbumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.mAlbumTitle = albumTitle;
    }

    public Uri getAlbumUri() {
        return mAlbumUri;
    }

    public void setAlbumUri(Uri albumUri) {
        this.mAlbumUri = albumUri;
    }

    public long getMillisTimes() {
        return mTimeStamp;
    }

    public long getSecTimes() {
        return mTimeStamp / 1000;
    }

    public String getFormatTimes() {
        long millis = mTimeStamp;
        millis /= 1000;
        long minutes = millis / 60;
        long sec = millis % 60;
        return minutes + ":" + sec;
    }

    public void setTimes(long times) {
        this.mTimeStamp = times;
    }

    public static String getFormatTimes(long millis) {
        millis /= 1000;
        long minutes = millis / 60;
        long sec = millis % 60;
        return minutes + ":" + sec;
    }
}
