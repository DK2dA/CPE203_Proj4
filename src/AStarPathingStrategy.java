import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();

        // define closed list
        Map<Point, Node> closed = new HashMap();

        //  define open list
        Comparator<Node> fCmp = Comparator.comparing(Node :: getF_cost);
        PriorityQueue<Node> open = new PriorityQueue<>(fCmp);
        open.add(new Node(start, 0, 0, null));

        while (!open.isEmpty()) {
            //  Filtered list containing neighbors you can actually move to
            Node cNode = open.poll();

            List<Point> neighbors = potentialNeighbors.apply(cNode.getPoint())
                    .filter(canPassThrough).filter(p -> !closed.containsKey(p))
                    .collect(Collectors.toList());
            for (Point p : neighbors) {
                Node nNode = new Node(p, cNode.getG_cost() + 1,
                        calcH(end, p), cNode);
                List<Point> oPts = open.stream().map(n -> n.getPoint()).collect(Collectors.toList());
                if (!oPts.contains(p)) {
                    open.add(nNode);
                }
            }

            //  Check if any of the neighbors are beside the target

            if (cNode.getPoint().adjacent(end)) {
                if (cNode.getPrior() != null) {
                    path.add(0, cNode.getPoint());

                    Node pNode = cNode.getPrior();
                    while (pNode.getPrior() != null) {
                        path.add(0, pNode.getPoint());
                        pNode = pNode.getPrior();
                    }
                }
                break;
            }

            // add the selected node to close list
            closed.put(cNode.getPoint(), cNode);
        }
        // return path
        return path;
    }

    private static double calcH(Point p1, Point p2) {
        return (p2.x - p1.x) + (p2.y - p1.y);
    }
}
