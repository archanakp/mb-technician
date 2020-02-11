package com.battmobile.battmobiletechnician.vehicle_module.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.FileUtil;
import com.battmobile.battmobiletechnician.utility.NetworkClient;
import com.battmobile.battmobiletechnician.utility.UploadAPIs;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.marchinram.rxgallery.RxGallery;
import com.rw.velocity.RequestBuilder;
import com.rw.velocity.Velocity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditServiceActivity extends AppCompatActivity {
    Menu menu;
    String image_url = "";
    public static final int RequestPermissionCode = 1;
    private Uri uri;
    ImageView imgProduct;
    File actualImage;
    String TAG = EditServiceActivity.class.getSimpleName();
    EditText serviceID, kmCompleted, nextServiceKm, receiptID;
    TextView serviceDate, nextServiceDate;
    RelativeLayout loader;
    private DatePickerDialog datePickerDialog;
    private Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        Utils.setActionBarScreen("Edit Service", getSupportActionBar());
        myCalendar = Calendar.getInstance();
        loader = findViewById(R.id.loader);
        Velocity.initialize(3);

        serviceID = findViewById(R.id.service_id);
        kmCompleted = findViewById(R.id.km_completed);
        nextServiceKm = findViewById(R.id.next_service_km);
        serviceDate = findViewById(R.id.service_date);
        nextServiceDate = findViewById(R.id.next_service_date);
        receiptID = findViewById(R.id.receipt_id);
        imgProduct = findViewById(R.id.img_product);

        serviceID.setText(getIntent().getStringExtra("service_id"));
        kmCompleted.setText(getIntent().getStringExtra("km_completed"));
        nextServiceKm.setText(getIntent().getStringExtra("next_service_km"));
        serviceDate.setText(getIntent().getStringExtra("service_date"));
        nextServiceDate.setText(getIntent().getStringExtra("next_service_date"));
        receiptID.setText(getIntent().getStringExtra("receipt_id"));
        image_url = getIntent().getStringExtra("receipt_image");

        Picasso.with(EditServiceActivity.this).load(image_url).placeholder(R.drawable.default_dp).into(imgProduct);

        serviceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectServiceDate();
            }
        });
        nextServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNextServiceDate();
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    final Dialog dialog = new Dialog(EditServiceActivity.this);
                    // Include dialog.xml file

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_gallary);
                    // Set dialog title
                    dialog.setTitle(null);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                    // set values for custom dialog components - text, image and button

                    TextView textCamera = dialog.findViewById(R.id.camera);
                    TextView textGallery = dialog.findViewById(R.id.gallery);
                    TextView textCancel = dialog.findViewById(R.id.cancel);
                    dialog.show();

                    textCamera.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onClick(View v) {

                            RxGallery.photoCapture(EditServiceActivity.this).subscribe(new Consumer<Uri>() {
                                @Override
                                public void accept(Uri uri) throws Exception {
                                    doStuffWithUris(uri);
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
                            RxGallery.gallery(EditServiceActivity.this, false, RxGallery.MimeType.IMAGE).subscribe(new Consumer<List<Uri>>() {
                                @Override
                                public void accept(List<Uri> uris) throws Exception {
                                    doStuffWithUris(uris.get(0));
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
        });

    }

    private void selectServiceDate() {
        datePickerDialog = new DatePickerDialog(EditServiceActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                Date date = myCalendar.getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                serviceDate.setText(formatter.format(date));
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void selectNextServiceDate() {
        datePickerDialog = new DatePickerDialog(EditServiceActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                Date date = myCalendar.getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                nextServiceDate.setText(formatter.format(date));
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void doStuffWithUris(Uri u) {
        uri = u;
        imgProduct.setImageURI(u);
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
        ActivityCompat.requestPermissions(EditServiceActivity.this, new
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
                        Toast.makeText(EditServiceActivity.this, R.string.permission_granted,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(EditServiceActivity.this, R.string.permission_allow, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_tick, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_tick) {
            if (Utils.isNetworkConnected(EditServiceActivity.this)) {
                if (serviceID.getText().toString().trim().length() == 0) {
                    serviceID.setError("Invalid Service ID");
                    serviceID.requestFocus();
                } else if (kmCompleted.getText().toString().trim().length() == 0) {
                    kmCompleted.setError("Invalid Km Completed");
                    kmCompleted.requestFocus();
                } else if (nextServiceKm.getText().toString().trim().length() == 0) {
                    nextServiceKm.setError("Invalid Next Service Km");
                    nextServiceKm.requestFocus();
                } else if (serviceDate.getText().toString().equalsIgnoreCase("Select Date")) {
                    Toast.makeText(EditServiceActivity.this, "Select Service Date", Toast.LENGTH_SHORT).show();
                } else if (nextServiceDate.getText().toString().equalsIgnoreCase("Select Date")) {
                    Toast.makeText(EditServiceActivity.this, "Select Next Service Date", Toast.LENGTH_SHORT).show();
                } else if (receiptID.getText().toString().trim().length() == 0) {
                    receiptID.setError("Invalid Receipt ID");
                    receiptID.requestFocus();
                } else {
                    if (uri == null) {
                        loader.setVisibility(View.VISIBLE);
                        updateImageAPI("");
                    } else {
                        loader.setVisibility(View.VISIBLE);
                        compressImage();
                    }
                }
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void compressImage() {
        try {

            actualImage = FileUtil.from(EditServiceActivity.this, uri);
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
                int file_size = Integer.parseInt(String.valueOf(actualImage.length() / 1024));
                int c_file_size = Integer.parseInt(String.valueOf(compressedImage.length() / 1024));
                Log.e(TAG, "generateWarrentywithImage: before" + file_size + ", after = " + c_file_size);
                uploadToServer(compressedImage);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void uploadToServer(File file) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<ResponseBody> call = uploadAPIs.uploadServiceImage(part, description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    String reponse = response.body().string();
                    JSONObject object = new JSONObject(reponse);
                    Log.e(TAG, "onVelocitySuccess data: " + object);
                    updateImageAPI(object.getJSONObject("data").getString("name"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onResponse: error ");
            }
        });
    }

    private void updateImageAPI(String image) {
        RequestBuilder requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.EDIT_SERVICE);
        requestBuilder.withPathParam("id", getIntent().getStringExtra("id"))
                .withFormData("vehicle_id", getIntent().getStringExtra("vehicle_id"))
                .withFormData("service_id", serviceID.getText().toString())
                .withFormData("km_completed", kmCompleted.getText().toString())
                .withFormData("service_date", serviceDate.getText().toString())
                .withFormData("next_service_date", nextServiceDate.getText().toString())
                .withFormData("next_service_km", nextServiceKm.getText().toString())
                .withFormData("receipt_id", receiptID.getText().toString());
        if (image.trim().length() > 0)
            requestBuilder.withFormData("receipt_image", image);
        requestBuilder.connect(new Velocity.ResponseListener() {
            @Override
            public void onVelocitySuccess(Velocity.Response response) {
                loader.setVisibility(View.GONE);
                try {
                    String sd = new String(response.body.getBytes());
                    JSONObject data = new JSONObject(sd);
                    if (!data.getBoolean("error")) {
                        setResult(Activity.RESULT_OK, new Intent());
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVelocityFailed(Velocity.Response error) {
                loader.setVisibility(View.GONE);
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
