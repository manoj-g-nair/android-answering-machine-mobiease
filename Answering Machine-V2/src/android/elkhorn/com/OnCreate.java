package android.elkhorn.com;




import android.elkhorn.com.Geocoder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OnCreate extends Activity {					//A object from this class creates the first screen of the application
    /** Called when the activity is first created. */
	private Button voice , sms ,onOff;						//initiation buttons
	private TextView txt;
	private AudioManager am;								//for the silent mode
	static boolean isOn;									//indicate the service is running
	private static final String TAG ="OnCreate";			// Tag for the logging
	private LocationManager locationManager;
	private Location currentLocation;
	private String txtLatitude;
	private String txtLongitude;
	static String localityName = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);						//set the main layout screen
        
        voice = (Button) findViewById(R.id.BtVoice);
        sms = (Button) findViewById(R.id.BtSms);
        onOff = (Button) findViewById(R.id.BtOnOff);
        txt = (TextView) findViewById(R.id.tv_act);
        isOn = false;
        txt.setTextColor(Color.RED);
      
        am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);	//current audio service to the am object (used to turn on silent mode)
        
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	this.locationManager.requestLocationUpdates("gps", (long)30000, (float) 10.0, new LocationListener()
		{
			public void onLocationChanged(Location arg0) 
			{
				handleLocationChanged(arg0);
			}
		
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
		
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
		
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
			}
		});
        
        Log.i(TAG,"Oncreate oncreate");     
        
    }
    private void handleLocationChanged(Location loc)
    {
    	// Save the latest location
    	this.currentLocation = loc;
    	// Update the latitude & longitude TextViews
    	this.txtLatitude=(Double.toString(loc.getLatitude()));
    	this.txtLongitude=(Double.toString(loc.getLongitude()));
    }
    private void handleReverseGeocodeClick()
    {
    	if (this.currentLocation != null)
    	{
    		// Kickoff an asynchronous task to fire the reverse geocoding
    		// request off to google
    		ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
    		task.applicationContext = this;
    		task.execute();
    	}
    	else
    	{
    		// If we don't know our location yet, we can't do reverse
    		// geocoding - display a please wait message
    		showToast("Please wait until we have a location fix from the gps");
    	}
    }
    public void showToast(CharSequence message)
    {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), message, duration);
		toast.show();
    }
	public class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, String>
    {
    	private ProgressDialog dialog;
    	protected Context applicationContext;
    	
    	@Override
    	protected void onPreExecute()
    	{
    		this.dialog = ProgressDialog.show(applicationContext, "Please wait...contacting the tubes.", 
                    "Requesting reverse geocode lookup", true);
    	}
    	
		@Override
		protected String doInBackground(Void... params) 
		{
			localityName = "";
			
			if (currentLocation != null)
			{
				localityName = Geocoder.reverseGeocode(currentLocation);
			}
			
			return localityName;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			this.dialog.cancel();
			//Utilities.showToast("Your Locality is: " + result, applicationContext);
		}
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater inflater = getMenuInflater();			//menu for the default menu button in Android devices
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {   //function for menu items
    	Log.v(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {
        
            case R.id.menuAbout:
            	Log.v(TAG, "menuAbout clicked");
            	Intent myInt4 =new Intent(this, About.class);
    			startActivity(myInt4);
            	return true;

            case R.id.menuquit:
            	Log.v(TAG, "menuQuit clicked");
            	System.exit(0);
            	return true;	
            	
            case R.id.menuSmsSet:
            	Log.v(TAG, "menuSMSset clicked");
            	Intent myInt2 =new Intent(this, Sms.class);
    			startActivity(myInt2);
    			//handleReverseGeocodeClick();
            	return true;
            	
            case R.id.menuVoiceSet:
            	Log.v(TAG, "menuVoice clicked");
            	Intent myInt =new Intent(this, Voice.class);
    		    startActivity(myInt);
    		    return true;
            default:
                if (!item.hasSubMenu()) {
                    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
        }

        return false;
    }
	public void voiceAction(View view) {
		     Intent myInt =new Intent(this, Voice.class);
		     startActivity(myInt);
		     
		 }
	public void smsAction(View view) {
			Intent myInt2 =new Intent(this, Sms.class);
			startActivity(myInt2);
			
	 }
	public void onOffAction(View view) {
		if(am.getRingerMode()!=AudioManager.RINGER_MODE_SILENT){
		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		onOff.setText("DEACTIVATE");
		txt.setText("ACTIVATED");
		txt.setTextColor(Color.GREEN);
		handleReverseGeocodeClick();
		isOn = true;
		}else{
		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		onOff.setText("ACTIVATE");
		txt.setText("DEACTIVATED");
		txt.setTextColor(Color.RED);
		isOn = false;
		}
	 }
	
}