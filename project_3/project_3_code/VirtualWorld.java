import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 1280;
   public static final int VIEW_HEIGHT = 960;
   public static final int TILE_WIDTH = 64;
   public static final int TILE_HEIGHT = 64;
   public static final int WORLD_WIDTH_SCALE = 2;
   public static final int WORLD_HEIGHT_SCALE = 2;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   public static final String IMAGE_LIST_FILE_NAME = "project_3/imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "project_3/world.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public long next_time;

   public static final Random rand = new Random();

   public static Charmander charmy;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }
//      System.out.println("Row: " + view.getViewport().getRow());
//      System.out.println("Col: " + view.getViewport().getCol());
      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         Point pos = charmy.getPosition();
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               Point up = new Point(pos.x, pos.y - 1);
               if(charmy.move(charmy.world, pos, up)){
                  dy = -1;
               }
               break;
            case DOWN:
               Point down = new Point(pos.x, pos.y + 1);
               if(charmy.move(charmy.world, pos,down)) {
                  dy = 1;
               }
               break;
            case LEFT:
               Point left = new Point(pos.x - 1, pos.y);
               charmy.setImageIndex(1);
               if(charmy.move(charmy.world, pos,left)) {
                  dx = -1;
                  charmy.setDirection("left");
               }
               break;
            case RIGHT:
               Point right = new Point(pos.x + 1, pos.y);
               charmy.setImageIndex(0);
               if(charmy.move(charmy.world, pos,right)) {
                  dx = 1;
                  charmy.setDirection("right");
               }
               break;
         }
         view.shiftView(dx, dy);
      }
   }

   public void mouseClicked(){
      Point pos = charmy.getPosition();
//      System.out.println(pos);
//      System.out.println(mouseX);
//      System.out.println(mouseY);
      charmy.makeFire(imageStore,  view.getViewport().getCol() + (mouseX / 64), view.getViewport().getRow() + (mouseY / 64));
//      charmy.makeFire(imageStore, mouseX, mouseY);
//      System.out.println("clicked");
   }



   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         //we only want entities that are active, otherwise we don't care (stupid obstacles
         if (entity instanceof activeEntity){
//            System.out.println(entity.getId());
            if (((activeEntity) entity).getActionPeriod() > 0)
               ((activeEntity)entity).scheduleActions(scheduler, world, imageStore);
         }
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
