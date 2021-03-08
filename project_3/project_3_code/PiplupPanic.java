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

            Point target = new Point(rand.nextInt(40), rand.nextInt(30));


            if (
                    this.moveToPoint( world, target, scheduler, imageStore))
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

    public boolean moveToPoint(WorldModel world, Point target, EventScheduler scheduler, ImageStore imageStore){
//        System.out.println("moving");

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



        Octo octo = new piplupCalmFactory().createEntity(this.getPosition());
//                entityFactory.createPiplupCalm(this.getPosition());


        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }


    //original as used by octo
    public Point nextPosition(WorldModel world, Point destPos)
    {

        SingleStepPathingStrategy s = new SingleStepPathingStrategy();
        List<Point> newPos = s.computePath(this.getPosition(), destPos, p -> !world.isOccupied(p), PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);

        if(newPos.toArray().length > 0){
            return newPos.get(0);
        }
        return this.getPosition();

//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x,
//                    this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
    }




}
