package io.github.rushiranpise.gameunlocker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.security.KeyStore;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressLint("DiscouragedPrivateApi")
@SuppressWarnings("ConstantConditions")
public class GAMEUNLOCKER implements IXposedHookLoadPackage {

    private static final String TAG = GAMEUNLOCKER.class.getSimpleName();

    // Packages to Spoof as Samsung Galaxy On7
    private static final String[] packagesToChangeSamsungGalaxyOn7 = {
        "com.tencent.ig",
        "com.pubg.krmobile",
        "com.vng.pubgmobile",
        "com.rekoo.pubgm",
        "com.pubg.imobile",
        "com.tencent.gam",
        "com.tencent.iglite"
    };

    private static final String[] packagesToChange120FPS = {
        "tp.fps90",
        "tq.tech.Fps"
    };

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        String packageName = loadPackageParam.packageName;

        // Samsung
        if (Arrays.asList(packagesToChangeSamsungGalaxyOn7).contains(packageName)) {
            propsToChangeSamsungGalaxyOn7();
            XposedBridge.log("Spoofed " + packageName + " as Samsung Galaxy On7");
        }

        if (Arrays.asList(packagesToChange120FPS).contains(packageName)) {
            propsToChange120FPS();
            XposedBridge.log("Spoofed " + packageName + " as Samsung Galaxy On7 120FPS");
        }
    }

    // samsung
    // Props to Spoof as Samsung Galaxy On7
    private static void propsToChangeSamsungGalaxyOn7() {
        setPropValue("MANUFACTURER", "samsung");
        setPropValue("MODEL", "SM-G600S");
    }

    private static void propsToChange120FPS() {
        setPropValue("MANUFACTURER", "samsung");
        setPropValue("MODEL", "SM-G600S");
        setPropValue("DEVICE", "SM-G600FY");
    }

    private static void setPropValue(String key, Object value) {
        try {
            Log.d(TAG, "Defining prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            XposedBridge.log("Failed to set prop: " + key + "\n" + Log.getStackTraceString(e));
        }
    }
}
