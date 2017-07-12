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
import com.lognsys.kalrav.util.PropertyReader;
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
import java.util.Properties;

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
    private String fb_id, google_id;


    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";

    ArrayList<UserInfo> userInfos;
    UserInfoDAOImpl userDaoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);

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
            } else if (editAddress.getText().toString().length() == 0) {
                editAddress.setError("Address is Required");
                editAddress.requestFocus();
            } else if (editCities.getText().toString().length() == 0) {
                editCities.setError("City is Required");
                editCities.requestFocus();
            } else if (editState.getText().toString().length() == 0) {
                editState.setError("State is Required");
                editState.requestFocus();
            }else if (editPincode.getText().toString().length() == 0) {
                editPincode.setError("Zipcode is Required");
                editPincode.requestFocus();
            }else if (!Services.isValidZipCode(editPincode.getText().toString()) && editPincode.getText().toString().length() >6) {
                editPincode.setError("Please enter valid zipcode");
            }
            else if (editMobileNumber.getText().toString().length() == 0) {
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
               /* RegisteredTask task = new RegisteredTask(realname, username, auth_id, phone,address, city, state, zipcode);
                task.execute();*/
                postReq(realname, username, auth_id, phone,address, city, state, zipcode);
            }
        }
    }

    public void postReq(final String realname, final String username,final String auth_id,final String phone,
                        final String address,final String city,final String state,final String zipcode){

        String post_create_user_url=properties.getProperty(Constants.API_URL_USER.post_create_user_url.name());
        KalravApplication.getInstance().getPrefs().showpDialog(RegisterActivity.this);


        StringRequest postRequest = new StringRequest(Request.Method.POST, post_create_user_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        KalravApplication.getInstance().getPrefs().hidepDialog(RegisterActivity.this);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if(error!=null && error.getMessage() !=null){
                            Toast.makeText(getApplicationContext(),"error VOLLEY "+error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                        }Log.d("Error.Response", error.getMessage());
                        KalravApplication.getInstance().getPrefs().hidepDialog(RegisterActivity.this);

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                String firstname,lastname,device;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("realname", realname);
                String[] splited = null;

                Log.d("realname","realname   "  +realname);

                if (realname != null  && realname.contains(" ")) {
                    splited =realname.split(" ");
                    firstname = splited[0] == null ? "" : splited[0];
                    lastname = splited[1] == null ? "" : splited[1];
                } else {
                    firstname = "";
                    lastname = "";
                }
                device=KalravApplication.getInstance().getPrefs().getDevice_token();
                if(device==null){
                    KalravApplication.getInstance().invokeFCMService(getApplicationContext());
                }
                device=KalravApplication.getInstance().getPrefs().getDevice_token();
                Log.d("realname","username   "  +username);
                Log.d("realname","auth_id   "  +"auth_id");
                Log.d("realname","phone   "  +phone);
                Log.d("realname","address   "  +address);
                Log.d("realname","city   "  +city);
                Log.d("realname","state   "  +state);
                Log.d("realname","zipcode   "  +zipcode);
                Log.d("realname","device   "  +"device");
                Log.d("realname","firstname   "  +firstname);
                Log.d("realname","lastname   "  +lastname);

                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                params.put("username", username);
                params.put("auth_id", auth_id);
                params.put("phone", phone);
                params.put("address", address);
                params.put( "city" ,city);
                params.put("state", state);
                params.put("zipcode", zipcode);
                params.put("provenance", "Google");
                params.put("role", "GUEST");
                params.put("group", "NONE");
                params.put("notification", String.valueOf(false));
                params.put("enabled", String.valueOf(false));
                params.put("device", device);
                params.put("firstname", firstname);
                params.put("lastname", lastname);

                return params;
            }
        };
//        queue.add(postRequest);
        KalravApplication.getInstance().addToRequestQueue(postRequest);

    }

}
