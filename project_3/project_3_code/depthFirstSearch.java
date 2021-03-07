import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class depthFirstSearch implements PathingStrategy {
    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Point currPos = start;
        LinkedList<Point> path = new LinkedList<>();
        LinkedList<Point> searched = new LinkedList<>();


        System.out.println(end);
        //main loop
        while(true){
            LinkedList<Point> directions = new LinkedList<>();

            List<Point> possible_directions = potentialNeighbors
                    .apply(currPos)
                    .filter(canPassThrough)
                    .collect(Collectors.toList()); // gives us possible neighbors to visit

            //make sure the only directions we want have not been searched yet
            for (Point p : possible_directions){
                if (!searched.contains(p)){
                    directions.add(p);
                }

            }
//            System.out.println(currPos);
//            directions = directions.stream().filter(p -> !searched.contains(p)).collect(Collectors.toList()); // make sure

            //if there is no where we can go
            if (directions.size() == 0){
                int lastIndex = path.size() - 1;
                path.remove(lastIndex);
                currPos = path.get(lastIndex - 1); //get last element from java path
                continue; //and we try again from previous point
            }

            currPos = directions.get(0);

            searched.add(currPos); //we've moved, we are adding it to searched
            path.add(currPos); //we are adding it to path
            System.out.println("curr: " + currPos + " End: " + end);
            if (currPos.x == end.x && currPos.y == end.y){
                System.out.println("end");
                break;
            } //we've found our path


        }

        System.out.println(path);

        return path;






    }




}
