package android.elkhorn.com;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.elkhorn.com.Contact;
import android.elkhorn.com.R;
import android.elkhorn.com.R.layout;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

public class ContactListDemo extends ListActivity implements Runnable{
	 
    private List<Contact> contacts = null;
    private Contact con;
    private ContactArrayAdapter cAdapter;
    private ProgressDialog prog = null;
    private Context thisContext = this;
    DBAdapter db ;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prog = ProgressDialog.show(this, "Answering Machine", "Getting Contacts", true, false);
        
        db = new DBAdapter(this);
        Thread thread = new Thread(this);
        thread.start();
 
    }
    protected void onStop(){
        super.onStop();
        Intent myInt2 =new Intent(this, Sms.class);
		startActivity(myInt2);

        
    }
 
    public void run() {
        if (contacts == null)
        {
            contacts = fillContactsList();
 
        }
        handler.sendEmptyMessage(0);
    }
 
    private List<Contact> fillContactsList() {
        List<Contact> tmpList = new ArrayList<Contact>();
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(c.moveToNext()){
            String ContactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone =c.getString(
                    c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if(Integer.parseInt(hasPhone) == 1){
                Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"='"+ContactID+"'",
                        null, null);
                while(phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    con = new Contact();
                    con.setName(name);
                    con.setNumber(number);
                    tmpList.add(con);
                }
                phoneCursor.close();
            }
 
        }
        c.close();
        Collections.sort(tmpList);
        return tmpList;
    }
 
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            prog.dismiss();
            cAdapter = new ContactArrayAdapter(thisContext, R.layout.listitemlayout, contacts);
            getListView().setFastScrollEnabled(true);
          //  cAdapter.getView(0, null,null);
            setListAdapter(cAdapter);
 
        }
    };
 
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView label1 = ((TwoLineListItem) v).getText1();
        TextView label2 = ((TwoLineListItem) v).getText2();
        String name = label1.getText().toString();
        String phoneNumber = label2.getText().toString();
        
        
        if(isThere(name)==-1){
        	 db.open();        
             long id1;
             
             id1 = db.insertTitle(
             		name,
             		phoneNumber);        
             db.close();
             Toast.makeText(this, "Added to response list "+name+" "+ phoneNumber, Toast.LENGTH_SHORT).show();
        }else{
        	 db.open();
             if (db.deleteTitle(isThere(name)))
                 Toast.makeText(this, "Delete successful! "+name+" is removed from the list ", 
                     Toast.LENGTH_LONG).show();
             else
                 Toast.makeText(this, "Delete failed.", 
                     Toast.LENGTH_LONG).show();            
             db.close();

             contacts = null;
        }
       // Toast.makeText(this, "Selected "+name+" "+ phoneNumber, Toast.LENGTH_SHORT).show();
    }
    public int isThere(String name)
    {
    	db.open();
        Cursor c = db.getAllTitles();      
    	if (c.moveToFirst())
        {   		
            do {           	
                if(c.getString(1).equalsIgnoreCase(name)){              	
                	int id = Integer.parseInt(c.getString(0)); 
                	//db.close();
                	return id;
                }
            } while (c.moveToNext());
        }
    	db.close();
    	
      return -1;       
    } 

}