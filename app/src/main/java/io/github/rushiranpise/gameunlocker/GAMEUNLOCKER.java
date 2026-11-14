package io.github.rushiranpise.gameunlocker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.github.libxposed.api.XposedModule;

@SuppressLint("DiscouragedPrivateApi")
public class GAMEUNLOCKER extends XposedModule {

    private static final String TAG = "GAMEUNLOCKER";

    // Target game packages (keep in sync with META-INF/xposed/scope.list)
    private static final Set<String> TARGET_PACKAGES = new HashSet<>(Arrays.asList(
            "com.tencent.ig",
            "com.pubg.krmobile",
            "com.vng.pubgmobile",
            "com.rekoo.pubgm",
            "com.pubg.imobile",
            "com.tencent.gam",
            "com.tencent.iglite",
            "com.tencent.igfit"
    ));

    @Override
    public void onPackageLoaded(@NonNull PackageLoadedParam param) {
        if (!param.isFirstPackage()) return;

        String packageName = param.getPackageName();
        if (TARGET_PACKAGES.contains(packageName)) {
            applyDeviceProfile();
            Log.i(TAG, "Device profile applied for " + packageName);
        }
    }

    private static void applyDeviceProfile() {
        setPropValue("BRAND", "samsung");
        setPropValue("MANUFACTURER", "samsung");
        setPropValue("MODEL", "SM-A300H");
        setPropValue("DEVICE", "a33g");
    }

    private static void setPropValue(String key, Object value) {
        try {
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop: " + key, e);
        }
    }
}
