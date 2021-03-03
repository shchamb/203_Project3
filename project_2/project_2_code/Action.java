/*
Action: ideally what our various entities might do in our virutal world
 */

abstract class Action
{
   private Entity entity;
   protected Entity getEntity() {
      return entity;
   }

   public Action(Entity entity)
   {
      this.entity = entity;
   }


   public abstract void executeAction(EventScheduler scheduler);




   }






