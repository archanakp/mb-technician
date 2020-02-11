package com.battmobile.battmobiletechnician.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.BuildConfig;
import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.cash_in_hand.CashInHandActivity;
import com.battmobile.battmobiletechnician.chat.ChatUserList;
import com.battmobile.battmobiletechnician.chat.MessageUserList;
import com.battmobile.battmobiletechnician.expense_module.ExpenseActivity;
import com.battmobile.battmobiletechnician.incentive_module.IncentivesActivity;
import com.battmobile.battmobiletechnician.inventory_module.InventoryActivity;
import com.battmobile.battmobiletechnician.job_module.JobActivity;
import com.battmobile.battmobiletechnician.notification.MyNotificationManager;
import com.battmobile.battmobiletechnician.overtime_module.overtime.OvertimeActivity;
import com.battmobile.battmobiletechnician.referral.ReferralActivity;
import com.battmobile.battmobiletechnician.sold_batteries.SoldBatteriesActivity;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.battmobile.battmobiletechnician.vehicle_module.vehicle.VehicleActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends Activity implements View.OnClickListener {
    LinearLayout jobs, CashInHand;
    RelativeLayout expense, inventory, vehicle, overtime, incentives, sales, referrals,chat_layout;
    SessionManager sessionManager;
    TextView tvName, tvEmail;
    ImageView imgMenu;
    CircleImageView profileImage;
    public static FirebaseDatabase secondaryDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(HomeActivity.this);
        profileImage = findViewById(R.id.profile_image);
        imgMenu = findViewById(R.id.img_menu);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        jobs = findViewById(R.id.job_module);
        CashInHand = findViewById(R.id.cash_in_hand_module);
        expense = findViewById(R.id.expense_module);
        inventory = findViewById(R.id.inventory_module);
        vehicle = findViewById(R.id.vehicle_module);
        chat_layout = findViewById(R.id.msg_layout);
        overtime = findViewById(R.id.overtime_module);
        incentives = findViewById(R.id.incentives_module);
        sales = findViewById(R.id.sales_module);
        referrals = findViewById(R.id.referrals_module);
        imgMenu.setOnClickListener(this);
        jobs.setOnClickListener(this);
        CashInHand.setOnClickListener(this);
        expense.setOnClickListener(this);
        inventory.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        overtime.setOnClickListener(this);
        incentives.setOnClickListener(this);
        sales.setOnClickListener(this);
        referrals.setOnClickListener(this);
        chat_layout.setOnClickListener(this);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(BuildConfig.appid) // Required for Analytics.
                .setApiKey(BuildConfig.apikey) // Required for Auth.
                .setDatabaseUrl(BuildConfig.url) // Required for RTDB.
                .build();

        try{
            FirebaseApp.initializeApp(this  , options, BuildConfig.name);
            FirebaseApp app = FirebaseApp.getInstance(BuildConfig.name);
            secondaryDatabase = FirebaseDatabase.getInstance(app);
        }
        catch (IllegalStateException e)
        {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvName.setText(sessionManager.getFIRST_NAME() + " " + sessionManager.getLAST_NAME());
        tvEmail.setText(sessionManager.getEMAIL());
        Picasso.with(HomeActivity.this).load(sessionManager.getPROFILE_IMAGE()).placeholder(R.drawable.profile_pic).into(profileImage);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_menu: {
                PopupMenu popup = new PopupMenu(HomeActivity.this, imgMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_home, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_item_profile) {
                            if (Utils.isNetworkConnected(HomeActivity.this)) {
                                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                            } else
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                        } else if (id == R.id.menu_item_logout) {
                            sessionManager.logoutUser();
                        }
                        return true;
                    }
                });
                popup.show();
            }
            break;
            case R.id.job_module:
                startActivity(new Intent(HomeActivity.this, JobActivity.class).putExtra("id",sessionManager.getTECHNICIAN_ID()));
                break;
            case R.id.cash_in_hand_module:
                startActivity(new Intent(HomeActivity.this, CashInHandActivity.class));
                break;
            case R.id.expense_module:
                startActivity(new Intent(HomeActivity.this, ExpenseActivity.class));
                break;
            case R.id.inventory_module:
                startActivity(new Intent(HomeActivity.this, InventoryActivity.class));
                break;
            case R.id.vehicle_module:
                startActivity(new Intent(HomeActivity.this, VehicleActivity.class));
                break;
            case R.id.overtime_module:
                startActivity(new Intent(HomeActivity.this, OvertimeActivity.class));
                break;
            case R.id.incentives_module:
                startActivity(new Intent(HomeActivity.this, IncentivesActivity.class));
                break;
            case R.id.sales_module:
                startActivity(new Intent(HomeActivity.this, SoldBatteriesActivity.class));
                break;
            case R.id.referrals_module:
                startActivity(new Intent(HomeActivity.this, ReferralActivity.class));
                break;

            case R.id.msg_layout :
                startActivity(new Intent(HomeActivity.this, MessageUserList.class));
                break;


        }

    }
}
