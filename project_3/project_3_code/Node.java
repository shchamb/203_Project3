import java.util.LinkedList;
import java.util.Objects;

public class Node implements Comparable<Node>{


    @Override
    public String toString() {
        return "Node{" +
                "path=" + path +
                ", g=" + g +
                ", h=" + h +
                ", f=" + f +
                ", pos=" + pos +
                '}';
    }

    public LinkedList<Point> path;
    public static Point goal;

    public int g; //so other variable can get to it
    public int h;
    public int f;
    public Point pos;


    Node(Point pos, Node prev, int g){



    this.pos = pos;
    this.g = g;
    this.h = dist_from_goal(pos);
    this.f = g + h;

    if (prev == null){
        this.path = new LinkedList<>();
    }
    else{
        this.path = (LinkedList<Point>) prev.path.clone();
        this.path.add(pos);
    }


    }

    public static int dist_from_goal(Point pos){

        int distance = Math.abs(goal.x - pos.x) + Math.abs(goal.y - pos.y);
        return distance;
    }


    //set only on the first node
    public static void setGoal(Point goal) {
        Node.goal = goal;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(pos, node.pos);
    }

    @Override
    public int hashCode() {
        //the only thing defining a hash code is this
        return Objects.hash(pos);
    }

    public int compareTo(Node other){
        return this.f - other.f;
    }

    //set only for first node
    public void setg() {
        this.g = 0;
    }


}
