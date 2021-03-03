public class activityAction extends Action{


    private WorldModel world;
    private ImageStore imageStore;

    activityAction(Entity entity, WorldModel world, ImageStore imageStore){
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        ((activeEntity)this.getEntity()).executeActivity(this.world,
                this.imageStore, scheduler);
    }
}
