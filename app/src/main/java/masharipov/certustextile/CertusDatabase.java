package masharipov.certustextile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CertusDatabase {
    private static String ID = "ID";
    private static String TAG = "TAG";
    private static String COLLAR = "COLLAR";
    private static String STYLEURI = "STYLEURI";
    private static String FRONTURI = "FRONTURI";
    private static String BACKURI = "BACKURI";
    private static String SIDEURI = "SIDEURI";
    private static String GENDER = "GENDER";
    private static String SIZE = "SIZE";


    private SQLiteDatabase sdb;
    private List<List<RecyclerData>> data;
    private String tag, tableName;
    private Context context;

    public CertusDatabase(List<List<RecyclerData>> list, String name, Context c) {
        data = list;
        tableName = name;
        context = c;

        if (data == null) Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < data.size(); i++) {
            List<RecyclerData> recyclerDataList = data.get(i);
            if (recyclerDataList != null)
                recyclerDataList.remove(recyclerDataList.size() - 1);
        }


    }

    public void saveToDB() {
        String uniqueID;
        ExternalDbOpenHelper externalDbOpenHelper = new ExternalDbOpenHelper(context, "certusDB.db");
        sdb = externalDbOpenHelper.getWritableDatabase();
        try {
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    uniqueID = Long.toString(System.currentTimeMillis());
                    if (data.get(i) != null) {
                        for (int j = 0; j < data.get(i).size(); j++) {
                            RecyclerData recyclerData = data.get(i).get(j);
                            ContentValues content = new ContentValues();
                            content.put(ID, uniqueID);
                            content.put(TAG, recyclerData.tag);
                            content.put(COLLAR, recyclerData.collar);
                            if (recyclerData.styleUri != null)
                                content.put(STYLEURI, recyclerData.styleUri.toString());
                            if (recyclerData.frontUri != null)
                                content.put(FRONTURI, recyclerData.frontUri.toString());
                            if (recyclerData.backUri != null)
                                content.put(BACKURI, recyclerData.backUri.toString());
                            if (recyclerData.sideUri != null)
                                content.put(SIDEURI, recyclerData.sideUri.toString());
                            content.put(GENDER, recyclerData.gender);
                            content.put(SIZE, recyclerData.size);
                            sdb.insert(tableName, null, content);
                            Toast.makeText(context, "values inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<List<RecyclerData>> getFromDB() {
        List<List<RecyclerData>> list = new ArrayList<>();
        return list;
    }
}