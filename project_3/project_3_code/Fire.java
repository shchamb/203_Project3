import processing.core.PImage;
import java.util.concurrent.TimeUnit;

import java.util.List;

public class Fire extends Entity{

    public static final int FIRE_NUM_PROPERTIES = 3;
    public static final int FIRE_ID = 1;
    public static final String FIRE_KEY = "fire";
    public static final int FIRE_ACTION_PERIOD = 2;
    public static final int FIRE_ANIMATION_PERIOD = 3;
//    public Point pos;


    public Fire(String id, Point position, List<PImage> images) {
        super(id, position, images);
//        this.pos = pos;
    }

    public void beFire(WorldModel world){
        System.out.println("Fire!");
        world.addEntity(this);


//        Point pt = this.getPosition();
//        String id = "grass";
//        world.setBackground(pt,
//                new Background(id, imageStore.getImageList(id)));
        //wait?
//        world.removeEntity(this);
    }





}
