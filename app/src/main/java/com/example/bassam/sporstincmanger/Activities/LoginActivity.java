package com.example.bassam.sporstincmanger.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.Entities.UserEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.app.Config;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    GlobalVars globalVars;
    ProgressDialog progressDialog;

    EditText phone_edittext, pass_edittext;
    String email, pass;

    String received_pass, received_phone, received_name, received_imgUrl,received_date_of_birth;
    int received_id, received_gender, received_type;

    boolean all_good;
    private boolean connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Configuring user.....");

        phone_edittext = findViewById(R.id.phoneEditText_login);
        pass_edittext = findViewById(R.id.passEditText_login);

    }

    @SuppressLint("StaticFieldLeak")
    public void loginClicked(View view) {

        email = phone_edittext.getText().toString();
        pass = pass_edittext.getText().toString();
        all_good = true;

        if (email.equals("") ){
            show_toast("Email is missing");

        } else if (pass.equals("")) {
            show_toast("Email is missing");

        } else {
            JSONObject where_info = new JSONObject();
            try {
                where_info.put("email", email);
                where_info.put("pass",pass);

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl(Constants.selectData);
                HashMap<String,String> params = new HashMap<>();
                params.put("table","users");
                params.put("where",where_info.toString());

                httpCall.setParams(params);

                progressDialog.show();
                new HttpRequest(){
                    @Override
                    public void onResponse(JSONArray response) {
                        super.onResponse(response);
                        connectionStatus = connectionTimeOut;
                        checkUserLogin(response);
                    }
                }.execute(httpCall);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void checkUserLogin(JSONArray response) {
        if (response != null){
            try {
                JSONObject result = response.getJSONObject(0);
                received_type = result.getInt("type");
                if (received_type == 3 || received_type == 4) {
                    received_id = result.getInt("id");
                    received_name = result.getString("name");
                    received_imgUrl = result.getString("ImageUrl");
                    received_gender= result.getInt("gender");
                    received_type = result.getInt("type");
                    received_phone = result.getString("phone");
                    received_pass = result.getString("pass");
                    received_date_of_birth = result.getString("date_of_birth");
                    ActiveUser(received_id);
                } else {
                    progressDialog.dismiss();
                    show_toast("Unauthorized login");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            if (connectionStatus){
                progressDialog.dismiss();
                show_toast("Poor Connection Try again Later..");
                return;
            }
            progressDialog.dismiss();
            show_toast("Wrong email address or password");
        }
    }

    private void ActiveUser(int user_id) {
        try {
            SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            String user_token = tokenPref.getString("regId", "");

            JSONObject where_info = new JSONObject();
            where_info.put("id",user_id);

            JSONObject values = new JSONObject();
            values.put("active",1);
            if (!user_token.equals(""))
                values.put("token",user_token);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",values.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    go_to_home();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void go_to_home(){
        globalVars.settAll(received_name, received_imgUrl , received_phone , pass, email,
                received_id, received_type, received_gender,received_date_of_birth);

        UserEntity userEntity = new UserEntity(received_name, received_imgUrl , received_phone,pass, email,
                received_id, received_type, received_gender,received_date_of_birth);

        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(userEntity);
        preferences.putString("CurrentUser", json);
        preferences.apply();

        progressDialog.dismiss();
        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void show_toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
