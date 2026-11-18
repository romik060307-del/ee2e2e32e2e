package com.ohli.ioslauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class AppRepository {
    private AppRepository() { }

    public static List<AppEntry> getLaunchableApps(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN, null);
        query.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolved = manager.queryIntentActivities(query, 0);
        List<AppEntry> apps = new ArrayList<>();
        Set<String> seenPackages = new HashSet<>();
        String ownPackage = context.getPackageName();

        for (ResolveInfo info : resolved) {
            if (info.activityInfo == null) continue;
            String packageName = info.activityInfo.packageName;
            if (packageName == null || packageName.equals(ownPackage) || seenPackages.contains(packageName)) continue;
            try {
                CharSequence label = info.loadLabel(manager);
                String className = info.activityInfo.name;
                AppEntry entry = new AppEntry(
                        label == null ? packageName : label.toString(),
                        packageName,
                        className,
                        info.loadIcon(manager));
                apps.add(entry);
                seenPackages.add(packageName);
            } catch (Exception ignored) {
                // A package can disappear while the launcher is refreshing its app list.
            }
        }

        Collections.sort(apps, new Comparator<AppEntry>() {
            @Override public int compare(AppEntry left, AppEntry right) {
                int leftRank = homeRank(left.label);
                int rightRank = homeRank(right.label);
                if (leftRank != rightRank) return Integer.compare(leftRank, rightRank);
                return left.label.toLowerCase(Locale.getDefault())
                        .compareTo(right.label.toLowerCase(Locale.getDefault()));
            }
        });
        return apps;
    }

    private static int homeRank(String label) {
        String value = label.toLowerCase(Locale.ROOT);
        String[][] preferred = {
                {"phone", "telepon", "dialer", "panggilan"},
                {"contact", "kontak"},
                {"message", "pesan", "sms"},
                {"camera", "kamera"},
                {"photo", "foto", "gallery", "galeri", "album"},
                {"calendar", "kalender"},
                {"clock", "jam", "alarm"},
                {"mail", "gmail", "email"},
                {"notes", "catatan"},
                {"maps", "peta"},
                {"browser", "chrome", "internet"},
                {"play store"},
                {"setting", "pengaturan", "setelan"}
        };
        for (int i = 0; i < preferred.length; i++) {
            for (String word : preferred[i]) if (value.contains(word)) return i;
        }
        return preferred.length + 1;
    }
}
