package com.ohli.ioslauncher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private List<AppEntry> apps = new ArrayList<>();
    private LauncherRoot root;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(15, 77, 132));
        getWindow().setNavigationBarColor(Color.rgb(18, 47, 75));
        getWindow().getDecorView().setSystemUiVisibility(0);
        apps = AppRepository.getLaunchableApps(this);
        buildHome();
    }

    @Override protected void onResume() {
        super.onResume();
        apps = AppRepository.getLaunchableApps(this);
        if (root != null) buildHome();
    }

    private void buildHome() {
        root = new LauncherRoot(this);
        root.setBackground(new WallpaperArt());

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.setClipToPadding(false);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(Ui.dp(this, 17), Ui.dp(this, 12), Ui.dp(this, 17), Ui.dp(this, 126));
        scroll.addView(content, new ScrollView.LayoutParams(-1, -2));
        root.addView(scroll, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView date = Ui.text(this, new SimpleDateFormat("EEEE, d MMMM", new Locale("id", "ID")).format(new Date()),
                15, Color.WHITE, true);
        date.setShadowLayer(3f, 0f, 1f, 0x55000000);
        top.addView(date, new LinearLayout.LayoutParams(0, Ui.dp(this, 42), 1f));
        TextView settings = Ui.text(this, "⚙", 23, Color.WHITE, true);
        settings.setGravity(Gravity.CENTER);
        settings.setBackground(Ui.rounded(0x42FFFFFF, 0x55FFFFFF, Ui.dp(this, 24)));
        top.addView(settings, new LinearLayout.LayoutParams(Ui.dp(this, 43), Ui.dp(this, 43)));
        settings.setOnClickListener(v -> openSettings());
        content.addView(top);

        LinearLayout widgets = new LinearLayout(this);
        widgets.setOrientation(LinearLayout.HORIZONTAL);
        widgets.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams widgetParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 116));
        widgetParams.topMargin = Ui.dp(this, 13);
        content.addView(widgets, widgetParams);

        LinearLayout weather = widgetCard("☼", "CUACA", "Tambah lokasi", "Atur lokasi untuk cuaca", 0x2AFFFFFF);
        weather.setOnClickListener(v -> {
            try { startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); }
            catch (Exception ignored) { Toast.makeText(this, "Pengaturan lokasi tidak tersedia.", Toast.LENGTH_SHORT).show(); }
        });
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, -1, 1f);
        half.rightMargin = Ui.dp(this, 6);
        widgets.addView(weather, half);

        Calendar calendar = Calendar.getInstance();
        String day = new SimpleDateFormat("d", Locale.getDefault()).format(calendar.getTime());
        String month = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID")).format(calendar.getTime());
        LinearLayout cal = widgetCard("▦", "KALENDER", day + "  " + month, "Ketuk untuk membuka kalender", 0x2AFFFFFF);
        cal.setOnClickListener(v -> {
            try { startActivity(new Intent(Intent.ACTION_VIEW).setData(CalendarContract.CONTENT_URI)); }
            catch (Exception ignored) { Toast.makeText(this, "Aplikasi kalender tidak ditemukan.", Toast.LENGTH_SHORT).show(); }
        });
        LinearLayout.LayoutParams second = new LinearLayout.LayoutParams(0, -1, 1f);
        second.leftMargin = Ui.dp(this, 6);
        widgets.addView(cal, second);

        TextView section = Ui.text(this, "Aplikasi", 15, Color.WHITE, true);
        section.setShadowLayer(3f, 0f, 1f, 0x55000000);
        LinearLayout.LayoutParams sectionParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 28));
        sectionParams.topMargin = Ui.dp(this, 14);
        content.addView(section, sectionParams);

        LinearLayout grid = new LinearLayout(this);
        grid.setOrientation(LinearLayout.VERTICAL);
        List<AppEntry> homeApps = new ArrayList<>(apps);
        if (homeApps.size() > 16) homeApps = homeApps.subList(0, 16);
        GridBuilder.addApps(this, grid, homeApps, 4, false);
        content.addView(grid, new LinearLayout.LayoutParams(-1, -2));

        TextView search = Ui.text(this, "⌕     Cari aplikasi", 14, Color.WHITE, false);
        search.setGravity(Gravity.CENTER);
        search.setBackground(Ui.rounded(0x55FFFFFF, 0x66FFFFFF, Ui.dp(this, 25)));
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 45));
        searchParams.topMargin = Ui.dp(this, 7);
        content.addView(search, searchParams);
        search.setOnClickListener(v -> openDrawer());

        LinearLayout dock = new LinearLayout(this);
        dock.setOrientation(LinearLayout.HORIZONTAL);
        dock.setGravity(Gravity.CENTER);
        dock.setPadding(Ui.dp(this, 8), Ui.dp(this, 7), Ui.dp(this, 8), Ui.dp(this, 4));
        Ui.styleGlass(dock, this);
        List<AppEntry> dockApps = new ArrayList<>();
        addDockMatch(dockApps, "phone", "telepon", "dialer");
        addDockMatch(dockApps, "chrome", "browser", "internet");
        addDockMatch(dockApps, "message", "pesan", "sms");
        addDockMatch(dockApps, "camera", "kamera");
        if (dockApps.isEmpty()) {
            for (int i = 0; i < Math.min(4, apps.size()); i++) dockApps.add(apps.get(i));
        }
        for (AppEntry entry : dockApps) {
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(0, Ui.dp(this, 77), 1f);
            dock.addView(ShortcutView.create(this, entry, true), itemParams);
        }
        FrameLayout.LayoutParams dockParams = new FrameLayout.LayoutParams(
                -1, Ui.dp(this, 88), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dockParams.setMargins(Ui.dp(this, 17), 0, Ui.dp(this, 17), Ui.dp(this, 7));
        root.addView(dock, dockParams);

        TextView openDrawer = Ui.text(this, "Geser ke atas  •  Semua aplikasi", 11, 0xF2FFFFFF, false);
        openDrawer.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams hintParams = new FrameLayout.LayoutParams(-1, Ui.dp(this, 21), Gravity.BOTTOM);
        hintParams.bottomMargin = Ui.dp(this, 98);
        root.addView(openDrawer, hintParams);
        openDrawer.setOnClickListener(v -> openDrawer());

        setContentView(root);
    }

    private LinearLayout widgetCard(String symbol, String heading, String main, String sub, int ignored) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(Ui.dp(this, 12), Ui.dp(this, 10), Ui.dp(this, 10), Ui.dp(this, 8));
        Ui.styleGlass(card, this);
        TextView title = Ui.text(this, symbol + "   " + heading, 10, 0xEEFFFFFF, true);
        card.addView(title, new LinearLayout.LayoutParams(-1, Ui.dp(this, 18)));
        TextView mainText = Ui.text(this, main, 16, Color.WHITE, true);
        mainText.setMaxLines(1);
        mainText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 28));
        mainParams.topMargin = Ui.dp(this, 5);
        card.addView(mainText, mainParams);
        TextView subText = Ui.text(this, sub, 9, 0xE6FFFFFF, false);
        subText.setMaxLines(2);
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(-1, -2);
        subParams.topMargin = Ui.dp(this, 2);
        card.addView(subText, subParams);
        return card;
    }

    private void addDockMatch(List<AppEntry> target, String... words) {
        for (AppEntry app : apps) {
            String label = app.label.toLowerCase(Locale.ROOT);
            boolean match = false;
            for (String word : words) if (label.contains(word)) match = true;
            if (match) {
                for (AppEntry used : target) if (used.packageName.equals(app.packageName)) return;
                target.add(app);
                return;
            }
        }
    }

    private void openDrawer() {
        startActivity(new Intent(this, AppDrawerActivity.class));
    }

    private void openSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private class LauncherRoot extends FrameLayout {
        private float downY;
        private float downX;
        LauncherRoot(MainActivity context) { super(context); }
        @Override public boolean dispatchTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downY = event.getY();
                downX = event.getX();
            }
            boolean result = super.dispatchTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float dy = event.getY() - downY;
                float dx = Math.abs(event.getX() - downX);
                if (downY > getHeight() - Ui.dp(MainActivity.this, 115)
                        && dy < -Ui.dp(MainActivity.this, 75) && dx < Ui.dp(MainActivity.this, 90)) {
                    openDrawer();
                }
            }
            return result;
        }
    }
}
