import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SharkNotFull extends Shark{
    private int resourceCount;

    public SharkNotFull(String id, Point position,
                        List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod, PathingStrategy strat)
    {
        super(id, position, images, resourceLimit, actionPeriod,
                animationPeriod, strat);
        this.resourceCount = resourceCount;
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {
        executeOctoNotFullActivity(world, imageStore,scheduler);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0), this.getAnimationPeriod());
    }

    public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = super.nextPositionOcto(world, target.getPosition());

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public boolean transform(WorldModel world,
                                EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= super.getResourceLimit())
        {
            OctoFull octo = WorldModel.createOctoFull(super.getId(), super.getResourceLimit(),
                    super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
                    super.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
