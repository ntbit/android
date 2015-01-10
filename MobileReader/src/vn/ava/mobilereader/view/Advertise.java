package vn.ava.mobilereader.view;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.util.NetWorkDetech;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class Advertise extends Fragment implements IMobilereader {

	private ProgressDialog dialog;
	private WebView webviewAbout;
	private final static String TAG = Advertise.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.about, null);

		webviewAbout = (WebView) view.findViewById(R.id.webviewAbout);

		new NetWorkDetech(getActivity());
		if(NetWorkDetech.isConnectingToInternet()){
			
			new GetData(getActivity()).execute("http://www.codejava.net/advertise");
		}
		else{
			
			showDialog("No internet connection, "
					+ "please check your network connectivity and then try again");
		}

		return view;
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

	private class GetData extends AsyncTask<String, Void, String> {

		private Context context;

		public GetData(Context context) {

			this.context = context;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if (dialog != null && dialog.isShowing()) {
				dialog.cancel();
			}
			webviewAbout.loadData(result, "text/html; charset=utf-8", "UTF-8");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = createProgressDialog(context);

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

					if (!elHref.attr("href").startsWith("http")
							&& !elHref.attr("href").startsWith("#")) {

						String href = elHref.attr("href");
						elHref.attr("href", "http://www.codejava.net" + href);
					}
				}

				Elements el6 = element5.select("a.at_icon img");
				el6.remove();

				String domHtml = "<html>" + "<head>" + "</head>" + "<body>"
						+ element5.html() + "</body>" + "<html>";

				return domHtml;

			} catch (IOException e) {

				Log.d("IO", e.getMessage(), e);
			} catch (SQLiteException exception) {

				Log.i("error insert", exception.getMessage(), exception);
			}
			return null;
		}
	}

	@Override
	public AlertDialog showDialog(String text) {
		
		AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

		dialog.setTitle("Notice");
		dialog.setMessage(text);
		dialog.setButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();;
			}
		});

		dialog.show();

		return dialog;
	}

}
