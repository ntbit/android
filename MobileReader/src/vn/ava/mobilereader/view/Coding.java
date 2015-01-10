package vn.ava.mobilereader.view;

import java.io.IOException;
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
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class Coding extends Fragment {

	private List<CategoriesItem> listBook;
	private ListView listViewBook;
	private ListViewCategoriesDetailAdapter adapter;
	private EditText edtKey;
	private ImageButton btnSearch;
	private DBAdapter db;
	private final String TAG = Coding.class.getSimpleName();
	private ImageButton btnDelete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.categories_detail_activity, null);

		initial(view);
		listViewBook = (ListView) view.findViewById(R.id.listViewCategories);

		new InsertData(getActivity(), 31)
				.execute("http://www.codejava.net/coding");

		listViewBook.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				startActivity(position);
			}
		});
		return view;
	}

	private void startActivity(int position) {

		Intent intent = new Intent(getActivity(), ViewContent.class);
		intent.putExtra("url", listBook.get(position).getUrl());
		intent.putExtra("title", listBook.get(position).getTitle());
		intent.putExtra("descript", listBook.get(position).getDescript());
		intent.putExtra("publish", listBook.get(position).getPublish());
		intent.putExtra("image", listBook.get(position).getImage());
		startActivity(intent);
	}

	private void initial(View view) {

		edtKey = (EditText) view.findViewById(R.id.edtKeyword);

		btnDelete = (ImageButton) view.findViewById(R.id.imageDelete);
		btnDelete.setVisibility(View.GONE);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				clearText();

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

		btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				doSearch();
			}
		});
	}

	private void clearText() {

		edtKey.setText("");
		btnDelete.setVisibility(View.GONE);
		doSearch();

	}

	private void doSearch() {

		adapter.searchFilter(edtKey.getText().toString().trim()
				.toLowerCase(Locale.getDefault()));
	}

	private ProgressDialog createProgressDialog(Context context) {

		ProgressDialog dialog = new ProgressDialog(context);
		try {

			dialog.show();

		} catch (BadTokenException e) {

			Log.d(TAG, e.getMessage(), e);
		}
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.progressdialog);

		return dialog;
	}

	public class InsertData extends AsyncTask<String, Void, Void> {

		private Context context;

		private int type;
		private ProgressDialog dialog;

		public InsertData(Context context) {

			this.context = context;
		}

		public InsertData(Context context, int type) {

			this.context = context;
			this.type = type;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (dialog != null && dialog.isShowing()) {
				dialog.cancel();
			}

			new SelectData(context, type).execute();
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

			int i = 0;
			int j = 0;

			try {

				while (true) {

					Document document = Jsoup
							.connect(params[0] + "?start=" + i + "0")
							.timeout(90000).get();

					Elements element = document.select("div.body");
					Elements element1 = element.select("div.container");
					Elements element2 = element1.select("div.row-fluid");
					Elements element3 = element2.select("main#content");
					Elements element4 = element3.select("div.category-list");
					Elements element5 = element4.select("div.cat-items");
					Elements element6 = element5.select("form#adminForm");
					Elements element7 = element6
							.select("table[class=category table table-striped]");

					if (element7.text().length() <= 0) {
						break;
					}

					Elements element8 = element7.select("tbody");
					Elements element9 = element8.select("tr");

					for (Element e : element9) {

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
						if (j == element9.size() - 3) {
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

	private class SelectData extends AsyncTask<Void, Void, Void> implements IMobilereader{

		private Context context;
		private int type;

		public SelectData(Context context, int type) {

			this.context = context;
			this.type = type;
		}

		@Override
		protected Void doInBackground(Void... params) {

			listBook = db.getTypeCategoriesDetail(type,5);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (!listBook.isEmpty()) {

				adapter = new ListViewCategoriesDetailAdapter(context, listBook);
				listViewBook.setAdapter(adapter);

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
			
			AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

			dialog.setTitle("Notice");
			dialog.setMessage(text);
			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				}
			});

			dialog.show();
			
			return dialog;
		}

	}

}
