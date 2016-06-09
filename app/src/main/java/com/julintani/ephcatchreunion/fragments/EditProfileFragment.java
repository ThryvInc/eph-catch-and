package com.julintani.ephcatchreunion.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.activities.MainActivity;
import com.julintani.ephcatchreunion.models.OnboardingManager;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.services.PushRegistrationIntentService;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ell on 6/3/16.
 */
public class EditProfileFragment extends Fragment {
    protected static final int REQUEST_MEDIA = 1;
    protected static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2;
    protected Uri mCurrentPhotoUri;
    public boolean isDuringOnboarding = false;
    protected ImageView profileImageView;
    protected EditText nameField;
    protected EditText majorField;
    protected EditText extrasField;
    protected EditText jobField;
    protected ProgressBar progressBar;
    protected User user;

    public static EditProfileFragment newInstance(){
        return new EditProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        profileImageView = (ImageView) view.findViewById(R.id.iv_profile);
        nameField = (EditText) view.findViewById(R.id.et_name);
        majorField = (EditText) view.findViewById(R.id.et_major);
        extrasField = (EditText) view.findViewById(R.id.et_extras);
        jobField = (EditText) view.findViewById(R.id.et_job);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_edit);

        user = User.getCurrentUser(getActivity());
        setupUser();

        return view;
    }

    protected void setupUser(){
        nameField.setText(user.getName());
        majorField.setText(user.getMajor());
        extrasField.setText(user.getExtracurriculars());
        jobField.setText(user.getCurrentActivity());

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForPhoto();
            }
        });

        setupUserImage();
    }

    protected void setupUserImage(){
        if (user.getImageUrl() != null){
            Glide.with(this).load(user.getImageUrl()).centerCrop().into(profileImageView);
        }
    }

    protected void handleImage(){
        try {
            String path = getPath(mCurrentPhotoUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            Bitmap bitmap = null;
            if (imageHeight > 2000 || imageWidth > 2000){
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                int inSampleSize = 1;
                final int halfHeight = imageHeight / 2;
                final int halfWidth = imageWidth / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > 1000
                        || (halfWidth / inSampleSize) > 1000) {
                    inSampleSize *= 2;
                }
                options.inSampleSize = inSampleSize;
                bitmap = BitmapFactory.decodeFile(path, options);
            }else {
                bitmap = BitmapFactory.decodeFile(path);
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageByteArray = stream.toByteArray();
            final ParseFile parseFile = new ParseFile(imageByteArray, "image/jpeg");
            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        String url = parseFile.getUrl();
                        if (url.startsWith("http:")){
                            url = url.replace("http:", "https:");
                        }
                        user.setImageUrl(url);
                        setupUserImage();
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "The image was too large!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            if (TextUtils.isEmpty(nameField.getText().toString().trim())){
                nameField.setError("Can't be empty!");
            }else if (TextUtils.isEmpty(user.getImageUrl())){
                Toast.makeText(getActivity(), "Please pick an image!", Toast.LENGTH_LONG).show();
            }else if (TextUtils.isEmpty(majorField.getText().toString().trim())){
                majorField.setError("Surely you majored in SOMEthing");
            }else if (TextUtils.isEmpty(extrasField.getText().toString().trim())){
                extrasField.setError("You must have done something!");
            }else if (TextUtils.isEmpty(jobField.getText().toString().trim())) {
                jobField.setError("You can't be doing nothing...");
            }else {
                User user = new User();
                user.setName(nameField.getText().toString().trim());
                user.setMajor(majorField.getText().toString().trim());
                user.setExtracurriculars(extrasField.getText().toString().trim());
                user.setCurrentActivity(jobField.getText().toString().trim());
                user.setImageUrl(this.user.getImageUrl());
                user.setPushToken(User.getCurrentUser(getActivity()).getPushToken());
                saveUser(user);
            }
        }
        return true;
    }

    protected void saveUser(final User user){
        try {
            JSONObject userJson = new JSONObject(new Gson().toJson(user));
            JSONObject json = new JSONObject();
            json.put("user", userJson);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, ServerInfo.BASE_URL + "users", json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (isDuringOnboarding){
                        OnboardingManager.setUserOnboarded(getActivity());
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                    try {
                        user.setId(Integer.valueOf(response.getJSONObject("data").getString("id")));
                        User.setCurrentUser(getActivity(), user);
                        getActivity().finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                    headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(getActivity()));
                    return headers;
                }
            };
            Volley.newRequestQueue(getActivity()).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void promptForPhoto(){
        int readExternalPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (readExternalPermissionCheck == PackageManager.PERMISSION_DENIED){
            FragmentCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL);
        }else {
            dispatchIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    promptForPhoto();
                }
                break;
            }
        }
    }

    protected void dispatchIntent(){
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pickIntent, REQUEST_MEDIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null){
                mCurrentPhotoUri = data.getData();

                handleImage();
            }
        }
    }

    @SuppressLint("NewApi")
    protected String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getActivity().getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String result = cursor.getString(column_index);
                    cursor.close();
                    return result;
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
