import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Caterpie extends movingEntity {
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

        Optional<Entity> caterpieTarget = world.findNearest(this.getPosition(), ho_oh.class);
        long nextPeriod = super.getActionPeriod();

        if (caterpieTarget.isPresent()) {
            Point tgtPos = caterpieTarget.get().getPosition();
            Point oldPos = this.getPosition();

            if (this.moveTo(world, caterpieTarget.get(), scheduler)) {


                nextPeriod += super.getActionPeriod();

                world.removeEntity(this);
                Quake quake = new Quake(Quake.QUAKE_ID, oldPos, imageStore.getImageList(Quake.QUAKE_KEY),
                        Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);



                world.addEntity(quake);
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new activityAction(this, world, imageStore),
                nextPeriod);
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
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

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
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
}
