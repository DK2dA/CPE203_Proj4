public class Node {
    private Point point;
    private int g_cost;
    private double h_cost;
    private double f_cost;
    private Node prior;

    public Node(Point pt, int g, double h, Node pr) {
        this.point = pt;
        this.g_cost = g;
        this.h_cost = h;
        this.f_cost = g + h;
        this.prior = pr;
    }

    public int compareTo (Node o) {
        return Double.compare(this.f_cost, o.f_cost);
    }

    public Point getPoint() { return this.point; }
    public int getG_cost() {
        return g_cost;
    }
    public double getH_cost() {
        return h_cost;
    }
    public double getF_cost() { return f_cost; }
    public Node getPrior() {
        return prior;
    }
}
