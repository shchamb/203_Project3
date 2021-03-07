import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Caterpie extends movingEntity {
    public static String CATERPIE_KEY = "caterpie";
    public static final String CATERPIE_ID_SUFFIX = " -- caterpie";
    public static final int CATERPIE_PERIOD_SCALE = 4;
    public static final int CATERPIE_ANIMATION_MIN = 50;
    public static final int CATERPIE_ANIMATION_MAX = 150;
    Random rand = new Random();
    public Point dest;

    private List<Point> path;

    public Caterpie(String id, Point position, Point dest, List path, List<PImage> images, int actionPeriod, int animationPeriod, WorldModel world) {
        super(id, position, images, actionPeriod, animationPeriod);


        this.dest = dest;
        //if empty path given, make the path from scratch
        if (path.size() == 0){
        PathingStrategy strategy = new depthFirstSearch();

        this.path = strategy.computePath(this.getPosition(), dest,
                p -> world.withinBounds(p),
                PathingStrategy.CARDINAL_NEIGHBORS);}
        //we just take the old path and make it ours
        else{
            this.path = path;
        }


    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Point dest = world.findNearest(this.getPosition(), ho_oh.class).get().getPosition();
        System.out.println(dest);
        long nextPeriod = super.getActionPeriod();







        if (this.path.size() != 0) {
            System.out.println(this.path);
            Point nextPos = this.path.get(0);
            this.path.remove(0); //Were moving here, we remove this position
//            if (world.isOccupied(nextPos)) {
//
//                Optional<Entity> maybePiplup = world.getOccupant(nextPos);
//                if(!(maybePiplup.equals(Optional.empty()))){
//                    if(maybePiplup.get() instanceof PiplupCalm){
////                        System.out.println("panic");
////                this.panic(world, scheduler);
//                        ((PiplupCalm) maybePiplup.get()).panic = true;
//
//                    }
//                }
//
//                scheduler.unscheduleAllEvents(this);
//                world.removeEntity(this);
//            }



                Caterpie c = new Caterpie("caterpie", nextPos, this.dest,
                        this.path,
                        imageStore.getImageList("caterpie"),
                        700, 10,
                        world);
                Point oldPos = this.getPosition();
                world.removeEntity(this);
                world.addEntity(c);





                scheduler.scheduleEvent(c,
                        new activityAction(c, world, imageStore),
                        nextPeriod);


        }

        else{
            scheduler.unscheduleAllEvents(this);

            world.removeEntity(this);

//            if(rand.nextInt(15) < 1){
//                Caterpie cat = new Caterpie("caterpie", dest, imageStore.getImageList("caterpie"), 700, 10);
//                world.addEntity(cat);
//                System.out.println("caterpie");
//                cat.scheduleActions(scheduler, world, imageStore);
//            }


        }
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
