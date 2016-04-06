package masharipov.certustextile;

public class RecyclerData {
    String styleUri, frontUri, sideUri, backUri;
    String gender, tag, collar, size, type, addText;
    int addID, sizePos, typePos, uniqueID;
    boolean isAddButton;

    public RecyclerData() {
        styleUri = null;
        frontUri = null;
        backUri = null;
        sideUri = null;
        isAddButton = true;
        addID = R.drawable.ic_add_green_800_24dp;
        addText = "Добавить";
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

    public void setAddText(String name) {
        addText = name;
    }

    public void setSize(String string, int pos) {
        sizePos = pos;
        size = string;
    }

    public void setImageUri(String type, String path) {
        switch (type) {
            case "style":
                styleUri = path;
                break;
            case "front":
                frontUri = path;
                break;
            case "back":
                backUri = path;
                break;
            case "side":
                sideUri = path;
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
    public String getImageUri(String type) {
        String returnPath;
        switch (type) {
            case "style":
                returnPath = styleUri;
                break;
            case "front":
                returnPath = frontUri;
                break;
            case "back":
                returnPath = backUri;
                break;
            case "side":
                returnPath = sideUri;
                break;
            default:
                returnPath = null;
                break;
        }
        return returnPath;
    }


    public int getAddID() {
        return addID;
    }

}