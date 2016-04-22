package masharipov.certustextile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.StickerData;
import masharipov.certustextile.stickeradd.TovarData;


public class CertusDatabase {

    private SQLiteDatabase sdb;
    private List<RecyclerData> goods;
    private List<StickerData> stickers;
    private String tag;
    private Context context;
    private String DB_NAME = "certus.db";
    private String tableNames[] = {"Futbolka", "Mayka", "Polo"};
    private boolean isFromEdit = true;
    private ExternalDbOpenHelper externalDbOpenHelper;

    public CertusDatabase(Context ctx) {
        context = ctx;
        externalDbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        sdb = externalDbOpenHelper.openDataBase();
    }

    public CertusDatabase(Context c, List<RecyclerData> list) {
        goods = list;
        context = c;
        externalDbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        sdb = externalDbOpenHelper.openDataBase();
    }

    public CertusDatabase(List<StickerData> item, Context ctx) {
        context = ctx;
        stickers = item;
        externalDbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        sdb = externalDbOpenHelper.openDataBase();
    }

    public void saveGoodsToDB(String tableName, boolean clearDB) {
        String ID = "ID";
        String TAG = "TAG";
        String COLLAR = "COLLAR";
        String STYLEURI = "STYLEURI";
        String FRONTURI = "FRONTURI";
        String BACKURI = "BACKURI";
        String SIDEURI = "SIDEURI";
        String GENDER = "GENDER";
        String SIZE = "SIZE";
        String uniqueID;

        if (clearDB) {
            sdb.delete(tableName, null, null);
        }

        if (goods != null && goods.size() > 0) {
            for (int i = 0; i < goods.size(); i++) {
                RecyclerData recyclerData = goods.get(i);
                ContentValues content = new ContentValues();
                content.put(ID, recyclerData.getID());
                content.put(TAG, recyclerData.getTag());
                content.put(COLLAR, recyclerData.getCollar());
                if (recyclerData.getImageUri("style") != null)
                    content.put(STYLEURI, recyclerData.getImageUri("style"));
                if (recyclerData.getImageUri("front") != null)
                    content.put(FRONTURI, recyclerData.getImageUri("front"));
                if (recyclerData.getImageUri("back") != null)
                    content.put(BACKURI, recyclerData.getImageUri("back"));
                if (recyclerData.getImageUri("side") != null)
                    content.put(SIDEURI, recyclerData.getImageUri("side"));
                content.put(GENDER, recyclerData.getGender());
                content.put(SIZE, recyclerData.getSize());
                sdb.insert(tableName, null, content);
                Log.v("DATAA", "From table " + tableName + " ID " + content.get(ID) + " TAG " + content.get(TAG) + " COLLAr " + content.get(COLLAR) + " SIZe " + content.get(SIZE) + " " + content.get(GENDER));
            }
        }
    }


    public List<RecyclerData> getTovarFromDB(String tableName) {
        List<RecyclerData> list = new ArrayList<>();
        String ID = "ID";
        String TAG = "TAG";
        String COLLAR = "COLLAR";
        String STYLEURI = "STYLEURI";
        String FRONTURI = "FRONTURI";
        String BACKURI = "BACKURI";
        String SIDEURI = "SIDEURI";
        String GENDER = "GENDER";
        String SIZE = "SIZE";
        Cursor cursor;
        cursor = sdb.query(tableName, new String[]{ID, TAG, COLLAR, STYLEURI, FRONTURI, BACKURI, SIDEURI, GENDER, SIZE}, null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            RecyclerData item = new RecyclerData();
            item.setID(cursor.getString(cursor.getColumnIndex(ID)));
            item.setTag(cursor.getString(cursor.getColumnIndex(TAG)));
            item.setCollar(cursor.getString(cursor.getColumnIndex(COLLAR)));

            if (cursor.getString(cursor.getColumnIndex(STYLEURI)) != null)
                item.setImageUri("style", cursor.getString(cursor.getColumnIndex(STYLEURI)));

            if (cursor.getString(cursor.getColumnIndex(FRONTURI)) != null)
                item.setImageUri("front", cursor.getString(cursor.getColumnIndex(FRONTURI)));

            if (cursor.getString(cursor.getColumnIndex(BACKURI)) != null)
                item.setImageUri("back", cursor.getString(cursor.getColumnIndex(BACKURI)));

            if (cursor.getString(cursor.getColumnIndex(SIDEURI)) != null)
                item.setImageUri("side", cursor.getString(cursor.getColumnIndex(SIDEURI)));

            item.setGender(cursor.getString(cursor.getColumnIndex(GENDER)));
            item.setSize(cursor.getString(cursor.getColumnIndex(SIZE)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        cursor.close();
        return list;
    }

    public void saveStickersToDB() {
        String ID = "ID";
        String TAG = "TAG";
        String URI = "URI";
        String ISALBUM = "ISALBUM";
        String tableName = "Sticker";

        sdb.delete(tableName, null, null);

        if (stickers != null) {
            for (int i = 0; i < stickers.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put(ID, stickers.get(i).getID());
                cv.put(TAG, stickers.get(i).getTAG());
                cv.put(URI, stickers.get(i).getURI());
                cv.put(ISALBUM, stickers.get(i).isAlbum());
                Log.v("DATAA", "ID = " + cv.getAsString(ID) + ", TAG = " + cv.getAsString(TAG) + ", URI = " + cv.getAsString(URI) + ", ISALBUM = " + cv.getAsString(ISALBUM));
                sdb.insert(tableName, null, cv);
            }
        } else {
        }
    }

    public List<StickerData> getStickersFromDB() {
        List<StickerData> list = new ArrayList<>();
        String ID = "ID";
        String TAG = "TAG";
        String URI = "URI";
        String ISALBUM = "ISALBUM";
        String tableName = "Sticker";

        Cursor cursor;
        cursor = sdb.query(tableName, new String[]{ID, TAG, URI, ISALBUM}, null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            StickerData item = new StickerData();
            item.setID(cursor.getString(cursor.getColumnIndex(ID)));
            item.setTAG(cursor.getString(cursor.getColumnIndex(TAG)));
            item.setURI(cursor.getString(cursor.getColumnIndex(URI)));
            item.setAlbum(cursor.getInt(cursor.getColumnIndex(ISALBUM)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        cursor.close();
        return list;
    }

    public void saveSlideshowItemsToDB() {
        String ID = "ID";
        String URI = "URI";
        String tableName = "Slideshow";

        sdb.delete(tableName, null, null);

        if (stickers != null) {
            for (int i = 0; i < stickers.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put(ID, stickers.get(i).getID());
                cv.put(URI, stickers.get(i).getURI());
                sdb.insert(tableName, null, cv);
            }
        } else {
        }
    }

    public List<StickerData> getSlideshowItemsFromDB() {

        List<StickerData> list = new ArrayList<>();
        String ID = "ID";
        String URI = "URI";
        String tableName = "Slideshow";

        Cursor cursor;
        cursor = sdb.query(tableName, new String[]{ID, URI}, null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            StickerData item = new StickerData();
            item.setID(cursor.getString(cursor.getColumnIndex(ID)));
            item.setURI(cursor.getString(cursor.getColumnIndex(URI)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        cursor.close();
        return list;
    }

    public void clearDB() {
        sdb.delete("Futbolka", null, null);
        sdb.delete("Mayka", null, null);
        sdb.delete("Polo", null, null);
    }

    public boolean isTableEmpty(String tableName) {

        Cursor cursor = sdb.query(tableName, null, null, null, null, null, null);
        return cursor == null || cursor.getCount() == 0;
    }


    public List<TovarData> getGoodsFromDB(String tableName) {
        List<TovarData> list = new ArrayList<>();
        String ID = "ID";
        String TAG = "TAG";
        String COLLAR = "COLLAR";
        String STYLEURI = "STYLEURI";
        String FRONTURI = "FRONTURI";
        String BACKURI = "BACKURI";
        String SIDEURI = "SIDEURI";
        String GENDER = "GENDER";
        String SIZE = "SIZE";

        Cursor cursor;
        cursor = sdb.query(tableName, new String[]{ID, FRONTURI}, null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            TovarData item = new TovarData();
            item.setID(cursor.getString(cursor.getColumnIndex(ID)));
            if (cursor.getString(cursor.getColumnIndex(FRONTURI)) != null)
                item.setFRONT(cursor.getString(cursor.getColumnIndex(FRONTURI)));
            item.setSectionText(tableName);
            item.setType(0);
            list.add(item);
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        cursor.close();
        return list;
    }
}





