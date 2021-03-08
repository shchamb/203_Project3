import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.stream.Stream;

public class depthFirstSearch implements PathingStrategy {
    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Point pos = start;
        List<Point> path = new ArrayList<>();
        List<Point> searched = new ArrayList<>();
        boolean goalFound = false;

        while(true){

            path.add(0, pos);
            searched.add(0, pos);

            List<Point> possible_directions = potentialNeighbors
                    .apply(pos)
                    .filter(canPassThrough)
                    .collect(Collectors.toList()); // gives us possible neighbors to visit
            boolean stuck = true;
            List<Point> new_possible_directions = new ArrayList(possible_directions);

            for(Point p: possible_directions){
                for(Point d: searched){
                    if(p.equals(d)){
                        new_possible_directions.remove(p);
                    }
                }
            }
            for(Point p: new_possible_directions){
                    searched.add(p);
                    pos = p;
                    stuck = false;
                    break;
            }
            if(pos.adjacent(end)){
                path.add(0, pos);
                break;
            }
            if(stuck){
                path.remove(0);
                pos = path.get(0);
                path.remove(0);
            }
        }

        Collections.reverse(path);
        path.remove(0);
        return path;
    }
}
