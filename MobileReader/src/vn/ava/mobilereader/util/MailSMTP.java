package vn.ava.mobilereader.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vn.ava.mobilereader.Instant.IMobilereader;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class MailSMTP implements IMobilereader {
	
	private static String subject = "";
	private static String content = "";
	private static String fromEmail = "";
	private static String name = "";
	private static String phone = "";
	private static final String TAG = MailSMTP.class.getSimpleName();
	private Context context;
	
	public MailSMTP(Context context){
		
		this.context = context;
	}
	
	public void sendMail(String subject, String content, 
			String fromEmail,String name,String phone){
		
		this.subject = subject;
		this.content = content;
		this.fromEmail = fromEmail;
		this.name = name;
		this.phone = phone;
		new RetreiveFeedTask().execute();
	}
	
	private class RetreiveFeedTask extends AsyncTask<Void, Void, Void> {
		
		private Session session;
		private Properties properConfig;
		private Message message;
		private ProgressDialog dialog;
		
		@Override
		protected Void doInBackground(Void... params) {

			try {
				
				message = new MimeMessage(session);
				message.setFrom(new InternetAddress(fromEmail));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(TO_EMAIL));
				message.setSubject(subject);
				message.setContent("From: "+name+"<br />"
						+"Your Email: "+fromEmail+"<br />"
						+"Phone Number: "+phone+"<br />"
						+"Subject: "+subject+"<br />"
						+"Mesage: "+content+"<br />", "text/html; charset=utf-8");

				Transport.send(message);
				
			} catch (AddressException e) {
				
				Log.d(TAG, e.getMessage(),e);
				
			} catch (MessagingException e) {
				
				Log.d(TAG, e.getMessage(),e);
				
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("sending...");
			dialog.show();
			
			properConfig = new Properties();
			
			properConfig.put(SMTP_HOST, HOST_MAIL);
			properConfig.put(SMTP_SOCKET_PORT, "465");
			properConfig.put(SMTP_CLASS,
					"javax.net.ssl.SSLSocketFactory");
			properConfig.put(SMTP_AUTHEN, "true");
			properConfig.put(SMTP_PORT, "465");

			session = Session.getDefaultInstance(properConfig,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									USER_LOGIN, PASS_LOGIN);
						}
					});
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
			Toast.makeText(context, "Your request has been send. "
					+ "Thank you for contacting us", Toast.LENGTH_LONG).show();
		}
		
		
		
	}

	@Override
	public AlertDialog showDialog(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
