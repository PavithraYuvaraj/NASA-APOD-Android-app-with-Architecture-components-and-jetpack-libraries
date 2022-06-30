package com.education.apictureofthedaynasa.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.education.apictureofthedaynasa.Picture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Picture.class}, version = 1, exportSchema = false)
public abstract class PictureRoomDatabase extends RoomDatabase{
    public abstract PictureDao mPictureDao();

    private static volatile PictureRoomDatabase mPictureRoomDatabase;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService mDatabaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PictureRoomDatabase getDatabaseInstance(final Context context) {
        if(mPictureRoomDatabase == null) {
            synchronized (PictureRoomDatabase.class) {
                if(mPictureRoomDatabase == null) {
                    mPictureRoomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            PictureRoomDatabase.class,
                            "picture_database")
                            .addCallback(mRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return mPictureRoomDatabase;
    }

    private static final RoomDatabase.Callback mRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            mDatabaseWriteExecutor.execute(() -> {
               /* PictureDao dao = mPictureRoomDatabase.mPictureDao();

                Picture picture = new Picture();
                picture.setTitle("Spiral Galaxy NGC 6744");
                picture.setExplanation("Beautiful spiral galaxy NGC 6744 is nearly 175,000 light-years across, larger than our own Milky Way. It lies some 30 million light-years distant in the southern constellation Pavo but appears as only a faint, extended object in small telescopes. We see the disk of the nearby island universe tilted towards our line of sight in this remarkably detailed galaxy portrait, a telescopic view that spans an area about the angular size of a full moon. In it, the giant galaxy's elongated yellowish core is dominated by the light from old, cool stars. Beyond the core, grand spiral arms are filled with young blue star clusters and speckled with pinkish star forming regions. An extended arm sweeps past smaller satellite galaxy NGC 6744A at the lower right. NGC 6744's galactic companion is reminiscent of the Milky Way's satellite galaxy the Large Magellanic Cloud.");
                picture.setHdUrl("https://apod.nasa.gov/apod/image/2206/NGC6744_chakrabarti2048R.jpg");
                picture.setDate("2022-06-23");
                dao.insert(picture);*/
            });
        }
    };
}
