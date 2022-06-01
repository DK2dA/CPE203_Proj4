public class Activity implements Action{
    public ActivityEntity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Activity(ActivityEntity entity, WorldModel world,
                    ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        executeActivityAction(scheduler);
    }

    private void executeActivityAction(EventScheduler scheduler) {
        this.entity.executeActivity(this.world, this.imageStore, scheduler);
    }

}