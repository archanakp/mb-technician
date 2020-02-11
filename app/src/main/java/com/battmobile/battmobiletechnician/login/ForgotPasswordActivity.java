package com.battmobile.battmobiletechnician.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.FormUtils;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText email;
    Button send;
    String TAG = "CheckMobileActivity";
    RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Utils.setActionBarScreen("Forgot Password", getSupportActionBar());
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        email = findViewById(R.id.email);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(ForgotPasswordActivity.this)) {
                    if (!FormUtils.checkEmail(email.getText().toString().trim()))
                        email.setError(UtilityConstraints.CustomToast.INVALID_EMAIL);
                    else {
                        forgotPasswordAPI(email.getText().toString().trim());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void forgotPasswordAPI(String email_address) {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.FORGOT_PASSWORD)
                .withFormData("email", email_address)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            new AlertDialog.Builder(ForgotPasswordActivity.this)
                                    .setMessage(mainObject.getString("message"))
                                    .setTitle("Alert!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            email.getText().clear();
                                            loader.setVisibility(View.GONE);
                                        }
                                    }).create().show();
                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                            loader.setVisibility(View.GONE);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
