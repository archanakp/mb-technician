package com.battmobile.battmobiletechnician.cash_in_hand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.job_module.JobActivity;
import com.battmobile.battmobiletechnician.utility.PaginationScrollListener;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.rw.velocity.RequestBuilder;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CashInHandActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvMyTransactions;
    List<CashInHandModel> list;
    SessionManager sessionManager;
    CashInHandAdapter cashInHandAdapter;
    RelativeLayout rlEmptyList;
    RelativeLayout rlHeader;
    RelativeLayout loader;
    Menu menu;
    int TRANSFER_REQUEST_CODE = 101;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBarMore;
    int total_count = 0, scroll_count = 0, page_no = 1;
    private boolean isLoading = false;
    String TAG = JobActivity.class.getSimpleName();
    private boolean isLastPage = false;
    RelativeLayout cashInHand, transfer, receive;
    TextView tvCashInHand, tvTransfer, tvReceive;
    View viewCashInHand, viewTransfer, viewReceive;
    String type = "cash_in_hand";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Utils.setActionBarScreen("Cash In Hand", getSupportActionBar());
        Velocity.initialize(3);
        cashInHand = findViewById(R.id.cash_in_hand);
        transfer = findViewById(R.id.transfer);
        receive = findViewById(R.id.receive);
        tvCashInHand = findViewById(R.id.tv_cash_in_hand);
        tvTransfer = findViewById(R.id.tv_transfer);
        tvReceive = findViewById(R.id.tv_receive);
        viewCashInHand = findViewById(R.id.view_cash_in_hand);
        viewTransfer = findViewById(R.id.view_transfer);
        viewReceive = findViewById(R.id.view_receive);
        loader = findViewById(R.id.loader);
        rvMyTransactions = findViewById(R.id.rv_my_transactions);
        list = new ArrayList<>();
        rlHeader = findViewById(R.id.rl_my_wallet);
        rlEmptyList = findViewById(R.id.rl_list_empty);
        progressBarMore = findViewById(R.id.more_progress);
        sessionManager = new SessionManager(CashInHandActivity.this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvMyTransactions.setLayoutManager(linearLayoutManager);
        cashInHandAdapter = new CashInHandAdapter(this, list);
        rvMyTransactions.setAdapter(cashInHandAdapter);
        rvMyTransactions.setNestedScrollingEnabled(false);
        getTransactionAPI(true);
        implementScrollListener();
        cashInHand.setOnClickListener(this);
        receive.setOnClickListener(this);
        transfer.setOnClickListener(this);
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
                            Log.e(TAG, "run: call page no = " + page_no);
                            progressBarMore.setVisibility(View.VISIBLE);
                            getTransactionAPI(false);

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
        new AlertDialog.Builder(CashInHandActivity.this)
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


    private void getTransactionAPI(boolean isFirstTimeCall) {
        if (isFirstTimeCall) {
            loader.setVisibility(View.VISIBLE);
            total_count = 0;
            scroll_count = 0;
            page_no = 1;
            list.clear();
        }
        RequestBuilder requestBuilder;
        switch (type) {
            case "cash_in_hand": {
                requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.CASH_IN_HAND_LIST);
                requestBuilder.withFormData("id", sessionManager.getTECHNICIAN_ID())
                        .withPathParam("page", "" + page_no);
                break;
            }
            case "transfer": {
                requestBuilder = Velocity.get(UtilityConstraints.UrlLocation.TRANSFER_LIST);
                requestBuilder.withPathParam("id", sessionManager.getTECHNICIAN_ID())
                        .withPathParam("page", "" + page_no);

                break;
            }
            case "receive": {
                requestBuilder = Velocity.get(UtilityConstraints.UrlLocation.RECEIVE_LIST);
                requestBuilder.withPathParam("id", sessionManager.getTECHNICIAN_ID())
                        .withPathParam("page", "" + page_no);
                break;
            }
            default: {
                requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.CASH_IN_HAND_LIST);
                requestBuilder.withFormData("id", sessionManager.getTECHNICIAN_ID())
                        .withPathParam("page", "" + page_no);
                break;
            }
        }
        requestBuilder.connect(new Velocity.ResponseListener() {
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
                            list.add(new CashInHandModel(
                                    object.getString("id"),
                                    object.getString("agent_name"),
                                    object.getString("tranferred_to_agent"),
                                    object.getString("amount"),
                                    object.getString("created_at"),
                                    object.getBoolean("transferred_cash_received")));
                        }
                        cashInHandAdapter.notifyDataSetChanged();
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
                    Toast.makeText(CashInHandActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_cash_transfer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_cash_transfer) {
            if (Utils.isNetworkConnected(CashInHandActivity.this)) {
                startActivityForResult(new Intent(CashInHandActivity.this, CashTranferActivity.class), TRANSFER_REQUEST_CODE);
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TRANSFER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            cashInHand.callOnClick();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cash_in_hand: {
                type = "cash_in_hand";
                getTransactionAPI(true);
                tvCashInHand.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTransfer.setTextColor(getResources().getColor(R.color.black));
                tvReceive.setTextColor(getResources().getColor(R.color.black));
                viewCashInHand.setVisibility(View.VISIBLE);
                viewTransfer.setVisibility(View.INVISIBLE);
                viewReceive.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.transfer: {
                type = "transfer";
                getTransactionAPI(true);
                tvCashInHand.setTextColor(getResources().getColor(R.color.black));
                tvTransfer.setTextColor(getResources().getColor(R.color.colorAccent));
                tvReceive.setTextColor(getResources().getColor(R.color.black));
                viewCashInHand.setVisibility(View.INVISIBLE);
                viewTransfer.setVisibility(View.VISIBLE);
                viewReceive.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.receive: {
                type = "receive";
                getTransactionAPI(true);
                tvCashInHand.setTextColor(getResources().getColor(R.color.black));
                tvTransfer.setTextColor(getResources().getColor(R.color.black));
                tvReceive.setTextColor(getResources().getColor(R.color.colorAccent));
                viewCashInHand.setVisibility(View.INVISIBLE);
                viewTransfer.setVisibility(View.INVISIBLE);
                viewReceive.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}