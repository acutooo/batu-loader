package com.pubgm.adapter;

import static com.pubgm.activity.MainActivity.fixinstallint;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.pubgm.activity.MainActivity;
import com.pubgm.floating.ToggleAim;
import com.pubgm.floating.ToggleBullet;
import com.pubgm.floating.ToggleSimulation;
import com.pubgm.libhelper.FileHelper;
import com.pubgm.utils.FLog;
import com.pubgm.utils.PermissionUtils;
import com.pubgm.utils.UiKit;

import org.lsposed.lsparanoid.Obfuscate;

import java.util.ArrayList;

import android.content.Intent;
import com.pubgm.floating.FloatService;
import com.pubgm.floating.Overlay;
import com.pubgm.floating.FloatRei;
import com.topjohnwu.superuser.Shell;
import com.pubgm.R;
import com.pubgm.libhelper.ApkEnv;

@Obfuscate
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public MainActivity activity;
    public ArrayList<Integer> imageValues;
    public ArrayList<String> titleValues;
    public ArrayList<String> versionValues;
    public ArrayList<String> statusValues;
    public ArrayList<String> packageValues;

    public RecyclerViewAdapter(MainActivity activity, ArrayList<Integer> imageValues, ArrayList<String> titleValues, ArrayList<String> versionValues, ArrayList<String> statusValues, ArrayList<String> packageValues) {
        this.activity = activity;
        this.imageValues = imageValues;
        this.titleValues = titleValues;
        this.versionValues = versionValues;
        this.statusValues = statusValues;
        this.packageValues = packageValues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_games, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.gameIcon.setImageResource(imageValues.get(position));
        holder.gameTitle.setText(titleValues.get(position));
        holder.gameVersion.setText(versionValues.get(position));
        holder.gameStatus.setText(statusValues.get(position));
        
        if (statusValues.get(position).equals("Risk")) {
            holder.gameStatus.setTextColor(Color.RED);
        } else if (statusValues.get(position).equals("Maintenance")) {
            holder.gameStatus.setTextColor(Color.YELLOW);
        } else if (statusValues.get(position).equals("Coming Soon")) {
            holder.gameStatus.setTextColor(Color.GRAY);
        } else {
            holder.gameStatus.setTextColor(Color.GREEN);
        }
        
        holder.okBtn.setOnClickListener(v -> {
            if (statusValues.get(position).equals("Maintenance") || statusValues.get(position).equals("Coming Soon")) {
                activity.toastImage(R.drawable.icon, "App is currently under: " + statusValues.get(position));
            } else {
                activity.doShowProgress(true);
                doInstallAndRun(holder, position);
            }
        });
        
        holder.noBtn.setOnClickListener(v -> {
            activity.doShowProgress(true);
            unInstallWithDellay(packageValues.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return imageValues.size();
    }
    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView gameIcon;
        private TextView gameTitle;
        private TextView gameVersion;
        private TextView gameStatus;
        private TextView okBtn;
        private TextView noBtn;
        
        public MyViewHolder(View itemView) {
            super(itemView);
            gameIcon = (ImageView)itemView.findViewById(R.id.gameIcon);
            gameTitle = (TextView)itemView.findViewById(R.id.gameTitle);
            gameVersion = (TextView)itemView.findViewById(R.id.gameVersion);
            gameStatus = (TextView)itemView.findViewById(R.id.gameStatus);
            okBtn = (TextView)itemView.findViewById(R.id.okBtn);
            noBtn = (TextView)itemView.findViewById(R.id.noBtn);
        }
    }
    
    private void doInstallAndRun(MyViewHolder holder, int position) {
    if (activity == null) {
        ToastUtils.showLong("Null Activity");
        return;
    }

    String pkg = packageValues.get(position);

    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(() -> {
        boolean hasRoot = Shell.rootAccess();

        if (hasRoot) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                activity.startPatcher();
            }, 5000);
            return;
        }

        if (ApkEnv.getInstance().isInstalled(pkg)) {
            if (ApkEnv.getInstance().launchApk(pkg)) {
                ToastUtils.showLong("Launching " + titleValues.get(position));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    activity.startPatcher();
                }, 5000);
            } else {
                ToastUtils.showLong("Failed to launch " + titleValues.get(position));
            }
        } else {
            if (ApkEnv.getInstance().installByPackage(pkg)) {
                ToastUtils.showLong("Installed " + titleValues.get(position));
                if (ApkEnv.getInstance().launchApk(pkg)) {
                    ToastUtils.showLong("Launching " + titleValues.get(position));
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        activity.startPatcher();
                    }, 5000);
                } else {
                    ToastUtils.showLong("Failed to launch " + titleValues.get(position));
                }
            } else {
                ToastUtils.showLong("Install failed for " + titleValues.get(position));
            }
        }
    });
}



    
    private void unInstallWithDellay(String packageName) {
        UiKit.defer().when(() -> {
            long time = System.currentTimeMillis();
            ApkEnv.getInstance().unInstallApp(packageName);
            time = System.currentTimeMillis() - time;
            long delta = 500L - time;
            if (delta > 0) {
                UiKit.sleep(delta);
            }
        }).done((res) -> {
            activity.doInitRecycler();
            activity.doHideProgress();
            activity.toastImage(R.drawable.ic_check, packageName + " was successfully uninstalled.");
        });
    }
}
