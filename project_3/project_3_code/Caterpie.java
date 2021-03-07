import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Caterpie extends Entity {
    public static String CATERPIE_KEY = "caterpie";
    public static final String CATERPIE_ID_SUFFIX = " -- caterpie";
    public static final int CATERPIE_PERIOD_SCALE = 4;
    public static final int CATERPIE_ANIMATION_MIN = 50;
    public static final int CATERPIE_ANIMATION_MAX = 150;


    public Caterpie(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
