package com.example.premission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_STORAGE = 300;
    private static final int REQUEST_CONTACTS = 400;
    private static final int REQUEST_GROUP_PERMISSIOM = 500;

    private static final int CAMERA = 1;
    private static final int STORAGE = 2;
    private static final int CONTACTS = 3;
    private static final int All = 4;

    PermissionUtil permissionUtil;
    Button btnRead, btnWrite, btnCamera, btnAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btnCamera);
        btnWrite = findViewById(R.id.btnWrite);
        btnRead = findViewById(R.id.btnRead);
        btnAll = findViewById(R.id.btnAll);

        btnRead.setOnClickListener(this);
        btnWrite.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnAll.setOnClickListener(this);

        permissionUtil = new PermissionUtil(this);

    }

    @Override
    public void onClick(View view) {
        if(view==btnCamera){
            camera();
        }
        else if(view==btnRead){
            read(); // read contacts
        }
        else if(view == btnWrite){
            write(); // write external storage
        }
        else if(view==btnAll){
            requestAll();
        }

    }
    //phase 4
    public void camera(){
        if(checkPermission(CAMERA)!=PackageManager.PERMISSION_GRANTED)
        {
            //did user aske second time
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                showPermissionExplanation(CAMERA);

            }
            else if (!permissionUtil.checkPermissionPreference("camera")) {
                requestPermission(CAMERA);
                permissionUtil.updatePermissionPreference("camera");
            }
            else {//go to setting applications
                Toast.makeText(this, "Please allow camere permission setting", Toast.LENGTH_LONG);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else {
            Toast.makeText(this, "you have camera permission", Toast.LENGTH_LONG);
            AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle("run time permission").setMessage("now you have camera permission")
                    .create();
            d.show();
        }
    }
    public void read(){
        if(checkPermission(CONTACTS)!=PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                showPermissionExplanation(CONTACTS);

            }
            else if (!permissionUtil.checkPermissionPreference("contacts")) {
                requestPermission(CONTACTS);
                permissionUtil.updatePermissionPreference("contacts");
            }
            else {//go to setting applications
                Toast.makeText(this, "Please allow contacts permission setting", Toast.LENGTH_LONG);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else {
            Toast.makeText(this, "you have contacts permission", Toast.LENGTH_LONG);
            AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle("run time permission").setMessage("you have contacts permission")
                    .create();
            d.show();

        }
    }
    public void write(){
        if(checkPermission(STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                showPermissionExplanation(STORAGE);

            }
            else if (!permissionUtil.checkPermissionPreference("storage")) {
                requestPermission(STORAGE);
                permissionUtil.updatePermissionPreference("storage");
            }
            else {//go to setting applications
                Toast.makeText(this, "Please allow write to storage permission setting", Toast.LENGTH_LONG);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else {
            Toast.makeText(this, "you have write storage permission", Toast.LENGTH_LONG);
            AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle("run time permission").setMessage("you have storage permission")
                    .create();
            d.show();

        }
    }

    //phase 1
    //---------Request Permissions
    public void requestPermission(int permission){
        if(permission == CAMERA){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
        }
        else if(permission == STORAGE){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE);
        }
        else if(permission == CONTACTS){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CONTACTS);
        }
    }


    //phase 2
    //---------Display Permissions Explanation
    public  void showPermissionExplanation(final int permission){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(permission==CAMERA){
            builder.setMessage("This app need to access to device camera.");
            builder.setTitle("Camera permission needed");
        }
        if(permission==CONTACTS){
            builder.setMessage("This app need to access to your contacts.");
            builder.setTitle("Contacts permission needed");
        }
        if(permission==STORAGE){
            builder.setMessage("This app need to access to your storage.");
            builder.setTitle("Storage permission needed");
        }
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(permission == CAMERA){
                    requestPermission(CAMERA);
                }
                if(permission == STORAGE){
                    requestPermission(STORAGE);
                }
                if(permission == CONTACTS){
                    requestPermission(CONTACTS);
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //phase 3
    //--------Check For permissions
    public int checkPermission(int permission){
        int status = PackageManager.PERMISSION_DENIED;
        if(permission == CAMERA){
            status = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);
        }
        else if(permission == STORAGE){
            status = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        else if(permission == CONTACTS){
            status = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS);
        }
        return status;
    }


    //phase 5
    public void requestAll(){
        ArrayList<String>permissionAsked = new ArrayList<String>();
        ArrayList<String>permissionAvailable= new ArrayList<String>();
        permissionAvailable.add(Manifest.permission.READ_CONTACTS);
        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionAvailable.add(Manifest.permission.CAMERA);

        for(int i=0;i<permissionAvailable.size();i++){
            if( ContextCompat.checkSelfPermission(this,
                    permissionAvailable.get(i))!=PackageManager.PERMISSION_GRANTED)
            {
                permissionAsked.add(permissionAvailable.get(i));
            }
        }//end of for
        requestGroupPermissions(permissionAsked);


    }
    //phase 6 : Request Group Permissions
    public void requestGroupPermissions(ArrayList<String>permissions){
        if(permissions!=null&& permissions.size()>0) {
            String[] permissionGroups = new String[permissions.size()];
            permissions.toArray(permissionGroups);
            ActivityCompat.requestPermissions(MainActivity.this, permissionGroups, REQUEST_GROUP_PERMISSIOM);
        }
        else
            Toast.makeText(this,
                    "You have permission to camera, storage and contacts",Toast.LENGTH_LONG).show();
    }

    //phase 7
    //permission response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CAMERA==requestCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You have camera permission",Toast.LENGTH_SHORT).show();

                AlertDialog d = new AlertDialog.Builder(this)
                        .setTitle("run time permission").setMessage("you have camera permission")
                        .create();
                d.show();
            }
            else {
                Toast.makeText(this, "You Do not have camera permission",Toast.LENGTH_SHORT).show();
            }
        }
        else if(REQUEST_CONTACTS==requestCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You have contacts permission",Toast.LENGTH_SHORT).show();
                AlertDialog d = new AlertDialog.Builder(this)
                        .setTitle("run time permission").setMessage("you have contact permission")
                        .create();
                d.show();
            }
            else {
                Toast.makeText(this, "You Do not have contacts permission",Toast.LENGTH_SHORT).show();
            }

        }
        else if(REQUEST_STORAGE==requestCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You have storage permission",Toast.LENGTH_SHORT).show();
                AlertDialog d = new AlertDialog.Builder(this)
                        .setTitle("run time permission").setMessage("you have storage permission")
                        .create();
                d.show();
            }
            else {
                Toast.makeText(this, "You Do not have storage permission",Toast.LENGTH_SHORT).show();
            }

        }
        else if(REQUEST_GROUP_PERMISSIOM == requestCode){
            String solutions = "";
            for(int i=0;i<permissions.length;i++){
                String status = "";
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    status="GRANTED";
                }
                else status = "DENIED";
                solutions += solutions + "\n" + permissions[i] + " :" + status;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Group Permissions Details : ");
            builder.setMessage(solutions);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }



}