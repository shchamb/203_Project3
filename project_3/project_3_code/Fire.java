import processing.core.PImage;
import java.util.concurrent.TimeUnit;

import java.util.List;

public class Fire extends activeEntity{

    public static final int FIRE_NUM_PROPERTIES = 3;
    public static final int FIRE_ID = 1;
    public static final String FIRE_KEY = "fire";
    public static final int FIRE_ACTION_PERIOD = 2;
    public static final int FIRE_ANIMATION_PERIOD = 3;
    public int actionPeriod = 11;
    public Point dest;
//    public int animationPeriod = 10;
//    public Point pos;


    public Fire(String id, Point position, Point dest, List<PImage> images) {
        super(id, position, images, 11);
        this.dest = dest;
//        this.pos = pos;
    }

    public void beFire(WorldModel world, ImageStore imageStore){
        System.out.println("Fire!");
        world.addEntity(this);
        Background.melt(world, new Point(this.getPosition().x, this.getPosition().y), imageStore);


//        Point pt = this.getPosition();
//        String id = "grass";
//        world.setBackground(pt,
//                new Background(id, imageStore.getImageList(id)));
        //wait?
//        world.removeEntity(this);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        System.out.println("I'm fire");
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new activityAction(this, world, imageStore),
                this.actionPeriod);

    }





}
