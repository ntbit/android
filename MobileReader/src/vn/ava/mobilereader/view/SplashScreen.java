package vn.ava.mobilereader.view;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.model.Categories;
import vn.ava.mobilereader.util.NetWorkDetech;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreen extends Activity implements IMobilereader {

	private ProgressBar progress;
	private NetWorkDetech networkDetech;
	private final String TAG = SplashScreen.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		
		networkDetech = new NetWorkDetech(this);

		progress = (ProgressBar) findViewById(R.id.progress);
		progress.setMax(80);

		if (networkDetech.isConnectingToInternet()) {

			new ParseDataIner(this).execute();
			
		} else {
			AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setTitle("Notice");
			alert.setMessage("No internet connection, "
					+ "please check your network connectivity and then try again");
			alert.setButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Intent i = new Intent(getApplicationContext(),
							FragmentMain.class);
					startActivity(i);
					finish();
				}
			});
			alert.show();
		}
	}


	private class ParseDataIner extends AsyncTask<Void, Integer, Void>
			implements IMobilereader {

		private Context context;
		
		private DBAdapter db;

		public ParseDataIner(Context context) {

			this.context = context;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progress.setMax(URL.length - 2);
			progress.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Intent i = new Intent(context, FragmentMain.class);
			((Activity) context).startActivity(i);
			((Activity) context).finish();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new DBAdapter(context);
			db.open();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				for (int i = 0; i < URL.length; i++) {

					Document document = Jsoup.connect(URL[i]).timeout(90000)
							.get();
					Elements element = document.select("div.body");
					Elements element1 = element.select("div.container");
					Elements element2 = element1.select("div.row-fluid");
					Elements element3 = element2.select("main#content");
					Elements element4 = element3.select("div.category-list");
					Elements element5 = element4.select("div.cat-children");
					Elements element6 = element5.select("table");
					Elements element7 = element6.select("tbody");
					Elements element8 = element7.select("tr");
					Elements element9 = element8.select("td");

					for (Element e : element9) {

						Categories categories = new Categories();

						Elements element10 = e.select("a");// href
						Elements element11 = element10.select("img");// src
						Elements element12 = e.select("span");

						Element elTitle = element12.select("b").first();

						if (element12.text().length() > 0) {

							categories.setTitle(elTitle.text());
							categories.setSrc("http://codejava.net"
									+ element11.attr("src"));
							categories.setUrl("http://codejava.net"
									+ element10.attr("href"));
							categories.setType(i);

							db.insertCategories(categories);
						}

					}
					publishProgress(i);
				}
			} catch (Exception ex) {

				Log.d(TAG, ex.getMessage(),ex);
			}
			return null;
		}

		@Override
		public AlertDialog showDialog(String text) {
			// TODO Auto-generated method stub
			return null;
		}

		
	}


	@Override
	public AlertDialog showDialog(String text) {
		// TODO Auto-generated method stub
		return null;
	}


}
