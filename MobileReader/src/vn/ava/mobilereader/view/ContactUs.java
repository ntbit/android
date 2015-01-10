package vn.ava.mobilereader.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.util.MailSMTP;
import vn.ava.mobilereader.util.NetWorkDetech;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ContactUs extends Fragment implements IMobilereader {

	private EditText edtName;
	private EditText edtEmail;
	private EditText edtPhone;
	private EditText edtSubject;
	private EditText edtMeassage;
	private Button btnSubmit;
	private Button btnReset;
	
	private static final String EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private Pattern pattern;
	private Matcher matcher;

	Session session = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.contact, null);
		new NetWorkDetech(getActivity());
		initial(view);

		return view;
	}

	private void initial(View view) {

		edtName = (EditText) view.findViewById(R.id.edtName);
		edtEmail = (EditText) view.findViewById(R.id.edtEmail);
		edtPhone = (EditText) view.findViewById(R.id.edtPhone);
		edtSubject = (EditText) view.findViewById(R.id.edtSubject);
		edtMeassage = (EditText) view.findViewById(R.id.edtMessage);
		btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
		btnReset = (Button)view.findViewById(R.id.btnReset);
				
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				sendMail();
			}

		});
		
		btnReset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				resetForm();
			}
		});

	}

	private void resetForm(){
		
		edtName.setText("");
		edtEmail.setText("");
		edtMeassage.setText("");
		edtPhone.setText("");
		edtSubject.setText("");
		edtEmail.setError(null);
		edtMeassage.setError(null);
		edtName.setError(null);
		edtPhone.setError(null);	
		edtSubject.setError(null);

	}
	
	private void sendMail(){
		
		if (NetWorkDetech.isConnectingToInternet()) {

			if (formValidate()) {

				new MailSMTP(getActivity()).sendMail(edtSubject
						.getText().toString(), edtMeassage.getText()
						.toString(), edtEmail.getText().toString(),
						edtName.getText().toString(), edtPhone
								.getText().toString());
			}
			
		} else {

			showDialog("No internet connection, "
					+ "please check your network connectivity and then try again");
		}

	}
	private boolean formValidate() {

		boolean isValidate = true;

		if (edtName.getText().length() == 0) {

			edtName.setError("You must enter your name");
			isValidate = false;
			
		} else if (edtEmail.getText().length() == 0) {

			edtEmail.setError("You must enter your e-mail");
			isValidate = false;
			
		} else if (edtSubject.getText().length() == 0) {

			edtSubject.setError("You must enter your subject");
			isValidate = false;
			
		} else if (edtMeassage.getText().length() == 0) {

			edtMeassage.setError("You must enter your message");
			isValidate = false;

		} else if (!emailValidate(edtEmail.getText().toString())) {

			edtEmail.setError("You must enter valid e-mail");
			isValidate = false;
		}
		return isValidate;

	}

	private boolean emailValidate(String email) {

		pattern = Pattern.compile(EMAIL);
		matcher = pattern.matcher(email);

		return matcher.matches();

	}

	@Override
	public AlertDialog showDialog(String text) {

		AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

		dialog.setTitle("Notice");
		dialog.setMessage(text);
		dialog.setButton("OK", new OnClickListener() {

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
