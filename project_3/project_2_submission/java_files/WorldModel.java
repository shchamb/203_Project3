import processing.core.PImage;

import java.util.*;

import static java.lang.Integer.parseInt;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{

   private final int numRows;
   private final int numCols;
   private final Background background[][];
   private final Entity occupancy[][];
   private final Set<Entity> entities;

   public static final int PROPERTY_KEY = 0;

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }


   public Set<Entity> getEntities() {
      return entities;
   }



   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<Entity>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }


   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -Fish.FISH_REACH; dy <= Fish.FISH_REACH; dy++)
      {
         for (int dx = -Fish.FISH_REACH; dx <= Fish.FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }


   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   private boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case Background.BGND_KEY:
               return parseBackground(properties, imageStore);
            case Octo.OCTO_KEY:
               return parseOcto(properties, imageStore);
            case Obstacle.OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case Fish.FISH_KEY:
               return parseFish(properties, imageStore);
            case Atlantis.ATLANTIS_KEY:
               return parseAtlantis(properties, imageStore);
            case Sgrass.SGRASS_KEY:
               return parseSgrass(properties, imageStore);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Background.BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
                 Integer.parseInt(properties[Background.BGND_ROW]));
         String id = properties[Background.BGND_ID];
         setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == Background.BGND_NUM_PROPERTIES;
   }

   private boolean parseOcto(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Octo.OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Octo.OCTO_COL]),
                 Integer.parseInt(properties[Octo.OCTO_ROW]));


         octoNotFull entity = new octoNotFull(properties[Octo.OCTO_ID],
                 pt,
                 imageStore.getImageList(Octo.OCTO_KEY),Integer.parseInt(properties[Octo.OCTO_LIMIT]), 0,
              Integer.parseInt(properties[Octo.OCTO_ACTION_PERIOD]), Integer.parseInt(properties[Octo.OCTO_ANIMATION_PERIOD]));



         tryAddEntity(entity);
      }

      return properties.length == Octo.OCTO_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
                 Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));


         Obstacle entity = new Obstacle(properties[Obstacle.OBSTACLE_ID],
                 pt, imageStore.getImageList(Obstacle.OBSTACLE_KEY));
         tryAddEntity(entity);
      }

      return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Fish.FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Fish.FISH_COL]),
                 Integer.parseInt(properties[Fish.FISH_ROW]));


         Fish entity = new Fish(properties[Fish.FISH_ID],pt,imageStore.getImageList(Fish.FISH_KEY),
                 Integer.parseInt(properties[Fish.FISH_ACTION_PERIOD]));

         tryAddEntity(entity);
      }

      return properties.length == Fish.FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Atlantis.ATLANTIS_COL]),
                 Integer.parseInt(properties[Atlantis.ATLANTIS_ROW]));




         Atlantis entity = new Atlantis(properties[Atlantis.ATLANTIS_ID],
                 pt, imageStore.getImageList(Atlantis.ATLANTIS_KEY), 0, 0);




         tryAddEntity(entity);
      }

      return properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Sgrass.SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Sgrass.SGRASS_COL]),
                 Integer.parseInt(properties[Sgrass.SGRASS_ROW]));


         Sgrass entity = new Sgrass(properties[Sgrass.SGRASS_ID],
                 pt,imageStore.getImageList(Sgrass.SGRASS_KEY), Integer.parseInt(properties[Sgrass.SGRASS_ACTION_PERIOD]), 0);


         tryAddEntity(entity);
      }

      return properties.length == Sgrass.SGRASS_NUM_PROPERTIES;
   }


   private void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   private boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }





   private Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }



   public Optional<Entity> findNearest(Point pos,
                                       Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
//
         if (entity.getClass() == kind)
         {

            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }
   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Entity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   private void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         setOccupancyCell( pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   private void setBackground(Point pos,
                                    Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(getOccupancyCell( pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   private Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.y][pos.x];
   }

   private void setOccupancyCell(Point pos,
                                       Entity entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return this.background[pos.y][pos.x];
   }

   private void setBackgroundCell(Point pos,
                                        Background background)
   {
      this.background[pos.y][pos.x] = background;
   }


}
