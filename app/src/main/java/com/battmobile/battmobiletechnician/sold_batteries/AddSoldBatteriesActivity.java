package com.battmobile.battmobiletechnician.sold_batteries;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import com.battmobile.battmobiletechnician.common.SelectBrandActivity;
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


public class AddSoldBatteriesActivity extends AppCompatActivity {
    Menu menu;
    EditText price, quantity;
    TextView date;
    RelativeLayout loader;
    private DatePickerDialog datePickerDialog;
    private Calendar myCalendar;
    SessionManager sessionManager;
    RelativeLayout rlSelectSource;
    TextView tvSourceTag, tvSource;
    final int BATTERY_REQUEST_CODE = 101;
    String source_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sold_batteries);
        Utils.setActionBarScreen("Add Sold Battery", getSupportActionBar());
        sessionManager = new SessionManager(AddSoldBatteriesActivity.this);
        rlSelectSource = findViewById(R.id.rl_select_source);
        tvSourceTag = findViewById(R.id.tv_source_tag);
        tvSource = findViewById(R.id.tv_source);
        myCalendar = Calendar.getInstance();
        loader = findViewById(R.id.loader);
        Velocity.initialize(3);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        date = findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        rlSelectSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddSoldBatteriesActivity.this, SelectBrandActivity.class)
                                .putExtra("for", "battery"),
                        BATTERY_REQUEST_CODE);
            }
        });


    }

    private void selectDate() {
        datePickerDialog = new DatePickerDialog(AddSoldBatteriesActivity.this, new DatePickerDialog.OnDateSetListener() {

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
            if (Utils.isNetworkConnected(AddSoldBatteriesActivity.this)) {
                if (price.getText().toString().trim().length() == 0) {
                    price.setError("Invalid Price");
                    price.requestFocus();
                } else if (quantity.getText().toString().trim().length() == 0) {
                    quantity.setError("Invalid Quantity");
                    quantity.requestFocus();
                } else if (date.getText().toString().equalsIgnoreCase("Select Date")) {
                    Toast.makeText(AddSoldBatteriesActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
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
        Velocity.post(UtilityConstraints.UrlLocation.ADD_SOLD_BATTERIES)
                .withFormData("sold_by", sessionManager.getTECHNICIAN_ID())
                .withFormData("stock_id", source_id)
                .withFormData("quantity", quantity.getText().toString())
                .withFormData("sold_price", price.getText().toString())
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {

                case BATTERY_REQUEST_CODE:
                    source_id = data.getStringExtra("id");
                    tvSource.setText(data.getStringExtra("name"));
                    break;
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
