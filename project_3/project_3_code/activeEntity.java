import processing.core.PImage;

import java.util.List;

public abstract class activeEntity extends Entity{
    private int actionPeriod;

    public activeEntity(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;

    }

    public int getActionPeriod() {
        return this.actionPeriod;
    }

    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);


    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void setActionPeriod(int x){
        this.actionPeriod = x;
    }
}
