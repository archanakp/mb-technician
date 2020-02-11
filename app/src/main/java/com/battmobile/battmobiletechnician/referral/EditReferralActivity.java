package com.battmobile.battmobiletechnician.referral;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditReferralActivity extends AppCompatActivity {
    Menu menu;
    EditText source, name, number;
    TextView date;
    RelativeLayout loader;
    private DatePickerDialog datePickerDialog;
    private Calendar myCalendar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);
        Utils.setActionBarScreen("Edit Referral", getSupportActionBar());
        sessionManager = new SessionManager(EditReferralActivity.this);
        myCalendar = Calendar.getInstance();
        loader = findViewById(R.id.loader);
        Velocity.initialize(3);
        source = findViewById(R.id.source);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        date = findViewById(R.id.date);

        source.setText(getIntent().getStringExtra("source"));
        name.setText(getIntent().getStringExtra("name"));
        number.setText(getIntent().getStringExtra("number"));
        date.setText(getIntent().getStringExtra("date"));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
    }

    private void selectDate() {
        datePickerDialog = new DatePickerDialog(EditReferralActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                Date calendarTime = myCalendar.getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date.setText(formatter.format(calendarTime));
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_tick, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_tick) {
            if (Utils.isNetworkConnected(EditReferralActivity.this)) {
                if (source.getText().toString().trim().length() == 0) {
                    source.setError("Invalid Source");
                    source.requestFocus();
                } else if (name.getText().toString().trim().length() == 0) {
                    name.setError("Invalid Name");
                    name.requestFocus();
                } else if (number.getText().toString().trim().length() == 0) {
                    number.setError("Invalid Number");
                    number.requestFocus();
                } else if (date.getText().toString().equalsIgnoreCase("Select Date")) {
                    Toast.makeText(EditReferralActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    loader.setVisibility(View.VISIBLE);
                    updateImageAPI();
                }
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateImageAPI() {
        Velocity.post(UtilityConstraints.UrlLocation.EDIT_REFERRAL)
                .withPathParam("id", getIntent().getStringExtra("id"))
                .withFormData("source", source.getText().toString())
                .withFormData("name", name.getText().toString())
                .withFormData("contact", number.getText().toString())
                .withFormData("date", date.getText().toString())
                .withFormData("created_by", sessionManager.getTECHNICIAN_ID())
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
