import processing.core.PImage;
import processing.core.*;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;

import java.util.List;
import java.util.Optional;

public class Charmander extends Entity{
    public WorldModel world;
    public int actionPeriod;
    public int animationPeriod;
    public static final int CHARMY_NUM_PROPERTIES = 7;
    public static final int CHARMY_LIMIT = 4;
    public static final String CHARMY_KEY = "charmy";
    public static final int CHARMY_COL = 2;
    public static final int CHARMY_ROW = 3;
    public static final int CHARMY_ACTION_PERIOD = 5;
    public static final int CHARMY_ANIMATION_PERIOD = 6;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String direction;




    public Charmander(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, WorldModel world) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.world = world;
        this.direction = "right";
    }



    public boolean move(WorldModel world, Point oldPos, Point pos){
        if (world.withinBounds(pos) && !pos.equals(oldPos) && !world.isOccupied(pos)){
            world.setOccupancyCell(oldPos, null);
            world.removeEntityAt(pos);
            world.setOccupancyCell(pos, this);
            this.setPosition(pos);
            return true;
        }
        return false;

    }

//
//public void keyPressed(KeyEvent e){
//        Point pos = this.getPosition();
//        switch( e.getKeyCode() ) {
//            case KeyEvent.VK_UP:
//                Point up = new Point(pos.x, pos.y - 1);
//                move(this.world, pos, up);
//                System.out.println("up");
//                break;
//            case KeyEvent.VK_DOWN:
//                Point down = new Point(pos.x, pos.y + 1);
//                move(world, pos,down);
//                break;
//            case KeyEvent.VK_LEFT:
//                Point left = new Point(pos.x - 1, pos.y);
//                move(world, pos,left);
//                break;
//            case KeyEvent.VK_RIGHT :
//                Point right = new Point(pos.x + 1, pos.y);
//                move(world, pos,right);
//                break;
//        }
//    }
    public void makeFire(ImageStore imageStore, int dirX, int dirY){
        if(this.direction == "right"){
//            Fire f = new Fire("fire", new Point(this.getPosition().x+1, this.getPosition().y + dirY), imageStore.getImageList(Fire.FIRE_KEY), 0, 0);
//            Fire f = new Fire("fire",new Point(this.getPosition().x + 1, this.getPosition().y) , new Point(dirX, dirY), imageStore.getImageList(Fire.FIRE_KEY));
            System.out.println("Charmander: " + ' ' + new Point(this.getPosition().x + 1, this.getPosition().y));
            System.out.println("Mouse Click: " + ' ' + new Point(dirX, dirY));
            Fire f = new Fire("fire", new Point(this.getPosition().x + 1, this.getPosition().y), new Point(dirX, dirY), imageStore.getImageList(Fire.FIRE_KEY));
                f.beFire(world, imageStore);
//            Background.melt(world, new Point(dirX, dirY), imageStore);
            }else{
//            Fire f = new Fire("fire", new Point(this.getPosition().x-1, this.getPosition().y + dirY), imageStore.getImageList(Fire.FIRE_KEY), 0, 0);
                Fire f = new Fire("fire", new Point(this.getPosition().x - 1, this.getPosition().y), new Point(dirX, dirY), imageStore.getImageList(Fire.FIRE_KEY));
                f.setImageIndex(1);
                f.beFire(world, imageStore);
//                Background.melt(world, new Point(dirX, dirY), imageStore);
            }
        }



    }




