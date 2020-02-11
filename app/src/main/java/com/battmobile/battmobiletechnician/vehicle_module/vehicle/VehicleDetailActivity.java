package com.battmobile.battmobiletechnician.vehicle_module.vehicle;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.battmobile.battmobiletechnician.vehicle_module.insurance.InsuranceActivity;
import com.battmobile.battmobiletechnician.vehicle_module.odometer.OdometerActivity;
import com.battmobile.battmobiletechnician.vehicle_module.service.ServiceActivity;

public class VehicleDetailActivity extends AppCompatActivity {
    LinearLayout insurance, service, odometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
        Utils.setActionBarScreen("Vehicle Detail", getSupportActionBar());
        insurance = findViewById(R.id.insurance);
        service = findViewById(R.id.service);
        odometer = findViewById(R.id.odometer);

        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleDetailActivity.this, InsuranceActivity.class)
                        .putExtra("vehicle_id", getIntent().getStringExtra("vehicle_id")));

            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleDetailActivity.this, ServiceActivity.class)
                        .putExtra("vehicle_id", getIntent().getStringExtra("vehicle_id")));
            }
        });
        odometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleDetailActivity.this, OdometerActivity.class)
                        .putExtra("vehicle_id", getIntent().getStringExtra("vehicle_id"))
                        .putExtra("km_completed", getIntent().getStringExtra("km_completed")));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
