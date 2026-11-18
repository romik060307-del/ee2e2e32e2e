package com.ohli.ioslauncher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppDrawerActivity extends Activity {
    private LinearLayout grid;
    private List<AppEntry> allApps;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(15, 77, 132));
        getWindow().setNavigationBarColor(Color.rgb(18, 47, 75));
        allApps = AppRepository.getLaunchableApps(this);

        FrameLayout root = new FrameLayout(this);
        root.setBackground(new WallpaperArt());
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(Ui.dp(this, 18), Ui.dp(this, 16), Ui.dp(this, 18), Ui.dp(this, 14));
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = Ui.text(this, "Perpustakaan App", 24, Color.WHITE, true);
        title.setShadowLayer(3f, 0f, 1f, 0x55000000);
        titleRow.addView(title, new LinearLayout.LayoutParams(0, Ui.dp(this, 48), 1f));
        TextView close = Ui.text(this, "✕", 20, Color.WHITE, true);
        close.setGravity(Gravity.CENTER);
        titleRow.addView(close, new LinearLayout.LayoutParams(Ui.dp(this, 44), Ui.dp(this, 44)));
        close.setOnClickListener(v -> finish());
        page.addView(titleRow);

        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(14);
        search.setTextColor(Color.WHITE);
        search.setHintTextColor(0xD9FFFFFF);
        search.setHint("⌕   Cari aplikasi");
        search.setPadding(Ui.dp(this, 15), 0, Ui.dp(this, 15), 0);
        search.setBackground(Ui.rounded(0x55FFFFFF, 0x77FFFFFF, Ui.dp(this, 24)));
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 48));
        searchParams.topMargin = Ui.dp(this, 12);
        page.addView(search, searchParams);

        ScrollView scroll = new ScrollView(this);
        scroll.setClipToPadding(false);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(-1, 0, 1f);
        scrollParams.topMargin = Ui.dp(this, 14);
        page.addView(scroll, scrollParams);
        grid = new LinearLayout(this);
        grid.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(grid, new ScrollView.LayoutParams(-1, -2));
        render(allApps);

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) { }
        });
        setContentView(root);
    }

    private void filter(String query) {
        String needle = query.toLowerCase(Locale.ROOT).trim();
        List<AppEntry> filtered = new ArrayList<>();
        for (AppEntry app : allApps) {
            if (app.label.toLowerCase(Locale.ROOT).contains(needle)) filtered.add(app);
        }
        render(filtered);
    }

    private void render(List<AppEntry> entries) {
        grid.removeAllViews();
        if (entries.isEmpty()) {
            TextView empty = Ui.text(this, "Aplikasi tidak ditemukan", 14, Color.WHITE, false);
            empty.setGravity(Gravity.CENTER);
            grid.addView(empty, new LinearLayout.LayoutParams(-1, Ui.dp(this, 90)));
        } else {
            GridBuilder.addApps(this, grid, entries, 4, false);
        }
    }
}
