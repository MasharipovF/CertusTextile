package masharipov.certustextile;

import android.net.Uri;

public class RecyclerData {
    Uri styleUri, frontUri, sideUri, backUri;
    String gender, tag, collar, size, type;
    int addID, sizePos, typePos, uniqueID;
    boolean isAddButton;

    public RecyclerData() {
        styleUri = null;
        frontUri = null;
        backUri = null;
        sideUri = null;
        isAddButton = true;
        addID = R.drawable.addphoto;
        size = null;
        sizePos = 0;
        gender = null;
        tag = "";
        collar = null;
        type = null;
        typePos = 0;
    }

    //SETTERS
    public void setAddID(int resID) {
        addID = resID;
    }

    public void setSize(String string, int pos) {
        sizePos = pos;
        size = string;
    }

    public void setImageUri(String type, Uri uri) {
        switch (type) {
            case "style":
                styleUri = uri;
                break;
            case "front":
                frontUri = uri;
                break;
            case "back":
                backUri = uri;
                break;
            case "side":
                sideUri = uri;
                break;
            default:
                break;
        }

    }

    public void setType(String string, int pos) {
        typePos = pos;
        type = string;
    }

    public void setGender(String string) {
        gender = string;
    }

    public void setCollar(String string) {
        collar = string;
    }


    public void setTag(String string) {
        tag = string;
    }


    //GETTERS
    public Uri getImageUri(String type) {
        Uri returnUri;
        switch (type) {
            case "style":
                returnUri = styleUri;
                break;
            case "front":
                returnUri = frontUri;
                break;
            case "back":
                returnUri = backUri;
                break;
            case "side":
                returnUri = sideUri;
                break;
            default:
                returnUri = null;
                break;
        }
        return returnUri;
    }


    public int getAddID() {
        return addID;
    }

}