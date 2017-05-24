package com.lognsys.kalrav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.lognsys.kalrav.db.UserInfoDAO;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.validation.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private UserInfo userInfo;
    private EditText editUsername;
    private EditText editName;
    private EditText editMobileNumber;
    private EditText editAddress;
    private EditText editCities;
    private EditText editState;
    private EditText editPincode;
    private Button btnRegister;
    //   String REST_REGISTER_USER="http://192.168.0.19:8080/createuser/0/dADAFSDBVXVXCV/deepu@gmail.com/deepu%20sharma/8767569879/location/provenance/2017-05-16%2017:09:51/true/false/device/google/city/state/401107/company/deepu/viv/Youth/Admin";
    String REST_REGISTER_USER = "http://192.168.0.19:8080/createuser/";
    private String fb_id, google_id;
    // Initializing a String Array

    ArrayList<UserInfo> userInfos;
    UserInfoDAOImpl userDaoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDaoImpl = new UserInfoDAOImpl(this);

        populateData();


    }

    private void populateData() {

        editUsername = (EditText) findViewById(R.id.editUsername);
        editName = (EditText) findViewById(R.id.editName);
        editMobileNumber = (EditText) findViewById(R.id.editMobileNumber);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editCities = (EditText) findViewById(R.id.editCities);
        editState = (EditText) findViewById(R.id.editState);
        editPincode = (EditText) findViewById(R.id.editPincode);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        if (KalravApplication.getInstance().getPrefs().getEmail() != null)
        {
            editUsername.setText(KalravApplication.getInstance().getPrefs().getEmail());
        }
        if (KalravApplication.getInstance().getPrefs().getName() != null) {
            editName.setText(KalravApplication.getInstance().getPrefs().getName());
        }


    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("", "Exception" + e);
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onClick(View v) {
//        RegisteredTask task =new RegisteredTast().execute();
        if (editName.getText().toString().length() == 0) {
            editName.setError(getString(R.string.hint_name));
            editName.requestFocus();
        } else {
            if (editUsername.getText().toString().length() == 0) {
                editUsername.setError("Email not entered");
                editUsername.requestFocus();

            } else if (!Services.isEmailValid(editUsername.getText().toString())) {
                editUsername.setError("Email is not valid");
            } else if (editMobileNumber.getText().toString().length() == 0) {
                editMobileNumber.setError("Mobile number is Required");
                editMobileNumber.requestFocus();
            } else if (!Services.isValidMobileNo(editMobileNumber.getText().toString()) && editMobileNumber.getText().toString().length() < 10) {
                editMobileNumber.setError("Please enter valid mobile no");
            } else {
                String username, realname, phone,address, city, state, zipcode;
                username = editUsername.getText().toString();
                realname = editName.getText().toString();
                phone = editMobileNumber.getText().toString();
                address = editAddress.getText().toString();
                city = editCities.getText().toString();
                state = editState.getText().toString();
                zipcode = editPincode.getText().toString();

                KalravApplication.getInstance().getPrefs().setName(realname);

                KalravApplication.getInstance().getPrefs().setEmail(username);
                KalravApplication.getInstance().getPrefs().setMobile(phone);
                String auth_id = null;
                    auth_id =  KalravApplication.getInstance().getPrefs().getUser_id();
                RegisteredTask task = new RegisteredTask(realname, username, auth_id, phone,address, city, state, zipcode);
                task.execute();
            }
        }
    }

    //Register and inserting  user records
    private class RegisteredTask extends AsyncTask<String, Void, String> {
        String auth_id, username, realname, phone, city, state, zipcode,address;
        int statusCode;
        ProgressDialog dialog;

        public RegisteredTask(String realname, String username, String auth_id,
                              String phone, String address, String city, String state, String zipcode) {
            this.username = username;
            this.realname = realname;
            this.phone = phone;
            this.auth_id = auth_id;
            this.city = city;
            this.address = address;
            this.state = state;
            this.zipcode = zipcode;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... place) {

            try {
                String postParameters, firstname, lastname;
                String[] splited = null;
                if (this.realname != null && this.realname.length() > 0 && this.realname.contains(" ")) {
                    splited = this.realname.split(" ");
                    firstname = splited[0] == null ? "" : splited[0];
                    lastname = splited[1] == null ? "" : splited[1];
                } else {
                    firstname = "-";
                    lastname = "-";
                }
                /*http://192.168.0.19:8080/createuser/0/priyank%20doshi/doshipriyank@gmail.com/authid124fnfj
                /8097526387/location/provenance/2017-05-13%2000:00:00/true/true/device/address/city/state/401107/company_name
                /priyank/doshi/group/role*/

                postParameters = "" + 0 + "/" + this.realname + "/" + this.username + "/" + this.auth_id + "/" + this.phone
                        + "/provenance/2017-05-16 17:09:51/" + true + "/" + true + "/android/"+ this.address+"/" + this.city
                        + "/"+this.state+"/"+this.zipcode+"/" + firstname + "/" + lastname + "/None/None";
                postParameters = postParameters.replace(" ", "%20");


                String urlString = REST_REGISTER_USER + postParameters;
                URL urlToRequest = new URL(urlString);
                Log.d("", "Test doInBackground urlToRequest " + urlToRequest);

                HttpURLConnection urlConnection =
                        (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        "application/json");
                Log.d("", "Test doInBackground urlConnection " + urlConnection);
                urlConnection.setFixedLengthStreamingMode(
                        postParameters.getBytes().length);
                Log.d("", "Test doInBackground postParameters " + postParameters);

                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);

                out.close();
                // handle issues
                statusCode = urlConnection.getResponseCode();
                Log.d("", "Test doInBackground statusCode" + statusCode);


                if (statusCode > 0) {
                    Log.d("", "Test doInBackground Status-Code 409: Conflict. " + statusCode);

                    InputStream in =
                            new BufferedInputStream(urlConnection.getInputStream());
                    try {

                        String values = getResponseText(in);
                        Log.d("", "Test doInBackground Status-Code 409: .Conflict values " + values);

                        return values;
                    } catch (IOException e) {
                        Log.d("", "Test doInBackground Status-Code 409: .Conflict IOException " + e);

                    }

                }


            } catch (Exception e) {
                Log.d("", "Test doInBackground Exception " + e);
            }
            return null;
        }

        private String getResponseText(InputStream inputStream) throws IOException {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            String ResponseString = total.toString();
            Log.d("", "Test getResponseText ResponseString " + ResponseString);

            r.close();
            return ResponseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("", "Test onPostExecute result " + result);
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    userInfo = new UserInfo();
                    userInfo.setId(jsonObject.getInt("id"));
                    userInfo.setName(jsonObject.getString("realname"));
                    userInfo.setEmail(jsonObject.getString("username"));
                    if (fb_id != null)
                        userInfo.setFb_id(fb_id);
                    if (google_id != null)
                        userInfo.setGoogle_id(google_id);
                    userInfo.setPhoneNo(jsonObject.getString("phone"));
                    userInfo.setAddress(jsonObject.getString("address"));
                    userInfo.setCity(jsonObject.getString("city"));
                    userInfo.setState(jsonObject.getString("state"));
                    userInfo.setZipcode(jsonObject.getString("zipcode"));
                    userInfo.setLoggedIn(Constants.LOG_IN);
//                        //save to the database

                    KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
                    KalravApplication.getInstance().setGlobalUserObject(userInfo);
                    Log.d("", "Global object Reg " + KalravApplication.getInstance().getGlobalUserObject());
                    userDaoImpl.addUser(userInfo);
                    KalravApplication.getInstance().getPrefs().setIsLogin(true);

                    dialog.dismiss();
                    KalravApplication.getInstance().showDialog(RegisterActivity.this, "User Created Successfully !!!");
                    Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();


                } else {
                    dialog.dismiss();
                }
            } catch (Exception e) {
//                KalravApplication.getInstance().showDialog(RegisterActivity.this,"Please check your  network connection");
                Log.d("", "onPostExecute Exception " + e);

            }
        }
    }


}
