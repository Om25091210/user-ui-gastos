package com.cu.gastosmerchant1.Dashboard.Settings;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Catalogue extends Fragment {

    View view;
    public static int GALLERY_CODE = 3;
    Uri selectedImageUri;
    String picturePath;
    int count = 0;
    FirebaseAuth auth;
    FirebaseUser user;
    TinyDB tinyDB;
    ProgressDialog progressBar;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> empty=new ArrayList<>();
    DatabaseReference reference;
    TextView next;
    ArrayList<String> catalogue_arrayList=new ArrayList<>();
    ImageView shop1,shop2,shop3,cat1,cat2,cat3;
    CardView cardView,cardView2,cardview3,cardView4,cardView5,cardview6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.catalogue, container, false);
        tinyDB=new TinyDB(getContext());
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid()).child("Shop_Information");
        cardView=view.findViewById(R.id.cardView);
        shop1=view.findViewById(R.id.shop1);
        cardView2=view.findViewById(R.id.cardView2);
        shop2=view.findViewById(R.id.shop2);
        cardview3=view.findViewById(R.id.cardview3);
        shop3=view.findViewById(R.id.shop3);
        cardView4=view.findViewById(R.id.cardView4);
        cat1=view.findViewById(R.id.cat1);
        cardView5=view.findViewById(R.id.cardView5);
        cat2=view.findViewById(R.id.cat2);
        cardview6=view.findViewById(R.id.cardview6);
        cat3=view.findViewById(R.id.cat3);
        next=view.findViewById(R.id.next);
        tinyDB.putListString("shop_pics",empty);
        tinyDB.putListString("catalogue_pics",empty);
        Log.e("size",""+tinyDB.getListString("shop_pics").size());
        view.findViewById(R.id.imageView).setOnClickListener(v->{
            tinyDB.putListString("shop_pics",arrayList);
            tinyDB.putListString("catalogue_pics",catalogue_arrayList);
            back();
        });
        next.setOnClickListener(v->{
            upload_data();
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_CODE);
                                count = 1;
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_CODE);
                                count = 2;
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        cardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_CODE);
                                count = 3;
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_CODE);
                                count = 4;
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_CODE);
                                count = 5;
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        cardview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_CODE);
                                count = 6;
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            Log.d("pathpath", "onActivityResult: " + selectedImageUri);
            picturePath = getPath(getContext(), selectedImageUri);
            String name = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            // textView.setText(name);
            Log.d("Picture Path", picturePath);
            Bitmap bmp = null;
            ByteArrayOutputStream bos = null;
            byte[] bt = null;
            String encodeString = null;
            try {
                bmp = BitmapFactory.decodeFile(picturePath);
                bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bt = bos.toByteArray();
                encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
                Log.d("base69", "onActivityResult: " + encodeString);
                if (count == 1) {
                    arrayList.add(picturePath);
                    shop1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    shop2.setVisibility(View.VISIBLE);
                } else if (count == 2) {
                    arrayList.add(picturePath);
                    shop2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    shop3.setVisibility(View.VISIBLE);
                } else if (count == 3) {
                    arrayList.add(picturePath);
                    shop3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                } else if (count == 4) {
                    catalogue_arrayList.add(picturePath);
                    cat1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    cat2.setVisibility(View.VISIBLE);
                } else if (count == 5) {
                    catalogue_arrayList.add(picturePath);
                    cat2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    cat3.setVisibility(View.VISIBLE);
                } else if (count == 6) {
                    catalogue_arrayList.add(picturePath);
                    cat3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }

                //  Api(encodeString,name);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void upload_data(){
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Uploading files...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        tinyDB.putListString("shop_pics",arrayList);
        tinyDB.putListString("catalogue_pics",catalogue_arrayList);

        Log.e("size",""+tinyDB.getListString("shop_pics").size());

        String shop1ref = user.getUid()+"/shop1_" + ".png";
        StorageReference storageref1 = FirebaseStorage.getInstance().getReference().child(shop1ref);
        try {
            InputStream st1 = new FileInputStream(new File(tinyDB.getListString("shop_pics").get(0)));
            storageref1.putStream(st1)
                    .addOnSuccessListener(ts1 ->
                            ts1.getStorage().getDownloadUrl().addOnCompleteListener(
                                    t1 -> {
                                        String shop_img1 = Objects.requireNonNull(t1.getResult()).toString();
                                        reference.child("ShopPhotoUri").setValue(shop_img1);
                                        Log.e("first",""+tinyDB.getListString("shop_pics").size());
                                        if(tinyDB.getListString("shop_pics").size()>=2){
                                            String shop2ref = user.getUid()+"/shop2_" + ".png";
                                            StorageReference storageref2 = FirebaseStorage.getInstance().getReference().child(shop2ref);
                                            try {
                                                InputStream st2 = new FileInputStream(new File(tinyDB.getListString("shop_pics").get(1)));
                                                storageref2.putStream(st2)
                                                        .addOnSuccessListener(ts2 ->
                                                                ts2.getStorage().getDownloadUrl().addOnCompleteListener(
                                                                        t2 -> {
                                                                            String shop_img2 = Objects.requireNonNull(t2.getResult()).toString();
                                                                            Log.e("second",""+tinyDB.getListString("shop_pics").size());
                                                                            reference.child("ShopPhotoUri1").setValue(shop_img2);
                                                                            if(tinyDB.getListString("shop_pics").size()>=3){
                                                                                String shop3ref = user.getUid()+"/shop3_" + ".png";
                                                                                StorageReference storageref3 = FirebaseStorage.getInstance().getReference().child(shop3ref);
                                                                                try {
                                                                                    InputStream st3 = new FileInputStream(new File(tinyDB.getListString("shop_pics").get(2)));
                                                                                    storageref3.putStream(st3)
                                                                                            .addOnSuccessListener(ts3 ->
                                                                                                    ts3.getStorage().getDownloadUrl().addOnCompleteListener(
                                                                                                            t3 -> {
                                                                                                                String shop_img3 = Objects.requireNonNull(t3.getResult()).toString();
                                                                                                                reference.child("ShopPhotoUri2").setValue(shop_img3);
                                                                                                                upload_catalogue();
                                                                                                            }));
                                                                                } catch (FileNotFoundException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            else{
                                                                                upload_catalogue();
                                                                            }
                                                                        }));
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            upload_catalogue();
                                        }
                                    }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void upload_catalogue() {
        if(tinyDB.getListString("catalogue_pics").size()>=1){//
            String catalogue1ref = user.getUid()+"/catalogue1_" + ".png";
            StorageReference storageref1 = FirebaseStorage.getInstance().getReference().child(catalogue1ref);
            try {
                InputStream st1 = new FileInputStream(new File(tinyDB.getListString("catalogue_pics").get(0)));
                storageref1.putStream(st1)
                        .addOnSuccessListener(ts1 ->
                                ts1.getStorage().getDownloadUrl().addOnCompleteListener(
                                        t1 -> {
                                            String shop_img1 = Objects.requireNonNull(t1.getResult()).toString();
                                            reference.child("CatalogueUri").setValue(shop_img1);
                                            if(tinyDB.getListString("catalogue_pics").size()>=2){
                                                String shop2ref = user.getUid()+"/catalogue2_" + ".png";
                                                StorageReference storageref2 = FirebaseStorage.getInstance().getReference().child(shop2ref);
                                                try {
                                                    InputStream st2 = new FileInputStream(new File(tinyDB.getListString("catalogue_pics").get(1)));
                                                    storageref2.putStream(st2)
                                                            .addOnSuccessListener(ts2 ->
                                                                    ts2.getStorage().getDownloadUrl().addOnCompleteListener(
                                                                            t2 -> {
                                                                                String shop_img2 = Objects.requireNonNull(t2.getResult()).toString();
                                                                                reference.child("CatalogueUri1").setValue(shop_img2);
                                                                                if(tinyDB.getListString("catalogue_pics").size()>=3){
                                                                                    String shop3ref = user.getUid()+"/catalogue3_" + ".png";
                                                                                    StorageReference storageref3 = FirebaseStorage.getInstance().getReference().child(shop3ref);
                                                                                    try {
                                                                                        InputStream st3 = new FileInputStream(new File(tinyDB.getListString("catalogue_pics").get(2)));
                                                                                        storageref3.putStream(st3)
                                                                                                .addOnSuccessListener(ts3 ->
                                                                                                        ts3.getStorage().getDownloadUrl().addOnCompleteListener(
                                                                                                                t3 -> {
                                                                                                                    String shop_img3 = Objects.requireNonNull(t3.getResult()).toString();
                                                                                                                    reference.child("CatalogueUri2").setValue(shop_img3);
                                                                                                                    progressBar.dismiss();
                                                                                                                    Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                                                                                    back();
                                                                                                                }));
                                                                                    } catch (FileNotFoundException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                                else{
                                                                                    Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                                                    progressBar.dismiss();
                                                                                    back();
                                                                                }
                                                                            }));
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                back();
                                            }
                                        }));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }//
        else {
            Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
            back();
        }
    }
    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }
    private void back(){
        FragmentManager fm=((FragmentActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
    }
}