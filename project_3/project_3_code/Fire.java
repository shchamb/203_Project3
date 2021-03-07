import processing.core.PImage;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import java.util.List;

public class Fire extends activeEntity{

    public static final int FIRE_NUM_PROPERTIES = 3;
    public static final int FIRE_ID = 1;
    public static final String FIRE_KEY = "fire";
    public static final int FIRE_ACTION_PERIOD = 2;
    public static final int FIRE_ANIMATION_PERIOD = 3;
    public int actionPeriod = 11;
    public Point dest;
    Random rand = new Random();

    private PathingStrategy strategy = new AStarPathingStrategy();
//    public int animationPeriod = 10;
//    public Point pos;


    public Fire(String id, Point position, Point dest, List<PImage> images) {
        super(id, position, images, 11);
        this.dest = dest;
//        this.pos = pos;
    }

    public void beFire(WorldModel world, ImageStore imageStore){
        world.addEntity(this);
        Background.melt(world, new Point(this.getPosition().x, this.getPosition().y), imageStore);

//        Point pt = this.getPosition();
//        String id = "grass";
//        world.setBackground(pt,
//                new Background(id, imageStore.getImageList(id)));
        //wait?
//        world.removeEntity(this);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        System.out.println("I'm fire");
        long nextPeriod = super.getActionPeriod();

        List<Point> path = strategy.computePath(this.getPosition(), this.dest,
               p -> world.isOccupied(p),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() != 0) {
            System.out.println(path);
            Point nextPos = path.get(0);

            if (world.isOccupied(nextPos)) {

                Optional<Entity> maybePiplup = world.getOccupant(nextPos);
                if(!(maybePiplup.equals(Optional.empty()))){
                    if(maybePiplup.get() instanceof PiplupCalm){
//                        System.out.println("panic");
//                this.panic(world, scheduler);
                        ((PiplupCalm) maybePiplup.get()).panic = true;

                    }
                }

                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
            }


            else {
                Fire f = new Fire("fire", nextPos, this.dest, imageStore.getImageList(Fire.FIRE_KEY));
                Point oldPos = this.getPosition();
                world.removeEntity(this);
                f.beFire(world, imageStore);
//                if(rand.nextInt(10) < 1){
//                    Caterpie cat = new Caterpie("caterpie", nextPos, imageStore.getImageList("caterpie"));
//                    world.addEntity(cat);
//                    System.out.println("caterpie");
//                }



                scheduler.scheduleEvent(f,
                        new activityAction(f, world, imageStore),
                        nextPeriod);
                if(rand.nextInt(15) < 1){
                    Point ho_oh_dest = world.findNearest(this.getPosition(), ho_oh.class).get().getPosition();
                    Caterpie cat = new Caterpie("caterpie", oldPos, ho_oh_dest,
                            new LinkedList<>(),
                            imageStore.getImageList("caterpie"),
                            700, 10,
                            world);
                    world.addEntity(cat);
                    System.out.println("caterpie");
                    cat.scheduleActions(scheduler, world, imageStore);
                }
            }
        }

        else{
            scheduler.unscheduleAllEvents(this);

            world.removeEntity(this);

            if(rand.nextInt(15) < 1){
                Point ho_oh_dest = world.findNearest(this.getPosition(), ho_oh.class).get().getPosition();

                Caterpie cat = new Caterpie("caterpie", this.dest, ho_oh_dest, new LinkedList(),
                        imageStore.getImageList("caterpie"),
                        700, 10,
                        world);
                world.addEntity(cat);
                System.out.println("caterpie");
                cat.scheduleActions(scheduler, world, imageStore);
            }


        }





    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){

        scheduler.scheduleEvent(this,
                new activityAction(this, world, imageStore),
                this.actionPeriod);

    }





}
