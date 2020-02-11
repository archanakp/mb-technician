package com.battmobile.battmobiletechnician.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.utility.FormUtils;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;


public class LoginActivity extends Activity {
    TextView forgotPassword;
    Button btnSubmit;
    TextInputEditText email, password;
    String TAG = "LoginActivity";
    SessionManager sessionManager;
    RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        sessionManager = new SessionManager(LoginActivity.this);
        forgotPassword = findViewById(R.id.forgotPassword);
        btnSubmit = findViewById(R.id.login);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(LoginActivity.this)) {
                    if (!FormUtils.checkEmail(email.getText().toString().trim())) {
                        email.setError(UtilityConstraints.CustomToast.INVALID_EMAIL);
                        email.requestFocus();
                    } else if (password.getText().toString().trim().length() < 6) {
                        password.setError(UtilityConstraints.CustomToast.INVALID_PASSWORD);
                        password.requestFocus();
                    } else {
                        if (sessionManager.getFIREBASE_TOKEN() == null) {
                            loader.setVisibility(View.VISIBLE);
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    loader.setVisibility(View.GONE);
                                    loginAPI(email.getText().toString().trim(), password.getText().toString().trim(), task.getResult().getToken());
                                }
                            });
                        } else
                            loginAPI(email.getText().toString().trim(), password.getText().toString().trim(), sessionManager.getFIREBASE_TOKEN());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginAPI(final String str_mobile, final String str_password, final String firebase_token) {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.LOGIN)
                .withFormData("email", str_mobile)
                .withFormData("password", str_password)
                .withFormData("firebase_token", firebase_token)
                .withFormData("type", "Technician")
                .withFormData("device_token", "123456")
                .withFormData("device_name", "Android")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                JSONObject data = mainObject.getJSONObject("data");
                                sessionManager.loginUser(
                                        data.getString("id"),
                                        data.getString("username"),
                                        data.getString("email"),
                                        data.getString("firstname"),
                                        data.getString("lastname"),
                                        data.getString("phone"),
                                        data.getString("usertype"),
                                        data.getString("profile_image"));
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finishAffinity();
                            } else {
                                Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        loader.setVisibility(View.GONE);
                        Timber.e("onVelocityFailed: " + error.body);
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}