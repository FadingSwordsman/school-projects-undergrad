package com.putable.frobworld.locd011;

public interface Placeable
{
    public void setLocation(int x, int y);
    public int[] getLocation();
    public PlaceType getType();
}
