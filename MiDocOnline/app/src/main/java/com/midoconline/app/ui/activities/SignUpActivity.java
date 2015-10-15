package com.midoconline.app.ui.activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midoconline.app.R;
import com.midoconline.app.Util.NetworkManager;
import com.midoconline.app.Util.StringUtils;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.beans.SignUpBean;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SignUpActivity.class.getName();
    private EditText mEdtName, mEdtEmail,mEdtPassword,mEdtSpecialityLicence,mEdtSurname,mEdtMobile, mEdtBirthday, mEdtGender;
    private Button mRequestApproval;
    private TextInputLayout mEmailTextInput, mNameTextInput, mPasswordTextInput, mSpecialityTextInput,mSurnameTextInput, mMobileTextInput, mBirthdayTextInput, mGenderTextInput;
    private SignUpBean mSignUpBean;
    private LinearLayout mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        intiView();
    }

    private void intiView() {
        mMainView = (LinearLayout) findViewById(R.id.MainLayoutWrapper);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtSpecialityLicence = (EditText) findViewById(R.id.edt_speciality_licence);
        mEdtSurname = (EditText) findViewById(R.id.edt_surname);
        mEdtMobile = (EditText) findViewById(R.id.edt_mobile);
        mEdtBirthday = (EditText) findViewById(R.id.edt_birthday);
        mEdtGender = (EditText) findViewById(R.id.edt_birthday);
        mRequestApproval = (Button) findViewById(R.id.btn_request_approval);
        mRequestApproval.setOnClickListener(this);
        mEmailTextInput = (TextInputLayout) findViewById(R.id.til_email);
        mNameTextInput = (TextInputLayout) findViewById(R.id.til_name);
        mPasswordTextInput = (TextInputLayout) findViewById(R.id.til_password);
        mSpecialityTextInput = (TextInputLayout) findViewById(R.id.til_speciality_licence);
        mSurnameTextInput = (TextInputLayout) findViewById(R.id.til_surname);
        mMobileTextInput = (TextInputLayout) findViewById(R.id.til_mobile);
        mBirthdayTextInput = (TextInputLayout) findViewById(R.id.til_birthday);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mRequestApproval){
            validation();
        }
    }

    private void validation(){
        if (StringUtils.isNotEmpty(mEdtName.getText().toString())&& StringUtils.isNotEmpty(mEdtSurname.getText().toString()) && StringUtils.isNotEmpty(mEdtEmail.getText().toString()) && StringUtils.isNotEmpty(mEdtPassword.getText().toString())&& StringUtils.isNotEmpty(mEdtMobile.getText().toString())&& StringUtils.isNotEmpty(mEdtBirthday.getText().toString()) && StringUtils.isNotEmpty(mEdtSpecialityLicence.getText().toString())){
            mSignUpBean = new SignUpBean(mEdtName.getText().toString(),mEdtSurname.getText().toString(),mEdtEmail.getText().toString(),"male",mEdtMobile.getText().toString(),mEdtBirthday.getText().toString(),mEdtPassword.getText().toString(),mEdtSpecialityLicence.getText().toString());
            if(NetworkManager.isConnectedToInternet(this)) {
                Utils.showProgress(this);
                ExecutePostRequest();
            }else {
                Utils.ShowSnackBar(mMainView,getString(R.string.no_internet_connection));
            }

        }else {
            if (!StringUtils.isNotEmpty(mEdtName.getText().toString())){
                mNameTextInput.setError(getString(R.string.name_error));
            }
            if (!StringUtils.isNotEmpty(mEdtEmail.getText().toString())){
                mEmailTextInput.setError(getString(R.string.email_error));
            }
            if (!StringUtils.isNotEmpty(mEdtPassword.getText().toString())){
                mPasswordTextInput.setError(getString(R.string.password_error));
            }
            if (!StringUtils.isNotEmpty(mEdtSpecialityLicence.getText().toString())){
                mSpecialityTextInput.setError(getString(R.string.speciality_licence_error));
            }if (!StringUtils.isNotEmpty(mEdtSurname.getText().toString())){
                mSurnameTextInput.setError(getString(R.string.surname_error));
            }
            if (!StringUtils.isNotEmpty(mEdtMobile.getText().toString())){
                mMobileTextInput.setError(getString(R.string.mobile_error));
            }
            if (!StringUtils.isNotEmpty(mEdtBirthday.getText().toString())){
                mBirthdayTextInput.setError(getString(R.string.birthday_error));
            }

        }

    }

    public void ExecutePostRequest(){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,"http://52.74.206.181:8010/tokens/user_sign_up", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                Log.d(TAG,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.closeProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",mSignUpBean.getName());
                params.put("email",mSignUpBean.getEmail());
                params.put("password", mSignUpBean.getPassword());
                params.put("specialize",mSignUpBean.getSpecialize());
                params.put("surname",mSignUpBean.getSurname());
                params.put("gender",mSignUpBean.getGender());
                params.put("dob",mSignUpBean.getBirthday());
                params.put("mobile_no",mSignUpBean.getMobilenumber());

                return params;
            }

            @Override
            public Map<String, String> getHeaders()  {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        queue.add(stringrequest);
    }
}
