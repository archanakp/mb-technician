package com.battmobile.battmobiletechnician.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectBrandActivity extends AppCompatActivity implements BrandAdapter.MyClickListener {

    RecyclerView recyclerView;
    BrandAdapter brandAdapter;
    EditText etSearch;
    SessionManager sessionManager;
    private List<BrandModel> listBrands;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_select_model);
        progress = new ProgressDialog(SelectBrandActivity.this);
        progress.setMessage(UtilityConstraints.Messages.LOADING);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        Velocity.initialize(3);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(SelectBrandActivity.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(SelectBrandActivity.this, R.drawable.divider));
        listBrands = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle_view);
        etSearch = findViewById(R.id.et_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(SelectBrandActivity.this,
                DividerItemDecoration.VERTICAL));

        switch (getIntent().getStringExtra("for")) {
            case "brand":
                Utils.setActionBarScreen("Select Brand", getSupportActionBar());
                brandAPI();
                break;
            case "supplier":
                Utils.setActionBarScreen("Select Supplier", getSupportActionBar());
                supplierAPI();
                break;
            case "technician":
                Utils.setActionBarScreen("Select Technician", getSupportActionBar());
                technicianAPI();
                break;
            case "user":
                Utils.setActionBarScreen("Select Technician", getSupportActionBar());
                userAPI();
                break;
            case "battery":
                etSearch.setHint("Search by Brand Name");
                Utils.setActionBarScreen("Select Battery", getSupportActionBar());
                batteryAPI();
                break;
        }


        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg) {
                // TODO Auto-generated method stub
                if (listBrands.size() > 0)
                    listBrands = brandAdapter.filter(etSearch.getText().toString().trim().toLowerCase(Locale.getDefault()));
            }
        });
    }

    private void brandAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.BRAND_LIST)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new BrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("brand_name")));
                                }
                                brandAdapter = new BrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(brandAdapter);
                                brandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        progress.dismiss();
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void supplierAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.GET_SUPPLIER)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new BrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("supplier_name"),
                                            jsonObject.getString("supplier_phone"),
                                            jsonObject.getString("supplier_address")));
                                }
                                brandAdapter = new BrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(brandAdapter);
                                brandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        progress.dismiss();
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void technicianAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.GET_TECHNICIAN)
                .withPathParam("type", sessionManager.getUSER_TYPE())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new BrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("phone"),
                                            jsonObject.getString("email")));
                                }
                                brandAdapter = new BrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(brandAdapter);
                                brandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        progress.dismiss();
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void userAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.GET_TECHNICIAN)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new BrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("phone"),
                                            jsonObject.getString("email")));
                                }
                                brandAdapter = new BrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(brandAdapter);
                                brandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        progress.dismiss();
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void batteryAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.STOCK_LIST)
                .withPathParam("type", "Scrap")
                .withPathParam("battery_source", sessionManager.getUSER_TYPE())
                .withPathParam("technician_id", sessionManager.getTECHNICIAN_ID())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new BrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("battery_model") + "(" + jsonObject.getString("brand_name") + "), " + jsonObject.getString("battery_size"),
                                            jsonObject.getString("battery_size"),
                                            jsonObject.getString("battery_size")));
                                }
                                brandAdapter = new BrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(brandAdapter);
                                brandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        progress.dismiss();
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

    @Override
    public void onItemClick(int position, View v) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", listBrands.get(position).getId());
        resultIntent.putExtra("name", listBrands.get(position).getTitle());
        resultIntent.putExtra("contact", listBrands.get(position).getMobile());
        resultIntent.putExtra("address", listBrands.get(position).getAddress());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}