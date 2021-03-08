import processing.core.PImage;

import java.util.List;

public class fireFactory implements entityFactory{
    public Fire createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();

        String id = "fire";
        List<PImage> images = imageStore.getImageList(Fire.FIRE_KEY);
        return new Fire(id, position, images);
    }
}
