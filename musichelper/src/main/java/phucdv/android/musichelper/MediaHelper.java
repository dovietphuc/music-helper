package phucdv.android.musichelper;

import android.Manifest;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MediaHelper {
    public interface OnFinishRetrieve {
        public void onFinish(List<Song> result);
    }

    public static void retrieveAllSong(Context context, OnFinishRetrieve onFinishRetrieve) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("READ_EXTERNAL_STORAGE is required. Ask for permission first");
        }

        new AsyncTaskLoader<List<Song>>(context) {
            @Override
            public List<Song> loadInBackground() {
                List<Song> result = new ArrayList<>();
                ContentResolver musicResolver = context.getContentResolver();
                Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor musicCursor = musicResolver.query(musicUri,
                        null,
                        MediaStore.Audio.Media.IS_MUSIC + " !=0",
                        null,
                        MediaStore.Audio.Media.TITLE + " ASC");
                if (musicCursor != null && musicCursor.moveToFirst()) {
                    do {
                        long thisId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                        String thisTitle = musicCursor.getString(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                        String thisArtist = musicCursor.getString(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                        String thisAlbum = musicCursor.getString(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID));
                        Uri thisAlbumUri;
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            Uri artUri = Uri.parse("content://media/external/audio/albumart");
                            thisAlbumUri = ContentUris.withAppendedId(artUri, albumId);
                        } else {
                            thisAlbumUri = ContentUris.withAppendedId(
                                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                    albumId
                            );
                        }

                        long thisTimes = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                        Song songItem = new Song();
                        songItem.setId(thisId);
                        songItem.setTitle(thisTitle);
                        songItem.setArtist(thisArtist);
                        songItem.setAlbumTitle(thisAlbum);
                        songItem.setAlbumUri(thisAlbumUri);
                        songItem.setTimes(thisTimes);
                        result.add(songItem);
                    } while (musicCursor.moveToNext());
                }
                return result;
            }

            @Override
            public void deliverResult(List<Song> data) {
                super.deliverResult(data);
                onFinishRetrieve.onFinish(data);
            }
        }.forceLoad();
    }
}
