import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab  extends movingEntity {
    public static final String CRAB_KEY = "crab";
    public static final String CRAB_ID_SUFFIX = " -- crab";
    public static final int CRAB_PERIOD_SCALE = 4;
    public static final int CRAB_ANIMATION_MIN = 50;
    public static final int CRAB_ANIMATION_MAX = 150;

    public Crab(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = world.findNearest(this.getPosition(), Sgrass.class);
        long nextPeriod = super.getActionPeriod();

        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveTo(world, crabTarget.get(), scheduler)) {

                Quake quake = new Quake(Quake.QUAKE_ID, tgtPos, imageStore.getImageList(Quake.QUAKE_KEY),
                        Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);


                world.addEntity(quake);
                nextPeriod += super.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new activityAction(this, world, imageStore),
                nextPeriod);
    }

    public boolean moveTo(WorldModel world,
                               Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(WorldModel world,
                                   Point destPos) {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Fish))) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Fish))) {
                newPos = this.getPosition();
            }
        }
        return newPos;
    }



}
