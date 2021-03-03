public class animationAction extends Action{
    private int repeatCount;

    animationAction(Entity entity, int repeatCount){
        super(entity);
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.getEntity().nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.getEntity(),
                    new animationAction(this.getEntity(),
                            Math.max(this.repeatCount - 1, 0)),
                    ((animatedEntity)this.getEntity()).getAnimationPeriod());
        }
}

//    public int getRepeatCount() {return repeatCount;}
}
