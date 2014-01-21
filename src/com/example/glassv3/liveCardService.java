package com.example.glassv3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.example.glassv3.model.Landmarks;
import com.google.android.glass.app.Card;
import com.example.glassv3.model.Place;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

//import android.R;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.RecyclerTraceType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class liveCardService extends Service implements LocationListener{
	private static final String TAG = "livecardService";
	private static final String LIVE_CARD_ID = "livecard";
	
	private TimelineManager mtimelineManager;
	private LiveCard mLivecard;

	Context context;
	private LocationManager locationManager;
	private Landmarks mLandmarks;
	public static String recognizedPlace;
	//private returnPlaceName placeName;
	public displayLastMeal last;
	
	public void onCreate(){
		super.onCreate();
		mtimelineManager = TimelineManager.from(this);
		context = getBaseContext();
		Log.d("livecardService", "oncreta LiveCard");
	locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	mLandmarks = new Landmarks(this);
	//placeName = new returnPlaceName(this);
	last = new displayLastMeal(this);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
        if (mLivecard == null) {
            Log.d(TAG, "Publishing LiveCard");
            mLivecard = mtimelineManager.createLiveCard(LIVE_CARD_ID);
          ;
            mLivecard.setViews(new RemoteViews(this.getPackageName(),
                    R.layout.livecard));
  
            
            Intent intent1 = new Intent(this, MainActivity.class);
  
            mLivecard.setAction(PendingIntent.getActivity(this,0, intent1,0));
     
            mLivecard.publish(LiveCard.PublishMode.SILENT);
     
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //criteria.setAltitudeRequired();

            String provider = locationManager.getBestProvider(criteria, true);
            Log.d(TAG, provider);
        	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);

            Log.d(TAG, provider);
            //

            List<String> providers = locationManager.getAllProviders();
            for (String provider1 : providers) {
                if (locationManager.isProviderEnabled(provider1)) {
                    Log.d(TAG, "Provider"+provider1);
                }
            }
            locationManager.requestLocationUpdates(provider, 10000, 0, this);
        }
        else {
        	//card is published
        	if(mLivecard.isPublished())
        	Log.d(TAG, " LiveCard present");
  
        	/*
        	RemoteViews mRemoteView = new RemoteViews(this.getPackageName(),
                    R.layout.livecard1);
        	
        	mLivecard.setViews(mRemoteView);
        	
        */
        }
		return START_STICKY;
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onDestroy(){
	    if (mLivecard != null) {
	        mLivecard.unpublish();
	        mLivecard = null;
	        Log.d(TAG, "UnPublishing LiveCard");
	       
	    }
	    

	    locationManager.removeUpdates(this);
	    mLandmarks = null;
	    last.destroyMealPics();
	    super.onDestroy();
	}
	
	


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Location"+location.toString());
        List<Place> places = mLandmarks.getNearbyLandmarks(
                location.getLatitude(), location.getLongitude());
        recognizedPlace = null;
        
        for (Place place : places) {
            
                Log.d(TAG, "Place"+place.getName());
                recognizedPlace = place.getName();
               
            
        }
       if(recognizedPlace!=null){
    	   if(recognizedPlace.equals("TSRB")){
    		   RemoteViews mRemoteView = new RemoteViews(this.getPackageName(),
                   R.layout.livecard1);    	
       	mLivecard.setViews(mRemoteView);
    	   }
    	   else if(recognizedPlace.equals("Starbucks")){
    		   RemoteViews mRemoteView = new RemoteViews(this.getPackageName(),
    	                R.layout.livecard2);    	
    	    	mLivecard.setViews(mRemoteView);
    	    	
    	   }
    	   
       }
       else 
   		{RemoteViews mRemoteView = new RemoteViews(this.getPackageName(),
                R.layout.livecard3);
		
    	
    	mLivecard.setViews(mRemoteView);
    	recognizedPlace = "Unrecognized";
       }

       last.initializeData(recognizedPlace);
       last.showLastMealPic();
		//placeName.setPlaceName(recognizedPlace);
		//Log.d(TAG, "placename"+placeName.getPlaceName());
	}

  
	public static String getRecognisedPlace(){
		return recognizedPlace;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method return
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
