package vn.ava.mobilereader.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import vn.ava.mobilereader.model.SubscribeForm;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

public class SubmitFormSubscribe {

	public SubmitFormSubscribe(Context context, SubscribeForm sub) {
		
		sendPost(sub);
	}

	private void sendPost(final SubscribeForm sub) {

		

		
			
			String paramsURL = "http://www.codejava.net/dev/?user%5Bname%5D="
					+ URLEncoder.encode(sub.getName())
					+ "&"
					+ "user%5Bemail%5D="
					+ URLEncoder.encode(sub.getEmail())
					+ "&"
					+ "user%5Bconfirm_email%5D="
					+ URLEncoder.encode(sub.getEmailConfirm())
					+ "&"
					+ "user%5Bjob%5D="
					+ URLEncoder.encode(sub.getJob())
					+ "&"
					+ "user%5Bstate_city%5D="
					+ URLEncoder.encode(sub.getCity())
					+ "&"
					+ "user%5Bcountry%5D="
					+ URLEncoder.encode(sub.getCountry())
					+ "&"
					+ "terms=on&ajax=1&ctrl=sub&task=optin&"
					+ "redirect=http%253A%252F%252Fwww.codejava.net%252Fdev%252Fnewsletter&"
					+ "redirectunsub=http%253A%252F%252Fwww.codejava.net%252Fdev%252Fnewsletter&"
					+ "option=com_acymailing&hiddenlists=6&acyformname=formAcymailing48791";
		
	}

}
