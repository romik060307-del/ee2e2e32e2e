package com.ohli.ioslauncher;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(15, 77, 132));
        getWindow().setNavigationBarColor(Color.rgb(18, 47, 75));

        FrameLayout root = new FrameLayout(this);
        root.setBackground(new WallpaperArt());
        ScrollView scroll = new ScrollView(this);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(Ui.dp(this, 19), Ui.dp(this, 18), Ui.dp(this, 19), Ui.dp(this, 25));
        scroll.addView(page, new ScrollView.LayoutParams(-1, -2));
        root.addView(scroll, new FrameLayout.LayoutParams(-1, -1));

        TextView back = Ui.text(this, "‹   Kembali", 15, Color.WHITE, true);
        page.addView(back, new LinearLayout.LayoutParams(-1, Ui.dp(this, 40)));
        back.setOnClickListener(v -> finish());

        TextView title = Ui.text(this, "Pengaturan Home", 27, Color.WHITE, true);
        title.setShadowLayer(3f, 0f, 1f, 0x55000000);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 48));
        titleParams.topMargin = Ui.dp(this, 7);
        page.addView(title, titleParams);
        TextView subtitle = Ui.text(this, "Personalisasi tampilan launcher", 13, 0xEEFFFFFF, false);
        page.addView(subtitle, new LinearLayout.LayoutParams(-1, Ui.dp(this, 26)));

        page.addView(section("TAMPILAN"));
        page.addView(settingRow("Wallpaper Liquid Glass", "Terapkan wallpaper gradasi biru-toska", "◈", this::applyWallpaper));
        page.addView(settingRow("Pratinjau layar kunci", "Contoh jam dan notifikasi bergaya iPhone", "◷", () ->
                startActivity(new Intent(this, LockPreviewActivity.class))));

        page.addView(section("SISTEM ANDROID"));
        page.addView(settingRow("Jadikan launcher utama", "Pilih OHLI iOS 27 Home sebagai aplikasi Home", "⌂", this::openHomeSettings));
        page.addView(settingRow("Pengaturan HP", "Buka pengaturan asli perangkat Vivo", "⚙", () -> openSystemSettings(Settings.ACTION_SETTINGS)));
        page.addView(settingRow("Pengaturan layar", "Kecerahan, layar, dan waktu tidur", "▣", () -> openSystemSettings(Settings.ACTION_DISPLAY_SETTINGS)));
        page.addView(settingRow("Wi-Fi dan jaringan", "Buka koneksi perangkat", "◉", () -> openSystemSettings(Settings.ACTION_WIFI_SETTINGS)));

        page.addView(section("TENTANG"));
        LinearLayout note = new LinearLayout(this);
        note.setOrientation(LinearLayout.VERTICAL);
        note.setPadding(Ui.dp(this, 15), Ui.dp(this, 14), Ui.dp(this, 15), Ui.dp(this, 14));
        Ui.styleGlass(note, this);
        TextView appName = Ui.text(this, "OHLI iOS 27 Home  ·  v1.0.0", 13, Color.WHITE, true);
        note.addView(appName);
        TextView explanation = Ui.text(this,
                "Launcher ini mengubah layar Home dan laci aplikasi. Layar kunci asli, panel notifikasi, font seluruh sistem, dan aplikasi Pengaturan Vivo tetap dikelola Android.",
                12, 0xE8FFFFFF, false);
        explanation.setLineSpacing(Ui.dp(this, 3), 1f);
        LinearLayout.LayoutParams explanationParams = new LinearLayout.LayoutParams(-1, -2);
        explanationParams.topMargin = Ui.dp(this, 8);
        note.addView(explanation, explanationParams);
        LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(-1, -2);
        noteParams.topMargin = Ui.dp(this, 8);
        page.addView(note, noteParams);

        TextView footer = Ui.text(this, "Dibuat untuk Android 8.0 atau lebih baru", 11, 0xDDFFFFFF, false);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 40));
        page.addView(footer, footerParams);

        setContentView(root);
    }

    private TextView section(String label) {
        TextView text = Ui.text(this, label, 10, 0xE8FFFFFF, true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, Ui.dp(this, 34));
        params.topMargin = Ui.dp(this, 12);
        text.setLayoutParams(params);
        return text;
    }

    private View settingRow(String title, String detail, String icon, Runnable action) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(Ui.dp(this, 13), Ui.dp(this, 11), Ui.dp(this, 13), Ui.dp(this, 11));
        Ui.styleGlass(row, this);
        TextView iconView = Ui.text(this, icon, 22, Color.WHITE, true);
        iconView.setGravity(Gravity.CENTER);
        row.addView(iconView, new LinearLayout.LayoutParams(Ui.dp(this, 42), Ui.dp(this, 46)));
        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        textColumn.setPadding(Ui.dp(this, 8), 0, 0, 0);
        TextView heading = Ui.text(this, title, 14, Color.WHITE, true);
        TextView supporting = Ui.text(this, detail, 10.5f, 0xE6FFFFFF, false);
        supporting.setMaxLines(2);
        LinearLayout.LayoutParams headingParams = new LinearLayout.LayoutParams(-1, -2);
        textColumn.addView(heading, headingParams);
        LinearLayout.LayoutParams supportingParams = new LinearLayout.LayoutParams(-1, -2);
        supportingParams.topMargin = Ui.dp(this, 4);
        textColumn.addView(supporting, supportingParams);
        row.addView(textColumn, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView arrow = Ui.text(this, "›", 23, 0xF2FFFFFF, false);
        arrow.setGravity(Gravity.CENTER);
        row.addView(arrow, new LinearLayout.LayoutParams(Ui.dp(this, 25), -1));
        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = Ui.dp(this, 9);
        row.setLayoutParams(params);
        return row;
    }

    private void applyWallpaper() {
        try {
            WallpaperManager manager = WallpaperManager.getInstance(this);
            manager.setBitmap(WallpaperArt.createBitmap(1080, 2400));
            Toast.makeText(this, "Wallpaper berhasil diterapkan.", Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            Toast.makeText(this, "Wallpaper belum dapat diterapkan pada perangkat ini.", Toast.LENGTH_LONG).show();
        }
    }

    private void openHomeSettings() {
        openSystemSettings(Settings.ACTION_HOME_SETTINGS);
    }

    private void openSystemSettings(String action) {
        try {
            startActivity(new Intent(action));
        } catch (Exception exception) {
            try { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
            catch (Exception ignored) { Toast.makeText(this, "Pengaturan sistem tidak tersedia.", Toast.LENGTH_SHORT).show(); }
        }
    }
}
