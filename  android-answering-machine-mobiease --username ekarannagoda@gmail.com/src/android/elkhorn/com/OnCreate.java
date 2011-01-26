package android.elkhorn.com;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OnCreate extends Activity {					//A object from this class creates the first screen of the application
    /** Called when the activity is first created. */
	private Button voice , sms ,onOff;						//initiation buttons
	private AudioManager am;								//for the silent mode
	static boolean isOn;									//indicate the service is running
	private static final String TAG ="OnCreate";			// Tag for the logging
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);						//set the main layout screen
        
        voice = (Button) findViewById(R.id.BtVoice);
        sms = (Button) findViewById(R.id.BtSms);
        onOff = (Button) findViewById(R.id.BtOnOff);
        isOn = false;
      
       // am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);	//current audio service to the am object
       
        
        
        Log.i(TAG,"Oncreate oncreate");     
        
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
		isOn = true;
		}else{
		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		onOff.setText("ACTIVATE");
		isOn = false;
		}
	 }
}