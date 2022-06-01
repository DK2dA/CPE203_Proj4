import processing.core.PImage;

import java.util.List;

public abstract class Octo extends AnimationEntity{
    private int resourceLimit;

    public int getResourceLimit() {
        return resourceLimit;
    }

    public Octo(String id, Point position,
                    List<PImage> images, int resourceLimit, int actionPeriod,
                    int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public Point nextPositionOcto(WorldModel world,
                                  Point destPos) {
        int horiz = Integer.signum(destPos.x - super.getPosition().x);
        Point newPos = new Point(super.getPosition().x + horiz,
                super.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - super.getPosition().y);
            newPos = new Point(super.getPosition().x,
                    super.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    abstract protected boolean moveTo(WorldModel world,
                                      Entity target, EventScheduler scheduler);

    abstract protected boolean transform(WorldModel world,
                                 EventScheduler scheduler, ImageStore imageStore);

}
