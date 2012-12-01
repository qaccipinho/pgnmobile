package com.pgn.gis;

import org.json.JSONObject;

import com.pgn.gis.libraries.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	Button btnLogin;
	EditText txtUserName;
	EditText txtPassword;
	TextView lblErrorMessage;
	
	public static final String TAG = "MobileGISPGN-Login";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        
        PrepareControlFromLayout();
    }
    
    private void PrepareControlFromLayout() {
    	this.btnLogin = (Button)findViewById(R.id.btnLogin);
    	this.txtUserName = (EditText)findViewById(R.id.userid);
    	this.txtPassword = (EditText)findViewById(R.id.user_password);
    	this.lblErrorMessage = (TextView)findViewById(R.id.lblErrorMessage);
    	
    	this.clearForm();

    	this.btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String userLDAP = txtUserName.getText().toString();
				String password = txtPassword.getText().toString();

				//User usr = new User();
				User usr = new User("http://gis.pgn.co.id/PGNServices/UntukAndroid/PGNGISServices.asmx");
				
				try {
					JSONObject json = usr.loginUser(userLDAP, password);
					String success = json.get("success").toString();
					
					if (success.equals("1")) {
						lblErrorMessage.setText("Login berhasil.");
						
						Intent i = new Intent(getApplicationContext(), MobilePGNActivity.class);
						//Intent i = new Intent(getApplicationContext(), TestActivity.class);
						String dataLogin = json.toString();
						i.putExtra("data", dataLogin);
						startActivity(i);
						
						clearForm();
					}
					else {
						lblErrorMessage.setText("Login gagal. Username atau password anda salah.");
					}
				}
				catch(Exception ex) {
					Log.d(TAG, ex.getMessage());
				}
				
			}
    		
    	});
    }
    
    private void clearForm() {
    	lblErrorMessage.setText("");
    	txtUserName.setText("");
    	txtPassword.setText("");
    }
    
    @Override
    public void onBackPressed() {
    	new AlertDialog.Builder(this)
    		.setTitle(R.string.title_exit_dialog)
    		.setMessage(R.string.message_exit_dialog)
    		.setNegativeButton(R.string.tidak, null)
    		.setPositiveButton(R.string.ya, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					LoginActivity.super.onBackPressed();
				}
			}).create().show();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_login, menu);
        return true;
    }

}
