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
            case Charmander.CHARMY_KEY:
               return parseCharmander(properties, imageStore);
            case Fire.FIRE_KEY:
               return parseFire(properties, imageStore);
            case ho_oh.HO_OH_KEY:
               return parseHO_OH(properties, imageStore);
         }
      }

      return false;
   }



   public boolean parseCharmander(String[] properties, ImageStore imageStore) {
      System.out.println(properties.length);
      if (properties.length == Charmander.CHARMY_NUM_PROPERTIES)
      {
         System.out.println("char");
         Point pt = new Point(Integer.parseInt(properties[Charmander.CHARMY_COL]),
                 Integer.parseInt(properties[Charmander.CHARMY_ROW]));

         Charmander entity = (Charmander) new charmanderFactory().createEntity(pt);
         this.tryAddEntity(entity);
      }

      return properties.length == Charmander.CHARMY_NUM_PROPERTIES;
   }

   public boolean parseFire(String[] properties, ImageStore imageStore) {
      if (properties.length == Fire.FIRE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Charmander.CHARMY_COL]),
                 Integer.parseInt(properties[Charmander.CHARMY_ROW]));

         Fire entity = (Fire) new fireFactory().createEntity(pt);

         entity.setDest(pt);

//
         this.tryAddEntity(entity);
      }

      return properties.length == Fire.FIRE_NUM_PROPERTIES;
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


         PiplupCalm entity = (PiplupCalm) new piplupCalmFactory().createEntity(pt);




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


         Obstacle entity = (Obstacle) new obstacleFactory().createEntity(pt);

         tryAddEntity(entity);
      }

      return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseHO_OH(String [] properties, ImageStore imageStore)
   {
      if (properties.length == ho_oh.HO_OH_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[ho_oh.HO_OH_COL]),
                 Integer.parseInt(properties[ho_oh.HO_OH_ROW]));


         ho_oh entity = (ho_oh) new ho_ohFactory().createEntity(pt);


         tryAddEntity(entity);
      }

      return properties.length == ho_oh.HO_OH_NUM_PROPERTIES;
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

   public boolean withinBounds(Point pos)
   {

      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public boolean isOccupied(Point pos)
   {
      System.out.println(pos);

      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public boolean isObstacle(Point pos){
      boolean ob = false;
      Optional<Entity> maybeOb = getOccupant(pos);
      if(!maybeOb.equals(Optional.empty())){
         if(maybeOb.get() instanceof Obstacle){
            ob = true;
         }
      }

      return withinBounds(pos) && ob;
   }

   public boolean isOccupiedByMoveable(Point pos){
      boolean mo = false;
      Optional<Entity> maybeOb = getOccupant(pos);
      if(!maybeOb.equals(Optional.empty())){
         if(maybeOb.get() instanceof movingEntity || maybeOb.get() instanceof Charmander ){
            mo = true;
         }
      }
      return mo;
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

   public Optional<Point> findNearestGrass(Point pos){
//      List<Background> ofType = new LinkedList<>();
//      for(Background[] b: background){
//         for(Background p: b){
//            if(p.getId() == "grass"){
//               ofType.add(p);
//            }
//         }
//      }
//      return nearestBackground(ofType, pos);
      List<Point> ofType = new LinkedList<>();
      for(int i = 0; i < numCols; i++){
         for(int j = 0; j < numRows; j++){
            Point p = new Point(i, j);
            Background b = getBackgroundCell(p);
            if(b.getId() == "grass"){
               ofType.add(p);
            }
         }
      }
      return nearestBackground(ofType, pos);

   }

   public Optional<Point> nearestBackground(List<Point> b, Point pos){
      if(b.isEmpty()){
         return Optional.empty();
      }
      Point nearest = b.get(0);
      int nearestDistance = nearest.distanceSquared(pos);

      for (Point other : b)
      {
         int otherDistance = other.distanceSquared(pos);

         if (otherDistance < nearestDistance)
         {
            nearest = other;
            nearestDistance = otherDistance;
         }
      }

      return Optional.of(nearest);

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
         if(entity instanceof Charmander){
            VirtualWorld.charmy = (Charmander) entity;
         }
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

   public void removeEntityAt(Point pos)
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

   public void setBackground(Point pos,
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

   public void setOccupancyCell(Point pos,
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
