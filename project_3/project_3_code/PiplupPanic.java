import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PiplupPanic extends Octo{

    Random rand = new Random();

    public PiplupPanic(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images,resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    public void executeActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler){

            Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                    Atlantis.class);
        Point target = new Point(rand.nextInt(40), rand.nextInt(30));

            if (
                    this.moveToPoint( world, target, scheduler))
            {
                //at atlantis trigger animation
//                ((activeEntity) fullTarget.get()).scheduleActions(scheduler, world, imageStore);

                //transform to unfull
                this.calmDown(world, scheduler, imageStore);
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
            return false;
        }

    public boolean moveToPoint(WorldModel world, Point target, EventScheduler scheduler){
        System.out.println("moving");
        if (this.getPosition().adjacent(target))
        {

            return true;
        }
        else
        {
            Point nextPos = nextPosition( world, target);

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

    private void calmDown(WorldModel world,
                          EventScheduler scheduler, ImageStore imageStore)
    {



        Octo octo = new PiplupCalm(this.getId(), this.getPosition(), imageStore.getImageList("octo"), this.getResourceLimit(), 0,
                this.getActionPeriod() * 8, this.getAnimationPeriod() * 4);

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
