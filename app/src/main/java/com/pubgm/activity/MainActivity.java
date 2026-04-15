package com.pubgm.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import static com.pubgm.Config.GAME_LIST_ICON;
import static com.pubgm.activity.LoginActivity.PASSKEY;
import static com.pubgm.activity.LoginActivity.USERKEY;
import static com.pubgm.activity.SplashActivity.mahyong;
import static com.pubgm.server.ApiServer.EXP;
import static com.pubgm.server.ApiServer.FixCrash;
import static com.pubgm.server.ApiServer.activity;
import static com.pubgm.server.ApiServer.mainURL;

import android.Manifest;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pubgm.Component.DownloadZip;
import com.pubgm.Component.Downtwo;
import com.pubgm.Component.SharedPreferencesManager;
import com.pubgm.R;
import com.pubgm.adapter.RecyclerViewAdapter;
import com.pubgm.floating.FloatRei;
import com.pubgm.floating.FloatService;
import com.pubgm.floating.Overlay;
import com.pubgm.floating.ToggleAim;
import com.pubgm.floating.ToggleBullet;
import com.pubgm.floating.ToggleSimulation;
import com.pubgm.libhelper.ApkEnv;
import com.pubgm.utils.ActivityCompat;
import com.pubgm.utils.FLog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.topjohnwu.superuser.Shell;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class MainActivity extends ActivityCompat {

    
    public static String socket;
    public static String daemonPath;
    public static boolean fixinstallint = false;
    public static boolean check = false;
    public static int hiderecord = 0;
    static MainActivity instance;

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    private static final int REQUEST_PERMISSIONS = 1;
    private static final String PREF_NAME = "espValue";
    private SharedPreferences sharedPreferences;
    String[] packageapp = {"com.tencent.ig", "com.pubg.krmobile", "com.vng.pubgmobile", "com.rekoo.pubgm","com.pubg.imobile"};
    public String nameGame = "PROTECTION GLOBAL";
    public String CURRENT_PACKAGE = "";
    public LinearProgressIndicator progres;
    public CardView enable, disable;
    public static int gameint = 1;
    public static int bitversi = 64;
    public static boolean noroot = false;
    public static int device = 1;
    public static String game = "com.tencent.ig";
    TextView root;
    public static boolean kernel = false;
    public static boolean Ischeck = false;
    public static boolean modestatus = false;
    public LinearLayout container;
    
    public static String modeselect;
    public static String typelogin;
    Context ctx;
    private boolean isResourcesUpdated = false;
    public static MainActivity get() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       // new DownloadZip(this).execute("1", mainURL());
        
        setContentView(R.layout.activity_navigation);
        
        
        init();
        
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        
        initMenu1();
        initMenu2();
        Loadssets();
        devicecheck();
        SettingESP();
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        instance = this;
        isLogin = true;
        
        
        
        
    }
    

    public void devicecheck(){
        root = findViewById(R.id.textroot);
        container = findViewById(R.id.container);
       // protection = findviewById(R.id.protection);

            FLog.info("Root not granted");
            modeselect =   "ANDROID " + Build.VERSION.RELEASE;
           // protectection.setVisibility(View.GONE);
            root.setText(getString(R.string.notooroot));
            doInitRecycler();
          //  container.setVisibility(View.VISIBLE);
            Ischeck = false;
            device = 2;
        
    }


   

    @SuppressLint("SetTextI18n")
    public void initMenu1() {
    // Initialize all UI components
   // ImageView start = findViewById(R.id.startmenu);
   // ImageView stop = findViewById(R.id.stopmenu);
    
  //  LinearLayout layoutprtc = findViewById(R.id.layoutprtc);
    LinearLayout menuselectesp = findViewById(R.id.menuselectesp);
//    Switch protection = findViewById(R.id.protection);
   // TextView textsstart = findViewById(R.id.textsstart);
   // TextView textversions = findViewById(R.id.textversions1);
    TextView textroot = findViewById(R.id.texttag);
  //  ImageView imgs1 = findViewById(R.id.imgs1);
    RadioGroup modesp = findViewById(R.id.groupmode);
    RadioGroup espmode = findViewById(R.id.groupesp);
    RadioGroup espstatus = findViewById(R.id.groupstatus);
    LinearLayout layhome2 = findViewById(R.id.option);
   // LinearLayout layhome3 = findViewById(R.id.hackkkk);
    LinearLayout showprotect = findViewById(R.id.showprotect);
    LinearLayout switchmode = findViewById(R.id.switchmodd);
    TextView modedetector = findViewById(R.id.modedetector); // Ensure this TextView exists in the layout
    ImageView statusImage = findViewById(R.id.oth_status_mode); // Ensure this ImageView exists in the layout
    LinearLayout container = findViewById(R.id.container);   
     //   showprotect.setEnabled(false); 

    // showprotect click listener
showprotect.setOnClickListener(v -> {
    // Check if resources have been updated
       if (!isResourcesUpdated) {
        // If resources are not updated, show the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  // Ensure you pass Activity context
        builder.setIcon(R.drawable.icon_toast_alert)
               .setTitle(R.string.error)
               .setMessage(R.string.please_update_protection_resources)
               .setPositiveButton(android.R.string.ok, null) // Dismiss button
               .show();
    } else {
        // If resources are updated, perform normal showprotect functionality
        switchmode.setVisibility(View.VISIBLE);
        layhome2.setVisibility(View.GONE);

        if (Shell.rootAccess()) {
            container.setVisibility(View.VISIBLE);
        } else {
            container.setVisibility(View.VISIBLE);
        }
       // layhome3.setVisibility(View.VISIBLE);
    }
});

    switchmode.setOnClickListener(v -> {
        layhome2.setVisibility(View.VISIBLE);
        //layhome3.setVisibility(View.GONE);
        switchmode.setVisibility(View.GONE);
        container.setVisibility(View.GONE); 
    });

    // Protection logic


    // Show/Hide ESP menu based on root status
    
        
        menuselectesp.setVisibility(View.GONE);
    
              
       

    // ESP Mode selection logic
    espmode.setOnCheckedChangeListener((group, checkedId) -> {
        switch (checkedId) {
            case R.id.esp64:
                bitversi = 64;
                break;
        }
    });

    // Mode selection logic
    modesp.setOnCheckedChangeListener((group, checkedId) -> {
        switch (checkedId) {
            case R.id.system:
                kernel = false;
                espmode.setEnabled(true);
                espstatus.setEnabled(true);
                break;
            case R.id.kernel:
                kernel = true;
                espmode.check(R.id.esp64);
                espmode.setEnabled(false);
                espstatus.check(R.id.espsafe);
                espstatus.setEnabled(false);
                break;
        }
    });

    // ESP status selection logic
    espstatus.setOnCheckedChangeListener((group, checkedId) -> {
        switch (checkedId) {
            case R.id.espsafe:
                textroot.setText(R.string.SafeMode);
                modestatus = false;
                break;
            case R.id.espunsafe:
                textroot.setText(R.string.BrutalMode);
                modestatus = true;
                break;
        }
        updateModeText(modedetector, statusImage);
    });

    // Start/Stop functionality
 /*   start.setOnClickListener(v -> {
        stop.setVisibility(View.VISIBLE);
        start.setVisibility(View.GONE);
        toastImage(R.drawable.success_250px, getString(R.string.start_floating_success));
        textsstart.setText(R.string.activity_mode_textsstop_text);
        startPatcher();
    });

    stop.setOnClickListener(v -> {
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        stopPatcher();
        textsstart.setText(R.string.activity_mode_textsstart_text);
        toastImage(R.drawable.ic_error, getString(R.string.stop_floating_success));
    });*/

    // Game selection logic
    
}



// Method to update mode text and status
private void updateModeText(TextView modedetector, ImageView statusImage) {
    if (!modestatus) {
        modedetector.setText("SAFE");
        modedetector.setTextColor(Color.parseColor("#008000"));
        statusImage.setImageResource(R.drawable.succ);
    } else {
        modedetector.setText("BRUTAL");
        modedetector.setTextColor(Color.parseColor("#F53138"));
        statusImage.setImageResource(R.drawable.failed);
    }
}

    
    

    @SuppressLint("ResourceAsColor")
    void initMenu2(){
        TextView device = findViewById(R.id.device);
        TextView android = findViewById(R.id.android);
        TextView model = findViewById(R.id.model);
        TextView username = findViewById(R.id.username);
        TextView leveluser = findViewById(R.id.leveluser);
        LinearLayout updatesresource = findViewById(R.id.updatesresource);
        LinearLayout logout = findViewById(R.id.logout);
        LinearLayout layoutother = findViewById(R.id.wkkw);
        LinearLayout showprotect = findViewById(R.id.showprotect);

        device.setText(Build.VERSION.RELEASE);
        android.setText(Build.MANUFACTURER);
        model.setText(Build.MODEL);
        username.setText(PASSKEY);
    
        // import androidx.appcompat.app.AlertDialog; // Ensure you import this
updatesresource.setOnClickListener(v -> {
AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  // Ensure you pass Activity context
builder.setIcon(R.drawable.updatebypass) // Set an icon for the dialog
       .setTitle(R.string.updatebypass)             // Set the title
       .setMessage(R.string.updatebypass_resources) // Set the message
       .setPositiveButton(R.string.button_positive, (dialog, which) -> {
           // Handle the first button click
           new DownloadZip(MainActivity.this).execute("1", mainURL());
            // Mark resources as updated once the download starts or completes
            isResourcesUpdated = true;
       })
       .setNegativeButton(R.string.button_negative, (dialog, which) -> {
           // Handle the second button click
          // checkAndDeleteFile(MainActivity.get());
       })
       .setNeutralButton(R.string.button_neutral, (dialog, which) -> {
           // Handle the third button click
           
            dialog.dismiss();  // Dismiss the dialog
       })
       .show(); // Show the dialog
});



        logout.setOnClickListener(v -> {
            showBottomSheetDialog3(
                    getResources().getDrawable(R.drawable.icon_toast_alert),
                    getString(R.string.confirm),
                    getString(R.string.Did_you_want_to_logout_application),
                    false,
                    null,
                    v1 -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        dismissBottomSheetDialog();
                    },
                    v2 -> {

                        dismissBottomSheetDialog();
                    }
            );
        });

    }


    public void SettingESP(){
        // Meminta izin jika belum diberikan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        findViewById(R.id.savesetting).setOnClickListener(v -> {
            try {
                importSharedPreferences();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to import", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.exportsetting).setOnClickListener(v -> {
            try {
                exportSharedPreferences();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to export", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.resetsetting).setOnClickListener(v -> {
            resetSharedPreferences();
            Toast.makeText(MainActivity.this, "Success Reset", Toast.LENGTH_SHORT).show();
        });
    }

    private void exportSharedPreferences() throws IOException {
        File srcFile = new File(getApplication().getDataDir().toString() + "/shared_prefs/"+ PREF_NAME + ".xml");
        File dstFile = new File(Environment.getExternalStorageDirectory(), PREF_NAME + ".xml");

        if (srcFile.exists()) {
            FileChannel src = null;
            FileChannel dst = null;
            try {
                src = new FileInputStream(srcFile).getChannel();
                dst = new FileOutputStream(dstFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                Toast.makeText(MainActivity.this, "Exported to " + dstFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } finally {
                if (src != null) {
                    src.close();
                }
                if (dst != null) {
                    dst.close();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Setting esp files file not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void importSharedPreferences() throws IOException {
        File srcFile = new File(Environment.getExternalStorageDirectory(), PREF_NAME + ".xml");
        File dstFile = new File(getApplication().getDataDir().toString() + "/shared_prefs/" + PREF_NAME + ".xml");

        if (srcFile.exists()) {
            FileChannel src = null;
            FileChannel dst = null;
            try {
                src = new FileInputStream(srcFile).getChannel();
                dst = new FileOutputStream(dstFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                Toast.makeText(MainActivity.this, "Imported from " + srcFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } finally {
                if (src != null) {
                    src.close();
                }
                if (dst != null) {
                    dst.close();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Setting esp file not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    void gameversion(LinearLayout a, LinearLayout b, LinearLayout c, LinearLayout d, LinearLayout e){
        a.setBackgroundResource(R.drawable.bgfituron);
        b.setBackgroundResource(R.drawable.bgfituroff);
        c.setBackgroundResource(R.drawable.bgfituroff);
        d.setBackgroundResource(R.drawable.bgfituroff);
        e.setBackgroundResource(R.drawable.bgfituroff);
    }


    void init() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        LinearLayout navhome = findViewById(R.id.navhome);
        LinearLayout navsetting = findViewById(R.id.navsetting);
        LinearLayout effecthome = findViewById(R.id.effecthome);
        LinearLayout effectsetting = findViewById(R.id.effectsetting);
        LinearLayout menu1 = findViewById(R.id.imenu1);
        LinearLayout menu2 = findViewById(R.id.imenu2);
        ImageView home = findViewById(R.id.imghome);
        ImageView sett = findViewById(R.id.imgsett);

        navhome.setOnClickListener(v -> {
            menu1.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.GONE);
           // effecthome.setBackgroundResource(R.drawable.button_normal);
          //  effectsetting.setBackgroundResource(R.drawable.buttonshape);
            effecthome.startAnimation(animation);

            home.setBackgroundResource(R.drawable.ic_home);
            sett.setBackgroundResource(R.drawable.outline_settings_24);
        });

        navsetting.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
           // effecthome.setBackgroundResource(R.drawable.buttonshape);
           // effectsetting.setBackgroundResource(R.drawable.button_normal);
            effectsetting.startAnimation(animation);

            home.setBackgroundResource(R.drawable.ic_home_outline);
            sett.setBackgroundResource(R.drawable.ic_helpon);
        });

    }


    ////////////////////////// Load Json ////////////////////////////////////////
    public void doInitRecycler() {
        doShowProgress(true);
        ArrayList<Integer> imageValues = new ArrayList<Integer>();
        ArrayList<String> titleValues = new ArrayList<String>();
        ArrayList<String> versionValues = new ArrayList<String>();
        ArrayList<String> statusValues = new ArrayList<String>();
        ArrayList<String> packageValues = new ArrayList<String>();
        try {
            String jsonLocation = loadJSONFromAsset("games.json");
            JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = jsonobject.getJSONArray("gamesList");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jb = (JSONObject) jarray.get(i);
                String title = jb.getString("title");
                String packageName = jb.getString("package");
                String version = jb.getString("version");
                String status = jb.getString("status");
                imageValues.add(GAME_LIST_ICON[i]);
                titleValues.add(title);
                versionValues.add(version);
                statusValues.add(status);
                packageValues.add(packageName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, imageValues, titleValues, versionValues, statusValues, packageValues);
        RecyclerView myView = (RecyclerView) findViewById(R.id.recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);
    }

    public void addAdditionalApp(boolean system, String packageName) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ApkEnv.getInstance().isInstalled(packageName)) {
                    doHideProgress();
                    ApkEnv.getInstance().launchApk(packageName);
                } else {
                    try {
                        if (ApkEnv.getInstance().installByPackage(packageName)) {
                            doHideProgress();
                            ApkEnv.getInstance().launchApk(packageName);
                        }
                    } catch (Exception err) {
                        FLog.error(err.getMessage());
                        doHideProgress();
                    }
                }
            }
        });
    }

    ////////////////////////// Panel Enc ////////////////////////////////////////

    

    ////////////////////////// Other ////////////////////////////////////////
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

  /*  public void launchbypass(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(game);
        if (launchIntent != null) {
            startActivity(launchIntent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bitversi == 64 ){
                        if (gameint >= 1 && gameint <= 4) {
                            Exec("/TW "+game+" 001", getString(R.string.bypass_launch_success));
                        } else if (gameint == 5) {
                            Exec("/TW "+game+" 002", getString(R.string.bypass_launch_success));
                        }
                    }else if (bitversi == 32){
                        if (gameint >= 1 && gameint <= 4) {
                            Exec("/TW "+game+" 22", getString(R.string.bypass_launch_success));
                        } else if (gameint == 5) {
                            Exec("/TW "+game+" 44", getString(R.string.bypass_launch_success));
                        }
                    }

                }
            }, 13000);
        }else{
            toastImage(R.drawable.ic_error,game + getString(R.string.not_installed_please_check));
        }
    }
*/
    public void Exec(String path, String toast) {
        try {
            ExecuteElf("su -c chmod 777 " + getFilesDir() + path);
            ExecuteElf("su -c " + getFilesDir() + path);
            ExecuteElf("chmod 777 " + getFilesDir() + path);
            ExecuteElf("" +  getFilesDir() + path);
            toastImage(R.drawable.ic_check, toast);
        } catch (Exception e) {
      }
    }

    private void ExecuteElf(String shell) {
        try {
            Runtime.getRuntime().exec(shell, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Loadssets() {
        MoveAssets(getFilesDir() + "/", "sock64");
        MoveAssets(getFilesDir() + "/", "socu64");
        MoveAssets(getFilesDir() + "/", "via.apk");
        MoveAssets(getFilesDir() + "/", "TW");
        MoveAssets(getFilesDir() + "/", "VNG");
        MoveAssets(getFilesDir() + "/", "kernels64");
    }

    private boolean MoveAssets(String outPath, String fileName) {
        File file = new File(outPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("--Method--", "copyAssetsSingleFile: cannot create directory.");
                return false;
            }
        }
        try {
            InputStream inputStream = getAssets().open(fileName);
            File outFile = new File(file, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPatcher();
      stopService(new Intent(MainActivity.get(), FloatService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), FloatRei.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, getString(R.string.please_click_icon_logout_for_exit), Toast.LENGTH_SHORT).show();
    }
    
    @Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean("isResourcesUpdated", isResourcesUpdated);
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (savedInstanceState != null) {
        isResourcesUpdated = savedInstanceState.getBoolean("isResourcesUpdated", false);
    }
}
    
    



    public LinearProgressIndicator getProgresBar() {
        if (progres == null) {
            progres = findViewById(R.id.progress);
        }
        return progres;
    }

    public void doShowProgress(boolean indeterminate) {
        if (progres == null) {
            return;
        }
        progres.setVisibility(View.VISIBLE);
        progres.setIndeterminate(indeterminate);

        if (!indeterminate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                progres.setMin(0);
            }
            progres.setMax(100);
        }
    }

    public void doHideProgress() {
        if (progres == null) {
            return;
        }
        progres.setIndeterminate(true);
        progres.setVisibility(View.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (FloatService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void startPatcher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.get())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 123);
            } else {
                startFloater();
            }
        }
    }

    private void startFloater() {
        
            startService(new Intent(MainActivity.get(), FloatService.class));
            loadAssets();
        } 

    private void stopPatcher() {
        stopService(new Intent(MainActivity.get(), FloatService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), FloatRei.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));
    }

    public void loadAssets() {
		String filepath =Environment.getExternalStorageDirectory() + "/Android/data/.tyb";
        FileOutputStream fos = null;
        try {
			fos = new FileOutputStream(filepath);
			byte[] buffer = "DO NOT DELETE".getBytes();
			fos.write(buffer, 0, buffer.length);
			fos.close();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		daemonPath =getFilesDir().toString() +"/sock64";
     //  libPath =getFilesDir().toString() +"/shayan";
		if(Shell.rootAccess()) {
			socket = "su -c " + daemonPath;
		} else {
			socket = daemonPath;
		}
		try {
			Runtime.getRuntime().exec("chmod 777 " + daemonPath);
			//Runtime.getRuntime().exec("chmod 777 " + libPath);
		} catch (IOException e) {
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        CountTimerAccout();
        boolean needsRecreate = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("needs_recreate", false);
        if (needsRecreate) {
            getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("needs_recreate", false)
                    .apply();
        }
    }

    private void CountTimerAccout() {
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date expiryDate = dateFormat.parse(EXP());
                long now = System.currentTimeMillis();
                long distance = expiryDate.getTime() - now;

                long days = distance / (24 * 60 * 60 * 1000);
                long hours = (distance / (60 * 60 * 1000)) % 24;
                long minutes = (distance / (60 * 1000)) % 60;
                long seconds = (distance / 1000) % 60;

                if (distance >= 0) {
                        TextView Hari = findViewById(R.id.days);
                        TextView Jam = findViewById(R.id.hours);
                        TextView Menit = findViewById(R.id.minutes);
                        TextView Detik = findViewById(R.id.second);
                        
                    Hari.setText(String.format("%02d", days));
                    Jam.setText(String.format("%02d", hours));
                    Menit.setText(String.format("%02d", minutes));
                    Detik.setText(String.format("%02d", seconds));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    handler.postDelayed(runnable, 0);
}




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        }
    }

}
