package vn.ava.mobilereader.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.model.CategoriesItem;
import vn.ava.mobilereader.myadapter.ListViewCategoriesDetailAdapter;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ListCategoriesItem extends Activity {

	private ListView listContent;
	private ListViewCategoriesDetailAdapter adapter;
	private List<CategoriesItem> listCategoriesDetail;
	private DBAdapter db;
	private Intent intent;
	private EditText edtKey;
	private ImageButton btnSearch;
	private ImageButton btnDelete;
	private int page = 1;
	private int itemadd = 10;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories_detail_activity);
		setupActionbar();

		db = new DBAdapter(this);
		db.open();
		intent = this.getIntent();
		listContent = (ListView) findViewById(R.id.listViewCategories);
		listCategoriesDetail = new ArrayList<CategoriesItem>();

		new InsertData(this, intent.getExtras().getInt("id"), 0, 5)
				.execute(intent.getExtras().getString("url"));

		listContent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				tranferData(position);
			}
		});
		listContent.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int count = listContent.getCount();
				if (scrollState == SCROLL_STATE_IDLE) {

					if (listContent.getLastVisiblePosition() >= count - 1) {

						if (listContent.getCount() != db.getCountArticle(intent
								.getExtras().getInt("id"))) {

							new InsertData(ListCategoriesItem.this, intent
									.getExtras().getInt("id"), page, itemadd)
									.execute(intent.getExtras()
											.getString("url"));
							page++;
							itemadd = itemadd + 5;
						} else {

							Toast.makeText(getApplicationContext(),
									"No more data", Toast.LENGTH_LONG).show();
						}

					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		initial();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}

	}

	private void tranferData(int position) {

		Intent intent = new Intent(getApplicationContext(), ViewContent.class);

		intent.putExtra("url", listCategoriesDetail.get(position).getUrl());
		intent.putExtra("title", listCategoriesDetail.get(position).getTitle());
		intent.putExtra("descript", listCategoriesDetail.get(position)
				.getDescript());
		intent.putExtra("publish", listCategoriesDetail.get(position)
				.getPublish());
		intent.putExtra("image", listCategoriesDetail.get(position).getImage());
		intent.putExtra("id", listCategoriesDetail.get(position).getId());
		startActivity(intent);
	}

	private void initial() {

		edtKey = (EditText) findViewById(R.id.edtKeyword);
		btnDelete = (ImageButton) findViewById(R.id.imageDelete);
		btnDelete.setVisibility(View.GONE);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				clearText();

			}
		});

		btnSearch = (ImageButton) findViewById(R.id.btnSearch);

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				doSearch();
			}
		});

		edtKey.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					doSearch();
					return true;
				}

				return false;
			}
		});
		edtKey.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				btnDelete.setVisibility(View.VISIBLE);

				if (edtKey.getText().length() == 0) {

					btnDelete.setVisibility(View.GONE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	private void clearText() {

		edtKey.setText("");
		btnDelete.setVisibility(View.GONE);
		doSearch();

	}

	private void doSearch() {

		try {
			
			adapter.searchFilter(edtKey.getText().toString().trim()
					.toLowerCase(Locale.getDefault()));
		} catch (NullPointerException ex) {
			Log.d("Error", ex.getMessage(), ex);
		}
	}

	public static ProgressDialog createProgressDialog(Context context) {

		ProgressDialog dialog = new ProgressDialog(context);

		try {

			dialog.show();
		} catch (BadTokenException e) {

		}
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.progressdialog);

		return dialog;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void setupActionbar() {

		ActionBar actionBar = this.getActionBar();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		View customView = getLayoutInflater().inflate(
				R.layout.custom_actionbar, null);

		TextView textTitle = (TextView) customView
				.findViewById(R.id.textViewName);
		intent = this.getIntent();

		textTitle.setText(intent.getExtras().getString("title"));

		View buttonFavious = customView
				.findViewById(R.id.imageButtonIconFavious);
		View iconShare = customView.findViewById(R.id.imageButtonIconShare);
		View buttonBack = customView.findViewById(R.id.imageButtonIconBack);
		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		LinearLayout layout = (LinearLayout) customView
				.findViewById(R.id.linear);
		layout.removeView(buttonFavious);
		layout.removeView(iconShare);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(customView, params);

	}

	public class InsertData extends AsyncTask<String, Void, Void> {

		private Context context;
		private String TAG = InsertData.class.getSimpleName();
		private DBAdapter db;
		private int type;
		private int id = 0;
		private int limit = 0;

		public InsertData(Context context) {

			this.context = context;
		}

		public InsertData(Context context, int type, int id, int limit) {

			this.context = context;
			this.type = type;
			this.id = id;
			this.limit = limit;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (dialog != null && dialog.isShowing()) {
				dialog.cancel();
			}

			new SelectData(context, type, limit).execute();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			db = new DBAdapter(context);
			db.open();

			dialog = createProgressDialog(context);
		}

		@Override
		protected Void doInBackground(String... params) {

			int j = 0;

			try {

				Document document = Jsoup
						.connect(params[0] + "?start=" + id + "0")
						.timeout(90000).get();

				Elements element = document.select("div.body")
						.select("div.container").select("div.row-fluid")
						.select("main#content").select("div.category-list")
						.select("div.cat-items").select("form#adminForm")
						.select("table[class=category table table-striped]");

				Elements element1 = element.select("tbody").select("tr");

				for (Element e : element1) { // element9

					CategoriesItem categoriesDetail = new CategoriesItem();

					Elements element2 = e.select("td.list-title").select("a");

					Elements element3 = e.nextElementSibling().select(
							"td[align=left][style=padding-top: 0px;]");// descript
					Elements element4 = e.nextElementSibling()
							.nextElementSibling()
							.select("td[align=right][style=padding-top: 0px;]");// publish
					Elements element5 = e.select("td[width=132][rowspan=3]")
							.select("img");

					if (element2.text().length() > 0) {

						categoriesDetail.setTitle(element2.text());
						categoriesDetail.setImage("http://www.codejava.net"
								+ element5.attr("src"));
						categoriesDetail.setDescript(element3.text());
						categoriesDetail.setPublish(element4.text());
						categoriesDetail.setUrl("http://www.codejava.net"
								+ element2.attr("href"));
						categoriesDetail.setType(type);

						db.insertCategoriesDetail(categoriesDetail);

					}
					j++;

					if (j == element1.size() - 3) {
						break;
					}

				}

			} catch (IOException e) {

				Log.d(TAG, e.getMessage(), e);
			} catch (NullPointerException e) {

				Log.d(TAG, e.getMessage(), e);
			}
			return null;
		}
	}

	private class SelectData extends AsyncTask<Void, Void, Void> implements
			IMobilereader {

		private Context context;
		private int type;
		private int limit;

		public SelectData(Context context, int type, int limit) {

			this.context = context;
			this.type = type;
			this.limit = limit;
		}

		@Override
		protected Void doInBackground(Void... params) {

			listCategoriesDetail = db.getTypeCategoriesDetail(type, limit);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (listCategoriesDetail.size() != 0) {

				adapter = new ListViewCategoriesDetailAdapter(context,
						listCategoriesDetail);
				listContent.setAdapter(adapter);
				listContent.setSelection(limit - 5);

			} else {

				showDialog("No internet connection, "
						+ "please check your network connectivity and then try again");
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new DBAdapter(context);
			db.open();
		}

		@Override
		public AlertDialog showDialog(String text) {

			AlertDialog dialog = new AlertDialog.Builder(context).create();

			dialog.setTitle("Notice");
			dialog.setMessage(text);
			dialog.setButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
					;
				}
			});

			dialog.show();

			return dialog;
		}

	}
}
