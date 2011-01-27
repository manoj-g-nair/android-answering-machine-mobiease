package android.elkhorn.com;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Voice extends Activity {
    /** Called when the activity is first created. */

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);
        
        
             
    }
	public void gotoHomeAction(View view) {
		Intent myInt5 =new Intent(this, OnCreate.class);
		startActivity(myInt5);
	     
}

}