package com.example.glassv3;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.example.glassv3.CameraActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class displayLastMeal {
	private String placeName;
	private static final String LIVE_CARD_ID = "lastMeal";
	private static int count = 0;
	private Context context;
	private String filePath;
	private TimelineManager mtimelineManager;
	private static LiveCard mLivecard;
	 
	
	public displayLastMeal(Context context){
		
		mtimelineManager = TimelineManager.from(context);
		//count = CameraActivity.totalCount(placeName);
		//count--;
		this.context = context;
		mLivecard = mtimelineManager.createLiveCard(LIVE_CARD_ID);
	}

	public void showLastMealPic(){
	
			
            Log.d("livecardService", "Publishing Last Meal");
            //mLivecard = mtimelineManager.getLiveCard(LIVE_CARD_ID);
            //ImageView jpgView = findViewById(R.id.imageView1);

            filePath = "/mnt/sdcard/Pictures/newPictures/"+placeName+"/test_"+count+".jpg";
            Log.d("livecardService", "Filepath"+filePath);
            RemoteViews view = new RemoteViews(context.getPackageName(),
                    R.layout.activity_compass);
            if(count==-1){
            	view.setTextViewText(R.id.textView1, "No Previous Pic");
            }
            else{
            view.setImageViewBitmap(R.id.imageView1, CameraActivity.decodeSampledBitmapFromResource(filePath, 600, 300));
            view.setTextViewText(R.id.textView1, "CGM 76");
            }
            mLivecard.setViews(view);
            
           
             //jpgView.setImageBitmap(
           		    //CameraActivity.decodeSampledBitmapFromResource(filePath, 600, 300));
  
            
            Intent intent1 = new Intent(context, MainActivity.class);
  
            mLivecard.setAction(PendingIntent.getActivity(context,0, intent1,0));
            if(!mLivecard.isPublished())
            	mLivecard.publish(LiveCard.PublishMode.SILENT);
	}

				

	public static void destroyMealPics(){
		if(mLivecard!=null){
	        mLivecard.unpublish();
	        mLivecard = null;
	        Log.d("livecardService", "Remove meal pic");
			
		}
	}

	public void initializeData(String recognizedPlace) {
		// TODO Auto-generated method stub
		this.placeName = recognizedPlace;
		count = CameraActivity.totalCount(placeName);
		count--;
	}
}