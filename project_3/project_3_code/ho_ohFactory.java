import processing.core.PImage;

import java.util.List;

public class ho_ohFactory implements entityFactory{

    public ho_oh createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();
        String id = "ho_oh";
        List<PImage> images = imageStore.getImageList(ho_oh.HO_OH_KEY);

        return new ho_oh(id, position, images);

    }

}
