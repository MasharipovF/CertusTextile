package masharipov.certustextile.stickeradd;

/**
 * Created by Farrukh on 06.04.2016.
 */
public class StickerData {
    String TAG, URI, ID;

    public StickerData() {
        URI = null;
        TAG = null;
        ID = null;
    }

    public void setID(String id) {
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

    public String getID() {
        return ID;
    }
}
