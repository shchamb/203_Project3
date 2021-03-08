import processing.core.PImage;

import java.util.List;

public class obstacleFactory implements entityFactory{

    public Obstacle createEntity(Point position){
        ImageStore imageStore = VirtualWorld.getImageStore();
        String id = "obstacle";
        List<PImage> images = imageStore.getImageList(Obstacle.OBSTACLE_KEY);

        return new Obstacle(id, position, images);
    }

}
