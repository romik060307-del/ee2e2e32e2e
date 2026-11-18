package com.ohli.ioslauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public final class AppEntry {
    public final String label;
    public final String packageName;
    public final String className;
    public final Drawable icon;

    public AppEntry(String label, String packageName, String className, Drawable icon) {
        this.label = label;
        this.packageName = packageName;
        this.className = className;
        this.icon = icon;
    }

    public void launch(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(packageName, className));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception exception) {
            android.widget.Toast.makeText(context, "Aplikasi tidak dapat dibuka.", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
