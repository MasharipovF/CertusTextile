package masharipov.certustextile;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.StickerAdd.GridItem;


public class CertusDatabase {

    private SQLiteDatabase sdb;
    private List<List<RecyclerData>> goods;
    private List<GridItem> stickers;
    private String tag, tableName;
    private Context context;

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

        stickers.remove(stickers.size() - 1);
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
        ExternalDbOpenHelper externalDbOpenHelper = new ExternalDbOpenHelper(context, "certusDB.db");
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
                            content.put(TAG, recyclerData.tag);
                            content.put(COLLAR, recyclerData.collar);
                            if (recyclerData.styleUri != null)
                                content.put(STYLEURI, recyclerData.styleUri);
                            if (recyclerData.frontUri != null)
                                content.put(FRONTURI, recyclerData.frontUri);
                            if (recyclerData.backUri != null)
                                content.put(BACKURI, recyclerData.backUri);
                            if (recyclerData.sideUri != null)
                                content.put(SIDEURI, recyclerData.sideUri);
                            content.put(GENDER, recyclerData.gender);
                            content.put(SIZE, recyclerData.size);
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
        ExternalDbOpenHelper externalDbOpenHelper = new ExternalDbOpenHelper(context, "certusDB.db");
        sdb = externalDbOpenHelper.getWritableDatabase();

        try {
            if (stickers != null) {
                Toast.makeText(context, "Sticker size " + Integer.toString(goods.size()), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < stickers.size(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(ID, stickers.get(i).getID());
                    cv.put(TAG, stickers.get(i).getTAG());
                    cv.put(URI, stickers.get(i).getURI());
                    sdb.insert(tableName, null, cv);
                    Toast.makeText(context, "Item " + Integer.toString(i) + " " + cv.getAsString(ID) + " " + cv.getAsString(TAG) + " " + cv.getAsString(URI), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Sticker Database is empty", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Sticker" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<List<RecyclerData>> getGoodsFromDB() {
        List<List<RecyclerData>> list = new ArrayList<>();
        return list;
    }
}