import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PiplupCalm extends Octo{

    Random rand = new Random();
    public boolean panic = false;

    public PiplupCalm(String id, Point position, List<PImage> images, int resourceLimit,
                      int resourceCount, int actionPeriod, int animationPeriod)
    {
        super(id, position,images,  resourceLimit, resourceCount, actionPeriod, animationPeriod);


    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
//                Fish.class);
        Point target = null;
        if(this.lookForFire(world)){
            panic = true;
        }
        Optional<Point> grassTarget = world.findNearestGrass(this.getPosition());
        if(!grassTarget.equals(Optional.empty())){
            target = grassTarget.get();
        }else{
            target = new Point(rand.nextInt(40), rand.nextInt(30));
        }
//
        if (!this.moveToPoint(world, target, scheduler, imageStore) ||
                !this.panic(world, scheduler, imageStore)) {


            scheduler.scheduleEvent(this,
                    new activityAction(this, world, imageStore),
                    this.getActionPeriod());
        }

    }




    public boolean moveToPoint(WorldModel world,
                          Point target, EventScheduler scheduler, ImageStore imageStore)
    {





        if(this.lookForFire(world)){
            panic = true;
            return true;
        }
        if (this.getPosition().equals(target))
        {
            Background.melt(world, this.getPosition(), imageStore);

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

    private boolean panic(WorldModel world,
                          EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.panic == true)
        {


            Octo octo = new piplupPanicFactory().createEntity(this.getPosition());
//                    entityFactory.createPiplupPanic(this.getPosition());



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

//        return newPos;
    }


    public boolean lookForFire(WorldModel world){
        List<Point> points = new ArrayList<>();
        points.add(new Point(this.getPosition().x + 1, this.getPosition().y));
        points.add(new Point(this.getPosition().x - 1, this.getPosition().y));
        points.add(new Point(this.getPosition().x - 1, this.getPosition().y -1));
        points.add(new Point(this.getPosition().x + 1, this.getPosition().y +1));
        points.add(new Point(this.getPosition().x - 1, this.getPosition().y +1));
        points.add(new Point(this.getPosition().x + 1, this.getPosition().y -1));
        points.add(new Point(this.getPosition().x, this.getPosition().y -1));
        points.add(new Point(this.getPosition().x, this.getPosition().y +1));

        points.add(new Point(this.getPosition().x + 2, this.getPosition().y));
        points.add(new Point(this.getPosition().x - 2, this.getPosition().y));
        points.add(new Point(this.getPosition().x - 2, this.getPosition().y -2));
        points.add(new Point(this.getPosition().x + 2, this.getPosition().y +2));
        points.add(new Point(this.getPosition().x - 2, this.getPosition().y +2));
        points.add(new Point(this.getPosition().x + 2, this.getPosition().y -2));
        points.add(new Point(this.getPosition().x, this.getPosition().y -2));
        points.add(new Point(this.getPosition().x, this.getPosition().y +2));

        for(Point p: points){
            Optional<Entity> maybeFire = world.getOccupant(p);
            if(!(maybeFire.equals(Optional.empty()))){
                if(maybeFire.get() instanceof Fire){
                    System.out.println("panic");
//                this.panic(world, scheduler);
//                    panic = true;
                    return true;
                }
            }

        }
        return false;


    }











    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler)
    {

        return false;
    }

}
