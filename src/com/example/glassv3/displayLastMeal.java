package com.example.glassv3;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.example.glassv3.CameraActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
	private Card card;
	
	
	public displayLastMeal(Context context){
		
		mtimelineManager = TimelineManager.from(context);
		//count = CameraActivity.totalCount(placeName);
		//count--;
		this.context = context;
		mLivecard = mtimelineManager.createLiveCard(LIVE_CARD_ID);
		card = new Card(context);
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
            	//view.setTextViewText(R.id.textView1, "No Previous Pic");
            }
            else{
            view.setImageViewBitmap(R.id.imageView1, CameraActivity.decodeSampledBitmapFromResource(filePath, 600, 300));
            view.setImageViewResource(R.id.imageView2, R.drawable.dinner_data);
            //view.setTextViewText(R.id.textView1, "CGM 76");
            /*
            card.setText("This card has a puppy background image.");
            card.setFootnote("How can you resist?");
            card.setImageLayout(Card.ImageLayout.FULL);
            card.addImage(Uri.parse("https://webpos.wlinformation.com/zonicorderingbr/clients/0133_rave-burger-san-mateo/images/misc/others/rave_burger_5.jpg"));
            mtimelineManager.insert(card);
            */
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