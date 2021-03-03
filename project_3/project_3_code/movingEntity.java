import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class movingEntity extends animatedEntity{
        public movingEntity(String id, Point position,
                              List<PImage> images, int actionPeriod, int animationPeriod)
        {
            super(id, position, images, actionPeriod, animationPeriod);


        }




        @Override
        public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public abstract Point nextPosition(WorldModel world, Point destPos);


    public abstract boolean moveTo(WorldModel world,
                                   Entity target, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, new activityAction(this, world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this, new animationAction(this, 0),
                super.getAnimationPeriod());
    }



}
