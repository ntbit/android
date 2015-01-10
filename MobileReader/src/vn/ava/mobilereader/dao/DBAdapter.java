package vn.ava.mobilereader.dao;

import java.util.ArrayList;
import java.util.List;

import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.model.Categories;
import vn.ava.mobilereader.model.CategoriesItem;
import vn.ava.mobilereader.model.PageContent;
import android.R.bool;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Toast;

public class DBAdapter extends SQLiteOpenHelper implements IMobilereader {

	private SQLiteDatabase db;

	public DBAdapter(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
	}

	public void open() throws SQLException {

		this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(TB_CREATE);
		db.execSQL(TB_DETAIL_CREATE);
		db.execSQL(TB_CONTENT_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		db.execSQL(TB_DROP);
		db.execSQL(TB_DETAIL_DROP);
		db.execSQL(TB_CONTENT_DROP);
		onCreate(db);
	}

	public void insertContent(PageContent content) throws SQLiteException{
		
		db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(TB_CT_TITLE, content.getTitle());
		contentValues.put(TB_CT_CONTENT, content.getContent());
		
		db.insert(DB_TABLE_CONTENT, null, contentValues);
	}
	
	public String getContent(String title) throws SQLiteException{
		
		String sql = "SELECT * FROM "+DB_TABLE_CONTENT +" WHERE title='"+title+"'";
		
		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		return cursor.getString(2);
	}
	
	public void insertCategories(Categories mc) throws SQLiteException {

		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TB_TITLE, mc.getTitle());
		values.put(TB_IMAGE, mc.getSrc());
		values.put(TB_URL, mc.getUrl());
		values.put(TB_TYPE, mc.getType());

		db.insert(DB_TABLE, null, values);
	}

	public void insertCategoriesDetail(CategoriesItem cd) {

		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TB_DT_TITLE, cd.getTitle());
		values.put(TB_DT_IMAGE, cd.getImage());
		values.put(TB_DT_URL, cd.getUrl());
		values.put(TB_DT_DESCRIP, cd.getDescript());
		values.put(TB_DT_PUBLISH, cd.getPublish());
		values.put(TB_DT_TYPE, cd.getType());
		db.insert(DB_TABLE_DETAIL, null, values);
	}

	public List<Categories> getAllCategories() {

		List<Categories> listCategories = new ArrayList<Categories>();

		String sql = "SELECT * FROM " + DB_TABLE;
		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {

			do {
				Categories mc = new Categories();
				mc.setId(Integer.parseInt(cursor.getString(0)));
				mc.setTitle(cursor.getString(1));
				mc.setSrc(cursor.getString(2));
				mc.setUrl(cursor.getString(3));
				mc.setType(Integer.parseInt(cursor.getString(4)));

				listCategories.add(mc);

			} while (cursor.moveToNext());
		}
		return listCategories;

	}

	public boolean countTableDEtai(boolean openDb) {
		
		if (openDb) {
			if (db == null || !db.isOpen()) {
				db = getReadableDatabase();
			}

			if (!db.isReadOnly()) {
				db.close();
				db = getReadableDatabase();
			}
		}
		Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_DETAIL, null);
		cursor.moveToFirst();

		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	public int getIDCategories(String src) {

		String sql = "SELECT id FROM " + DB_TABLE + " WHERE image= '" + src
				+ "'";
		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		return Integer.parseInt(cursor.getString(0));

	}

	public List<CategoriesItem> getAllCategoriesDetail() {

		List<CategoriesItem> listCategoriesDetail = new ArrayList<CategoriesItem>();

		String sql = "SELECT * FROM " + DB_TABLE_DETAIL;

		db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {

			do {
				CategoriesItem cd = new CategoriesItem();
				cd.setId(Integer.parseInt(cursor.getString(0)));
				cd.setTitle(cursor.getString(1));
				cd.setImage(cursor.getString(2));
				cd.setUrl(cursor.getString(3));
				cd.setDescript(cursor.getString(4));
				cd.setPublish(cursor.getString(5));
				cd.setType(Integer.parseInt(cursor.getString(6)));

				listCategoriesDetail.add(cd);

			} while (cursor.moveToNext());
		}
		return listCategoriesDetail;

	}

	public List<CategoriesItem> getTypeCategoriesDetail(int type,int limit) {

		List<CategoriesItem> listCategoriesDetail = new ArrayList<CategoriesItem>();

		String sql = "SELECT * FROM " + DB_TABLE_DETAIL + " WHERE type=" + type +" LIMIT "+limit;

		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {

			do {
				CategoriesItem cd = new CategoriesItem();
				cd.setId(Integer.parseInt(cursor.getString(0)));
				cd.setTitle(cursor.getString(1));
				cd.setImage(cursor.getString(2));
				cd.setUrl(cursor.getString(3));
				cd.setDescript(cursor.getString(4));
				cd.setPublish(cursor.getString(5));
				cd.setType(Integer.parseInt(cursor.getString(6)));

				listCategoriesDetail.add(cd);

			} while (cursor.moveToNext());
		}
		return listCategoriesDetail;

	}

	@Override
	public AlertDialog showDialog(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getCountArticle(int type){
		
		String sql = "SELECT * FROM " + DB_TABLE_DETAIL + " WHERE type=" + type;
		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		return cursor.getCount();
		
	}

}
