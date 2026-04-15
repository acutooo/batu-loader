package com.pubgm.libhelper;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.File;

import com.pubgm.BoxApplication;
import com.pubgm.R;
import com.pubgm.utils.FLog;

import top.niunaijun.blackbox.BlackBoxCore;

public class ApkEnv {
    private static final int USER_ID = 0; // BlackBox default user
    private static ApkEnv singleton;

    public static ApkEnv getInstance() {
        if (singleton == null) {
            singleton = new ApkEnv();
        }
        return singleton;
    }

    // === Get App Info ===
    public ApplicationInfo getApplicationInfo(String packageName) {
        try {
            return BoxApplication.getInstance()
                    .getPackageManager()
                    .getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException err) {
            FLog.error(err.getMessage());
            Toast.makeText(BoxApplication.getInstance(),
                    "App not found: " + packageName,
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // === BlackBox Wrappers ===
    public boolean isInstalled(String packageName) {
        try {
            return BlackBoxCore.get().isInstalled(packageName, USER_ID);
        } catch (Throwable t) {
            FLog.error("isInstalled failed: " + t.getMessage());
            return false;
        }
    }

    public boolean installApk(String packagePath) {
        try {
            BlackBoxCore.get().installPackageAsUser(packagePath, USER_ID);
            Toast.makeText(BoxApplication.getInstance(),
                    "Installed: " + packagePath,
                    Toast.LENGTH_SHORT).show();
            return true;
        } catch (Throwable t) {
            FLog.error("installApk failed: " + t.getMessage());
            Toast.makeText(BoxApplication.getInstance(),
                    "Install failed: " + packagePath,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean unInstallApk(String packageName) {
        try {
            BlackBoxCore.get().uninstallPackageAsUser(packageName, USER_ID);
            Toast.makeText(BoxApplication.getInstance(),
                    "Uninstalled: " + packageName,
                    Toast.LENGTH_SHORT).show();
            return true;
        } catch (Throwable t) {
            FLog.error("unInstallApk failed: " + t.getMessage());
            return false;
        }
    }

    public boolean launchApk(String packageName) {
        if (!isInstalled(packageName)) {
            Toast.makeText(BoxApplication.getInstance(),
                    "Client not installed",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            BlackBoxCore.get().launchApk(packageName, USER_ID);
            return true;
        } catch (Throwable t) {
            FLog.error("launchApk failed: " + t.getMessage());
            return false;
        }
    }

    // === Compatibility Shims ===
    public boolean installByFile(String packagePath) { return installApk(packagePath); }
    public boolean installByPackage(String packageName) { return installApk(packageName); }
    public void unInstallApp(String packageName) { unInstallApk(packageName); }
    public boolean isRunning(String packageName) { return false; } // Not supported
    public void stopRunningApp(String packageName) { FLog.error("stopRunningApp not supported with BlackBoxCore"); }
    public boolean tryAddLoader(String packageName) { return false; }
    public File getObbContainerPath(String packageName) { return BoxApplication.getInstance().getFilesDir(); }
    public ApplicationInfo getApplicationInfoContainer(String packageName) { return getApplicationInfo(packageName); }
}
