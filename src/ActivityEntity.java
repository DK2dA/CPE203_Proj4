import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity{
    private int actionPeriod;

    public ActivityEntity(String id, Point position,
                    List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public Action createActivityAction(WorldModel world,
                                       ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }

    public int getActionPeriod() {
        return this.actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    abstract protected void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler);
}
