package com.lognsys.kalrav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.CircleTransform;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.PropertyReader;
import com.lognsys.kalrav.util.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
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
    private ImageView profile_image;

    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";
    public static final String TAG = "SettingFragment";

    ArrayList<UserInfo> userInfos;
    UserInfoDAOImpl userDaoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);

        userDaoImpl = new UserInfoDAOImpl(this);
        Log.d(TAG, "Setting onCreate getCustomer_id " + KalravApplication.getInstance().getPrefs().getCustomer_id());

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
        profile_image= (ImageView) findViewById(R.id.profile_image);
        btnRegister.setOnClickListener(this);
        if (KalravApplication.getInstance().getGlobalUserObject() != null)
        {
            UserInfo userInfo=KalravApplication.getInstance().getGlobalUserObject() /*userDaoImpl.findUserByUsername(KalravApplication.getInstance().getPrefs().getEmail())*/;
            Log.d("Response","Rest populateData userInfo.toString() " +userInfo.toString());

            if(userInfo.getId()!=0)
                KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
            if(userInfo.getEmail()!=null)
            editUsername.setText(userInfo.getEmail());

            if (userInfo.getName() != null) {
                editName.setText(userInfo.getName());
            }
            if (userInfo.getPhoneNo() != null) {
                editMobileNumber.setText(userInfo.getPhoneNo());
            }
            if (userInfo.getCity() != null) {
                editCities.setText(userInfo.getCity());
            }
            if (userInfo.getState() != null) {
                editState.setText(userInfo.getState());
            }
            if (userInfo.getZipcode() != null) {
                editPincode.setText(userInfo.getZipcode());
            }
            if (userInfo.getAddress() != null) {
                editAddress.setText(userInfo.getAddress());
            }
        }


        if(KalravApplication.getInstance().getPrefs().getIsFacebookLogin()==true){
            if(KalravApplication.getInstance().getPrefs().getImage()!=null){
                String imgUrl = "https://graph.facebook.com/" + KalravApplication.getInstance().getPrefs().getImage() + "/picture?type=large";
//                Picasso.with(getApplicationContext()).load(imgUrl).into(imgNavHeaderBg);
                Glide.with(this).load(imgUrl)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profile_image);
            }
        }
        else{
            if(KalravApplication.getInstance().getPrefs().getImage()!=null){
//                Picasso.with(getApplicationContext()).load(KalravApplication.getInstance().getPrefs().getImage()).into(imgNavHeaderBg);
                Glide.with(this).load(KalravApplication.getInstance().getPrefs().getImage())
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profile_image);
            }

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
                if(KalravApplication.getInstance().getPrefs().getUser_id()!=null)
                    auth_id =  KalravApplication.getInstance().getPrefs().getUser_id();
               postReq( KalravApplication.getInstance().getPrefs().getCustomer_id(),realname, username, auth_id, phone,address, city, state, zipcode);
            }
        }
    }
    public void postReq(String Id, final String realname, final String username,final String auth_id,final String phone,
                        final String address,final String city,final String state,final String zipcode){
        int id=Integer.parseInt(Id);
        String put_update_user_url=properties.getProperty(Constants.API_URL_USER.put_update_user_url.name())+id;

        Log.d("realname","put_update_user_url   "  +put_update_user_url);
        KalravApplication.getInstance().getPrefs().showDialog(SettingActivity.this);
        JSONObject params = new JSONObject();
        try {
            String firstname,lastname,device;

//            params.put("id", Integer.parseInt(Id));
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
            params.put("username", username);
            if(auth_id!=null && auth_id.length()>0){
                params.put("auth_id", auth_id);
            }
            else{
                params.put("auth_id", "auth_id");
            }
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
            device=KalravApplication.getInstance().getPrefs().getDevice_token();
            if(device!=null && device.length()>0){
                params.put("device", device);
            }
            else{
                params.put("device", "device");
            }
            params.put("firstname", firstname);
            params.put("lastname", lastname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT,
                put_update_user_url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response","Rest response " +response);
                        try{
                            KalravApplication.getInstance().getPrefs().hidepDialog(SettingActivity.this);
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            userInfo = new UserInfo();

                            userInfo.setId(jsonObject.getInt("id"));

                            userInfo.setName(jsonObject.getString("realname"));

                            userInfo.setEmail(jsonObject.getString("username"));

                            if (fb_id != null)
                                userInfo.setFb_id(fb_id);

                            if (google_id != null)
                                userInfo.setGoogle_id(google_id);

                            userInfo.setPhoneNo(jsonObject.getString("phone"));

                            KalravApplication.getInstance().getPrefs().setMobile(userInfo.getPhoneNo());
                            userInfo.setAddress(jsonObject.getString("address"));

                            userInfo.setCity(jsonObject.getString("city"));

                            userInfo.setState(jsonObject.getString("state"));

                            userInfo.setZipcode(jsonObject.getString("zipcode"));

                            userInfo.setGroupname(jsonObject.getString("group"));

                            userInfo.setRole(jsonObject.getString("role"));

                            userInfo.setDevice(jsonObject.getString("device"));

                            userInfo.setProvenance(jsonObject.getString("provenance"));

                            userInfo.setEnabled(jsonObject.getBoolean("enabled"));

                            userInfo.setNotification(jsonObject.getBoolean("notification"));


                            userInfo.setLoggedIn(Constants.LOG_IN);
//                        //save to the database
                            userDaoImpl.addUser(userInfo);

//                          setting customer id
                            KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
                            KalravApplication.getInstance().getPrefs().setUser_Group_Name(userInfo.getGroupname());
                            KalravApplication.getInstance().getPrefs().setEmail(userInfo.getEmail());

                            KalravApplication.getInstance().setGlobalUserObject(userInfo);

                            Log.d("", "Global object Reg " + KalravApplication.getInstance().getGlobalUserObject());
                            KalravApplication.getInstance().getPrefs().setIsLogin(true);

                            Intent i = new Intent(SettingActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        //  YOUR RESPONSE
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d("Response","Rest volleyError" +volleyError);

                String json = null;
                String str=null;
                byte[] response=null;
                if(volleyError.networkResponse.data!=null)
                 response = volleyError.networkResponse.data;
                Log.d("Response","Rest volleyError response " +response);
                try {
                    if (response != null && response.length > 0) {
                        str = new String(response, "UTF-8");
                        Log.d("Response", "Rest volleyError str toString  " + str.toString());

                        try {
                            JSONObject object = new JSONObject(str.toString());
                            Log.d("Response", "Rest inside object  " + object);

                            int statusCode = object.getInt("statusCode");
                            Log.d("Response", "Rest inside statusCode  " + statusCode);

                            if (statusCode == 400) {
                                String msg = object.getString("msg");
                                displayMessage(msg);
                            } else if (statusCode == 406) {
                                String msg = object.getString("msg");
                                displayMessage(msg);
                            } else if (statusCode == 404) {
                                String msg = object.getString("msg");
                                displayMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch(UnsupportedEncodingException e){
                        e.printStackTrace();
                }

                KalravApplication.getInstance().getPrefs().hidepDialog(SettingActivity.this);

            }

            //Somewhere that has access to a context
            public void displayMessage(String toastString){
                Log.d("Response","Rest volleyError toastString  " +toastString );

                Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
            }


        });
        KalravApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }



}
