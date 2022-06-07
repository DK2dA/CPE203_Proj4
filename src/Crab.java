import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Crab extends AnimationEntity{

    //Quake stuff
    private static final String QUAKE_KEY = "quake";
    private PathingStrategy strategy;

    public Crab(String id, Point position,
                  List<PImage> images, int actionPeriod,
                int animationPeriod, PathingStrategy pStrat)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.strategy = pStrat;
    }

    public Point nextPositionCrab(WorldModel world,
                                  Point destPos)
    {
        List<Point> points;
        points = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (points.size() != 0)
            return points.get(0);
        return getPosition();
    }

    public boolean moveToCrab(WorldModel world,
                              Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionCrab(world, target.getPosition());

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

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(
                this.getPosition(), Sgrass.class);
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveToCrab(world, crabTarget.get(), scheduler))
            {
                Quake quake = WorldModel.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }
}
