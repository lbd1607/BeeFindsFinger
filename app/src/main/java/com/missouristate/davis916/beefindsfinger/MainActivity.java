package com.missouristate.davis916.beefindsfinger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Laura Davis CIS 262-902
 * 19 April 2018
 * This program demonstrates basic touch gestures.
 * The bee senses where the screen is being touched
 * and follows the touch until the user lifts their
 * finger from the screen and the bee returns to
 * its starting position on the flower.
 *
 */

public class MainActivity extends Activity {
    //Activity work is split into two threads:
    //Calculating bee movement - Background
    //Positioning the bee - UI Thread
    private Thread calculateThread;

    private ConstraintLayout mainLayout;
    private ImageView beeImageView;
    private ImageView flowerImageView;

    private Flower mFlower;
    private Bee mBee;

    private int xLocation;
    private int yLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Window properties are set with no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set the layout view
        setContentView(R.layout.activity_main);
        mainLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        //Instantiate flower and bee
        xLocation = 200;
        yLocation = 200;
        addFlower();
        buildBee();

        //Instantiate background thread
        calculateThread = new Thread(calculateAction);
    }//end onCreate

    private void addFlower(){
        //Create layout inflater
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Specify flower position
        int initialXPosition = xLocation;
        int initialYPosition = yLocation;

        mFlower = new Flower();
        mFlower.setX(initialXPosition);
        mFlower.setY(initialYPosition);

        //Add the flower
        flowerImageView = (ImageView)layoutInflater.inflate(R.layout.flower_image, null);
        flowerImageView.setX((float) mFlower.getX());
        flowerImageView.setY((float) mFlower.getY() + 50);
        mainLayout.addView(flowerImageView, 0);
    }//end addFlower()

    private void buildBee(){
        //Create a layout inflater to add visual views to the layout
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Specify the bee attributes
        int initialXPosition = xLocation;
        int initialYPosition = yLocation;
        int proportionalVelocity = 10;
        mBee = new Bee();
        mBee.setX(initialXPosition);
        mBee.setY(initialYPosition);
        mBee.setVelocity(proportionalVelocity);

        //Add the bee
        beeImageView = (ImageView) layoutInflater.inflate(R.layout.bee_image, null);
        beeImageView.setX((float) mBee.getX());
        beeImageView.setY((float) mBee.getY());
        mainLayout.addView(beeImageView, 0);
    }//end buildBee()

    @Override
    protected void onResume(){
        calculateThread.start();
        super.onResume();
    }//end onResume()

    @Override
    protected void onPause(){
        finish();
        super.onPause();
    }//end onPause()

    @Override
    protected void onDestroy(){
        finish();
        super.onDestroy();
    }//end onDestroy()

    //******************************** RUNNABLE ****************************************
    private Runnable calculateAction = new Runnable() {
        private static final int DELAY = 200;

        @Override
        public void run() {
            try {
                while (true) {
                    mBee.move(xLocation, yLocation);
                    Thread.sleep(DELAY);
                    threadHandler.sendEmptyMessage(0);
                }
            }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }//end run()

        };//end Runnable

        //*** Handler for updating between delays ***
        public Handler threadHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                //Set the bee at the correct x location
                beeImageView.setX((float) mBee.getX());
                beeImageView.setY((float) mBee.getY());
        }//end handleMessage

    };//end Handler

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Identify the touch action being performed
        int touchAction = event.getActionMasked();

        //Respond to possible touch events
        switch (touchAction) {
            //Bee finds a motionless finger
            case MotionEvent.ACTION_DOWN:
                xLocation = (int) event.getX();
                yLocation = (int) event.getY();
                break;
            //Bee returns to the flower when the finger is removed
            case MotionEvent.ACTION_UP:
                xLocation = mFlower.getX();
                yLocation = mFlower.getY();
                break;
            //Bee follows a moving finger
            case MotionEvent.ACTION_MOVE:
                xLocation = (int) event.getX();
                yLocation = (int) event.getY();
                break;
        }
        
        //Returns a true after handling the touch action event
        return true;
    }//end onTouchEvent()

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }//end createOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button,
        //as long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

}//end MainActivity class
