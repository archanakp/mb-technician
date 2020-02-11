package com.battmobile.battmobiletechnician.overtime_module.collection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.PaginationScrollListener;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CollectionActivity extends AppCompatActivity {

    RecyclerView rvMyTransactions;
    List<CollectionModel> list;
    TextView textViewBalance;
    SessionManager sessionManager;
    CollectionAdapter collectionAdapter;
    RelativeLayout rlEmptyList;
    RelativeLayout rlHeader;
    RelativeLayout loader;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBarMore;
    int total_count = 0, scroll_count = 0, page_no = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Utils.setActionBarScreen("Overtime Collection", getSupportActionBar());
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        rvMyTransactions = findViewById(R.id.rv_my_transactions);
        list = new ArrayList<>();
        rlHeader = findViewById(R.id.rl_my_wallet);
        rlEmptyList = findViewById(R.id.rl_list_empty);
        textViewBalance = findViewById(R.id.tv_balance);
        progressBarMore = findViewById(R.id.more_progress);
        sessionManager = new SessionManager(CollectionActivity.this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvMyTransactions.setLayoutManager(linearLayoutManager);
        collectionAdapter = new CollectionAdapter(this, list);
        rvMyTransactions.setAdapter(collectionAdapter);
        getCollectioAPI(true);
        implementScrollListener();
    }

    private void implementScrollListener() {
        rvMyTransactions.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (scroll_count < total_count) {
                            progressBarMore.setVisibility(View.VISIBLE);
                            getCollectioAPI(false);

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

    private void showAlert(String message) {
        new AlertDialog.Builder(CollectionActivity.this)
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


    private void getCollectioAPI(boolean isFirstTimeCall) {
        if (isFirstTimeCall) {
            loader.setVisibility(View.VISIBLE);
            total_count = 0;
            scroll_count = 0;
            page_no = 1;
            list.clear();
        }
        Velocity.get(UtilityConstraints.UrlLocation.OVERTIME_COLLECTION)
                .withPathParam("id", sessionManager.getTECHNICIAN_ID())
                .withPathParam("page", "" + page_no)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                JSONObject data = mainObject.getJSONObject("data");
                                JSONArray collection = data.getJSONArray("list");
                                textViewBalance.setText("AED " + data.getString("total_collection"));
                                for (int i = 0; i < collection.length(); i++) {
                                    JSONObject object = collection.getJSONObject(i);
                                    list.add(new CollectionModel(
                                            object.getString("id"),
                                            object.getString("job_receipt_no"),
                                            object.getString("created_at"),
                                            object.getString("bonus_amount")));
                                }
                                collectionAdapter.notifyDataSetChanged();
                                progressBarMore.setVisibility(View.GONE);
                                total_count = mainObject.getInt("total_records");
                                scroll_count = scroll_count + mainObject.getInt("count");
                                page_no++;
                                isLoading = false;
                                if (list.size() == 0)
                                    rlEmptyList.setVisibility(View.VISIBLE);
                                else rlEmptyList.setVisibility(View.GONE);

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
                            Toast.makeText(CollectionActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
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