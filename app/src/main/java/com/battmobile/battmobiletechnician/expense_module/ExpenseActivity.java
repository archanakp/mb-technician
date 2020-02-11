package com.battmobile.battmobiletechnician.expense_module;

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
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.job_module.JobActivity;
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

public class ExpenseActivity extends AppCompatActivity {

    RecyclerView rvMyTransactions;
    List<ExpenseModel> list;
    SessionManager sessionManager;
    ExpenseAdapter expenseAdapter;
    RelativeLayout rlEmptyList;
    RelativeLayout rlHeader;
    RelativeLayout loader;
    Menu menu;

    int ADD_REQUEST_CODE = 101, EDIT_REQUEST_CODE = 102;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBarMore;
    int total_count = 0, scroll_count = 0, page_no = 1;
    private boolean isLoading = false;
    String TAG = JobActivity.class.getSimpleName();
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Utils.setActionBarScreen("Expenses", getSupportActionBar());
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        rvMyTransactions = findViewById(R.id.rv_my_transactions);
        list = new ArrayList<>();
        rlHeader = findViewById(R.id.rl_my_wallet);
        rlEmptyList = findViewById(R.id.rl_list_empty);
        sessionManager = new SessionManager(ExpenseActivity.this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvMyTransactions.setLayoutManager(linearLayoutManager);
        progressBarMore = findViewById(R.id.more_progress);
        expenseAdapter = new ExpenseAdapter(this, this, list);
        rvMyTransactions.setAdapter(expenseAdapter);
        rvMyTransactions.setNestedScrollingEnabled(false);
        getExpenseAPI(true);
        implementScrollListener();

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
                            Log.e(TAG, "run: call page no = " + page_no);
                            progressBarMore.setVisibility(View.VISIBLE);
                            getExpenseAPI(false);


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


    public void editProduct(ExpenseModel model) {
        startActivityForResult(new Intent(ExpenseActivity.this, EditExpenseActivity.class)
                .putExtra("id", model.getId())
                .putExtra("date", model.getDate())
                .putExtra("amount", model.getAmount())
                .putExtra("purpose", model.getPurpose())
                .putExtra("description", model.getDescription())
                .putExtra("receipt_image", model.getReceipt_image())
                .putExtra("user_id", model.getUser_id()), EDIT_REQUEST_CODE);
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(ExpenseActivity.this)
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

    private void getExpenseAPI(Boolean isFirstTimeCall) {
        if (isFirstTimeCall) {
            loader.setVisibility(View.VISIBLE);
            total_count = 0;
            scroll_count = 0;
            page_no = 1;
            list.clear();

        }

        Velocity.get(UtilityConstraints.UrlLocation.EXPENSE_LIST)
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
                                JSONArray data = mainObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    list.add(new ExpenseModel(
                                            object.getString("id"),
                                            object.getString("amount"),
                                            object.getString("purpose"),
                                            object.getString("description"),
                                            object.getString("receipt_image"),
                                            object.getString("date"),
                                            object.getString("user")));
                                }
                                expenseAdapter.notifyDataSetChanged();
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
                        Timber.e("onVelocityFailed: " + error.body);
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(ExpenseActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            if (Utils.isNetworkConnected(ExpenseActivity.this)) {
                startActivityForResult(new Intent(ExpenseActivity.this, AddExpenseActivity.class), ADD_REQUEST_CODE);
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
            getExpenseAPI(true);

        } else if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getExpenseAPI(true);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}