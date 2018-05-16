package com.missouristate.davis916.beefindsfinger;

/**
 * Laura Davis CIS 262-902
 * 19 April 2018
 * This class creates the bee object
 */

public class Bee {
    private int mX;
    private int mY;
    private int mVelocity;

    public void setVelocity(int velocity){
        mVelocity = velocity;
    }

    public int getVelocity(){
        return mVelocity;
    }

    public void setX(int x){
        mX = x;
    }

    public void setY(int y){
        mY = y;
    }

    public int getX(){
        return mX;
    }

    public int getY(){
        return mY;
    }

    public void move(int destinationX, int destinationY){
        int distX = destinationX - mX;
        int distY = destinationY - mY;
        mX += distX / mVelocity;
        mY += distY / mVelocity;
    }

}//end Bee class
