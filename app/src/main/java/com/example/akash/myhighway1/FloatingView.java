package com.example.akash.myhighway1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Vishal on 5/12/2018.
 */

public class FloatingView extends Service {
    private WindowManager mWindowManager;
    private View mView;
    public FloatingView(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mView= LayoutInflater.from(this).inflate(R.layout.floatingicon,null);
        final WindowManager.LayoutParams params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity= Gravity.TOP|Gravity.LEFT;   //adding initial status of widget
        params.x=0;
        params.y=100;
        mWindowManager=(WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mView,params);


        TextView closeButton=(TextView) mView.findViewById(R.id.close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopSelf();
                }
            });
        mView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            int lastAction;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //remeber the initial location
                        initialX=params.x;
                        initialY=params.y;
                        //get the touch location
                        initialTouchX=motionEvent.getRawX();
                        initialTouchY=motionEvent.getRawY();
                        lastAction=motionEvent.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff= (int)(motionEvent.getRawX()-initialTouchX);
                        int Ydiff= (int)(motionEvent.getRawY()-initialTouchY);
                        if(lastAction==MotionEvent.ACTION_DOWN){
                            //click on widget event
                            Toast.makeText(FloatingView.this,"Clicked",Toast.LENGTH_SHORT).show();
                            /*Intent intent=new Intent(FloatingView.this,ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/
                        }
                        lastAction=motionEvent.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x= initialX+(int)(motionEvent.getRawX()-initialTouchX);
                        params.y= initialY+(int)(motionEvent.getRawY()-initialTouchY);
                        mWindowManager.updateViewLayout(mView,params);
                        lastAction=motionEvent.getAction();
                        return true;
                }
                return false;
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mView!=null) mWindowManager.removeView(mView);
    }
}
