import processing.core.PImage;

import java.util.List;

public class ho_oh extends Entity{
    public static final String HO_OH_KEY = "ho_oh";
    public static final int HO_OH_NUM_PROPERTIES = 4;
    public static final int HO_OH_ID = 1;
    public static final int HO_OH_COL = 2;
    public static final int HO_OH_ROW = 3;




    private int tributes;

    public ho_oh(String id, Point position, List<PImage> images) {
        super(id, position, images);
        this.tributes = 0;
    }

    public int getTributes() {
        return tributes;
    }

    public void setTributes(int tributes) {
        this.tributes = tributes;
    }

}
