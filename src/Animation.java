public class Animation implements Action{
    public AnimationEntity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Animation(AnimationEntity entity, WorldModel world,
                     ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        executeAnimationAction(scheduler);
    }

    private void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    this.entity.createAnimationAction(
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }

}