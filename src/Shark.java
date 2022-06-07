import processing.core.PImage;

import java.util.List;

public abstract class Shark extends AnimationEntity{
    private int resourceLimit;
    private PathingStrategy strategy;

    public Shark(String id, Point position,
                 List<PImage> images, int resourceLimit, int actionPeriod,
                 int animationPeriod, PathingStrategy pStrat)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.strategy = pStrat;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public Point nextPositionOcto(WorldModel world,
                                  Point destPos) {
        List<Point> points;
        points = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (points.size() != 0)
            return points.get(0);
        return getPosition();
    }

    abstract protected boolean moveTo(WorldModel world,
                                      Entity target, EventScheduler scheduler);

    abstract protected boolean transform(WorldModel world,
                                 EventScheduler scheduler, ImageStore imageStore);

}
