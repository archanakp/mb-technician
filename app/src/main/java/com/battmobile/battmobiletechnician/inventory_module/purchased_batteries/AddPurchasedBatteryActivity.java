package com.battmobile.battmobiletechnician.inventory_module.purchased_batteries;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.inventory_module.scrap_batteries.SelectProductModel;
import com.battmobile.battmobiletechnician.utility.FileUtil;
import com.battmobile.battmobiletechnician.utility.NetworkClient;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.battmobile.battmobiletechnician.utility.UploadAPIs;
import com.battmobile.battmobiletechnician.utility.UtilityConstraints;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.marchinram.rxgallery.RxGallery;
import com.rw.velocity.RequestBuilder;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class AddPurchasedBatteryActivity extends AppCompatActivity {
    Menu menu;
    RelativeLayout rlImage;
    public static final int RequestPermissionCode = 1;
    private Uri uri;
    ImageView imgProduct;
    File actualImage;
    String TAG = AddPurchasedBatteryActivity.class.getSimpleName();
    List<String> listCategoryForShow, listSubCategoryForShow;
    List<SelectProductModel> listCategory, listSubCategory;
    ArrayAdapter<String> spCategoryAdapter, spSubCategoryAdapter;
    Spinner spinnerCategory, spinnerSubCategory;
    RadioButton rbLower, rbRetail, rbHigher;
    EditText brandName, brandModel, sku, size, quantity, etCost, etLower, etRetail, etHigher;
    TextInputLayout inputBrandName, inputSKU, inputBrandModel, inputSize;
    SessionManager sessionManager;
    RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchased_battery);
        Utils.setActionBarScreen("Add Purchased Battery", getSupportActionBar());
        loader = findViewById(R.id.loader);
        Velocity.initialize(3);
        sessionManager = new SessionManager(AddPurchasedBatteryActivity.this);
        brandName = findViewById(R.id.brand_name);
        brandModel = findViewById(R.id.brand_model);
        sku = findViewById(R.id.sku);
        size = findViewById(R.id.size);
        quantity = findViewById(R.id.quantity);
        inputBrandName = findViewById(R.id.input_brand_name);
        inputBrandModel = findViewById(R.id.input_brand_model);
        inputSKU = findViewById(R.id.input_sku);
        inputSize = findViewById(R.id.input_size);
        rlImage = findViewById(R.id.rl_add_image);
        imgProduct = findViewById(R.id.img_product);
        etCost = findViewById(R.id.cost);
        etLower = findViewById(R.id.lower);
        etRetail = findViewById(R.id.retail);
        etHigher = findViewById(R.id.higher);
        rbLower = findViewById(R.id.rb_lower);
        rbRetail = findViewById(R.id.rb_retail);
        rbHigher = findViewById(R.id.rb_higher);
        listCategory = new ArrayList<>();
        listCategoryForShow = new ArrayList<>();
        listSubCategory = new ArrayList<>();
        listSubCategoryForShow = new ArrayList<>();
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerSubCategory = findViewById(R.id.spinner_sub_category);

        spCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryForShow);
        spCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spCategoryAdapter);

        spSubCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSubCategoryForShow);
        spSubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategory.setAdapter(spSubCategoryAdapter);
        apiCategory();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listCategory.get(position).getId().trim().length() == 0) {
                    inputBrandName.setVisibility(View.VISIBLE);

                } else {
                    getSubCategoryAPI(listCategory.get(position).getTitle());
                    inputBrandName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listSubCategory.get(position).getId().trim().length() == 0) {
                    inputBrandModel.setVisibility(View.VISIBLE);
                    inputSKU.setVisibility(View.VISIBLE);
                    inputSize.setVisibility(View.VISIBLE);
                } else {
                    inputBrandModel.setVisibility(View.GONE);
                    inputSKU.setVisibility(View.GONE);
                    inputSize.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    final Dialog dialog = new Dialog(AddPurchasedBatteryActivity.this);

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

                            RxGallery.photoCapture(AddPurchasedBatteryActivity.this).subscribe(new Consumer<Uri>() {
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
                            RxGallery.gallery(AddPurchasedBatteryActivity.this, false, RxGallery.MimeType.IMAGE).subscribe(new Consumer<List<Uri>>() {
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
                            dialog.dismiss();
                        }
                    });
                } else {
                    requestPermission();
                }

            }
        });

    }

    private void apiCategory() {
        loader.setVisibility(View.VISIBLE);

        Velocity.get(UtilityConstraints.UrlLocation.SEARCH_BRAND_LIST)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            if (mainObject.getInt("status") == 1) {
                                JSONArray categoryArray = mainObject.getJSONArray("data");
                                for (int i = 0; i < categoryArray.length(); i++) {
                                    JSONObject object = categoryArray.getJSONObject(i);
                                    listCategory.add(new SelectProductModel(
                                            object.getString("id"),
                                            object.getString("brand_name"),
                                            object.getString("brand_name")));
                                    listCategoryForShow.add(object.getString("brand_name"));
                                }
                                listCategory.add(new SelectProductModel("", listCategory.size() > 0 ? "Other" : "N/A", listCategory.size() > 0 ? "Other" : "N/A"));
                                listCategoryForShow.add(listCategoryForShow.size() > 0 ? "Other" : "N/A");
                                spCategoryAdapter.notifyDataSetChanged();
                            } else
                                showAlert(mainObject.getString("message"));
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
                            Toast.makeText(AddPurchasedBatteryActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getSubCategoryAPI(String brand_id) {
        listSubCategory.clear();
        listSubCategoryForShow.clear();
        Velocity.get(UtilityConstraints.UrlLocation.SEARCH_PRODUCT_LIST)
                .withPathParam("filter", brand_id)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            if (mainObject.getInt("status") == 1) {
                                JSONArray categoryArray = mainObject.getJSONArray("data");
                                for (int i = 0; i < categoryArray.length(); i++) {
                                    JSONObject object = categoryArray.getJSONObject(i);
                                    listSubCategory.add(new SelectProductModel(
                                            object.getString("id"),
                                            object.getString("battery_model"),
                                            object.getString("battery_size")));
                                    listSubCategoryForShow.add(object.getString("battery_model") + ", " + object.getString("battery_size"));
                                }
                                listSubCategory.add(new SelectProductModel("", listSubCategory.size() > 0 ? "Other" : "N/A", listCategory.size() > 0 ? "Other" : "N/A"));
                                listSubCategoryForShow.add(listSubCategoryForShow.size() > 0 ? "Other" : "N/A");
                                spSubCategoryAdapter.notifyDataSetChanged();
                                if (listSubCategory.get(0).getId().trim().length() == 0) {
                                    inputBrandModel.setVisibility(View.VISIBLE);
                                    inputSKU.setVisibility(View.VISIBLE);
                                    inputSize.setVisibility(View.VISIBLE);
                                } else {
                                    inputBrandModel.setVisibility(View.GONE);
                                    inputSKU.setVisibility(View.GONE);
                                    inputSize.setVisibility(View.GONE);
                                }
                            } else
                                Toast.makeText(AddPurchasedBatteryActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddPurchasedBatteryActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void showAlert(String message) {
        new AlertDialog.Builder(AddPurchasedBatteryActivity.this)
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
        ActivityCompat.requestPermissions(AddPurchasedBatteryActivity.this, new
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
                        Toast.makeText(AddPurchasedBatteryActivity.this, R.string.permission_granted,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(AddPurchasedBatteryActivity.this, R.string.permission_allow, Toast.LENGTH_SHORT).show();
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
            if (Utils.isNetworkConnected(AddPurchasedBatteryActivity.this)) {
                if (listCategory.get(spinnerCategory.getSelectedItemPosition()).getId().trim().length() == 0 && brandName.getText().toString().trim().length() == 0) {
                    brandName.setError("Enter Brand Name");
                    brandName.requestFocus();
                } else if (listSubCategory.get(spinnerSubCategory.getSelectedItemPosition()).getId().trim().length() == 0 && brandModel.getText().toString().trim().length() == 0) {
                    brandModel.setError("Enter Model Name");
                    brandModel.requestFocus();
                } else if (listSubCategory.get(spinnerSubCategory.getSelectedItemPosition()).getId().trim().length() == 0 && sku.getText().toString().trim().length() == 0) {
                    sku.setError("Enter SKU");
                    sku.requestFocus();
                } else if (listSubCategory.get(spinnerSubCategory.getSelectedItemPosition()).getId().trim().length() == 0 && size.getText().toString().trim().length() == 0) {
                    size.setError("Enter Size");
                    size.requestFocus();
                } else if (quantity.getText().toString().trim().length() == 0) {
                    quantity.setError("Invalid Quantity");
                    quantity.requestFocus();
                } else if (etCost.getText().toString().trim().length() == 0) {
                    etCost.setError("Invalid Cost Price");
                    etCost.requestFocus();
                } else if (etLower.getText().toString().trim().length() == 0) {
                    etLower.setError("Invalid Lower Price");
                    etLower.requestFocus();
                } else if (etRetail.getText().toString().trim().length() == 0) {
                    etRetail.setError("Invalid Retail Price");
                    etRetail.requestFocus();
                } else if (etHigher.getText().toString().trim().length() == 0) {
                    etHigher.setError("Invalid Higher Price");
                    etHigher.requestFocus();
                } else if (uri == null) {
                    Toast.makeText(AddPurchasedBatteryActivity.this, "Upload Image", Toast.LENGTH_SHORT).show();
                } else {
                    loader.setVisibility(View.VISIBLE);
                    compressImage();
                }

            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void compressImage() {
        try {

            actualImage = FileUtil.from(AddPurchasedBatteryActivity.this, uri);
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
        MultipartBody.Part part = MultipartBody.Part.createFormData("battery_image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<ResponseBody> call = uploadAPIs.uploadScrapBatteryImage(part, description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String reponse = response.body().string();
                    JSONObject object = new JSONObject(reponse);
                    Log.e(TAG, "onVelocitySuccess data: " + object);
                    addPurchasedAPI(object.getJSONObject("data").getString("name"));
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

    private void addPurchasedAPI(String image) {
        RequestBuilder requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.ADD_SCRAP_PURCHASED_BATTERY);
        requestBuilder.withFormData("battery_type", "New")
                .withFormData("battery_source", sessionManager.getUSER_TYPE())
                .withFormData("quantity", quantity.getText().toString().trim())
                .withFormData("technician_id", sessionManager.getTECHNICIAN_ID())
                .withFormData("battery_image", image)
                .withFormData("cost_price", etCost.getText().toString().trim())
                .withFormData("lower_retail_sale_price", etLower.getText().toString().trim())
                .withFormData("retail_sale_price", etRetail.getText().toString().trim())
                .withFormData("higher_retail_sale_price", etHigher.getText().toString().trim())
                .withFormData("sale_price_type", (rbLower.isChecked()) ? "Lower Retail" : (rbRetail.isChecked()) ? "Retail" : (rbHigher.isChecked()) ? "Higher Retail" : "");
        if (listCategory.get(spinnerCategory.getSelectedItemPosition()).getId().trim().length() == 0) {
            requestBuilder.withFormData("is_brand_id", "0")
                    .withFormData("brand", brandName.getText().toString().trim());
        } else {
            requestBuilder.withFormData("is_brand_id", "1")
                    .withFormData("brand_id", listCategory.get(spinnerCategory.getSelectedItemPosition()).getId());
        }
        if (listSubCategory.get(spinnerSubCategory.getSelectedItemPosition()).getId().trim().length() == 0) {
            requestBuilder.withFormData("is_product_id", "0")
                    .withFormData("battery_model", brandModel.getText().toString().trim())
                    .withFormData("battery_sku", sku.getText().toString().trim())
                    .withFormData("battery_size", size.getText().toString().trim());
        } else {
            requestBuilder.withFormData("is_product_id", "1")
                    .withFormData("product_id", listSubCategory.get(spinnerSubCategory.getSelectedItemPosition()).getId());
        }
        requestBuilder
                .connect(new Velocity.ResponseListener() {
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
