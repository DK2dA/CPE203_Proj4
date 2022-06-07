import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SharkFull extends Shark{

    public SharkFull(String id, Point position,
                     List<PImage> images, int resourceLimit,
                     int actionPeriod, int animationPeriod, PathingStrategy strat)
    {
        super(id, position, images, resourceLimit, actionPeriod,
                animationPeriod, strat);
    }

    public void executeActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), Atlantis.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((Atlantis) fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            this.transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(WorldModel world,
                              Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionOcto(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
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
        OctoNotFull octo = WorldModel.createOctoNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
        return false;
    }
}
