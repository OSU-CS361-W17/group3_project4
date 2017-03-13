package edu.oregonstate.cs361.battleship;

/**
 * Created by michaelhilton on 1/5/17.
 */
public class Ship {
    public String name;
    public int length;
    public Coordinate start;
    public Coordinate end;
    public boolean stealth = false;
    public boolean ezTarget = false;
    public boolean isPlaced = false;

    public Ship(){
        name = "unnamed";
        length = 0;
        start = new Coordinate(0,0);
        end = new Coordinate(0,0);
    }

    public Ship(String n, int l,Coordinate s, Coordinate e) {
        name = n;
        length = l;
        start = s;
        end = e;
    }


    public void setLocation(Coordinate s, Coordinate e) {
        start = s;
        end = e;

    }

    public boolean covers(Coordinate test) {
        //horizontal
        if(start.getAcross() == end.getAcross()){
            if(test.getAcross() == start.getAcross()){
                if((test.getDown() >= start.getDown()) &&
                (test.getDown() <= end.getDown()))
                return true;
            } else {
                return false;
            }
        }
        //vertical
        else{
            if(test.getDown() == start.getDown()){
                if((test.getAcross() >= start.getAcross()) &&
                        (test.getAcross() <= end.getAcross()))
                    return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public String getName() {
        return name;
    }


    public boolean scan(Coordinate coor) {
        if(!this.stealth) {
            if (covers(coor)) {
                return true;
            }
            if (covers(new Coordinate(coor.getAcross() - 1, coor.getDown()))) {
                return true;
            }
            if (covers(new Coordinate(coor.getAcross() + 1, coor.getDown()))) {
                return true;
            }
            if (covers(new Coordinate(coor.getAcross(), coor.getDown() - 1))) {
                return true;
            }
            if (covers(new Coordinate(coor.getAcross(), coor.getDown() + 1))) {
                return true;
            }
        }
        return false;
    }

    public void setShipPlaced() {isPlaced = true;}

}
