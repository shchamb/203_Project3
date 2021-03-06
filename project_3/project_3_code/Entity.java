

import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


abstract class Entity
{
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;






   public Entity(String id, Point position,
                 List<PImage> images)
   {

      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;

   }
   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

//   public abstract void executeActivity(WorldModel world,
//                                        ImageStore imageStore, EventScheduler scheduler);

//   public abstract void scheduleActions(EventScheduler scheduler,
//                                        WorldModel world, ImageStore imageStore);

   public PImage getCurrentImage()
   {
      return this.images.get(this.imageIndex);

   }

   public String getId() {
      return id;
   }

   public Point getPosition() {
      return position;
   }

   public void setPosition(Point position) {
      this.position = position;
   }

   public List<PImage> getImages() {
      return images;
   }

   public int getImageIndex() {
      return imageIndex;
   }

   public void setImageIndex(int x){
      imageIndex = x;
   }



















}
