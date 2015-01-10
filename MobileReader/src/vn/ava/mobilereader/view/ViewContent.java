package vn.ava.mobilereader.view;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.model.CategoriesItem;
import vn.ava.mobilereader.model.PageContent;
import vn.ava.mobilereader.util.SharePreference;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContent extends Activity {

	private Intent intent;
	private static final String TAG = ViewContent.class.getSimpleName();
	private SharePreference share;
	private ImageButton iconfavourite;
	private boolean flag = false;
	private CategoriesItem categories;
	private ProgressDialog dialog;
	private DBAdapter db;
	private String title = null;
	private WebView webView;
	private String url = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_content);
		setupActionbar();

		share = new SharePreference();
		intent = this.getIntent();
		url = intent.getExtras().getString("url");
		new InsertData(this).execute(intent.getExtras().getString("url"));

	}

	@Override
	public void onPause() {
		super.onDestroy();
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		intent = this.getIntent();
		title = intent.getExtras().getString("title");

		categories = new CategoriesItem();
		categories.setTitle(intent.getExtras().getString("title"));
		categories.setPublish(intent.getExtras().getString("publish"));
		categories.setUrl(intent.getExtras().getString("url"));
		categories.setDescript(intent.getExtras().getString("descript"));
		categories.setImage(intent.getExtras().getString("image"));
		categories.setId(intent.getExtras().getInt("id"));

		flag = checkFavoriteItem(categories);

		if (flag == true) {

			iconfavourite.setImageResource(R.drawable.ic_star_grey600_36dp);
		} else {

			iconfavourite.setImageResource(R.drawable.ic_star_white_36dp);
		}

	}

	private void setupActionbar() {

		ActionBar actionBar = this.getActionBar();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		View customView = getLayoutInflater().inflate(
				R.layout.custom_actionbar, null);

		iconfavourite = (ImageButton) customView
				.findViewById(R.id.imageButtonIconFavious);

		iconfavourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				favouriteClick(categories);
			}

		});
		View buttonBack = customView.findViewById(R.id.imageButtonIconBack);
		View iconShare = customView.findViewById(R.id.imageButtonIconShare);

		iconShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				shareClick();
			}
		});

		TextView textTitle = (TextView) customView
				.findViewById(R.id.textViewName);

		intent = this.getIntent();
		textTitle.setText(intent.getExtras().getString("title"));
		textTitle.setSelected(true);

		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (webView.canGoBack()) {

					webView.goBack();

				} else {
					dialog.dismiss();
					finish();
				}

			}
		});

		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(customView, params);
	}

	private void shareClick() {

		intent = this.getIntent();
		String url = intent.getExtras().getString("url");
		Intent intentShare = new Intent(Intent.ACTION_SEND);
		intentShare.setType("text/plain");
		intentShare.putExtra(Intent.EXTRA_TEXT, url);

		boolean checkAppFacebook = false;
		List<ResolveInfo> filter = getPackageManager().queryIntentActivities(
				intentShare, 0);

		for (ResolveInfo info : filter) {

			if (info.activityInfo.packageName.toLowerCase().startsWith(
					"com.facebook")) {

				intentShare.setPackage(info.activityInfo.packageName);
				checkAppFacebook = true;
				break;
			}

		}
		if (!checkAppFacebook) {
			String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
					+ url;
			intentShare = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}
		startActivity(intentShare);

	}

	private void favouriteClick(CategoriesItem categories) {

		if (flag == false) {

			Toast.makeText(getApplicationContext(), "This article has been added to your favourites",
					Toast.LENGTH_LONG).show();
			share.addFavourite(getApplicationContext(), categories);
			iconfavourite.setImageResource(R.drawable.ic_star_grey600_36dp);
			flag = true;

		} else {

			Toast.makeText(getApplicationContext(), "This article has been removed from your favourites",
					Toast.LENGTH_LONG).show();
			share.removeFavorite(getApplicationContext(), categories);
			iconfavourite.setImageResource(R.drawable.ic_star_white_36dp);
			iconfavourite.setTag("white");
			flag = false;
		}

	}

	public boolean checkFavoriteItem(CategoriesItem checkCategories) {

		boolean check = false;
		List<CategoriesItem> favorites = share
				.getFavourite(getApplicationContext());
		if (favorites != null) {
			for (CategoriesItem cate : favorites) {
				if (cate.equals(checkCategories)) {
					check = true;
					break;
				}
			}
		}
		return check;
	}

	public static ProgressDialog createProgressDialog(Context context) {

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private class InsertData extends AsyncTask<String, Void, Void>  {

		private Context context;

		public InsertData(Context context) {

			this.context = context;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);

			new GetData(context).execute(title);

			if (dialog != null && dialog.isShowing()) {
				dialog.cancel();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			db = new DBAdapter(context);
			dialog = createProgressDialog(context);

		}

		@Override
		protected Void doInBackground(String... params) {

			try {

				PageContent content = new PageContent();

				Document document = Jsoup.connect(params[0]).timeout(90000)
						.get();
				Elements element1 = document.select("div.body");

				Elements element2 = element1.select("div.container");
				Elements element3 = element2.select("div.row-fluid");
				Elements element4 = element3.select("main#content");
				Elements element5 = element4.select("div.item-page");

				Elements e = element5.select("h1 a");
				e.attr("style", "text-decoration:none;" + "font-size:22px;"
						+ "color:#000");
				Elements el = element5.select("div[class=btn-group right]");
				el.remove();

				Elements elDetail = element5.select("dt.article-info-term");
				elDetail.remove();

				Elements el1 = element5.select("div#multiad-block");
				el1.remove();

				Elements el2 = element5.select("div#jc");
				el2.remove();

				for (Element elSrc : element5.select("img")) {

					if (!elSrc.attr("src").startsWith("http")) {

						String src = elSrc.attr("src");
						elSrc.attr("src", "http://www.codejava.net" + src);

					}

				}

				for (Element elHref : element5.select("a")) {

					if (!elHref.attr("href").startsWith("http")) {

						if (!elHref.attr("href").startsWith("#")) {

							String href = elHref.attr("href");
							elHref.attr("href", "http://www.codejava.net"
									+ href);
						}
					}
				}

				Elements el4 = element5.select("pre");

				Log.d("PREEEE", el4.toString());

				el4.attr("style", "padding: 8.5px;" + "white-space: pre-wrap;"
						+ "margin: 0 0 9px;" + "font-size: 12px;"
						+ "line-height: 18px;" + "background-color: #f5f5f5;"
						+ "border: 1px solid rgba(0,0,0,0.15);"
						+ "border-radius: 4px;");

				el4.removeAttr("class");
				el4.attr("class", "brush: java");

				Elements el6 = element5.select("a.at_icon img");
				el6.remove();

				String domHtml = "<html>" + "<head>" + "</head>" + "<body>"
						+ element5.html()
						+ "<link href='http://zollback.bl.ee/syntax_highlighter/styles/shCore.css' rel='stylesheet' type='text/css'>"
						+ "<link href='http://zollback.bl.ee/syntax_highlighter/styles/shThemeDefault.css' rel='stylesheet' type='text/css'>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shCore.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shAutoloader.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushXml.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushJScript.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushCss.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushJava.js' type='text/javascript'></script>"
						+ "<script src='file:///android_asset/jquery.min.js' type='text/javascript'></script>"
						+ "<script type='text/javascript'>SyntaxHighlighter.all();</script>"
						+ "<style>"
						+ ".btn {"
						+ " background: #3498db;"
						+ "background-image: -webkit-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: -moz-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: -ms-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: -o-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: linear-gradient(to bottom, #3498db, #2980b9);"
						+ "-webkit-border-radius: 28;"
						+ "-moz-border-radius: 28;" + "border-radius: 28px;"
						+ "font-family: Arial;" + "color: #ffffff;"
						+ "font-size: 48px;" + "padding: 10px 23px 10px 20px;"
						+ "text-decoration: none;" + " margin: 20px auto;"
						+ "}</style>" + "<div style='text-align:center'>"
						+ "<a class='btn' href='" + url
						+ "#comment'>View Comment</a>" + "</div>" + "</body>"
						+ "<html>";
				content.setTitle(title);
				content.setContent(domHtml);
				db.insertContent(content);

			} catch (IOException e) {

				Log.d(TAG, e.getMessage(), e);

			} catch (SQLiteException exception) {

				Log.i("error la inserare child", exception.getMessage(),
						exception);
			}
			return null;
		}
	}

	private class GetData extends AsyncTask<String, Void, String> implements IMobilereader {

		private Context context;

		public GetData(Context context) {

			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {
			try {

				return db.getContent(params[0]);

			} catch (Exception ex) {

				return "\u0000";
			}
		}

		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);

			if (!result.contains("\u0000")) {

				webView.getSettings()
						.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
				webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
				webView.setScrollbarFadingEnabled(false);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setUseWideViewPort(true);
				// webView.setWebChromeClient(new WebChromeClient());
				webView.getSettings().setBuiltInZoomControls(true);
				webView.setWebViewClient(new WebViewClient() {

					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {

						if (url.contains("http://www.codejava.net")
								&& !url.endsWith("#comment")) {

							if (url.contains("http://www.codejava.net/download-attachment")) {

								Intent browserIntent = new Intent(
										Intent.ACTION_VIEW, Uri.parse(url));
								startActivity(browserIntent);

								return true;

							} else {

								try {

									view.loadData(new ParseHTML(context)
											.execute(url).get(),
											"text/html; charset=utf-8", "UTF-8");

								} catch (InterruptedException e) {

									Log.d(TAG, e.getMessage(), e);

								} catch (ExecutionException e) {

									Log.d(TAG, e.getMessage(), e);
								}
							}
							return false;

						}

						else {

							Intent browserIntent = new Intent(
									Intent.ACTION_VIEW, Uri.parse(url));
							startActivity(browserIntent);

							return true;
						}

					}

				});
				webView.loadData(
						URLEncoder.encode(result).replaceAll("\\+", "%20"),
						"text/html; charset=utf-8", "UTF-8");
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
			webView = (WebView) findViewById(R.id.webViewContent);
		}

		@Override
		public AlertDialog showDialog(String text) {
			
			AlertDialog dialog = new AlertDialog.Builder(context).create();

			dialog.setTitle("Notice");
			dialog.setMessage(text);
			dialog.setButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();;
				}
			});

			dialog.show();

			return dialog;
		}

	}

	private class ParseHTML extends AsyncTask<String, Void, String> {

		private Context context;

		public ParseHTML(Context context) {

			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {

			try {

				Document document = Jsoup.connect(params[0]).timeout(90000)
						.get();

				Elements element1 = document.select("div.body");

				Elements element2 = element1.select("div.container");
				Elements element3 = element2.select("div.row-fluid");
				Elements element4 = element3.select("main#content");
				Elements element5 = element4.select("div.item-page");

				Elements e = element5.select("h1 a");
				e.attr("style", "text-decoration:none;" + "font-size:22px;"
						+ "color:#000");
				categories.setTitle(e.text());
				Elements el = element5.select("div[class=btn-group right]");
				el.remove();

				Elements elDetail = element5.select("dt.article-info-term");
				elDetail.remove();

				Elements el1 = element5.select("div#multiad-block");
				el1.remove();

				Elements el2 = element5.select("div#jc");
				el2.remove();

				for (Element elSrc : element5.select("img")) {

					if (!elSrc.attr("src").substring(0, 4).contains("http")) {

						String src = elSrc.attr("src");
						elSrc.attr("src", "http://www.codejava.net" + src);

					}

				}

				for (Element elHref : element5.select("a")) {

					if (!elHref.attr("href").startsWith("http") && 
							!elHref.attr("href").startsWith("#")) {

							String href = elHref.attr("href");
							elHref.attr("href", "http://www.codejava.net"
									+ href);
						
					}
				}

				Elements el4 = element5.select("pre");
				el4.attr("style", "padding: 8.5px;" + "white-space: pre-wrap;"
						+ "margin: 0 0 9px;" + "font-size: 12px;"
						+ "line-height: 18px;" + "background-color: #f5f5f5;"
						+ "border: 1px solid rgba(0,0,0,0.15);"
						+ "border-radius: 4px;");

				Elements el6 = element5.select("a.at_icon img");
				el6.remove();

				String domHtml = "<html>" + "<head>" + "</head>" + "<body>"
						+ element5.html()
						+ "<link href='http://zollback.bl.ee/syntax_highlighter/styles/shCore.css' rel='stylesheet' type='text/css'>"
						+ "<link href='http://zollback.bl.ee/syntax_highlighter/styles/shThemeDefault.css' rel='stylesheet' type='text/css'>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shCore.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shAutoloader.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushXml.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushJScript.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushCss.js' type='text/javascript'></script>"
						+ "<script src='http://zollback.bl.ee/syntax_highlighter/scripts/shBrushJava.js' type='text/javascript'></script>"
						+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
						+ "<style>"
						+ ".btn {"
						+ " background: #3498db;"
						+ "background-image: -webkit-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: -moz-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: -ms-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: -o-linear-gradient(top, #3498db, #2980b9);"
						+ "background-image: linear-gradient(to bottom, #3498db, #2980b9);"
						+ "-webkit-border-radius: 28;"
						+ "-moz-border-radius: 28;" + "border-radius: 28px;"
						+ "font-family: Arial;" + "color: #ffffff;"
						+ "font-size: 48px;" + "padding: 10px 23px 10px 20px;"
						+ "text-decoration: none;" + "}</style>"
						+ "<div style='text-align:center;margin-top:10px;'>"
						+ "<a class='btn' href='" + params[0]
						+ "#comment'>View Comment</a>" + "</div>" + "</body>"
						+ "<html>";

				return domHtml;

			} catch (IOException e) {

				Log.d(TAG, e.getMessage(), e);
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new DBAdapter(context);
			db.open();
		}

	}

}
