package masharipov.certustextile.stickeradd;

/**
 * Created by Farrukh on 06.04.2016.
 */
public class StickerData {
    private String TAG, URI, ID;
    private int placeHolder;
    private int isAlbum;

    public StickerData() {
        URI = null;
        TAG = null;
        ID = null;
        isAlbum = 0;
    }

    public void setPlaceHolder(int resID) {
        placeHolder = resID;
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

    public void setAlbum(int f) {
        isAlbum = f;
    }

    public int isAlbum() {
        return isAlbum;
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

    public int getPlaceHolder() {
        return placeHolder;
    }
}
