package vn.ava.mobilereader.view;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.model.CategoriesItem;
import vn.ava.mobilereader.model.ListDrawerItem;
import vn.ava.mobilereader.myadapter.ListViewDrawerNavigater;
import vn.ava.mobilereader.util.NetWorkDetech;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMain extends Activity implements IMobilereader {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private Fragment fragment;
	private ListView listView;
	private NetWorkDetech networkDetech;
	private TextView textViewName;
	private ListDrawerItem[] listItem;
	private static final String TAG = Fragment.class.getSimpleName();
	private DBAdapter db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmentmain);
		setupActionbar();
		initial();
		replaceFragment();
		db = new DBAdapter(getApplicationContext());
		if(!db.countTableDEtai(true)){
			
			new InsertData(this, 30).execute("http://www.codejava.net/books");
			new InsertData(this, 31).execute("http://www.codejava.net/coding");
		}
		if (!NetWorkDetech.isConnectingToInternet()) {

			Toast.makeText(getApplicationContext(),
					"No network connection. Reading in offline mode.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void initial() {

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView = (ListView) findViewById(R.id.left_drawer);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.open, R.string.close);

		drawerLayout.setDrawerListener(drawerToggle);

		listItem = new ListDrawerItem[13];

		for (int i = 0; i < listItem.length; i++) {

			if (i == 7) {

				listItem[i] = new ListDrawerItem("Socical", 0, 1);
			} else {
				
				listItem[i] = new ListDrawerItem(TITLE_NAV[i], ICON[i], 0);
			}
		}

		ListViewDrawerNavigater adapter = new ListViewDrawerNavigater(this,
				R.id.textViewTitleNav, listItem);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				displayView(position);

			}
		});

	}

	private void displayView(int position) {

		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = new MainFragment();
			textViewName.setText("Home");
			break;

		case 1:
			fragment = new Advertise();
			textViewName.setText("Advertise");
			break;
		case 2:
			fragment = new Favourite();
			textViewName.setText("Favourite");
			break;
		case 3:
			fragment = new ContactUs();
			textViewName.setText("Contact Us");
			break;
		case 4:

			fragment = new Subscription();
			textViewName.setText("Subscription");
			break;
		case 5:
			fragment = new Quizz();
			textViewName.setText("Quizz");
			break;
		case 6:
			fragment = new About();
			textViewName.setText("About");
			break;
			
		case 8:
			startActivity("http://www.codejava.net/feeds");
			break;
		
		case 9:
			startActivity("https://www.facebook.com/codejava");
			break;
			
		case 10:
			startActivity("https://twitter.com/codejavadotnet");
			break;
			
		case 11:
			startActivity("https://plus.google.com/+CodejavaNet/posts");
			break;
		case 12:
			startActivity("https://delicious.com/codejavadotnet");
			break;
		default:
			Log.d("Error", "error");
			break;
		}

		if (fragment != null) {

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			drawerLayout.closeDrawers();
		}
	}

	private void startActivity(String url) {

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	private void replaceFragment() {

		Fragment fragment = new MainFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Notice");
		dialog.setMessage("Are you sure you want to quit");
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				finish();
			}
		});
		AlertDialog alertDialog = dialog.create();
		alertDialog.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupActionbar() {

		ActionBar actionBar = this.getActionBar();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.rgb(238, 60, 39)));
		View customView = getLayoutInflater().inflate(
				R.layout.custom_actionbar, null);
		View buttonBack = customView.findViewById(R.id.imageButtonIconBack);
		View iconHome = customView.findViewById(R.id.imageButtonIconHome);
		View iconShare = customView.findViewById(R.id.imageButtonIconShare);
		View buttonFavious = customView
				.findViewById(R.id.imageButtonIconFavious);
		textViewName = (TextView) customView.findViewById(R.id.textViewName);

		LinearLayout layout = (LinearLayout) customView
				.findViewById(R.id.linear);
		layout.removeView(buttonBack);
		layout.removeView(buttonFavious);
		layout.removeView(iconHome);
		layout.removeView(iconShare);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(customView, params);

	}

	@Override
	public AlertDialog showDialog(String text) {
		return null;
	}


	public class InsertData extends AsyncTask<String, Void, Void> {

		private Context context;
		private DBAdapter db;
		private int type;

		public InsertData(Context context) {

			this.context = context;
		}

		public InsertData(Context context, int type) {

			this.context = context;
			this.type = type;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			db = new DBAdapter(context);
			db.open();

		}

		@Override
		protected Void doInBackground(String... params) {

			int i = 0;
			int j = 0;

			try {

				while (true) {

					Document document = Jsoup
							.connect(params[0] + "?start=" + i + "0")
							.timeout(90000).get();


					Elements element = document.select("div.body")
							.select("div.container").select("div.row-fluid")
							.select("main#content").select("div.category-list")
							.select("div.cat-items").select("form#adminForm")
							.select("table[class=category table table-striped]");

					if (element.text().length() <= 0) {
						break;
					}

					Elements element8 = element.select("tbody").select("tr");

					for (Element e : element8) {

						CategoriesItem categoriesDetail = new CategoriesItem();

						Elements element10 = e.select("td.list-title");
						Elements element11 = element10.select("a");// title, url

						Elements element12 = e.nextElementSibling().select(
								"td[align=left][style=padding-top: 0px;]");// descript
						Elements element13 = e
								.nextElementSibling()
								.nextElementSibling()
								.select("td[align=right][style=padding-top: 0px;]");// publish
						Elements element14 = e
								.select("td[width=132][rowspan=3]");
						Elements element15 = element14.select("img");// src

						if (element11.text().length() > 0) {

							categoriesDetail.setTitle(element11.text());
							categoriesDetail.setImage("http://www.codejava.net"
									+ element15.attr("src"));
							categoriesDetail.setDescript(element12.text());
							categoriesDetail.setPublish(element13.text());
							categoriesDetail.setUrl("http://www.codejava.net"
									+ element11.attr("href"));
							categoriesDetail.setType(type);

							db.insertCategoriesDetail(categoriesDetail);
						}
						j++;
						if (j == element8.size() - 3) {
							break;
						}
					}
					i++;
					j = 0;
				}
			} catch (IOException e) {

				Log.d(TAG, e.getMessage(), e);
			} catch (NullPointerException e) {

				Log.d(TAG, e.getMessage(), e);
			}
			return null;
		}
	}

}
