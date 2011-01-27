package android.elkhorn.com;


import android.database.Cursor;
import android.elkhorn.com.ContactListDemo;
import android.graphics.Color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Sms extends Activity {
    /** Called when the activity is first created. */
	static String rplymsg;
	private EditText rplytxt;
	private TextView rplytxtt,inboxtxt;
	private Editor smsReplyEditor;
	private static final String TAGS ="SMS";
	private ListView lv1;
	public static final String PREFS_NAME = "SMSconfig";
	DBAdapter db;

	//private String starr[]={"Android","iPhone","BlackBerry","AndroidPeople"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);
    
        rplytxt = (EditText) findViewById(R.id.EditText);
        rplytxtt = (TextView) findViewById(R.id.text_reply);
        inboxtxt = (TextView) findViewById(R.id.text_inbox);
        inboxtxt.setBackgroundColor(Color.DKGRAY);
        inboxtxt.setTextColor(Color.GREEN);
        db = new DBAdapter(this);
        
               
       // rplymsg = rplytxt.getText().toString();
       // rplytxtt.setText("Reply Massage   "+rplymsg);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String replymsg = settings.getString("rplymsg", "I'll reply you soon");
        
        rplytxtt.setText("Reply Massage   "+rplymsg);
        rplytxt.setText(rplymsg);
        
        if(rplymsg == null){                            // by default set these string to text feilds and String variable
        	rplytxtt.setText("I'll reply you soon");
            rplytxt.setText("I'll reply you soon");
            replymsg =("I'll reply you soon");
        }
        
        inboxtxt.setText(" ");
        db.open();
        Cursor c = db.getAllTitles();
        if (c.moveToFirst())
        {
    		
    		System.out.println("eraafdadafdfadaaaaaaaaaaaaaaaaaaaaaaaaaaa      adding to txt");
            do { 
            	   inboxtxt.append(Html.fromHtml("<b>" + c.getString(1)+ "</b>" +"  "+ "<small>" + c.getString(2)+ "</small>" + "<br />" ));
                }
             while (c.moveToNext());
        }

        db.close();
       // lv1=(ListView)findViewById(R.id.ListView01);  // By using setAdpater method in listview we an add string array in list.
        //lv1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, starr));
        
        Log.i(TAGS,"SMS Oncreate");
             
    }
    protected void onStop(){
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        rplytxtt = (TextView) findViewById(R.id.text_reply);
        rplymsg = rplytxt.getText().toString();
        editor.putString("rplymsg",rplymsg);

        // Commit the edits!
        finish();
        editor.commit();

        
    }


	public void applyAction(View view) {
		 rplymsg = rplytxt.getText().toString();
	     rplytxtt.setText("Reply Massage   "+rplymsg);
 }
	public void contactListAction(View view) {
		Intent myInt2 =new Intent(this, ContactListDemo.class);
		startActivity(myInt2);
	     
}
	public void gotoHomeAction(View view) {
		Intent myInt5 =new Intent(this, OnCreate.class);
		startActivity(myInt5);
	     
}
	public void clearTextAction(View view) {		 
	     rplytxtt.setText("Reply Massage   ");
	     rplytxt.setText(" ");
	     rplymsg = rplytxt.getText().toString();
	     
}

}