import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class octoFullActivity  extends Octo{


    public octoFullActivity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images,resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    public void executeActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler){

            Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                    Atlantis.class);

            if (fullTarget.isPresent() &&
                    this.moveTo( world, fullTarget.get(), scheduler))
            {
                //at atlantis trigger animation
                ((activeEntity) fullTarget.get()).scheduleActions(scheduler, world, imageStore);

                //transform to unfull
                this.transformFull(world, scheduler, imageStore);
            }
            else
            {
                scheduler.scheduleEvent(this,
                        new activityAction(this, world, imageStore),
                        this.getActionPeriod());
            }
    }

        public boolean moveTo(WorldModel world,
                Entity target, EventScheduler scheduler)
        {
            if (this.getPosition().adjacent(target.getPosition()))
            {
                return true;
            }
            else
            {
                Point nextPos = nextPosition(world, target.getPosition());

                if (!this.getPosition().equals(nextPos))
                {
                    Optional<Entity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent())
                    {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }

                    world.moveEntity(this, nextPos);
                }
                return false;
            }
        }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {



        Octo octo = new octoNotFull(this.getId(), this.getPosition(), this.getImages(), this.getResourceLimit(), 0,
                this.getActionPeriod(), this.getAnimationPeriod());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }


    //original as used by octo
    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x,
                    this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

}
