package com.battmobile.battmobiletechnician.referral;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.PaginationScrollListener;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.battmobile.battmobiletechnician.vehicle_module.vehicle.VehicleDetailActivity;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ReferralActivity extends AppCompatActivity {

    RecyclerView rvMyTransactions;
    List<ReferralModel> list;
    SessionManager sessionManager;
    ReferralAdapter referralAdapter;
    RelativeLayout rlEmptyList;
    RelativeLayout rlHeader;
    RelativeLayout loader;
    int ADD_REQUEST_CODE = 101, EDIT_REQUEST_CODE = 102;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBarMore;
    int total_count = 0, scroll_count = 0, page_no = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Utils.setActionBarScreen("Referral", getSupportActionBar());
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        rvMyTransactions = findViewById(R.id.rv_my_transactions);
        list = new ArrayList<>();
        rlHeader = findViewById(R.id.rl_my_wallet);
        rlEmptyList = findViewById(R.id.rl_list_empty);
        progressBarMore = findViewById(R.id.more_progress);

        sessionManager = new SessionManager(ReferralActivity.this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvMyTransactions.setLayoutManager(linearLayoutManager);
        referralAdapter = new ReferralAdapter(this, this, list);
        rvMyTransactions.setAdapter(referralAdapter);
        rvMyTransactions.setNestedScrollingEnabled(false);
        getOdometerAPI(true);
        implementScrollListener();
        referralAdapter.setOnItemClickListener(new ReferralAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                startActivity(new Intent(ReferralActivity.this, VehicleDetailActivity.class)
                        .putExtra("vehicle_id", list.get(position).getId()));
            }
        });
    }

    public void editProduct(ReferralModel model) {
        startActivityForResult(new Intent(ReferralActivity.this, EditReferralActivity.class)
                .putExtra("id", model.getId())
                .putExtra("source", model.getSource())
                .putExtra("name", model.getName())
                .putExtra("number", model.getNumber())
                .putExtra("created_by", model.getCreated_by())
                .putExtra("date", model.getDate()), EDIT_REQUEST_CODE);
    }

    private void implementScrollListener() {
        rvMyTransactions.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (scroll_count < total_count) {
                            progressBarMore.setVisibility(View.VISIBLE);
                            getOdometerAPI(false);

                        }
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return 1;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_add) {
            if (Utils.isNetworkConnected(ReferralActivity.this)) {
                startActivityForResult(new Intent(ReferralActivity.this, AddReferralActivity.class)
                        , ADD_REQUEST_CODE);
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getOdometerAPI(true);

        } else if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getOdometerAPI(true);

        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(ReferralActivity.this)
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

    private void getOdometerAPI(boolean isFirstTimeCall) {
        if (isFirstTimeCall) {
            loader.setVisibility(View.VISIBLE);
            total_count = 0;
            scroll_count = 0;
            page_no = 1;
            list.clear();
        }

        Velocity.get(UtilityConstraints.UrlLocation.REFERRAL_LIST)
                .withPathParam("page", "" + page_no)
                .withPathParam("id", sessionManager.getTECHNICIAN_ID())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                JSONArray data = mainObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    list.add(new ReferralModel(
                                            object.getString("id"),
                                            object.getString("source"),
                                            object.getString("name"),
                                            object.getString("contact"),
                                            object.getString("created_by"),
                                            object.getString("date")));
                                }
                                referralAdapter.notifyDataSetChanged();
                                progressBarMore.setVisibility(View.GONE);
                                total_count = mainObject.getInt("total_records");
                                scroll_count = scroll_count + mainObject.getInt("count");
                                page_no++;
                                isLoading = false;
                            } else {
                                if (page_no != 1) {
                                    progressBarMore.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Unable to load more data", Toast.LENGTH_SHORT).show();
                                } else {
                                    showAlert(mainObject.getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        loader.setVisibility(View.GONE);
                        progressBarMore.setVisibility(View.GONE);
                        Timber.e("onVelocityFailed: " + error.body);
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(ReferralActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
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