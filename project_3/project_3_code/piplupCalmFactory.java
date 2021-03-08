import processing.core.PImage;

import java.util.List;

public class piplupCalmFactory implements entityFactory{

    public PiplupCalm createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();
        String id = "PiplupCalm";
        int actionPeriod = 992;
        int animationPeriod = 100;
        List<PImage> images = imageStore.getImageList(Octo.OCTO_KEY);
        return new PiplupCalm(id, position, images, 3, 0, actionPeriod, animationPeriod);

    }
}
