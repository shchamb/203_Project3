import java.util.List;
import processing.core.PImage;
import java.util.*;

final class Background
{
   private final String id;
   private final List<PImage> images;
   private int imageIndex;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   public int getImageIndex() {
      return imageIndex;
   }
   public String getId() {
      return id;
   }
   public List<PImage> getImages() {
      return images;
   }



   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }
   public PImage getCurrentImage()
   {

      return this.getImages()
              .get(this.getImageIndex());

   }
   public static void melt(WorldModel world, Point pos, ImageStore imageStore){
      Point above = new Point(pos.x, pos.y-1);
      Point below = new Point(pos.x, pos.y+1);
      Point dUp = new Point(pos.x+1, pos.y-1);
      Point dDown = new Point(pos.x+1, pos.y+1);
      Point front = new Point(pos.x+1, pos.y);
      if(world.getOccupant(pos).get() instanceof Fire || world.getOccupant(pos).get() instanceof Caterpie){
         world.setBackground(pos, new Background("grass", imageStore.getImageList("grass")));
//         world.setBackground(above, new Background("grass", imageStore.getImageList("grass")));
//         world.setBackground(below, new Background("grass", imageStore.getImageList("grass")));
//         world.setBackground(dUp, new Background("grass", imageStore.getImageList("grass")));
//         world.setBackground(dDown, new Background("grass", imageStore.getImageList("grass")));
         world.setBackground(front, new Background("grass", imageStore.getImageList("grass")));
      }else if(world.getOccupant(pos).get() instanceof PiplupCalm){
         world.setBackground(pos, new Background("sea", imageStore.getImageList("sea")));
      }
   }


}
