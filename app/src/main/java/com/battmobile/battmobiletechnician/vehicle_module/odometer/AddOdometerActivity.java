package com.battmobile.battmobiletechnician.vehicle_module.odometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;


public class AddOdometerActivity extends AppCompatActivity {
    Menu menu;
    EditText currentReading;
    RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_odometer);
        Utils.setActionBarScreen("Add Odometer", getSupportActionBar());
        loader = findViewById(R.id.loader);
        Velocity.initialize(3);
        currentReading = findViewById(R.id.current_reading);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_tick, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_tick) {
            if (Utils.isNetworkConnected(AddOdometerActivity.this)) {
                if (currentReading.getText().toString().trim().length() == 0) {
                    currentReading.setError("Invalid Insurance ID");
                    currentReading.requestFocus();
                } else {
                    loader.setVisibility(View.VISIBLE);
                    addOdometerAPI();
                }
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addOdometerAPI() {
        Velocity.post(UtilityConstraints.UrlLocation.ADD_ODOMETER)
                .withFormData("vehicle_id", getIntent().getStringExtra("vehicle_id"))
                .withFormData("previous_reading", getIntent().getStringExtra("km_completed"))
                .withFormData("current_reading", currentReading.getText().toString())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {
                                setResult(Activity.RESULT_OK, new Intent());
                                finish();
                            }
                            Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        loader.setVisibility(View.GONE);
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
