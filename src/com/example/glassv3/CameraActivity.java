package com.example.glassv3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.example.glassv3.R;
import com.example.glassv3.liveCardService;
import com.example.glassv3.model.Place;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class CameraActivity extends Activity{
	private static final int IMG_REQUEST = 3;
	File mediaStorage = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES);
	FileObserver obs;
	Cursor imageCursor;
	static String fullPath;
	File file;
	Uri outputFileUri;
	//returnPlaceName name = new returnPlaceName(this);
	static int countTSRB = 0;
	static int countStarbucks = 0;
	static int countUnrecognized = 0;
	String destPath = null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//final Intent intent = getIntent();m
		Intent camera   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		
		camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		
		
		startActivityForResult(camera, IMG_REQUEST);
		/*
		Card card1 = new Card(getBaseContext());
		card1.setText("This card has a footer.");
		card1.setInfo("I'm the footer!");
		View card1View = card1.toView();
		String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		Log.d("Base", baseDir);
		setContentView(card1View);
		/*
		 
		*/
	
	}

	
	 protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == IMG_REQUEST) {
             if (resultCode == RESULT_OK) {
                 // A contact was picked.  Here we will just display it
                 // to the user.
                 
            	 obs = new FileObserver("/mnt/sdcard/DCIM/Camera"){
        			 
     				@Override
     				public void onEvent(int event, String path) {
     					int id2 = getLastImageId();
     					 Log.d("MyService","Fileobserver");
     					if(event==FileObserver.CREATE&&!path.equals(".probe")){
     								Log.d("MyService", "Created");
     						int id1 = getLastImageId();
     						while(id1 ==id2){
     							id1 = getLastImageId();
     						}
     						Log.d("MyService", "Fianl+id1"+id1);
     						obs.stopWatching();
     						Log.d("MyService",getPath());
     						
     						
     						runOnUiThread(new Runnable() {
     						     public void run() {

     						//stuff that updates ui
     						    	 
     	    						 setContentView(R.layout.activity_compass);
     	    						 Log.d("MyService", "Inside UI");
     	    						
     	    						 //Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/20131210_213458_691.jpg");
     	    						//Log.d("MyService", "inside ui thread"+bitmap.getHeight());
     	    						
     	    		               String file =  getPath();
     	    		               ImageView jpgView = (ImageView)findViewById(R.id.imageView1);
     	    		              jpgView.setImageBitmap(
     	    		            		    decodeSampledBitmapFromResource(file, 600, 300));
									
     						    }
     						});
     						

     						
     						File sourceFile = new File(getPath());
     						
     							Log.d("MyService","RecognisedPlace"+liveCardService.getRecognisedPlace());
     							
     						if(liveCardService.getRecognisedPlace().equals("TSRB"))
     						{
     							destPath = 	"/mnt/sdcard/Pictures/newPictures/"+liveCardService.getRecognisedPlace()+"/test_"+countTSRB+".jpg";
     							countTSRB++;
     							
     						}
     						else if(liveCardService.getRecognisedPlace().equals("Starbucks")){
     							destPath = 	"/mnt/sdcard/Pictures/newPictures/"+liveCardService.getRecognisedPlace()+"/test_"+countStarbucks+".jpg";
     							countStarbucks++;
     						}
     						else {
     							destPath = 	"/mnt/sdcard/Pictures/newPictures/"+liveCardService.getRecognisedPlace()+"/test_"+countUnrecognized+".jpg";
     							countUnrecognized++;
     						}
     					//destPath = 	"/mnt/sdcard/Pictures/newPictures/"+liveCardService.getRecognisedPlace()+"/test.jpg";
     						File destFile = new File(destPath);
     						Log.d("MyService", "NEwPath"+destPath);
     						FileChannel source = null;
     		                FileChannel destination = null;
     		                try {
								source = new FileInputStream(sourceFile).getChannel();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
     		                try {
								destination = new FileOutputStream(destFile).getChannel();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
     		                if (destination != null && source != null) {
     		                    try {
									destination.transferFrom(source, 0, source.size());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
     		                }
     		                if (source != null) {
     		                    try {
									source.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
     		                }
     		                if (destination != null) {
     		                    try {
									destination.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
     		                }

     					}    
     					else {
     						//Log.d("MyService", "Not Created");
     						
     					}
     					
     				}
          		 
          	 };obs.startWatching(); 
            	 	
            		 Log.d("MyService","Sucas");
            		 
             }
         }
     }
	 
	 private int getLastImageId(){
		    final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
		    final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
		    @SuppressWarnings("deprecation")
			Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
		    if(imageCursor.moveToFirst()){
		        int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
		        fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
		        Log.d("MyService", "getLastImageId::id " + id);
		        Log.d("MyService", "getLastImageId::path " + fullPath);
		        //imageCursor.close();
		        setPath(fullPath);
		        return id;
		    }else{
		        return 0;
		    }
		}
	private void setPath(String fullPath) {
		this.fullPath =  fullPath;
		// TODO Auto-generated method stub
		
	}

	private static String getPath(){
		return fullPath;
	}
	@Override
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	 public void onDestroy() {
		 	
	        super.onDestroy();
	        
	        if(imageCursor!=null){
	        	imageCursor = null;
	        }
	    //obs.stopWatching();
	    Log.d("MyService", "Destroyed");
	    
	    
	    	
	    
}
	
	
	public static Bitmap decodeSampledBitmapFromResource(String file, int reqWidth, int reqHeight) {
		
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    //BitmapFactory.decodeResource(res, resId, options);
	    BitmapFactory.decodeFile(file, options);
	    // Calculate inSampleS
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    //return BitmapFactory.decodeResource(res, resId, options);
	    return BitmapFactory.decodeFile(file, options);
	}
	 
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }
    
    return inSampleSize;
}

	public static int totalCount(String placeName){
		if(placeName.equals("TSRB")){
			//return 3;
			return countTSRB;
		}
		else if(placeName.equals("Starbucks")){
			//return 3;
			return countStarbucks;
		}
		else //return 2;
			return countUnrecognized;
	}
	
}
