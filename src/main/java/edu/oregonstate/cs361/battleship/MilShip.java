package edu.oregonstate.cs361.battleship;

/**
 * Created by casters on 2/24/17.
 */
public class MilShip extends Ship{

    public MilShip(String n, int l,Coordinate s, Coordinate e, boolean isSneaky) {
        super(n,l,s,e);
        stealth = isSneaky;
    }
}
