package com.example.glassv3;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private boolean resumed;
	boolean start = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		//startService(new Intent(MainActivity.this, liveCardService.class));
		Log.d("livecardService","Main");
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Log.d("livecardService","Inflate menu");
		//resumed = false;
		
		return true;
	}
	
	public void onResume(){
		super.onResume();
		Log.d("livecardService","Resume menu");
		//resumed = true;
		openOptionsMenu();
	}
	
    protected void onPause() {
        super.onPause();
        //resumed = false;
    }

    public boolean onOptionsItemSelected(MenuItem item){
		Log.d("livecardService","Selected");
		int itemId = item.getItemId();
		if (itemId == R.id.stop) {
			stopService(new Intent(MainActivity.this, liveCardService.class));
			//displayLastMeal.destroyMealPics();
			Log.d("livecardService","Stop");
			return true;
		} else {
			Log.d("livecardService","Flase");
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	public void onOptionsMenuClosed(Menu menu){
		super.onOptionsMenuClosed(menu);
		//start = false;
		finish();
		
	}
	

}
