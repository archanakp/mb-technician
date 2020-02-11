package com.battmobile.battmobiletechnician.job_module;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.FileUtil;
import com.battmobile.battmobiletechnician.utility.NetworkClient;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UploadAPIs;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.marchinram.rxgallery.RxGallery;
import com.rw.velocity.RequestBuilder;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProcessJobActivity extends AppCompatActivity {
    Button submit;
    TextInputEditText brand, size, confirmBrand, amount, firstDigit, lastDigit, transactionNo, scrapBrand,
            scrapModel, scrapWeight, manualPrice;
    CheckBox cashReceived, overtime;
    TextInputLayout inputLayoutFirstDigit, inputLayoutLastDigit, inputLayoutTransactionNo,
            inputLayoutScrapBrand, inputLayoutScrapModel, inputLayoutScrapWeight;
    ImageView imgCustomer, imgMulkiya;
    RadioGroup paymentMode, scrap;
    RadioButton cash, card, scrapYes, scrapNo;
    RelativeLayout loader, rlScrapImage;
    SessionManager sessionManager;
    public static final int RequestPermissionCode = 1;
    private File fileCustomer, fileMulkiya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_job);
        initData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!confirmBrand.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("brand"))) {
                    confirmBrand.setError("Invalid Brand");
                    confirmBrand.requestFocus();
                } else if (amount.getText().toString().trim().length() == 0) {
                    amount.setError("Invalid Amount");
                    amount.requestFocus();
                } else if (card.isChecked() && firstDigit.getText().toString().trim().length() != 4) {
                    firstDigit.setError("Invalid digit");
                    firstDigit.requestFocus();
                } else if (card.isChecked() && lastDigit.getText().toString().trim().length() != 4) {
                    lastDigit.setError("Invalid digit");
                    lastDigit.requestFocus();
                } else if (card.isChecked() && transactionNo.getText().toString().trim().length() == 0) {
                    transactionNo.setError("Invalid Transaction No");
                    transactionNo.requestFocus();
                } else if (fileCustomer == null) {
                    Toast.makeText(ProcessJobActivity.this, "Upload Customer Picture", Toast.LENGTH_SHORT).show();
                } else if (fileMulkiya == null) {
                    Toast.makeText(ProcessJobActivity.this, "Upload Mulkiya Picture", Toast.LENGTH_SHORT).show();
                } else if (scrapYes.isChecked() && scrapBrand.getText().toString().trim().length() == 0) {
                    scrapBrand.setError("Invalid Brand");
                    scrapBrand.requestFocus();
                } else if (scrapYes.isChecked() && scrapModel.getText().toString().trim().length() == 0) {
                    scrapModel.setError("Invalid Model");
                    scrapModel.requestFocus();
                } else if (scrapYes.isChecked() && scrapWeight.getText().toString().trim().length() == 0) {
                    scrapWeight.setError("Invalid Weight");
                    scrapWeight.requestFocus();
                } else if (manualPrice.getText().toString().trim().length() == 0) {
                    manualPrice.setError("Invalid Price");
                    manualPrice.requestFocus();
                } else {
                    uploadCustomerMulkiyaImageToServer("Customer", fileCustomer);
                }
            }
        });
    }

    private void initData() {
        Utils.setActionBarScreen("Process", getSupportActionBar());
        brand = findViewById(R.id.brand);
        size = findViewById(R.id.size);
        confirmBrand = findViewById(R.id.confirm_brand);
        amount = findViewById(R.id.amount);
        firstDigit = findViewById(R.id.first_digit);
        lastDigit = findViewById(R.id.last_digit);
        transactionNo = findViewById(R.id.transction_no);
        scrapBrand = findViewById(R.id.scrap_brand);
        scrapModel = findViewById(R.id.scrap_model);
        scrapWeight = findViewById(R.id.scrap_weight);
        manualPrice = findViewById(R.id.manual_price);
        cashReceived = findViewById(R.id.cash_received);
        overtime = findViewById(R.id.overtime);
        imgCustomer = findViewById(R.id.img_customer);
        imgMulkiya = findViewById(R.id.img_mulkiya);
        paymentMode = findViewById(R.id.payment_mode);
        scrap = findViewById(R.id.scrap);
        cash = findViewById(R.id.rb_cash);
        card = findViewById(R.id.rb_card);
        scrapYes = findViewById(R.id.rb_yes);
        scrapNo = findViewById(R.id.rb_no);
        loader = findViewById(R.id.loader);
        submit = findViewById(R.id.submit);
        rlScrapImage = findViewById(R.id.rl_scrap_image);
        inputLayoutFirstDigit = findViewById(R.id.input_layout_first_digit);
        inputLayoutLastDigit = findViewById(R.id.input_layout_last_digit);
        inputLayoutTransactionNo = findViewById(R.id.input_layout_transction_no);
        inputLayoutScrapBrand = findViewById(R.id.input_layout_scrap_brand);
        inputLayoutScrapModel = findViewById(R.id.input_layout_scrap_model);
        inputLayoutScrapWeight = findViewById(R.id.input_layout_scrap_weight);
        sessionManager = new SessionManager(ProcessJobActivity.this);
        Velocity.initialize(3);
        brand.setText(getIntent().getStringExtra("brand"));
        size.setText(getIntent().getStringExtra("size"));
        paymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("Cash")) {
                    inputLayoutFirstDigit.setVisibility(View.GONE);
                    inputLayoutLastDigit.setVisibility(View.GONE);
                    inputLayoutTransactionNo.setVisibility(View.GONE);
                } else {
                    inputLayoutFirstDigit.setVisibility(View.VISIBLE);
                    inputLayoutLastDigit.setVisibility(View.VISIBLE);
                    inputLayoutTransactionNo.setVisibility(View.VISIBLE);
                }
            }
        });
        scrap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("No")) {
                    inputLayoutScrapBrand.setVisibility(View.GONE);
                    inputLayoutScrapModel.setVisibility(View.GONE);
                    inputLayoutScrapWeight.setVisibility(View.GONE);
                    rlScrapImage.setVisibility(View.GONE);
                } else {
                    inputLayoutScrapBrand.setVisibility(View.VISIBLE);
                    inputLayoutScrapModel.setVisibility(View.VISIBLE);
                    inputLayoutScrapWeight.setVisibility(View.VISIBLE);
                    rlScrapImage.setVisibility(View.VISIBLE);
                }
            }
        });

        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog("Customer");
            }
        });
        imgMulkiya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog("Mulkiya");
            }
        });
    }

    private void imageDialog(final String type) {
        if (checkPermission()) {
            final Dialog dialog = new Dialog(ProcessJobActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_gallary);
            dialog.setTitle(null);
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

            TextView textCamera = dialog.findViewById(R.id.camera);
            TextView textGallery = dialog.findViewById(R.id.gallery);
            TextView textCancel = dialog.findViewById(R.id.cancel);
            dialog.show();

            textCamera.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onClick(View v) {

                    RxGallery.photoCapture(ProcessJobActivity.this).subscribe(new Consumer<Uri>() {
                        @Override
                        public void accept(Uri uri) throws Exception {
                            compressImage(type, uri);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
                    dialog.dismiss();
                }
            });

            textGallery.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onClick(View v) {
                    // Close dialog
                    RxGallery.gallery(ProcessJobActivity.this, false, RxGallery.MimeType.IMAGE).subscribe(new Consumer<List<Uri>>() {
                        @Override
                        public void accept(List<Uri> uris) throws Exception {
                            compressImage(type, uris.get(0));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
                    dialog.dismiss();
                }
            });
            textCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    dialog.dismiss();
                }
            });
        } else {
            requestPermission();
        }
    }

    public void compressImage(String type, Uri uri) {
        File actualImage;
        try {
            if (type.equals("Customer")) {
                actualImage = FileUtil.from(ProcessJobActivity.this, uri);
            } else if (type.equals("Mulkiya")) {
                actualImage = FileUtil.from(ProcessJobActivity.this, uri);
            } else {
                actualImage = FileUtil.from(ProcessJobActivity.this, uri);
            }

            if (actualImage == null) {
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
            } else {
                File compressedImage = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(actualImage);

                if (type.equals("Customer")) {
                    fileCustomer = compressedImage;
                    imgCustomer.setImageBitmap(BitmapFactory.decodeFile(fileCustomer.getAbsolutePath()));
                } else if (type.equals("Mulkiya")) {
                    fileMulkiya = compressedImage;
                    imgMulkiya.setImageBitmap(BitmapFactory.decodeFile(fileMulkiya.getAbsolutePath()));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void uploadCustomerMulkiyaImageToServer(final String type, File file) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        RequestBody requestBodyJobID = RequestBody.create(MediaType.parse("multipart/form-data"), getIntent().getStringExtra("id"));
        RequestBody requestBodyType = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        Call<ResponseBody> call = uploadAPIs.uploadJobImage(part, description, requestBodyJobID, requestBodyType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String reponse = response.body().string();
                    JSONObject object = new JSONObject(reponse);
                    if (type.equals("Customer")) {
                        uploadCustomerMulkiyaImageToServer("Mulkiya", fileMulkiya);
                    } else {
                        processJobAPI();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.d("onResponse: error ");
            }
        });
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ProcessJobActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(ProcessJobActivity.this, R.string.permission_granted,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(ProcessJobActivity.this, R.string.permission_allow, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void processJobAPI() {
        loader.setVisibility(View.VISIBLE);
        RequestBuilder requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.PROCESS_JOB);
        requestBuilder.withFormData("technician_id", sessionManager.getTECHNICIAN_ID())
                .withFormData("job_id", getIntent().getStringExtra("id"))
                .withFormData("payment_method", cash.isChecked() ? "Cash" : "Card")
                .withFormData("manual_price", manualPrice.getText().toString())
                .withFormData("amount", amount.getText().toString())
                .withFormData("overtime", overtime.isChecked() ? "1" : "0")
                .withFormData("cash_received", cashReceived.isChecked() ? "1" : "0")
                .withFormData("is_scrap", scrapYes.isChecked() ? "1" : "0");
        if (cash.isChecked()) {
            requestBuilder.withFormData("first_4", firstDigit.getText().toString())
                    .withFormData("last_4", lastDigit.getText().toString())
                    .withFormData("auth_code", transactionNo.getText().toString());
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
                        startActivity(new Intent(ProcessJobActivity.this, AddScrapBatteryActivity.class)
                                .putExtra("job_id", getIntent().getStringExtra("id")));
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
