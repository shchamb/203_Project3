import processing.core.PImage;

import java.util.List;

public abstract class animatedEntity extends activeEntity{
    private int animationPeriod;

    public animatedEntity(String id, Point position,
                        List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;

    }


    @Override
    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

//    @Override
//    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;

    }

}
