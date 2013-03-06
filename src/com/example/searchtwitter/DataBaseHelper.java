package com.example.searchtwitter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
@Athresh

**/
public class DataBaseHelper extends SQLiteOpenHelper {

	static final String dbName = "tweetsDB";
	static final String contactsTable = "tweetsTable";
	static final String colID = "ID";
	static final String colName = "Name";
	static final String colImageURL = "ImageURL";
	static final String colLocation = "Location";
	static final String colText = "Text";

	public DataBaseHelper(Context context) {
		super(context, dbName, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + contactsTable + " (" + colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ colName + " TEXT, " + colImageURL + " TEXT, " 
				+ colLocation + " TEXT, " + colText + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + contactsTable);
		onCreate(db);

	}
	
	public void Clear() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + contactsTable);
	}

	void saveTweet(Tweet newContact) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(colName, newContact.getName());
		cv.put(colImageURL, newContact.getImageURL());
		cv.put(colLocation, newContact.getLocation());
		cv.put(colText, newContact.getText());

		db.insert(contactsTable, null, cv);
		db.close();

	}

	List<Tweet> getAllTweets() {
		List<Tweet> allContacts = new ArrayList<Tweet>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + contactsTable, null);

		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			Tweet contact = cursorToTweet(cur);
			allContacts.add(contact);
			cur.moveToNext();
		}

		cur.close();
		return allContacts;

	}

	List<Tweet> getFilteredTweets(String filter) {
		List<Tweet> allContacts = new ArrayList<Tweet>();

		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM " + contactsTable + " Where "
				+ colLocation + " like '%" + filter + "%'";

		Cursor cur = db.rawQuery(query, null);

		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			Tweet contact = cursorToTweet(cur);
			allContacts.add(contact);
			cur.moveToNext();
		}

		cur.close();
		return allContacts;

	}

	private Tweet cursorToTweet(Cursor cursor) {
		Tweet contact = new Tweet();
		contact.setID(cursor.getInt(0));
		contact.setName(cursor.getString(1));
		contact.setImageURL(cursor.getString(2));
		contact.setLocation(cursor.getString(3));
		contact.setText(cursor.getString(4));
		return contact;
	}

	void deletTweet(int contactID) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(contactsTable, colID + "=?",
				new String[] { String.valueOf(contactID) });
		db.close();
	}

}
