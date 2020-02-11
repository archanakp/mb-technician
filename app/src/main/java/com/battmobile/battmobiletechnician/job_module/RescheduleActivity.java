package com.battmobile.battmobiletechnician.job_module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class RescheduleActivity extends AppCompatActivity {
    EditText reason;
    Button submit;
    RelativeLayout loader;
    SessionManager sessionManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        reason = findViewById(R.id.reason);
        submit = findViewById(R.id.submit);
        loader = findViewById(R.id.loader);
        sessionManager = new SessionManager(RescheduleActivity.this);
        Utils.setActionBarScreen("Reschedule job", getSupportActionBar());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reason.getText().toString().trim().length() == 0) {
                    reason.setError("Invalid Reason");
                    reason.requestFocus();
                } else rescheduleAPI();
            }
        });
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(RescheduleActivity.this)
                .setMessage(message)
                .setTitle("Alert!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                }).create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void rescheduleAPI() {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.RESCHEDULE_JOB)
                .withFormData("job_id", getIntent().getStringExtra("id"))
                .withFormData("technician_id", sessionManager.getTECHNICIAN_ID())
                .withFormData("reaschedule_reason", reason.getText().toString())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                setResult(Activity.RESULT_OK, new Intent());
                                finish();
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
