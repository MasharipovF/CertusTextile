package masharipov.certustextile.stickeradd;

/**
 * Created by Farrukh on 11.04.2016.
 */
public class TovarData {
    String ID, STYLE, FRONT, COLLAR, TABLENAME;
    int isSection;
    int idiwka;

    public TovarData() {
        ID = null;
        STYLE = null;
        FRONT = null;
        COLLAR = null;
    }

    public void setID(String id) {
        ID = id;
    }

    public void setFRONT(String front) {
        FRONT = front;
    }

    public void setType(int type) {
        isSection = type;
    }

    public void setSectionText(String text) {
        TABLENAME = text;
    }

    public String getID() {
        return ID;
    }

    public String getFRONT() {
        return FRONT;
    }

    public int getType() {
        return isSection;
    }

    public String getSectionText() {
        return TABLENAME;
    }
}
