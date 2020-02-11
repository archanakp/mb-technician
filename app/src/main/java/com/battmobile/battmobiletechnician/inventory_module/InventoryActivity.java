package com.battmobile.battmobiletechnician.inventory_module;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.inventory_module.purchased_batteries.PurchasedBatteryActivity;
import com.battmobile.battmobiletechnician.inventory_module.scrap_batteries.ScrapBatteryActivity;
import com.battmobile.battmobiletechnician.inventory_module.assigned_batteries.AssignedBatteryActivity;
import com.battmobile.battmobiletechnician.utility.Utils;

public class InventoryActivity extends AppCompatActivity {
    LinearLayout assigned_batteries, scrap_batteries, purchased_batteries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Utils.setActionBarScreen("Inventory", getSupportActionBar());
        assigned_batteries = findViewById(R.id.assigned_batteries);
        scrap_batteries = findViewById(R.id.scrap_batteries);
        purchased_batteries = findViewById(R.id.purchased_batteries);

        assigned_batteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, AssignedBatteryActivity.class));

            }
        });
        scrap_batteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, ScrapBatteryActivity.class));
            }
        });
        purchased_batteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, PurchasedBatteryActivity.class));
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
