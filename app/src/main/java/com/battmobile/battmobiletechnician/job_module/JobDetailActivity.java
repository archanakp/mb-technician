package com.battmobile.battmobiletechnician.job_module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class JobDetailActivity extends AppCompatActivity {
    int RESCHEDULE_REQUEST_CODE = 101, PROCESS_REQUEST_CODE = 102;
    TextView name, address, carInfo, issue, price, note, requestOvertime, reschedule, process;
    ImageView imgCall;
    RelativeLayout loader, rlMain;
    boolean isDetailChnaged = false;
    JSONObject jsonObject;
    SessionManager sessionManager;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        imgCall = findViewById(R.id.img_call);
        loader = findViewById(R.id.loader);
        rlMain = findViewById(R.id.rl_main);
        carInfo = findViewById(R.id.car_info);
        issue = findViewById(R.id.issue);
        price = findViewById(R.id.price);
        note = findViewById(R.id.note);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        reschedule = findViewById(R.id.reschedule);
        process = findViewById(R.id.process);
        requestOvertime = findViewById(R.id.request_overtime);
        id=getIntent().getStringExtra("id");
        Velocity.initialize(3);

        sessionManager = new SessionManager(JobDetailActivity.this);
        Utils.setActionBarScreen("Job Detail", getSupportActionBar());
        getDetailAPI();
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + jsonObject.getString("customer_phone"))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(JobDetailActivity.this, RescheduleActivity.class)
                        .putExtra("id", id), RESCHEDULE_REQUEST_CODE);
            }
        });
        requestOvertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOvertimeAPI();
            }
        });
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(new Intent(JobDetailActivity.this, ProcessJobActivity.class)
                                    .putExtra("id", id)
                                    .putExtra("brand", jsonObject.getJSONArray("new_battery").getJSONObject(0).getString("battery_brand"))
                                    .putExtra("size", jsonObject.getJSONArray("new_battery").getJSONObject(0).getString("battery_size"))
                            , RESCHEDULE_REQUEST_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2

        if (requestCode == PROCESS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getDetailAPI();
            isDetailChnaged = true;
        } else if (requestCode == RESCHEDULE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getDetailAPI();
            isDetailChnaged = true;
        }
    }

    private void getDetailAPI() {
        loader.setVisibility(View.VISIBLE);

        Velocity.get(UtilityConstraints.UrlLocation.JOB_DETAIL)
                .withPathParam("id", id)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);
                        try {
                            String data = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(data);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                JSONArray dataArray = mainObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    jsonObject = dataArray.getJSONObject(i);
                                    name.setText(jsonObject.getString("customer_name"));
                                    address.setText(jsonObject.getString("location"));
                                    carInfo.setText(jsonObject.getString("vehicle_name") + " " + jsonObject.getString("vehicle_model") + " " + jsonObject.getString("vehicle_number"));
                                    issue.setText(jsonObject.getString("issues_faced"));
                                    price.setText(jsonObject.getString("price_offered"));
                                    note.setText(jsonObject.getString("quick_note"));
                                    if (jsonObject.getString("job_status").equalsIgnoreCase("Pending")) {
                                        reschedule.setVisibility(View.VISIBLE);
                                        process.setVisibility(View.VISIBLE);
                                        requestOvertime.setVisibility(View.VISIBLE);
                                        if (jsonObject.getBoolean("overtime")) {
                                            requestOvertime.setVisibility(View.GONE);
                                        } else
                                            requestOvertime.setVisibility(View.VISIBLE);
                                    } else if (jsonObject.getString("job_status").equalsIgnoreCase("Completed")) {
                                        reschedule.setVisibility(View.GONE);
                                        process.setVisibility(View.GONE);
                                        requestOvertime.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                showAlert(mainObject.getString("message"));
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
                            Toast.makeText(JobDetailActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void requestOvertimeAPI() {

        loader.setVisibility(View.VISIBLE);

        Velocity.post(UtilityConstraints.UrlLocation.REQUEST_OVERTIME)
                .withFormData("job_id", id)
                .withFormData("agent_id", sessionManager.getTECHNICIAN_ID())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        rlMain.setVisibility(View.VISIBLE);
                        try {
                            String data = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(data);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                getDetailAPI();
                            } else {
                                showAlert(mainObject.getString("message"));
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
                            Toast.makeText(JobDetailActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(JobDetailActivity.this)
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
                        getDetailAPI();
                    }
                }).create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isDetailChnaged)
            setResult(Activity.RESULT_OK, new Intent());
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isDetailChnaged)
            setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}
