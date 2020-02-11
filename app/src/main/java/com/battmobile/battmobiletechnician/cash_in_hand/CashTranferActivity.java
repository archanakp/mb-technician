package com.battmobile.battmobiletechnician.cash_in_hand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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

import timber.log.Timber;

public class CashTranferActivity extends AppCompatActivity {
    RelativeLayout rlSelectSource;
    TextView tvSourceTag, tvSource, tvAdd, tvCancel;
    EditText etAmount;
    final int USER_REQUEST_CODE = 101;
    RelativeLayout loader;
    SessionManager sessionManager;
    String source_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_transfer);
        Utils.setActionBarScreen("Cash Transfer", getSupportActionBar());
        rlSelectSource = findViewById(R.id.rl_select_source);
        tvSourceTag = findViewById(R.id.tv_source_tag);
        tvSource = findViewById(R.id.tv_source);
        etAmount = findViewById(R.id.et_amount);
        sessionManager = new SessionManager(CashTranferActivity.this);
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        tvAdd = findViewById(R.id.tv_add);
        tvCancel = findViewById(R.id.tv_cancel);
        rlSelectSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CashTranferActivity.this, SelectBrandActivity.class)
                                .putExtra("for", "user"),
                        USER_REQUEST_CODE);
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (source_id.trim().length() == 0) {
                    if (source_id.trim().length() == 0)
                        Toast.makeText(CashTranferActivity.this, "Select Technician", Toast.LENGTH_SHORT).show();
                } else if (etAmount.getText().toString().trim().length() == 0) {
                    etAmount.setError("Invalid Quantity");
                    etAmount.requestFocus();
                } else {
                    transferAPI(etAmount.getText().toString().trim());
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void transferAPI(String amount) {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.CASH_TRANSFER)
                .withFormData("user_id", sessionManager.getTECHNICIAN_ID())
                .withFormData("transferred_to", source_id)
                .withFormData("amount", amount)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {

                case USER_REQUEST_CODE:
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
