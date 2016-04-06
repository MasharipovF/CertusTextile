package masharipov.certustextile.StickerAdd;

/**
 * Created by Farrukh on 06.04.2016.
 */
public class GridItem {
    String TAG, URI;
    int ID;
    boolean delButtonVisible;

    public GridItem() {
        URI = null;
        TAG = null;
        delButtonVisible = false;
    }

    public void setID(int id) {
        ID = id;
    }

    public void setTAG(String tag) {
        TAG = tag;
    }

    public void setURI(String uri) {
        URI = uri;
    }

    public String getURI() {
        return URI;
    }

    public String getTAG() {
        return TAG;
    }

    public int getID() {
        return ID;
    }
}
