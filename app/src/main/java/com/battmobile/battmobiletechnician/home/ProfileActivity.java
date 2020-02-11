package com.battmobile.battmobiletechnician.home;

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
import android.os.Environment;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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

public class ProfileActivity extends AppCompatActivity {
    TextInputEditText email, firstname, lastname, phone;
    TextView changePassword;
    RelativeLayout loader;
    Button update;
    SessionManager sessionManager;
    public static final int RequestPermissionCode = 1;
    CircleImageView profileImage;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Utils.setActionBarScreen("My Profile", getSupportActionBar());
        sessionManager = new SessionManager(ProfileActivity.this);
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        profileImage = findViewById(R.id.profile_image);
        email = findViewById(R.id.email);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phone = findViewById(R.id.phone);
        changePassword = findViewById(R.id.changePassword);
        update = findViewById(R.id.update);
        email.setText(sessionManager.getEMAIL());
        firstname.setText(sessionManager.getFIRST_NAME());
        lastname.setText(sessionManager.getLAST_NAME());
        phone.setText(sessionManager.getPHONE());
        Picasso.with(ProfileActivity.this).load(sessionManager.getPROFILE_IMAGE()).placeholder(R.drawable.profile_pic).into(profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    final Dialog dialog = new Dialog(ProfileActivity.this);
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

                            RxGallery.photoCapture(ProfileActivity.this).subscribe(new Consumer<Uri>() {
                                @Override
                                public void accept(Uri u) throws Exception {
                                    uri = u;
                                    profileImage.setImageURI(uri);

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
                            RxGallery.gallery(ProfileActivity.this, false, RxGallery.MimeType.IMAGE).subscribe(new Consumer<List<Uri>>() {
                                @Override
                                public void accept(List<Uri> uris) throws Exception {
                                    uri = uris.get(0);
                                    profileImage.setImageURI(uris.get(0));

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
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_chnage_password);
                // Set dialog title
                dialog.setTitle(null);
                final TextInputEditText cPassword = dialog.findViewById(R.id.cPassword);
                final TextInputEditText nPassword = dialog.findViewById(R.id.nPassword);
                Button change = dialog.findViewById(R.id.change);
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cPassword.getText().toString().trim().length() < 6) {
                            cPassword.setError(UtilityConstraints.CustomToast.INVALID_PASSWORD);
                            cPassword.requestFocus();
                        } else if (nPassword.getText().toString().trim().length() < 6) {
                            nPassword.setError(UtilityConstraints.CustomToast.INVALID_PASSWORD);
                            nPassword.requestFocus();
                        } else {
                            dialog.dismiss();
                            changePasswordAPI(cPassword.getText().toString(), nPassword.getText().toString());
                        }
                    }
                });
                dialog.show();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstname.getText().toString().trim().length() == 0) {
                    firstname.setError("Invalid First Name");
                    firstname.requestFocus();
                }
                if (lastname.getText().toString().trim().length() == 0) {
                    lastname.setError("Invalid Last Name");
                    lastname.requestFocus();
                } else if (phone.getText().toString().trim().length() == 0) {
                    phone.setError("Invalid Mobile");
                    phone.requestFocus();
                } else {
                    loader.setVisibility(View.VISIBLE);
                    if (uri == null) {
                        updateAPI("", "");
                    } else
                        compressImage();
                }
            }
        });
    }

    private void changePasswordAPI(final String cPassword, final String nPassword) {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.CHANGE_PASSWORD)
                .withFormData("user_type", sessionManager.getUSER_TYPE())
                .withFormData("user_id", sessionManager.getTECHNICIAN_ID())
                .withFormData("old_password", cPassword)
                .withFormData("password", nPassword)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                showAlert(mainObject.getString("message"));
                            } else {
                                showAlert(mainObject.getString("message"));
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

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ProfileActivity.this, new
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
                        Toast.makeText(ProfileActivity.this, R.string.permission_granted,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(ProfileActivity.this, R.string.permission_allow, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public void compressImage() {
        try {

            File actualImage = FileUtil.from(ProfileActivity.this, uri);
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
                uploadToServer(compressedImage);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void uploadToServer(File file) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        final UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<ResponseBody> call = uploadAPIs.uploadProfileImage(part, description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject mainObject = new JSONObject(response.body().string());
                    if (!mainObject.getBoolean("error")) {
                        updateAPI(mainObject.getJSONObject("data").getString("name"), mainObject.getJSONObject("data").getString("profile_image"));
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Unable to upload image", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //   Log.e(TAG, "onResponse: error ");
            }
        });
    }

    private void updateAPI(String image_name, final String image_url) {
        RequestBuilder requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.UPDATE_PROFILE);
        if (image_name.trim().length() != 0)
            requestBuilder.withFormData("profile_image", image_name);
        requestBuilder.withFormData("user_id", sessionManager.getTECHNICIAN_ID())
                .withFormData("email", sessionManager.getEMAIL())
                .withFormData("usertype", sessionManager.getUSER_TYPE())
                .withFormData("username", sessionManager.getUSER_NAME())
                .withFormData("firstname", firstname.getText().toString().trim())
                .withFormData("lastname", lastname.getText().toString().trim())
                .withFormData("phone", phone.getText().toString().trim())
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
                                sessionManager.setFIRST_NAME(firstname.getText().toString());
                                sessionManager.setLAST_NAME(lastname.getText().toString());
                                sessionManager.setPHONE(phone.getText().toString());
                                if (image_url.trim().length() != 0)
                                    sessionManager.setPROFILE_IMAGE(image_url);
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

    private void showAlert(String message) {
        new AlertDialog.Builder(ProfileActivity.this)
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


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
