import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PiplupCalm extends Octo{

    Random rand = new Random();
    public boolean panic = false;

    public PiplupCalm(String id, Point position, List<PImage> images, int resourceLimit,
                      int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position,images,  resourceLimit, resourceCount, actionPeriod, animationPeriod);


    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
//                Fish.class);
        Point target = new Point(rand.nextInt(40), rand.nextInt(30));
//
        if (!this.moveToPoint(world, target, scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore)) {


            scheduler.scheduleEvent(this,
                    new activityAction(this, world, imageStore),
                    this.getActionPeriod());
        }

    }




    public boolean moveToPoint(WorldModel world,
                          Point target, EventScheduler scheduler)
    {

        Optional<Entity> maybeFireLeft = world.getOccupant(new Point(this.getPosition().x + 1, this.getPosition().y));
        Optional<Entity> maybeFireRight = world.getOccupant(new Point(this.getPosition().x - 1, this.getPosition().y));
        if(!(maybeFireLeft.equals(Optional.empty()))){
            if(maybeFireLeft.get() instanceof Fire){
                System.out.println("panic");
//                this.panic(world, scheduler);
                panic = true;
                return true;
            }


        }
        if(!(maybeFireRight.equals(Optional.empty()))){
            if(maybeFireRight.get() instanceof Fire){
                System.out.println("panic");
                panic = true;
//                this.panic(world, scheduler);
                return true;
            }


        }
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

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.panic == true)
        {


            Octo octo = new PiplupPanic(this.getId(), this.getPosition(), imageStore.getImageList("octoFull"),  this.getResourceLimit(), 0,
                    this.getActionPeriod() / 8, this.getAnimationPeriod() / 4);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public Point nextPosition(WorldModel world,
                              Point destPos)
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













    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler)
    {

        return false;
    }

}
