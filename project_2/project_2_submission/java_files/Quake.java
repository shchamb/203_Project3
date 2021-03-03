import processing.core.PImage;

import java.util.List;

public class Quake  extends animatedEntity{

    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }


//    @Override
//    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        scheduler.unscheduleAllEvents(this);
//        world.removeEntity(this);
//    }






    public void scheduleActions(EventScheduler scheduler,
                                         WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new activityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                new animationAction(this, Quake.QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
