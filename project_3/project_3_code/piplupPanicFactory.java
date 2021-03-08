import processing.core.PImage;

import java.util.List;

public class piplupPanicFactory implements entityFactory{

    public PiplupPanic createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();

        String id = "PiplupPanic";
        int actionPeriod = 124;
        int animationPeriod = 25;
        List<PImage> images = imageStore.getImageList("octoFull");
        return new PiplupPanic(id, position, images, 3, 0, actionPeriod, animationPeriod);

    }
}
