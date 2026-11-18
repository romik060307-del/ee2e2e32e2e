package com.ohli.ioslauncher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LockPreviewActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(15, 77, 132));
        getWindow().setNavigationBarColor(Color.rgb(18, 47, 75));

        FrameLayout root = new FrameLayout(this);
        root.setBackground(new WallpaperArt());
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER_HORIZONTAL);
        page.setPadding(Ui.dp(this, 22), Ui.dp(this, 40), Ui.dp(this, 22), Ui.dp(this, 24));
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        TextView date = Ui.text(this,
                new SimpleDateFormat("EEEE, d MMMM", new Locale("id", "ID")).format(new Date()),
                15, Color.WHITE, true);
        date.setGravity(Gravity.CENTER);
        page.addView(date, new LinearLayout.LayoutParams(-1, Ui.dp(this, 35)));

        TextView time = Ui.text(this, new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()),
                64, 0xFFE7F5FF, false);
        time.setGravity(Gravity.CENTER);
        time.setShadowLayer(5f, 0f, 2f, 0x66000000);
        page.addView(time, new LinearLayout.LayoutParams(-1, Ui.dp(this, 100)));

        TextView caption = Ui.text(this, "Tampilan pratinjau", 12, 0xEFFFFFFF, false);
        caption.setGravity(Gravity.CENTER);
        page.addView(caption, new LinearLayout.LayoutParams(-1, Ui.dp(this, 28)));

        TextView spacer = new TextView(this);
        page.addView(spacer, new LinearLayout.LayoutParams(1, 0, 1f));

        LinearLayout notification = new LinearLayout(this);
        notification.setOrientation(LinearLayout.VERTICAL);
        notification.setPadding(Ui.dp(this, 16), Ui.dp(this, 15), Ui.dp(this, 16), Ui.dp(this, 15));
        Ui.styleGlass(notification, this);
        TextView app = Ui.text(this, "●   NOTIFIKASI CONTOH", 10, Color.WHITE, true);
        notification.addView(app);
        TextView message = Ui.text(this, "Selamat datang di OHLI iOS 27 Home. Tampilan ini hanya contoh; notifikasi perangkat tetap berasal dari Android.",
                13, Color.WHITE, false);
        message.setLineSpacing(Ui.dp(this, 3), 1f);
        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(-1, -2);
        messageParams.topMargin = Ui.dp(this, 8);
        notification.addView(message, messageParams);
        page.addView(notification, new LinearLayout.LayoutParams(-1, -2));

        TextView dismiss = Ui.text(this, "Kembali ke Home", 13, Color.WHITE, true);
        dismiss.setGravity(Gravity.CENTER);
        dismiss.setBackground(Ui.rounded(0x4AFFFFFF, 0x80FFFFFF, Ui.dp(this, 22)));
        LinearLayout.LayoutParams dismissParams = new LinearLayout.LayoutParams(-1, Ui.dp(this, 47));
        dismissParams.topMargin = Ui.dp(this, 16);
        page.addView(dismiss, dismissParams);
        dismiss.setOnClickListener(v -> finish());
        setContentView(root);
    }
}
