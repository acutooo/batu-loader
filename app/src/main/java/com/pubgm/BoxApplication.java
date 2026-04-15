package com.pubgm;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.io.File;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.app.configuration.ClientConfiguration;

public class BoxApplication extends Application {

    private static BoxApplication instance;
    private boolean internetAvailable = false;

    // Existing getter
    public static BoxApplication getInstance() {
        return instance;
    }

    // Alias used by older codebases: BoxApplication.get()
    // Adds compatibility so older sources compile without changes.
    public static BoxApplication get() {
        return instance;
    }

    // Convenience to return application context
    public static Context getContext() {
        return instance == null ? null : instance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            BlackBoxCore.get().doAttachBaseContext(base, new ClientConfiguration() {
                @Override
                public String getHostPackageName() {
                    return base.getPackageName();
                }

                @Override
                public boolean isHideRoot() {
                    return true;
                }

                @Override
                public boolean isHideXposed() {
                    return true;
                }

                @Override
                public boolean isEnableDaemonService() {
                    return false;
                }

                // If you want to provide custom install handling you can implement here.
                // Note: this method was in your snippet without @Override — keep as-is or remove.
                public boolean requestInstallPackage(File file) {
                    PackageInfo packageInfo = base.getPackageManager()
                            .getPackageArchiveInfo(file.getAbsolutePath(), 0);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            BlackBoxCore.get().doCreate();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    // chmod helper (wraps runtime exec)
    public void doChmod(String path, int mode) {
        try {
            // Mode is numeric (e.g., 755). Ensure path is quoted to handle spaces.
            Runtime.getRuntime().exec(new String[] { "sh", "-c", "chmod " + mode + " \"" + path + "\"" });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Internet availability tracking
    public void setInternetAvailable(boolean available) {
        this.internetAvailable = available;
    }

    public boolean isInternetAvailable() {
        return internetAvailable;
    }
}
