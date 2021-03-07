import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Caterpie extends movingEntity{
    public static String CATERPIE_KEY = "caterpie";
    public static final String CATERPIE_ID_SUFFIX = " -- caterpie";
    public static final int CATERPIE_PERIOD_SCALE = 4;
    public static final int CATERPIE_ANIMATION_MIN = 50;
    public static final int CATERPIE_ANIMATION_MAX = 150;


    public Caterpie(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.getPosition(), Atlantis.class); //change to statue when created
        long nextPeriod = super.getActionPeriod();

        if (target.isPresent()) {
            Point tgtPos = target.get().getPosition();

            if (this.moveTo(world, target.get(), scheduler)) {

                Quake quake = new Quake(Quake.QUAKE_ID, tgtPos, imageStore.getImageList(Quake.QUAKE_KEY),
                        Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);


                world.addEntity(quake);
                nextPeriod += super.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
                world.removeEntity(this);
            }
        }


    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        return null;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }
}
