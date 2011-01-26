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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
//import android.telephony.gsm.SmsMessage;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.text.GetChars;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;



public class SmsReceiver extends BroadcastReceiver  {
	Vector<String> words=new Vector<String>();
	
	 
	private SmsMessage[] getMessagesFromIntent(Intent intent)
     {
             SmsMessage retMsgs[] = null;
             Bundle bdl = intent.getExtras();
             try{
                     Object pdus[] = (Object [])bdl.get("pdus");
                     retMsgs = new SmsMessage[pdus.length];
                     for(int n=0; n < pdus.length; n++)
                     {
                             byte[] byteData = (byte[])pdus[n];
                             retMsgs[n] = SmsMessage.createFromPdu(byteData);
                     }       
                     
             }catch(Exception e)
             {
                     Log.e("GetMessages", "fail", e);
             }
             return retMsgs;
     }
     
     public void onReceive(Context context, Intent intent) 
     {
    	 String msgs = Sms.rplymsg;
    //	 ContentResolver cr = getContentResolver();
     //    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);

 ///   	 SmsManager.getDefault().sendTextMessage("5556", null, msgs,null,null); 
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") && OnCreate.isOn)
         {                    
         SmsMessage msg[] = getMessagesFromIntent(intent);
         
         for(int i=0; i < msg.length; i++)
         {
                 String message = msg[i].getDisplayMessageBody();
                 if(message != null && message.length() > 0)
                 {
                         Log.i("MessageListener:",  message);
                    
                              String address= msg[i].getOriginatingAddress();
                              String body= msg[i].getDisplayMessageBody();
                              SmsManager.getDefault().sendTextMessage(address, null, msgs,null,null);
                              SmsManager.getDefault().sendTextMessage(address, null, body,null,null);
                         }
                  
                         
             }
         }
       }
  }