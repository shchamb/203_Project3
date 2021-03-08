import processing.core.PImage;

import java.util.List;

public class charmanderFactory implements entityFactory{

    public Charmander createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();

        int actionPeriod = 5;
        int animationPeriod = 6;
        WorldModel world = VirtualWorld.getWorld();
        List<PImage> images = imageStore.getImageList(Charmander.CHARMY_KEY);
        return new Charmander("charmy", position, images, actionPeriod, animationPeriod, world);
    }

}
