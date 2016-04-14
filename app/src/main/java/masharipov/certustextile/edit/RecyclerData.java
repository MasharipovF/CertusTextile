package masharipov.certustextile.edit;

import masharipov.certustextile.R;

public class RecyclerData {
    String styleUri, frontUri, sideUri, backUri, uniqueID;
    String gender, tag, collar, size, type, addText;
    int addID, sizePos, selectedColor;
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
        gender = null;
        tag = "";
        collar = null;
        type = null;
    }

    //SETTERS
    public void setAddID(int resID) {
        addID = resID;
    }

    public void setAddText(String name) {
        addText = name;
    }

    public void setSize(String string) {
        size = string;
    }

    public void setID(String id) {
        uniqueID = id;
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
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

    public void setType(String string) {
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

    public String getID() {
        return uniqueID;
    }

    public String getTag() {
        return tag;
    }

    public String getCollar() {
        return collar;
    }

    public String getSize() {
        return size;
    }

    public String getGender() {
        return gender;
    }

    public int getAddID() {
        return addID;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

}