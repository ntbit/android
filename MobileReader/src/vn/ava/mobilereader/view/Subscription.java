package vn.ava.mobilereader.view;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.model.SubscribeForm;
import vn.ava.mobilereader.util.NetWorkDetech;
import vn.ava.mobilereader.util.SubmitFormSubscribe;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Subscription extends Fragment implements IMobilereader {

	private Spinner spnCountry;
	private Button btnSubmit;
	private Button btnReset;
	private EditText edtName;
	private EditText edtEmail;
	private EditText edtEmailConfirm;
	private EditText edtJob;
	private EditText edtCity;
	private CheckBox chkTerm;
	private TextView tvTerm;
	
	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.subscription, null);
		initial(view);

		return view;
	}

	private void initial(final View view) {

		spnCountry = (Spinner) view.findViewById(R.id.spnCountrySub);
		btnSubmit = (Button) view.findViewById(R.id.btnSubmitSub);
		btnReset = (Button)view.findViewById(R.id.btnResetSub);
		edtName = (EditText) view.findViewById(R.id.edtNameSub);
		edtEmail = (EditText) view.findViewById(R.id.edtEmailSub);
		edtEmailConfirm = (EditText) view.findViewById(R.id.edtEmailConfirmSub);
		edtJob = (EditText) view.findViewById(R.id.edtJobSub);
		edtCity = (EditText) view.findViewById(R.id.edtCitySub);
		chkTerm = (CheckBox) view.findViewById(R.id.chkTermSub);
		tvTerm = (TextView) view.findViewById(R.id.tvTerm);

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				submitForm();
			}
		});
		btnReset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				resetForm();
			}
		});
		spnCountry.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listCountry()));
		tvTerm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showTerm();
			}
		});

	}

	private void resetForm(){
		
		edtCity.setText("");
		edtEmail.setText("");
		edtEmailConfirm.setText("");
		edtJob.setText("");
		edtName.setText("");
		edtName.setError(null);
		edtCity.setError(null);
		edtEmail.setError(null);
		edtEmailConfirm.setError(null);
		edtJob.setError(null);
		chkTerm.setError(null);
		
		
	}
	private void showTerm() {

		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.create();
		final WebView webview = new WebView(getActivity());
		webview.loadUrl("http://www.codejava.net/terms?tmpl=component/");

		webview.setWebViewClient(new WebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		dialog.setView(webview);
		dialog.show();

	}
	
	private void submitForm() {

		if (NetWorkDetech.isConnectingToInternet()) {

			if (formValidate()) {

				String paramsURL = "http://www.codejava.net/dev/?user%5Bname%5D="
						+ URLEncoder.encode(edtName.getText().toString())
						+ "&"
						+ "user%5Bemail%5D="
						+ URLEncoder.encode(edtEmail.getText().toString())
						+ "&"
						+ "user%5Bconfirm_email%5D="
						+ URLEncoder.encode(edtEmailConfirm.getText()
								.toString())
						+ "&"
						+ "user%5Bjob%5D="
						+ URLEncoder.encode(edtJob.getText().toString())
						+ "&"
						+ "user%5Bstate_city%5D="
						+ URLEncoder.encode(edtCity.getText().toString())
						+ "&"
						+ "user%5Bcountry%5D="
						+ URLEncoder.encode(spnCountry.getSelectedItem()
								.toString())
						+ "&"
						+ "terms=on&ajax=1&ctrl=sub&task=optin&"
						+ "redirect=http%253A%252F%252Fwww.codejava.net%252Fdev%252Fnewsletter&"
						+ "redirectunsub=http%253A%252F%252Fwww.codejava.net%252Fdev%252Fnewsletter&"
						+ "option=com_acymailing&hiddenlists=6&acyformname=formAcymailing48791";

				getMeassage(paramsURL);
			}

		} else {

			showDialog("No internet connection, "
					+ "please check your network connectivity and then try again");
		}

	}

	private String getMeassage(final String url) {

		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				Document doc;

				try {

					doc = Jsoup.connect(url).get();
					JSONObject object = new JSONObject(doc.text());

					return object.getString("message");

				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {

				super.onPostExecute(result);
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

			}

		}.execute();

		return null;
	}

	private boolean formValidate() {

		boolean checkValid = true;

		if (edtName.getText().length() == 0) {

			edtName.setError("You must enter your name");
			checkValid = false;

		} else if (edtEmail.getText().length() == 0) {

			edtEmail.setError("You must enter your email");
			checkValid = false;

		} else if (edtEmailConfirm.getText().length() == 0) {

			edtEmailConfirm.setError("You must enter your email confirm");
			checkValid = false;

		} else if (edtJob.getText().length() == 0) {

			edtJob.setError("You must enter your job");
			checkValid = false;

		} else if (edtCity.getText().length() == 0) {

			edtCity.setError("You must enter your city");
			checkValid = false;

		} else if (!edtEmailConfirm.getText().toString()
				.equals(edtEmail.getText().toString())) {

			edtEmailConfirm.setError("The e-mail and confirm e-mail not match");
			checkValid = false;

		} else if (!emailValid(edtEmail.getText().toString())) {

			edtEmail.setError("You must enter valid e-mail");
			checkValid = false;

		} else if (!chkTerm.isChecked()) {

			chkTerm.setError("Please check the Terms");
			checkValid = false;
		}

		return checkValid;
	}

	private boolean emailValid(String email) {

		pattern = Pattern.compile(EMAIL);
		matcher = pattern.matcher(email);

		return matcher.matches();

	}

	private List<String> listCountry() {

		String[] listLocale = Locale.getISOCountries();

		List<String> listCountry = new ArrayList<String>();

		for (String country : listLocale) {

			Locale locale = new Locale("", country);
			listCountry.add(locale.getDisplayCountry());
		}
		Collections.sort(listCountry);
		return listCountry;
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
				;
			}
		});

		dialog.show();

		return dialog;
	}
}
