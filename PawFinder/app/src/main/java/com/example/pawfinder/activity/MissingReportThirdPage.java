
package com.example.pawfinder.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.db.DBContentProvider;
import com.example.pawfinder.db.PetSQLHelper;
import com.example.pawfinder.model.Address;
import com.example.pawfinder.model.FireBaseUploadnfo;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.sync.PetSqlSync;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissingReportThirdPage extends AppCompatActivity {

    private int numberOfSelected = 1;

    private ImageView imageView;
    private Button uploadImage;
    private Button finish;
    private EditText phoneNumberET;
    private TextInputLayout layoutPhone;
    private TextInputLayout layoutImage;
    private EditText infoET;

    private Uri uri;

    private Double lon;
    private Double lat;
    private String name;
    private PetGender gender;
    private PetType type;
    private String date;

    private String phone;
    private String info;

    private Pet pet;
    private static PrefConfig prefConfig;
    private Toolbar toolbar;
    File imgFile;
    private boolean uspeh = false;
    private ProgressDialog progressDialog;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 213;

    private ExifInterface ei;
    private Bitmap imageBitMap;

    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private String imageName;
    private String imageUrl;
    private ProgressBar progressBar;

    private Bitmap bitmap = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_missing_report_third_page);

        prefConfig = new PrefConfig(this);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("images");
        ///////////////////

        setTitle(R.string.title_missing_third);
        setTitle(R.string.title_missing_third);
        imageView = findViewById(R.id.upload_image_view);
        uploadImage = findViewById(R.id.choose_pet_image);
        phoneNumberET = findViewById(R.id.enter_phone_number);
        layoutPhone = findViewById(R.id.text_input_layout_phone);
        layoutImage = findViewById(R.id.layout_button);
        infoET = findViewById(R.id.enter_add_info);
        finish = findViewById(R.id.btn_missing_report_third);
        progressBar = (ProgressBar) findViewById(R.id.report_progress_bar);


        Intent help = getIntent();

        lat = help.getDoubleExtra("PET_LOST_LAT", 0);
        lon = help.getDoubleExtra("PET_LOST_LON", 0);
        name = help.getStringExtra("PET_NAME");
        gender = PetGender.valueOf(help.getStringExtra("PET_GENDER"));
        type = PetType.valueOf(help.getStringExtra("PET_TYPE"));
        date = help.getStringExtra("PET_DATE_LOST");

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checking if permission is not granted
                if (ContextCompat.checkSelfPermission(MissingReportThirdPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(
                            MissingReportThirdPage.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_READ_EXTERNAL_STORAGE);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, numberOfSelected);
                }



            }
        });

        phoneNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!phoneNumberET.getText().toString().isEmpty()) {
                    layoutPhone.setError(null);
                }
            }

        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                    if (phoneNumberET.getText().toString().isEmpty() || phoneNumberET.getText().toString() == null) {
                        keyboardDown();
                        layoutPhone.setError((getText(R.string.phone_blank)));
                    } else if (imageView.getDrawable() == null) {
                        keyboardDown();
                        layoutImage.setError(getText(R.string.image_blank));
                    } else {
                        Address address = new Address(lon, lat);
                        User user = new User();
                        Geocoder geocoder;
                        List<android.location.Address> fullAddressFromMap;
                        geocoder = new Geocoder(MissingReportThirdPage.this, Locale.getDefault());
                        try {
                            fullAddressFromMap = geocoder.getFromLocation(lat, lon, 1);
                            String street = fullAddressFromMap.get(0).getAddressLine(0);
                            String city = fullAddressFromMap.get(0).getAddressLine(1);
                            String country = fullAddressFromMap.get(0).getAddressLine(2);

                            String[] splitAddress = street.split(",");
                            if (splitAddress.length >= 2) {
                                String s = splitAddress[0];
                                String ci = splitAddress[1];
                                //String c = splitAddress[2];

                                String sPlace[] = s.split(" ");
                                int size = sPlace.length;
                                if (size > 0){
                                    String n = sPlace[size - 1];
                                    String streetBack = "";
                                    for (int i = 0; i < (size - 1); i++) {
                                        streetBack += sPlace[i] + " ";
                                    }
                                    address = new Address(ci, streetBack, n, lon, lat);
                                }else{
                                    address = new Address(ci, s, "1", lon, lat);
                                }
                            }else{
                                address = new Address("", "", "1", lon, lat);
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (prefConfig.readLoginStatus()) {
                            user.setEmail(prefConfig.readUserEmail());
                        }
                        Log.d("DATUM", "STRING " + date + " email " + user.getEmail());
                        pet = new Pet(type, name, gender, infoET.getText().toString(), date, phoneNumberET.getText().toString(), false, user, address);

                        addPet(pet);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //(resultCode == numberOfSelected &&???
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();

            imageBitMap = compressImage(data.getData());
            imageView.setImageBitmap(imageBitMap);

            uploadImage();

        }


    }

    private void keyboardDown(){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void addPet(Pet petAdd) {
        imageBitMap = compressImage(uri);
        if (NetworkTool.getConnectivityStatus(getApplicationContext()) == NetworkTool.TYPE_NOT_CONNECTED) {
            Toast.makeText(this, R.string.network_disabled, Toast.LENGTH_SHORT).show();
            ArrayList<Pet> pets = new ArrayList<>();
            petAdd.setSent(false);      //nije otisao na back
            pets.add(petAdd);
            PetSqlSync.fillDatabase(pets, this, 3);
        } else {
            petAdd.setSent(true);       //otisao na back
            pet.setImage(imageUrl);
            progressDialog = new ProgressDialog(MissingReportThirdPage.this);
            progressDialog.setTitle(MissingReportThirdPage.this.getResources().getString(R.string.missing_report_dialog_title));
            progressDialog.setMessage(MissingReportThirdPage.this.getResources().getString(R.string.dialog_message));
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<Pet> call = ServiceUtils.petService.postMissing(pet);
            call.enqueue(new Callback<Pet>() {
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {

                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), R.string.add_pet_success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MissingReportThirdPage.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //finish();
                    }

                }

                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Log.d("Error", t.getMessage() != null ? t.getMessage() : "error");
                }
            });
        }
    }

    // This function is called when user accept or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, numberOfSelected);
            }
            else {
                Toast.makeText(MissingReportThirdPage.this, R.string.galery_access_permission, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPathFromUri(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public Bitmap compressImage(Uri imageUri){

        String filePath = getPathFromUri(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 720.0f;
        float maxWidth = 1280.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

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

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
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

        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), scaleMatrix, true);

        Bitmap newMap = null;
        try {
            newMap = rotateImageIfRequired(this, scaledBitmap, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newMap;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
            {
                inSampleSize++;
            }

        return inSampleSize;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage(){
        if(uri != null)
        {

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            imageBitMap.compress(Bitmap.CompressFormat.JPEG,50, byteArray);
            final byte[] niz = byteArray.toByteArray();

            imageName = System.currentTimeMillis() + "." + getFileExtension(uri);
            StorageReference fileReference = mStorageRef.child(imageName);
            fileReference.putBytes(niz)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 5000);

                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = result.getResult().toString();
                                    //Toast.makeText(MissingReportThirdPage.this, "Upload uspesaaaan", Toast.LENGTH_LONG).show();

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MissingReportThirdPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("Puklo", "Ovde ");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);

                            if((int)progress == progressBar.getMax())
                            {
                                finish.setEnabled(true);
                            }
                        }
                    });

        }

    }

    public  Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public  Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
