import processing.core.PImage;

import java.util.List;

public class caterpieFactory implements entityFactory{

    public Caterpie createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();

        String id = "caterpie";
        List<PImage> images = imageStore.getImageList("caterpie");
        WorldModel world = VirtualWorld.getWorld();
        int actionPeriod = 700;
        int animationPeriod = 10;
        return new Caterpie(id, position, images, actionPeriod, animationPeriod, world);

    }
}
