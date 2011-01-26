package android.elkhorn.com;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prog = ProgressDialog.show(this, "ContactListDemo", "Getting Contacts", true, false);
		Thread thread = new Thread(this);
		thread.start();

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
			setListAdapter(cAdapter);

		}
	};
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView label = ((TwoLineListItem) v).getText2();
		String phoneNumber = label.getText().toString();
		Toast.makeText(this, "Selected " + phoneNumber, Toast.LENGTH_SHORT).show();
	}
}