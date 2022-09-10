package com.cu.gastosmerchant1.registration.new_flow;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.Dashboard.Settings.Catalogue;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityProfileDetailsBinding;
import com.cu.gastosmerchant1.model.Account_Information;
import com.cu.gastosmerchant1.registration.AddQR;
import com.cu.gastosmerchant1.registration.BasicDetails;
import com.cu.gastosmerchant1.registration.GetStates;
import com.cu.gastosmerchant1.registration.State;
import com.cu.gastosmerchant1.registration.new_flow.model.Discount;
import com.cu.gastosmerchant1.registration.new_flow.model.Services;
import com.cu.gastosmerchant1.registration.new_flow.model.Shop_Information;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class ProfileDetails extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    ActivityProfileDetailsBinding binding;
    ActivityResultLauncher<Intent> startActivityForImage;
    Dialog dialog1;
    String longitude,latitude;
    FirebaseAuth auth;
    Shop_Information shop_information;
    String wallet;
    FirebaseUser user;
    private static final int CAMERA_REQUEST = 1888; // field
    private boolean b=false;
    private static final int REQUEST_LOCATION = 1;
    DatabaseReference reference,ref_merchant,ref_mer;
    private ArrayList<String> locations = new ArrayList<>();
    ArrayList<State> statesArrayList = new ArrayList<>();
    String image;
    Services services;
    String from;
    String url_city = "https://raw.githubusercontent.com/zimmy9537/Indian-States-And-Districts/master/states-and-districts.json";
    String city_json;
    String cover_selectedImagePath="",shop_selectedImagePath="";
    private ArrayList<String> states = new ArrayList<>();
    LocationManager locationManager;
    TinyDB tinyDB;
    ArrayList<Discount> list_discount=new ArrayList<>();
    static private Timestamp timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityProfileDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tinyDB=new TinyDB(ProfileDetails.this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        reference= FirebaseDatabase.getInstance().getReference().child("Details").child("Category");
        ref_mer= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        from=getIntent().getStringExtra("profile");
        startActivityForImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Uri selectedImageUri = result.getData().getData();
                        addImageNote(selectedImageUri);
                    }
                }
        );
        Fresco.initialize(
                ProfileDetails.this,
                ImagePipelineConfig.newBuilder(ProfileDetails.this)
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        ref_merchant= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid());
        binding.textView13.setText(user.getPhoneNumber()+"");
        shop_information=new Shop_Information();
        get_details();
        fillCategories(categories);
        getCityData();
        get_data();
        get_wallet();
        TinyDB tinyDB=new TinyDB(ProfileDetails.this);
        timestamp = new Timestamp(System.currentTimeMillis());//timestamp.getTime()

        if(tinyDB.getListString("discount_list").size()>=2) {
            int limit;
            int amt_limit;
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(0));
            if(tinyDB.getListString("discount_list").get(1).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(1).trim());
            Discount discount=new Discount(amt_limit,limit);
            list_discount.add(discount);
        }
        if(tinyDB.getListString("discount_list").size()>=4){
            int limit;
            int amt_limit;
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(2));
            if(tinyDB.getListString("discount_list").get(3).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(3).trim());
            Discount discount=new Discount(amt_limit,limit);
            list_discount.add(discount);
        }
        if(tinyDB.getListString("discount_list").size()==6){
            int limit;
            int amt_limit;
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(4));
            if(tinyDB.getListString("discount_list").get(5).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(5).trim());
            Discount discount=new Discount(amt_limit,limit);
            list_discount.add(discount);
        }
        binding.fourthLayout1.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.cps,new Catalogue())
                    .addToBackStack(null)
                    .commit();
        });
        binding.citySpinner.setEnabled(false);
        binding.linearLayout.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.layout_camera);
            ImageView textView=dialog1.findViewById(R.id.close);
            textView.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            LinearLayout linearLayout=dialog1.findViewById(R.id.upload_gal);
            linearLayout.setOnClickListener(v2->{
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                }
                else{
                    dialog1.dismiss();
                    image="Cover";
                    selectImage();
                }
            });
            LinearLayout linearLayout1=dialog1.findViewById(R.id.take_picture);
            linearLayout1.setOnClickListener(v2->{
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileDetails.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_STORAGE_PERMISSION);
                }
                else{
                    dialog1.dismiss();
                    image="Cover";
                    takePicture();
                }
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();

        });
        binding.linearLayout2.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.layout_camera);
            ImageView textView=dialog1.findViewById(R.id.close);
            textView.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            LinearLayout linearLayout=dialog1.findViewById(R.id.upload_gal);
            linearLayout.setOnClickListener(v2->{
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                }
                else{
                    dialog1.dismiss();
                    image="shopImage";
                    selectImage();
                }
            });
            LinearLayout linearLayout1=dialog1.findViewById(R.id.take_picture);
            linearLayout1.setOnClickListener(v2->{
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileDetails.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_STORAGE_PERMISSION);
                }
                else{
                    dialog1.dismiss();
                    image="shopImage";
                    takePicture();
                }
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();
        });

        binding.shopaddress.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.bottomsheet_detail);
            ImageView close=dialog1.findViewById(R.id.close);
            close.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            EditText editText=dialog1.findViewById(R.id.editTextTextMultiLine);
            TextView next=dialog1.findViewById(R.id.next);
            next.setOnClickListener(v1->{
                if(!editText.getText().toString().trim().equals(""))
                    binding.shopAdd.setText(editText.getText().toString().trim());
                else
                    Toast.makeText(this, "Please Add details to continue.", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();
        });
        binding.shopAreaLay.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.bottomsheet_detail);
            ImageView close=dialog1.findViewById(R.id.close);
            close.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            EditText editText=dialog1.findViewById(R.id.editTextTextMultiLine);
            TextView next=dialog1.findViewById(R.id.next);
            next.setOnClickListener(v1->{
                if(!editText.getText().toString().trim().equals(""))
                    binding.shopArea.setText(editText.getText().toString().trim());
                else
                    Toast.makeText(this, "Please Add details to continue.", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();
        });
        binding.ownerNameLay.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.bottomsheet_detail);
            ImageView close=dialog1.findViewById(R.id.close);
            close.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            EditText editText=dialog1.findViewById(R.id.editTextTextMultiLine);
            TextView next=dialog1.findViewById(R.id.next);
            next.setOnClickListener(v1->{
                if(!editText.getText().toString().trim().equals(""))
                    binding.ownername.setText(editText.getText().toString().trim());
                else
                    Toast.makeText(this, "Please Add details to continue.", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();
        });
        binding.emailadressLay.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.bottomsheet_detail);
            ImageView close=dialog1.findViewById(R.id.close);
            close.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            EditText editText=dialog1.findViewById(R.id.editTextTextMultiLine);
            TextView next=dialog1.findViewById(R.id.next);
            next.setOnClickListener(v1->{
                if(!editText.getText().toString().trim().equals(""))
                    binding.emailadress.setText(editText.getText().toString().trim());
                else
                    Toast.makeText(this, "Please Add details to continue.", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();
        });
        binding.shopNameLay.setOnClickListener(v->{
            dialog1 = new Dialog(ProfileDetails.this);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.bottomsheet_detail);
            ImageView close=dialog1.findViewById(R.id.close);
            close.setOnClickListener(v1->{
                dialog1.dismiss();
            });
            EditText editText=dialog1.findViewById(R.id.editTextTextMultiLine);
            TextView next=dialog1.findViewById(R.id.next);
            next.setOnClickListener(v1->{
                if(!editText.getText().toString().trim().equals(""))
                    binding.shopName.setText(editText.getText().toString().trim());
                else
                    Toast.makeText(this, "Please Add details to continue.", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            });
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.show();
        });


        binding.shopCord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    ActivityCompat.requestPermissions(ProfileDetails.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    OnGps();
                } else {

                    if(b==false)
                    {
                        ActivityCompat.requestPermissions(ProfileDetails.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        binding.prog.setVisibility(View.VISIBLE);
                        getLocation();
                    }

                }
            }
        });
        binding.fourthLayout.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.cps,new SelectServices())
                    .addToBackStack(null)
                    .commit();
        });

        binding.imageView.setOnClickListener(v->{
            finish();
        });
        if(tinyDB.getListString("Saved_data_step_1").size()!=0) {
            cover_selectedImagePath = tinyDB.getListString("Saved_data_step_1").get(0) + "";
            shop_selectedImagePath = tinyDB.getListString("Saved_data_step_1").get(1) + "";
        }
        else{
            cover_selectedImagePath="";
            shop_selectedImagePath="";
        }
        binding.next.setOnClickListener(v->{
            if(!binding.ownername.getText().toString().trim().equals("------")) {
                if(!binding.emailadress.getText().toString().trim().equals("------")) {
                    if(!binding.shopName.getText().toString().trim().equals("------")) {
                        if (!cover_selectedImagePath.equals("")) {
                            if (!shop_selectedImagePath.equals("")) {
                                if (!binding.shopAdd.getText().toString().trim().equals("----------")) {
                                    if (!binding.shopState.getText().toString().trim().equals("Select Shop State")) {
                                        if (!binding.shopCity.getText().toString().trim().equals("Select Shop City")) {
                                            if (!binding.shopArea.getText().toString().trim().equals("Enter Area")) {
                                                if (!binding.shopCategory.getText().toString().trim().equals("Select Category")) {
                                                    if (!binding.shopSubCategory.getText().toString().trim().equals("Select Sub-Categories")) {
                                                        if (longitude != null && latitude != null) {
                                                            if(tinyDB.getListBoolean("DB_services").size()!=0) {
                                                                if (from == null) {
                                                                    ProgressDialog progressBar = new ProgressDialog(ProfileDetails.this);
                                                                    progressBar.setCancelable(true);//you can cancel it by pressing back button
                                                                    progressBar.setMessage("Uploading files...");
                                                                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                                    progressBar.show();
                                                                    Account_Information account_information = new Account_Information(
                                                                            binding.emailadress.getText().toString().trim(),
                                                                            binding.ownername.getText().toString().trim(),
                                                                            user.getPhoneNumber().substring(3),
                                                                            "",
                                                                            "",
                                                                            wallet,
                                                                            binding.shopName.getText().toString().trim());
                                                                    ref_mer.child(user.getUid()).child("Account_Information").setValue(account_information);
                                                                    ArrayList<String> strings = new ArrayList<>();
                                                                    strings.add(cover_selectedImagePath);//0
                                                                    strings.add(shop_selectedImagePath);//1
                                                                    strings.add(binding.shopCategory.getText().toString().trim());//2
                                                                    strings.add(binding.shopSubCategory.getText().toString().trim());//3
                                                                    strings.add(binding.shopState.getText().toString().trim());//4
                                                                    strings.add(binding.shopCity.getText().toString().trim());//5
                                                                    strings.add(binding.shopAdd.getText().toString().trim());//6
                                                                    strings.add(longitude);//7
                                                                    strings.add(latitude);//8
                                                                    strings.add(binding.shopArea.getText().toString().trim());//9
                                                                    strings.add(binding.ownername.getText().toString().trim());//10
                                                                    strings.add(binding.emailadress.getText().toString().trim());//11
                                                                    strings.add(binding.shopName.getText().toString().trim());//12
                                                                    tinyDB.putListString("Saved_data_step_1", strings);
                                                                    progressBar.dismiss();
                                                                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                                                                            .beginTransaction()
                                                                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                                                            .add(R.id.cps, new Add_QR())
                                                                            .addToBackStack(null)
                                                                            .commit();
                                                                } else {
                                                                    Log.e("cover_selected",cover_selectedImagePath+"");
                                                                    Log.e("cover_selected",shop_selectedImagePath+"");
                                                                    if (!cover_selectedImagePath.equals("ok")  && !shop_selectedImagePath.equals("ok")) {
                                                                        upload_images();
                                                                        Log.e("cover_selectedImagePath", cover_selectedImagePath);
                                                                        Log.e("shop_selectedImagePath", shop_selectedImagePath);
                                                                    }
                                                                    else if(!cover_selectedImagePath.equals("ok")){
                                                                        upload_cover();
                                                                    }
                                                                    else if(!shop_selectedImagePath.equals("ok")){
                                                                        upload_shopProfile();
                                                                    }
                                                                    else {
                                                                        upload();
                                                                    }
                                                                }
                                                            }else{
                                                                Snackbar.make(binding.constraint, "Please select the Shop Services...", Snackbar.LENGTH_LONG)
                                                                        .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                                        .setTextColor(Color.parseColor("#FFFFFF"))
                                                                        .setBackgroundTint(Color.parseColor("#2D796D"))
                                                                        .show();
                                                            }
                                                        } else {
                                                            Snackbar.make(binding.constraint, "Please select the Shop Location...", Snackbar.LENGTH_LONG)
                                                                    .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                                                    .setBackgroundTint(Color.parseColor("#2D796D"))
                                                                    .show();
                                                        }
                                                    } else {
                                                        Snackbar.make(binding.constraint, "Please select the Shop Category...", Snackbar.LENGTH_LONG)
                                                                .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                                .setTextColor(Color.parseColor("#FFFFFF"))
                                                                .setBackgroundTint(Color.parseColor("#2D796D"))
                                                                .show();
                                                    }
                                                } else {
                                                    Snackbar.make(binding.constraint, "Please select the Shop Category...", Snackbar.LENGTH_LONG)
                                                            .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                            .setTextColor(Color.parseColor("#FFFFFF"))
                                                            .setBackgroundTint(Color.parseColor("#2D796D"))
                                                            .show();
                                                }
                                            } else {
                                                Snackbar.make(binding.constraint, "Please select the Shop Area...", Snackbar.LENGTH_LONG)
                                                        .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                        .setTextColor(Color.parseColor("#FFFFFF"))
                                                        .setBackgroundTint(Color.parseColor("#2D796D"))
                                                        .show();
                                            }
                                        } else {
                                            Snackbar.make(binding.constraint, "Please select the Shop City...", Snackbar.LENGTH_LONG)
                                                    .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                                    .setBackgroundTint(Color.parseColor("#2D796D"))
                                                    .show();
                                        }
                                    } else {
                                        Snackbar.make(binding.constraint, "Please select the Shop State...", Snackbar.LENGTH_LONG)
                                                .setActionTextColor(Color.parseColor("#ea4a1f"))
                                                .setTextColor(Color.parseColor("#FFFFFF"))
                                                .setBackgroundTint(Color.parseColor("#2D796D"))
                                                .show();
                                    }
                                } else {
                                    Snackbar.make(binding.constraint, "Please select the shop address...", Snackbar.LENGTH_LONG)
                                            .setActionTextColor(Color.parseColor("#ea4a1f"))
                                            .setTextColor(Color.parseColor("#FFFFFF"))
                                            .setBackgroundTint(Color.parseColor("#2D796D"))
                                            .show();
                                }
                            } else {
                                Snackbar.make(binding.constraint, "Please select the shop photo...", Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.parseColor("#ea4a1f"))
                                        .setTextColor(Color.parseColor("#FFFFFF"))
                                        .setBackgroundTint(Color.parseColor("#2D796D"))
                                        .show();
                            }
                        } else {
                            Snackbar.make(binding.constraint, "Please select the cover photo...", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#ea4a1f"))
                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                    .setBackgroundTint(Color.parseColor("#2D796D"))
                                    .show();
                        }
                    } else {
                        Snackbar.make(binding.constraint, "Please select the Shop Name...", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.parseColor("#ea4a1f"))
                                .setTextColor(Color.parseColor("#FFFFFF"))
                                .setBackgroundTint(Color.parseColor("#2D796D"))
                                .show();
                    }
                } else {
                    Snackbar.make(binding.constraint, "Please select the Email...", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#ea4a1f"))
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setBackgroundTint(Color.parseColor("#2D796D"))
                            .show();
                }
            }else {
                Snackbar.make(binding.constraint, "Please select the Owner Name...", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#ea4a1f"))
                        .setTextColor(Color.parseColor("#FFFFFF"))
                        .setBackgroundTint(Color.parseColor("#2D796D"))
                        .show();
            }
        });
    }

    private void upload_shopProfile() {
        //for image storing
        ProgressDialog progressBar = new ProgressDialog(ProfileDetails.this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Uploading files...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        String imagepath_profile = user.getUid()+"/profile_" + binding.ownername.getText().toString().trim() + ".png";

        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(imagepath_profile);
        try {
            InputStream stream1 = new FileInputStream(new File(shop_selectedImagePath));

            storageReference1.putStream(stream1)
                    .addOnSuccessListener(taskSnapshot1 ->
                            taskSnapshot1.getStorage().getDownloadUrl().addOnCompleteListener(
                                    task1 -> {
                                        String profile_image_link = Objects.requireNonNull(task1.getResult()).toString();
                                        ref_merchant.child("Shop_Information").child("ProfilePhotoUri").setValue(profile_image_link);
                                        ref_merchant.child("Shop_Information").child("ShopAddress").setValue(binding.shopAdd.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopState").setValue(binding.shopState.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopDistrict").setValue(binding.shopCity.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopArea").setValue(binding.shopArea.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopName").setValue(binding.shopName.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("category").setValue(binding.shopCategory.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("subCategory").setValue(binding.shopSubCategory.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopAddressLongitude").setValue(longitude);
                                        ref_merchant.child("Shop_Information").child("shopAddressLatitude").setValue(latitude);
                                        services=new Services(tinyDB.getListBoolean("DB_services").get(0),tinyDB.getListBoolean("DB_services").get(1)
                                                ,tinyDB.getListBoolean("DB_services").get(2),tinyDB.getListBoolean("DB_services").get(3)
                                                ,tinyDB.getListBoolean("DB_services").get(4),tinyDB.getListBoolean("DB_services").get(5));
                                        ref_merchant.child("Shop_Information").child("services").setValue(services);
                                        ref_merchant.child("Shop_Information").child("discounts").setValue(list_discount);
                                        Account_Information account_information = new Account_Information(
                                                binding.emailadress.getText().toString().trim(),
                                                binding.ownername.getText().toString().trim(),
                                                user.getPhoneNumber().substring(3),
                                                "",
                                                "",
                                                wallet,
                                                binding.shopName.getText().toString().trim());
                                        ref_mer.child(user.getUid()).child("Account_Information").setValue(account_information);
                                        Toast.makeText(this, "Changes Saved!!", Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();
                                        finish();
                                    }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void upload_cover() {
        //for image storing
        ProgressDialog progressBar = new ProgressDialog(ProfileDetails.this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Uploading files...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        String imagepath = user.getUid()+"/cover_" + binding.ownername.getText().toString().trim() + ".png";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagepath);
        try {
            InputStream stream = new FileInputStream(new File(cover_selectedImagePath));

            storageReference.putStream(stream)
                    .addOnSuccessListener(taskSnapshot ->
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                    task -> {
                                        String shop_cover_image_link = Objects.requireNonNull(task.getResult()).toString();
                                        ref_merchant.child("Shop_Information").child("CoverPhotoUri").setValue(shop_cover_image_link);
                                        ref_merchant.child("Shop_Information").child("ShopAddress").setValue(binding.shopAdd.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopState").setValue(binding.shopState.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopDistrict").setValue(binding.shopCity.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopArea").setValue(binding.shopArea.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopName").setValue(binding.shopName.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("category").setValue(binding.shopCategory.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("subCategory").setValue(binding.shopSubCategory.getText().toString().trim());
                                        ref_merchant.child("Shop_Information").child("shopAddressLongitude").setValue(longitude);
                                        ref_merchant.child("Shop_Information").child("shopAddressLatitude").setValue(latitude);
                                        services=new Services(tinyDB.getListBoolean("DB_services").get(0),tinyDB.getListBoolean("DB_services").get(1)
                                                ,tinyDB.getListBoolean("DB_services").get(2),tinyDB.getListBoolean("DB_services").get(3)
                                                ,tinyDB.getListBoolean("DB_services").get(4),tinyDB.getListBoolean("DB_services").get(5));
                                        ref_merchant.child("Shop_Information").child("services").setValue(services);
                                        ref_merchant.child("Shop_Information").child("discounts").setValue(list_discount);
                                        Account_Information account_information = new Account_Information(
                                                binding.emailadress.getText().toString().trim(),
                                                binding.ownername.getText().toString().trim(),
                                                user.getPhoneNumber().substring(3),
                                                "",
                                                "",
                                                wallet,
                                                binding.shopName.getText().toString().trim());
                                        ref_mer.child(user.getUid()).child("Account_Information").setValue(account_information);
                                        Toast.makeText(this, "Changes Saved!!", Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();
                                        finish();
                                    }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void upload() {
        Account_Information account_information = new Account_Information(
                binding.emailadress.getText().toString().trim(),
                binding.ownername.getText().toString().trim(),
                user.getPhoneNumber().substring(3),
                "",
                "",
                wallet,
                binding.shopName.getText().toString().trim());
        ref_mer.child(user.getUid()).child("Account_Information").setValue(account_information);
        ref_merchant.child("Shop_Information").child("ShopAddress").setValue(binding.shopAdd.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("shopState").setValue(binding.shopState.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("shopDistrict").setValue(binding.shopCity.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("shopArea").setValue(binding.shopArea.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("shopName").setValue(binding.shopName.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("category").setValue(binding.shopCategory.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("subCategory").setValue(binding.shopSubCategory.getText().toString().trim());
        ref_merchant.child("Shop_Information").child("shopAddressLongitude").setValue(longitude);
        ref_merchant.child("Shop_Information").child("shopAddressLatitude").setValue(latitude);
        services=new Services(tinyDB.getListBoolean("DB_services").get(0),tinyDB.getListBoolean("DB_services").get(1)
                ,tinyDB.getListBoolean("DB_services").get(2),tinyDB.getListBoolean("DB_services").get(3)
                ,tinyDB.getListBoolean("DB_services").get(4),tinyDB.getListBoolean("DB_services").get(5));
        ref_merchant.child("Shop_Information").child("services").setValue(services);
        ref_merchant.child("Shop_Information").child("discounts").setValue(list_discount);

        Toast.makeText(this, "Changes Saved!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void upload_images() {
        //for image storing
        ProgressDialog progressBar = new ProgressDialog(ProfileDetails.this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Uploading files...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        String imagepath = user.getUid()+"/cover_" + binding.ownername.getText().toString().trim() + ".png";

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagepath);
        try {
            InputStream stream = new FileInputStream(new File(cover_selectedImagePath));

            storageReference.putStream(stream)
                    .addOnSuccessListener(taskSnapshot ->
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                    task -> {
                                        String shop_cover_image_link = Objects.requireNonNull(task.getResult()).toString();
                                        //for image storing
                                        String imagepath_profile = user.getUid()+"/profile_" + binding.ownername.getText().toString().trim() + ".png";

                                        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(imagepath_profile);
                                        try {
                                            InputStream stream1 = new FileInputStream(new File(shop_selectedImagePath));

                                            storageReference1.putStream(stream1)
                                                    .addOnSuccessListener(taskSnapshot1 ->
                                                            taskSnapshot1.getStorage().getDownloadUrl().addOnCompleteListener(
                                                                    task1 -> {
                                                                        String profile_image_link = Objects.requireNonNull(task1.getResult()).toString();
                                                                        ref_merchant.child("Shop_Information").child("CoverPhotoUri").setValue(shop_cover_image_link);
                                                                        ref_merchant.child("Shop_Information").child("ProfilePhotoUri").setValue(profile_image_link);
                                                                        ref_merchant.child("Shop_Information").child("ShopAddress").setValue(binding.shopAdd.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("shopState").setValue(binding.shopState.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("shopDistrict").setValue(binding.shopCity.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("shopArea").setValue(binding.shopArea.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("shopName").setValue(binding.shopName.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("category").setValue(binding.shopCategory.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("subCategory").setValue(binding.shopSubCategory.getText().toString().trim());
                                                                        ref_merchant.child("Shop_Information").child("shopAddressLongitude").setValue(longitude);
                                                                        ref_merchant.child("Shop_Information").child("shopAddressLatitude").setValue(latitude);
                                                                        services=new Services(tinyDB.getListBoolean("DB_services").get(0),tinyDB.getListBoolean("DB_services").get(1)
                                                                                ,tinyDB.getListBoolean("DB_services").get(2),tinyDB.getListBoolean("DB_services").get(3)
                                                                                ,tinyDB.getListBoolean("DB_services").get(4),tinyDB.getListBoolean("DB_services").get(5));
                                                                        ref_merchant.child("Shop_Information").child("services").setValue(services);
                                                                        ref_merchant.child("Shop_Information").child("discounts").setValue(list_discount);
                                                                        Account_Information account_information = new Account_Information(
                                                                                binding.emailadress.getText().toString().trim(),
                                                                                binding.ownername.getText().toString().trim(),
                                                                                user.getPhoneNumber().substring(3),
                                                                                "",
                                                                                "",
                                                                                wallet,
                                                                                binding.shopName.getText().toString().trim());
                                                                        ref_mer.child(user.getUid()).child("Account_Information").setValue(account_information);
                                                                        Toast.makeText(this, "Changes Saved!!", Toast.LENGTH_SHORT).show();
                                                                        progressBar.dismiss();
                                                                        finish();
                                                                    }));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void get_data() {
        ref_merchant.child("Shop_Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String coverPhotoUri=snapshot.child("CoverPhotoUri").getValue(String.class);
                    String profilePhotoUri=snapshot.child("ProfilePhotoUri").getValue(String.class);
                    String category=snapshot.child("category").getValue(String.class);
                    String shopaddress=snapshot.child("ShopAddress").getValue(String.class);
                    String lat=snapshot.child("shopAddressLatitude").getValue(String.class);
                    String lon=snapshot.child("shopAddressLongitude").getValue(String.class);
                    String shoparea=snapshot.child("shopArea").getValue(String.class);
                    String shopdistrict=snapshot.child("shopDistrict").getValue(String.class);
                    String shopname=snapshot.child("shopName").getValue(String.class);
                    String shopstate=snapshot.child("shopState").getValue(String.class);
                    String subcategory=snapshot.child("subCategory").getValue(String.class);
                    cover_selectedImagePath="ok";
                    shop_selectedImagePath="ok";
                    if(coverPhotoUri!=null) {
                        Uri uri = Uri.parse(coverPhotoUri);
                        binding.imageNote.setImageURI(uri);
                    }
                    if(profilePhotoUri!=null) {
                        Uri uriPhoto = Uri.parse(profilePhotoUri);
                        binding.shopImage.setImageURI(uriPhoto);
                    }
                    if(category!=null)
                        binding.shopCategory.setText(category);
                    if(shopaddress!=null)
                        binding.shopAdd.setText(shopaddress);
                    if(lon!=null && lat!=null) {
                        longitude = lon;
                        latitude=lat;
                        binding.plus.setImageResource(R.drawable.ic_ticksquare);
                    }
                    if(shoparea!=null)
                        binding.shopArea.setText(shoparea);
                    if(shopdistrict!=null)
                        binding.shopCity.setText(shopdistrict);
                    if(shopstate!=null)
                        binding.shopState.setText(shopstate);
                    if(shopname!=null)
                        binding.shopName.setText(shopname);
                    if(subcategory!=null)
                        binding.shopSubCategory.setText(subcategory);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void get_details() {
        ref_merchant.child("Account_Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shop_name=snapshot.child("shop_name").getValue(String.class);
                    String email=snapshot.child("emailAddress").getValue(String.class);
                    String OwnerName=snapshot.child("ownerName").getValue(String.class);
                    binding.ownername.setText(OwnerName);
                    binding.emailadress.setText(email);
                    binding.shopName.setText(shop_name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void takePicture(){ //you can call this every 5 seconds using a timer or whenever you want
        Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    private void OnGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation(){

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            /*binding.plus.setImageResource(R.drawable.ic_ticksquare);
            binding.prog.setVisibility(View.GONE);
            b=true;*/
            b=true;
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(ProfileDetails.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = String.valueOf(locationResult.getLocations().get(latestLocationIndex).getLatitude());
                            longitude = String.valueOf(locationResult.getLocations().get(latestLocationIndex).getLongitude());
                            Log.v(BasicDetails.class.getSimpleName(), "latitude:- " + latitude + ", longitude:- " + longitude);
                            binding.plus.setImageResource(R.drawable.ic_ticksquare);
                            binding.prog.setVisibility(View.GONE);
                            b = true;
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fillCategories(ArrayList<String> categories) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear();
                categories.add("Select Category");
                for(DataSnapshot ds:snapshot.getChildren()){
                    categories.add(ds.getKey());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(ProfileDetails.this, R.layout.support_simple_spinner_dropdown_item, categories);

                categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                binding.categorySpinner.setAdapter(categoryAdapter);
                binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        binding.shopCategory.setText(adapterView.getItemAtPosition(i).toString());
                        get_sub_categories(adapterView.getItemAtPosition(i).toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //do nothing
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void get_wallet() {
        ref_mer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).child("Account_Information").exists()){
                    wallet=snapshot.child(user.getUid()).child("Account_Information").child("wallet").getValue(String.class);
                    Log.e("here1",wallet+"");
                }
                else{
                    wallet="0";
                    Log.e("here2",wallet+"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void get_sub_categories(String toString) {
        ArrayList<String> list_sub_categories=new ArrayList<>();
        reference.child(toString).child("SubCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_sub_categories.clear();
                list_sub_categories.add("Select Sub-Categories");
                for(int i=0;i<snapshot.getChildrenCount();i++){
                    list_sub_categories.add(snapshot.child(i+"").child("name").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(ProfileDetails.this, R.layout.support_simple_spinner_dropdown_item, list_sub_categories);

                categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                binding.subCategorySpinner.setAdapter(categoryAdapter);
                binding.subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        binding.shopSubCategory.setText(adapterView.getItemAtPosition(i).toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //do nothing
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    void getCityData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_city, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                city_json = response;

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                GetStates getStates = gson.fromJson(city_json, GetStates.class);

                statesArrayList = new ArrayList<>();
                statesArrayList = getStates.getStates();
                State state0 = new State();
                ArrayList<String> state0arrayList=new ArrayList<>();
                state0arrayList.add("Select Shop City");
                state0.setState("Select Shop State");
                state0.setDistricts(state0arrayList);

                statesArrayList.add(0, state0);

                states = new ArrayList<>();
                for (State state : statesArrayList) {
                    states.add(state.getState());
                    Log.v("state", state.getState());
                }
                //state
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(ProfileDetails.this, R.layout.spinner_text, states);
                binding.stateSpinner.setAdapter(stateAdapter);
                binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        binding.shopState.setText(adapterView.getItemAtPosition(i).toString());
                        binding.citySpinner.setEnabled(true);
                        String stateString = adapterView.getItemAtPosition(i).toString();
                        for (State state : statesArrayList) {
                            if (state.getState().equals(stateString)) {
                                locations = new ArrayList<>();
                                locations = state.getDistricts();
                                locations.add(0, "Select Shop City");
                            }
                        }

                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(ProfileDetails.this, R.layout.support_simple_spinner_dropdown_item, locations);
                        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        binding.citySpinner.setAdapter(locationAdapter);
                        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                binding.shopCity.setText(adapterView.getItemAtPosition(i).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //do nothing
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("errorResponse", error.getMessage()+"");
            }
        });

        queue.add(stringRequest);

    }

    private void selectImage() {
        // If you have access to the external storage, do whatever you need
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForImage.launch(intent);
    }
    private void addImageNote(Uri imageUri) {
        if(image.equals("Cover")) {
            CropImage.activity(imageUri)
                    .setAspectRatio(16, 9)
                    .start(this);
        }else{
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        dialog1.dismiss();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
            Log.d("Logos", "onActivityResult: "+image);
            if(image.equals("Cover")) {
                cover_selectedImagePath = getRealPathFromUri(ProfileDetails.this,getImageUri(ProfileDetails.this,picture,image));
                Log.e("this",cover_selectedImagePath+"");
                binding.imageNote.setImageBitmap(picture);
                //hey this is were the bug was
            }
            else{
                shop_selectedImagePath = getRealPathFromUri(ProfileDetails.this,getImageUri(ProfileDetails.this,picture,image));
                Log.e("this",shop_selectedImagePath+"");
                binding.shopImage.setImageBitmap(picture);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if(image.equals("Cover")) {
                    cover_selectedImagePath = compressImage(resultUri + "");
                    binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(cover_selectedImagePath));
                }
                else{
                    shop_selectedImagePath = compressImage(resultUri + "");
                    binding.shopImage.setImageBitmap(BitmapFactory.decodeFile(shop_selectedImagePath));
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage,String cover) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.WEBP, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, ""+cover, null);
        return Uri.parse(path);
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight+1;
        int actualWidth = options.outWidth+1;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            assert scaledBitmap != null;
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(getExternalFilesDir(null).getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}