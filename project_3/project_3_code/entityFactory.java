import processing.core.PImage;
import java.util.LinkedList;
import java.util.List;

public class entityFactory {

    public static Charmander createCharmander(Point position, ImageStore images, WorldModel world){

        int actionPeriod = 5;
        int animationPeriod = 6;
        return new Charmander("charmy", position, images.getImageList(Charmander.CHARMY_KEY), actionPeriod, animationPeriod, world);
    }

    public static Fire createFire(Point position, Point dest, ImageStore imageStore){
        String id = "fire";
        List<PImage> images = imageStore.getImageList(Fire.FIRE_KEY);

        return new Fire(id, position, dest, images);
    }

    public static PiplupCalm createPiplupCalm(Point position, ImageStore imageStore){
        String id = "PiplupCalm";
        int actionPeriod = 992;
        int animationPeriod = 100;
        List<PImage> images = imageStore.getImageList(Octo.OCTO_KEY);
        return new PiplupCalm(id, position, images, 3, 0, actionPeriod, animationPeriod);


    }

    public static PiplupPanic createPiplupPanic(Point position, ImageStore imageStore){
        String id = "PiplupPanic";
        int actionPeriod = 124;
        int animationPeriod = 25;
        List<PImage> images = imageStore.getImageList("octoFull");
        return new PiplupPanic(id, position, images, 3, 0, actionPeriod, animationPeriod);

    }

    public static Caterpie createCaterpie(Point position, Point dest, List<Point> path, ImageStore imageStore, WorldModel world){
        String id = "caterpie";
        List<PImage> images = imageStore.getImageList("caterpie");
        int actionPeriod = 700;
        int animationPeriod = 10;

        return new Caterpie(id, position, dest, path, images, actionPeriod, animationPeriod, world);

    }

    public static Obstacle createObstacle(Point position, ImageStore imageStore){
        String id = "obstacle";
        List<PImage> images = imageStore.getImageList(Obstacle.OBSTACLE_KEY);

        return new Obstacle(id, position, images);
    }

    public static ho_oh createHo_oh(Point position, ImageStore imageStore){
        String id = "ho_oh";
        List<PImage> images = imageStore.getImageList(ho_oh.HO_OH_KEY);

        return new ho_oh(id, position, images);


    }



}
