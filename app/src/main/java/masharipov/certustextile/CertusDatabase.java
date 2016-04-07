package masharipov.certustextile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.GridItem;


public class CertusDatabase {

    private SQLiteDatabase sdb;
    private List<List<RecyclerData>> goods;
    private List<GridItem> stickers;
    private String tag, tableName;
    private Context context;
    private String DB_NAME = "certusDB.db";

    public CertusDatabase(Context ctx) {
        context = ctx;
    }

    public CertusDatabase(List<List<RecyclerData>> list, String name, Context c) {
        goods = list;
        tableName = name;
        context = c;

        if (goods == null) Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < goods.size(); i++) {
            List<RecyclerData> recyclerDataList = goods.get(i);
            if (recyclerDataList != null)
                recyclerDataList.remove(recyclerDataList.size() - 1);
        }


    }

    public CertusDatabase(List<GridItem> item, Context ctx) {
        context = ctx;
        stickers = item;
    }

    public void saveGoodsToDB() {
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
        ExternalDbOpenHelper externalDbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        sdb = externalDbOpenHelper.getWritableDatabase();
        try {
            if (goods != null) {
                Toast.makeText(context, "Data size " + Integer.toString(goods.size()), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < goods.size(); i++) {
                    uniqueID = Long.toString(System.currentTimeMillis());
                    if (goods.get(i) != null) {
                        for (int j = 0; j < goods.get(i).size(); j++) {
                            RecyclerData recyclerData = goods.get(i).get(j);
                            ContentValues content = new ContentValues();
                            content.put(ID, uniqueID);
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
                            Toast.makeText(context, tableName + " " + content.get(TAG) + " " + content.get(COLLAR) + " " + content.get(SIZE) + " " + content.get(GENDER), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveStickersToDB() {
        String ID = "ID";
        String TAG = "TAG";
        String URI = "URI";
        String tableName = "Sticker";
        ExternalDbOpenHelper externalDbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        sdb = externalDbOpenHelper.getWritableDatabase();

        try {
            if (stickers != null) {
                Toast.makeText(context, "Sticker size " + Integer.toString(stickers.size()), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < stickers.size(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(ID, stickers.get(i).getID());
                    cv.put(TAG, stickers.get(i).getTAG());
                    cv.put(URI, stickers.get(i).getURI());
                    long rowid = sdb.insert(tableName, null, cv);
                    Toast.makeText(context, "Item " + Integer.toString(i) + "\n ID " + cv.getAsString(ID) + "\nTAG " + cv.getAsString(TAG) + "\nURI " + cv.getAsString(URI) + "\n ROW NUMBER" + Long.toString(rowid), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Sticker Database is empty", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Sticker" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<GridItem> getStickersFromDB() {
        List<GridItem> list = new ArrayList<>();
        String ID = "ID";
        String TAG = "TAG";
        String URI = "URI";
        String tableName = "Sticker";

        ExternalDbOpenHelper externalDbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        sdb = externalDbOpenHelper.openDataBase();
        Cursor cursor;
        cursor = sdb.query(tableName, new String[]{ID, TAG, URI}, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            GridItem item = new GridItem();
            item.setID(cursor.getString(cursor.getColumnIndex(ID)));
            if (cursor.getString(cursor.getColumnIndex(TAG)) != null)
                item.setTAG(cursor.getString(cursor.getColumnIndex(TAG)));
            item.setURI(cursor.getString(cursor.getColumnIndex(URI)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<List<RecyclerData>> getGoodsFromDB() {
        List<List<RecyclerData>> list = new ArrayList<>();
        return list;
    }
}


