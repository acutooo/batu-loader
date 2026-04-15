package com.pubgm.activity;

import static com.pubgm.activity.SplashActivity.mahyong;
import static com.pubgm.server.ApiServer.FixCrash;
import static com.pubgm.server.ApiServer.activity;
import static com.pubgm.server.ApiServer.getOwner;
import static com.pubgm.server.ApiServer.mainURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.pubgm.Component.DownloadZip;
import com.pubgm.Component.Downtwo;
import com.pubgm.Component.Prefs;
import com.pubgm.R;
import com.pubgm.utils.ActivityCompat;
import com.pubgm.utils.FLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.File;
import java.util.Locale;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class LoginActivity extends ActivityCompat {

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    private static final String QUESTION = "Q: %s";
    private static final String ANSWER = "A: %s";
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    private ImageView showpassword;
    private static String ModeSelect;
    public static boolean Kooontoool = true;
    
    public static String USERKEY, PASSKEY;
    CardView btnSignIn;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar(this);
        setContentView(R.layout.activity_login);

        loadbahasa();
        initDesign();
        OverlayPermision();
    }

    public void initDesign(){
        prefs = Prefs.with(this);
        final Context m_Context = this;
        TextView textUsername = findViewById(R.id.textUsername);
        showpassword = findViewById(R.id.viewpw);
        textUsername.setText(prefs.read(USER, ""));
        textUsername.setText(prefs.read(PASS, ""));
        Intent intent = getIntent();
        btnSignIn = findViewById(R.id.loginBtn);
        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textUsername = findViewById(R.id.textUsername);
                        if (!textUsername.getText().toString().isEmpty()
                                && !textUsername.getText().toString().isEmpty()) {
                            prefs.write(USER, textUsername.getText().toString());
                            prefs.write(PASS, textUsername.getText().toString());
                            String userKey = textUsername.getText().toString().trim();
                            String passKey = textUsername.getText().toString().trim();
                            Login(LoginActivity.this, userKey, ModeSelect);
                            USERKEY = userKey;
                            PASSKEY = passKey;
                        }

                        if (textUsername.getText().toString().isEmpty()
                                && textUsername.getText().toString().isEmpty()) {
                            textUsername.setError(getString(R.string.please_enter_username));
                        }
                        if (textUsername.getText().toString().isEmpty()) {
                            textUsername.setError(getString(R.string.please_enter_username));
                        }
                    }
                });

        showpassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                ImageView passwordToggle = findViewById(R.id.viewpw);

                if (isPasswordVisible) {
                    passwordToggle.setBackgroundResource(R.drawable.baseline_visibility_off_24);
                    textUsername.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordToggle.setBackgroundResource(R.drawable.baseline_remove_red_eye_24);
                    textUsername.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

                isPasswordVisible = !isPasswordVisible;
            }
        });

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager =
                                (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        String ed = clipboardManager.getText().toString();
                        if (ed.length() > 5) {
                            textUsername.setText(ed);
                        } else {
                            Toast.makeText(m_Context, String.format(QUESTION,R.string.please_copy_licence_and_paste), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        TextView getKey = findViewById(R.id.registerText);
        getKey.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getOwner()));
                        startActivity(intent);
                    }
                });


        PowerSpinnerView powerSpinnerView = findViewById(R.id.bahasa);
        powerSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                switch (newIndex) {

                    case 0:
                        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("bahasa", "zh");
                        editor.apply();
                        recreate();
                        break;

                    case 1:
                        SharedPreferences sharedPreferences1 = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("bahasa", "ar");
                        editor1.apply();
                        recreate();
                        break;

                    case 2:
                        SharedPreferences sharedPreferences2 = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString("bahasa", "en");
                        editor2.apply();
                        recreate();
                        break;

                }

                Toast.makeText(getApplicationContext(), newItem, Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void setLightStatusBar(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        activity.getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
    }

    public static void goLogin(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    public void OverlayPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setMessage(R.string.please_allow_permision_floating);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }



    public static void setModeSelect(String mode) {
        ModeSelect = mode;
    }

    public static String getModeSelect() {
        return ModeSelect;
    }

    private static void Login(final LoginActivity m_Context, final String userKey, final String modeSelect) {
        LayoutInflater inflater = LayoutInflater.from(m_Context);
        View viewloading = inflater.inflate(R.layout.animation_login, null);
        AlertDialog dialogloading =
                new AlertDialog.Builder(m_Context, 5)
                        .setView(viewloading)
                        .setCancelable(false)
                        .create();
        dialogloading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogloading.show();

        final Handler loginHandler =
                new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {
                            Intent i = new Intent(m_Context.getApplicationContext(), MainActivity.class);
                m_Context.startActivity(i);
                            Toast.makeText(m_Context, "SDK Server Connected", Toast.LENGTH_SHORT).show();
                        } else if (msg.what == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(m_Context, 5);
                            builder.setTitle(m_Context.getString(R.string.erorserver));
                            builder.setMessage(msg.obj.toString());
                            builder.setCancelable(false);
                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    });
                            builder.show();
                        }
                        dialogloading.dismiss();
                    }
                };

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String result = suckmydick(m_Context, userKey);
                        if (result.equals("OK")) {
                            loginHandler.sendEmptyMessage(0);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            loginHandler.sendMessage(msg);
                        }
                    }
                })
                .start();
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            OverlayPermision();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            InstllUnknownApp();
        } else if (requestCode == REQUEST_MANAGE_UNKNOWN_APP_SOURCES) {
            if (!isPermissionGaranted()) {
                takeFilePermissions();
            }
        }
    }

    private void setLokasi(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bahasa", lang);
        editor.apply();
    }

    private void loadbahasa() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String bahasa = sharedPreferences.getString("bahasa", "");
        setLokasi(bahasa);
    }

    private static native String suckmydick(Context mContext, String userKey);
   // private static native String Check(Context mContext, String userKey, String level);
}
