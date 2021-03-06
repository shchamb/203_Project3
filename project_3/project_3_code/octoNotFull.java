import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class octoNotFull extends Octo{

    Random rand = new Random();

    public octoNotFull(String id, Point position, List<PImage> images, int resourceLimit,
                       int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position,images,  resourceLimit, resourceCount, actionPeriod, animationPeriod);


    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new activityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }


    public boolean moveTo(WorldModel world,
                                  Entity target, EventScheduler scheduler)
    {

        Optional<Entity> maybeFireLeft = world.getOccupant(new Point(this.getPosition().x + 1, this.getPosition().y));
        Optional<Entity> maybeFireRight = world.getOccupant(new Point(this.getPosition().x - 1, this.getPosition().y));
        if(!(maybeFireLeft.equals(Optional.empty()))){
            if(maybeFireLeft.get() instanceof Fire){
                System.out.println("panic");
                this.panic(world, scheduler);
                return false;
            }


        }
        if(!(maybeFireRight.equals(Optional.empty()))){
            if(maybeFireRight.get() instanceof Fire){
                System.out.println("panic");
                this.panic(world, scheduler);
                return false;
            }


        }
        if (this.getPosition().adjacent(target.getPosition()))
        {

            this.incrementResourceCount();
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPosition( world, target.getPosition());

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
        if (this.getResourceCount() >= this.getResourceLimit())
        {


            Octo octo = new octoFullActivity(this.getId(), this.getPosition(), this.getImages(),  this.getResourceLimit(), 0,
                    this.getActionPeriod(), this.getAnimationPeriod());

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

    public boolean panic(WorldModel world, EventScheduler scheduler){
        int oldActionPeriod = this.getActionPeriod();
        int oldAnimationPeriod = this.getAnimationPeriod();
        this.setAnimationPeriod(this.getAnimationPeriod() / 2);
        this.setActionPeriod(this.getActionPeriod() / 2);
        Point destPos = new Point(rand.nextInt( 40), rand.nextInt( 40));
        Point pos = this.nextPosition(world, destPos);
        if (this.getPosition().adjacent(destPos))
        {
            this.setActionPeriod(oldActionPeriod);
            this.setAnimationPeriod(oldAnimationPeriod);
            return true;
        }
        else if (!this.getPosition().equals(pos))
        {
            Optional<Entity> occupant = world.getOccupant(pos);
            if (occupant.isPresent())
            {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, pos);
        }
        return false;

    }


}
