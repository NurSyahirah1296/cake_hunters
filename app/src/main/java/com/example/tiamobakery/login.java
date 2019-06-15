package com.example.tiamobakery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class login extends AppCompatActivity {
    TextView textviewreg,textviewforgot;
    EditText etemail,etpassword;
    Button loginbtn;
    SharedPreferences sharedPreferences;
    CheckBox cbrememberme;
    Dialog dialogforgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etemail = findViewById(R.id.etEmail);
        etpassword = findViewById(R.id.etPassword);
        loginbtn = findViewById(R.id.loginbtn);
        textviewreg = findViewById(R.id.registerhere);
        cbrememberme = findViewById(R.id.rememberme);
        textviewforgot = findViewById(R.id.forgotpassword);
        textviewreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString();
                String pass = etpassword.getText().toString();

                loginUser(email,pass);
            }
        });
        cbrememberme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbrememberme.isChecked()){
                    String email = etemail.getText().toString();
                    String pass = etpassword.getText().toString();
                    savePref(email,pass);
                }
            }
        });
        textviewforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });
        loadPref();
    }

    private void savePref(String e, String p) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", e);
        editor.putString("password", p);
        editor.commit();
        Toast.makeText(this, "Preferences has been saved", Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String premail = sharedPreferences.getString("email", "");
        String prpass = sharedPreferences.getString("password", "");
        if (premail.length()>0){
            cbrememberme.setChecked(true);
            etemail.setText(premail);
            etpassword.setText(prpass);
        }
    }

    private void loginUser(final String email, final String pass) {
        class LoginUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(login.this,
                        "Login user","...",false,false);
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("password",pass);
                requesthandler rh = new requesthandler();
                String s = rh.sendPostRequest
                        ("https://tiamobakery.000webhostapp.com/cakehunters/login.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("failed")){
                    Toast.makeText(login.this, "Login Failed", Toast.LENGTH_LONG).show();
                }else if (s.length()>7){

                    String[] val = s.split(",");
                    Intent intent = new Intent(login.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userid",email);
                    bundle.putString("name",val[0]);
                    bundle.putString("phone",val[1]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.execute();
    }

    void forgotPasswordDialog(){
        dialogforgotpass = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialogforgotpass.setContentView(R.layout.forgotpassword);
        dialogforgotpass.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final EditText edemail = dialogforgotpass.findViewById(R.id.userEmails);
        Button btnsendemail = dialogforgotpass.findViewById(R.id.sendemailbtn);
        btnsendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgotemail =  edemail.getText().toString();
                sendPassword(forgotemail);
            }
        });
        dialogforgotpass.show();

    }

    private void sendPassword(final String forgotemail) {
        class SendPassword extends AsyncTask<Void,String,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap();
                hashMap.put("email",forgotemail);
                requesthandler rh = new requesthandler();
                String s = rh.sendPostRequest("https://tiamobakery.000webhostapp.com/cakehunters/verify_email.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(login.this, "Success. Check your email", Toast.LENGTH_LONG).show();
                    dialogforgotpass.dismiss();
                }else{
                    Toast.makeText(login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        SendPassword sendPassword = new SendPassword();
        sendPassword.execute();
    }
}
