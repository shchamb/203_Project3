import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass extends activeEntity{

    public static final String SGRASS_KEY = "seaGrass";
    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;

    public Sgrass(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {

            Fish fish = new Fish(Fish.FISH_ID_PREFIX + this.getId(), openPt.get(),
                    imageStore.getImageList(Fish.FISH_KEY), Fish.FISH_CORRUPT_MIN +
                    VirtualWorld.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN));

            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
               new activityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    public void scheduleActions(EventScheduler scheduler,
                                         WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent( this,
                new activityAction(this, world, imageStore),
                this.getActionPeriod());
    }
}
