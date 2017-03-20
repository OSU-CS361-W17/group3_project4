package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    private MilShip aircraftCarrier = new MilShip("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0),false);
    private MilShip battleship = new MilShip("Battleship",4, new Coordinate(0,0),new Coordinate(0,0),true);
    private CivShip clipper = new CivShip("clipper",3, new Coordinate(0,0),new Coordinate(0,0));
    private CivShip dinghy = new CivShip("dinghy",1, new Coordinate(0,0),new Coordinate(0,0));
    private MilShip submarine = new MilShip("Submarine",2, new Coordinate(0,0),new Coordinate(0,0), true);

    private MilShip computer_aircraftCarrier = new MilShip("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7), false);
    private MilShip computer_battleship = new MilShip("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8), true);
    private CivShip computer_clipper = new CivShip("Computer_clipper",3, new Coordinate(4,1),new Coordinate(4,3));
    private CivShip computer_dinghy = new CivShip("Computer_dinghy",1, new Coordinate(7,3),new Coordinate(7,3));
    private MilShip computer_submarine = new MilShip("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8),true);

    ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;

    boolean scanResult = false;
    boolean scanRequest = false;
    int hardAI = 0;


    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("clipper")) {
        return clipper;
        } if(shipName.equalsIgnoreCase("dinghy")) {
            return dinghy;
        }if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public BattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        int rowint = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        this.getShip(shipName).setShipPlaced();
        if(orientation.equals("horizontal")){
            if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+5));
            } if(shipName.equalsIgnoreCase("battleship")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+4));
            } if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+2));
            } if(shipName.equalsIgnoreCase("dinghy")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt));
            }if(shipName.equalsIgnoreCase("submarine")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt + 2));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+5,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("clipper")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+2,colInt));
                } if(shipName.equalsIgnoreCase("dinghy")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt));
                }if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 2, colInt));
                }
        }
        this.scanRequest = false;
        return this;
    }

    public void shootAtComputer(int row, int col) {
        Coordinate coor = new Coordinate(row,col);
        if(computer_aircraftCarrier.covers(coor)){
            computerHits.add(coor);
        }else if (computer_battleship.covers(coor)){
            computerHits.add(coor);
        }else if (computer_clipper.covers(coor)){
            sinkComputerCivShip(computer_clipper);
        }else if (computer_dinghy.covers(coor)){
            computerHits.add(coor);
        }else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
        } else {
            computerMisses.add(coor);
        }
        this.scanRequest = false;
    }
    //splits based on AI level
    public void shootAtPlayer() {
        if(hardAI==1){
            hardShot();
        } else {
            ezShot();
        }
    }
    public void ezShot(){
        int max = 10;
        int min = 1;
        Coordinate coor = new Coordinate(0, 0);
        do {
            Random random = new Random();
            int randRow = random.nextInt(max - min + 1) + min;
            int randCol = random.nextInt(max - min + 1) + min;
            coor = new Coordinate(randRow, randCol);
        }while(!goodShot(coor));

        playerShot(coor);
        return;
    }
    //loops through all known hits and wraps them in shots such that all hits will be surrounded by hits or misses. Prioritizes lines of hits. Randomizes if all hits are wrapped.
    public void hardShot(){
        int max = 10;
        int min = 1;
        Coordinate fireOn = new Coordinate(0,0);
        Coordinate coor = new Coordinate(0,0);
        for(int ctr = 0; ctr<playerHits.size(); ctr++){
            fireOn = hitTile(playerHits.get(ctr), false);//tells hitTile to only return good coordinate if it's on a line of hits
            if(fireOn.getAcross()!=0 && fireOn.getDown()!=0) {
                playerShot(fireOn);
                return;
            }
        }
        for(int ctr = 0; ctr<playerHits.size(); ctr++){
            fireOn = hitTile(playerHits.get(ctr), true);//tells hitTile to return a coordinate next to a hit even if it's not on a line of hits (because if there are any hitlinesthis block isn't reached)
            if(fireOn.getAcross()!=0 && fireOn.getDown()!=0) {
                playerShot(fireOn);
                return;
            }
        }

        do {//Gives up on being smart and starts guessing
            Random random = new Random();
            int randRow = random.nextInt(max - min + 1) + min;
            int randCol = random.nextInt(max - min + 1) + min;
            coor = new Coordinate(randRow, randCol);
        }while(!goodShot(coor));//generates coordinates until it gets one it hasnt shot at yet
            playerShot(coor);
            return;
    }
    //When desperate, hitTile returns a coordinate adjacent to a hit, or {0,0} if none available. When not desperate, returns a coordinate on a line of hits, or {0,0} if no hitlines are available.
    public Coordinate hitTile(Coordinate toCheck, boolean isDesperate){
        Coordinate toRet = new Coordinate(0,0);
        Coordinate tmpCoord = new Coordinate(toCheck);
        for(int ctr1 = -1; ctr1<2; ctr1++){
            for(int ctr2 = -1; ctr2<2; ctr2++){
                if((ctr1==0 || ctr2==0)){//avoids diagonal displacement
                    tmpCoord.setAcross(toCheck.getAcross()+ctr1);
                    tmpCoord.setDown(toCheck.getDown()+ctr2);
                    if(goodShot(tmpCoord)&&hitLine(tmpCoord)){//checking if square has already been fired upon and if it coincides with a line of hits
                        toRet.setAcross(tmpCoord.getAcross());
                        toRet.setDown(tmpCoord.getDown());
                        return toRet;
                    } else if(goodShot(tmpCoord) && isDesperate){//when desperate, shoots at any goodShot() regardless of whether it's a hitline
                        toRet.setAcross(tmpCoord.getAcross());
                        toRet.setDown(tmpCoord.getDown());
                        return toRet;
                    }
                }
            }

        }
        return toRet;//When desperate, returning {0,0} indicating toCheck is wrapped. When not desperate, returning {0,0} indicating toCheck is not on a hitline
    }
    //loops through playerHits and playerMisses checking if toCheck has been fired upon yet
    public boolean goodShot(Coordinate toCheck){
        if(toCheck.getAcross()<1 || toCheck.getAcross()>10)
            return false;
        if(toCheck.getDown()<1 || toCheck.getDown()>10)
            return false;
        for(int ctr = 0; ctr<playerHits.size(); ctr++){
            if(playerHits.get(ctr).getAcross() == toCheck.getAcross() && playerHits.get(ctr).getDown() == toCheck.getDown())
                return false;
        }
        for(int ctr = 0; ctr<playerMisses.size(); ctr++){
            if(playerMisses.get(ctr).getAcross() == toCheck.getAcross() && playerMisses.get(ctr).getDown() == toCheck.getDown())
                return false;
        }
        return true;
    }
    //returns true if toCheck would continue a line of 2 or more hits
    public boolean hitLine(Coordinate toCheck){
        Coordinate loopCoor;
        Coordinate tmpCoor;
        for(int ctr = 0; ctr<playerHits.size(); ctr++){
            loopCoor = playerHits.get(ctr);

            if(loopCoor.getAcross()==toCheck.getAcross() && loopCoor.getDown()==toCheck.getDown()-1) {
                tmpCoor = new Coordinate(toCheck.getAcross(), toCheck.getDown()-2);
                if(isHit(tmpCoor)) {
                    return true;
                }
            }else if(loopCoor.getAcross()==toCheck.getAcross() && loopCoor.getDown()==toCheck.getDown()+1){
                tmpCoor = new Coordinate(toCheck.getAcross(), toCheck.getDown()+2);
                if(isHit(tmpCoor)) {
                    return true;
                }
            }else if(loopCoor.getDown()==toCheck.getDown() && loopCoor.getAcross()==toCheck.getAcross()-1){
                tmpCoor = new Coordinate(toCheck.getAcross()-2, toCheck.getDown());
                if(isHit(tmpCoor)) {
                    return true;
                }
            }else if(loopCoor.getDown()==toCheck.getDown() && loopCoor.getAcross()==toCheck.getAcross()+1){
                tmpCoor = new Coordinate(toCheck.getAcross()+2, toCheck.getDown());
                if(isHit(tmpCoor)) {
                    return true;
                }
            }
        }

        System.out.println("returning false");
        return false;
    }
    //couldn't get contains() working with ArrayList<Coordinate>(), so I built my own
    public boolean isHit(Coordinate coor){
        Coordinate tmpCoor;
        for(int ctr = 0; ctr<playerHits.size(); ctr++){
            tmpCoor = playerHits.get(ctr);
            if(tmpCoor.getAcross()==coor.getAcross() && tmpCoor.getDown()==coor.getDown())
                return true;
        }
        return false;
    }

    void playerShot(Coordinate coor) {
        if(playerMisses.contains(coor) || playerHits.contains(coor)){
            System.out.println("Dupe");
        }

        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
        }else if (battleship.covers(coor)){
            playerHits.add(coor);
        }else if (clipper.covers(coor)){
            sinkPlayerCivShip(clipper);
        }else if (dinghy.covers(coor)){
            playerHits.add(coor);
        }else if (submarine.covers(coor)){
            playerHits.add(coor);
        } else {
            playerMisses.add(coor);
        }
    }


    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt,colInt);
        scanResult = false;
        if(computer_aircraftCarrier.scan(coor)){
            scanResult = true;
        }
        else if (computer_battleship.scan(coor)){
            scanResult = true;
        }else if (computer_clipper.scan(coor)){
            scanResult = true;
        }else if (computer_dinghy.scan(coor)){
            scanResult = true;
        }else if (computer_submarine.scan(coor)){
            scanResult = true;
        } else {
            scanResult = false;
        }
        scanRequest = true;
    }



    public void sinkComputerCivShip (CivShip ship) {
        int across = ship.start.getAcross();
        int down = ship.start.getDown();
        if (ship.start.getAcross() != ship.end.getAcross()) {
            for (int i =0; i < ship.length; i++) {
                Coordinate hitCoor = new Coordinate(across + i, down);
                computerHits.add(hitCoor);
            }
        }
        else {
            for (int i = 0; i < ship.length; i++) {
                Coordinate hitCoor = new Coordinate(across , down + i);
                computerHits.add(hitCoor);
            }
        }
    }



    public void sinkPlayerCivShip (CivShip ship) {
        int across = ship.start.getAcross();
        int down = ship.start.getDown();
        if (ship.start.getAcross() != ship.end.getAcross()) {
            for (int i =0; i < ship.length; i++) {
                Coordinate hitCoor = new Coordinate(across + i, down);
                playerHits.add(hitCoor);
            }
        }
        else {
            for (int i = 0; i < ship.length; i++) {
                Coordinate hitCoor = new Coordinate(across , down + i);
                playerHits.add(hitCoor);
            }
        }
    }

    public boolean getScanResult() {
        return scanResult;
    }
}