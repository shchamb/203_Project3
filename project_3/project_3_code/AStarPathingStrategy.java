import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;

class AStarPathingStrategy
        implements PathingStrategy
{

    public PriorityQueue<Node> frontier;
    public HashMap<Point, Integer> closed;

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough1,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        this.frontier = new PriorityQueue<Node>();
        this.closed = new HashMap<Point, Integer>();

        Node.setGoal(end); // gives us our end goal
        Node current = new Node(start, null, 0); // initial set up of node

        update_frontier(current, canPassThrough1, potentialNeighbors);

        while (current.h != 0){
//            System.out.println(current);
            closed.put(current.pos, 0);
            current = this.frontier.poll();
            if (current == null){
                return new LinkedList<>(); // return an empty list if we can't reach goal
            }
            update_frontier(current, canPassThrough1, potentialNeighbors);
             //so we can't go back to current
        }

        current.path.remove(current.path.size()-1);
        return current.path;






    }

    public void update_frontier(Node node, Predicate<Point> canPassThrough, Function<Point, Stream<Point>> possible_neighbors){
//        System.out.println(node);
        List<Point> potential_neighbors = possible_neighbors
                .apply(node.pos)
//                .filter(canPassThrough)
                .collect(Collectors.toList());



        for (Point neighbor: potential_neighbors){
            if (this.closed.containsKey(neighbor)){
                continue;
            }
            this.frontier.add(new Node(neighbor, node, node.g+1)); //Adding data to frontier
        }
    }



}
