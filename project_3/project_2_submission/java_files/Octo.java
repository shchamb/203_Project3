import processing.core.PImage;

import java.util.List;

public abstract class Octo extends movingEntity{
    private int resourceLimit;
    private int resourceCount;

    public static final String OCTO_KEY = "octo";
    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;
    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    public Octo(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public abstract Point nextPosition(WorldModel world, Point destPos);


    public abstract boolean moveTo(WorldModel world,
                                   Entity target, EventScheduler scheduler);

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void incrementResourceCount() {
        this.resourceCount += 1;
    }
}
