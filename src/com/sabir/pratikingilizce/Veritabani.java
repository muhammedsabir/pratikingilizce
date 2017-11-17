package com.sabir.pratikingilizce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class Veritabani extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "learnlist.db";
	private static final String TABLE_NAME = "learn_point";
	private static final int SCHEMA_VERSION = 2;

	public Veritabani(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		//context.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE learn_point (_id, kategori TEXT NOT NULL, deger INTEGER NOT NULL,dilbilgisi TEXT,slayt TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no-op, since will not be called until 2nd schema
		// version exists
	}
	
	public void dropTable() throws Exception{
	   SQLiteDatabase dbConn = this.getWritableDatabase();
	   String sql = "drop table "+TABLE_NAME;
	   dbConn.execSQL(sql);
	   dbConn.execSQL("CREATE TABLE learn_point (_id, kategori TEXT NOT NULL, deger INTEGER NOT NULL,dilbilgisi TEXT,slayt TEXT );");
	}

	public Cursor getAll() {
		return (getReadableDatabase().rawQuery(
				"SELECT _id, kategori, deger ,slayt FROM learn_point",
				null));
	}

	public void insert(int id,String kategori, int deger,String dil_bilgisi,String slayt) {
		SQLiteDatabase dbConn = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("_id", id);
		cv.put("kategori", kategori);
		cv.put("deger", deger);
		cv.put("dilbilgisi", dil_bilgisi);
		cv.put("slayt", slayt);
		try {
			dbConn.isOpen();
			dbConn.insert(TABLE_NAME, null, cv);
		} catch (Exception e) {
			Log.e(TABLE_NAME, e.getMessage().toString());
		}finally{
			dbConn.close();
		}
		
		//getWritableDatabase().insert("learn_point", null, cv);
//		if (dbConn.isOpen()) {
//			dbConn.insert("learn_point", "kategori", cv);
//			dbConn.close();
//		}
	}

	public String getkategori(Cursor c) {
		return (c.getString(1));
	}

	public String getdeger(Cursor c) {
		return (c.getString(2));
	}
	public String getdil_bilgisi(Cursor c){
		return (c.getString(3));
	}
	public String getslayt(Cursor c){
		return (c.getString(4));
	}

}