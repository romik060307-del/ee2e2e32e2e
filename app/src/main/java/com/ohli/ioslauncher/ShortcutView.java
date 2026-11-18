package com.ohli.ioslauncher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;

public final class ShortcutView {
    private ShortcutView() { }

    public static View create(Context context, AppEntry app, boolean compact) {
        LinearLayout item = new LinearLayout(context);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER_HORIZONTAL);
        int vertical = Ui.dp(context, compact ? 3 : 5);
        item.setPadding(Ui.dp(context, 2), vertical, Ui.dp(context, 2), vertical);

        FrameLayout tile = new FrameLayout(context);
        int tileSize = Ui.dp(context, compact ? 53 : 59);
        GradientDrawable tileBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{tileColor(app.label, true), tileColor(app.label, false)});
        tileBg.setCornerRadius(Ui.dp(context, 15));
        tileBg.setStroke(Ui.dp(context, 1), 0x36FFFFFF);
        tile.setBackground(tileBg);
        tile.setElevation(Ui.dp(context, 3));

        String symbol = symbolFor(app.label);
        if (symbol != null) {
            TextView glyph = new TextView(context);
            glyph.setText(symbol);
            glyph.setTextColor(glyphColor(app.label));
            glyph.setTextSize(symbol.length() > 1 ? 19 : 26);
            glyph.setGravity(Gravity.CENTER);
            glyph.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            tile.addView(glyph, new FrameLayout.LayoutParams(-1, -1));
        } else {
            ImageView icon = new ImageView(context);
            icon.setImageDrawable(app.icon);
            icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(
                    Ui.dp(context, 38), Ui.dp(context, 38), Gravity.CENTER);
            tile.addView(icon, iconParams);
        }

        TextView label = Ui.text(context, app.label, compact ? 10 : 10.5f,
                Color.WHITE, false);
        label.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        label.setMaxLines(2);
        label.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(-1, Ui.dp(context, 27));
        labelParams.topMargin = Ui.dp(context, 5);
        item.addView(tile, new LinearLayout.LayoutParams(tileSize, tileSize));
        item.addView(label, labelParams);

        item.setClickable(true);
        item.setFocusable(true);
        item.setOnClickListener(v -> app.launch(context));
        item.setOnLongClickListener(v -> {
            android.widget.Toast.makeText(context, app.label + " · ketuk untuk membuka", android.widget.Toast.LENGTH_SHORT).show();
            return true;
        });
        return item;
    }

    private static String normalized(String label) {
        return label == null ? "" : label.toLowerCase(Locale.ROOT);
    }

    private static String symbolFor(String label) {
        String s = normalized(label);
        if (has(s, "phone", "telepon", "dialer", "panggilan")) return "☎";
        if (has(s, "message", "pesan", "sms", "chat")) return "●";
        if (has(s, "camera", "kamera")) return "◉";
        if (has(s, "photo", "foto", "gallery", "galeri")) return "✿";
        if (has(s, "setting", "pengaturan", "setelan")) return "⚙";
        if (has(s, "calendar", "kalender")) return "18";
        if (has(s, "chrome", "browser", "internet", "safari")) return "◎";
        if (has(s, "play store", "app store")) return "A";
        if (has(s, "whatsapp")) return "☎";
        if (has(s, "youtube")) return "▶";
        if (has(s, "tiktok")) return "♪";
        if (has(s, "facebook")) return "f";
        if (has(s, "instagram")) return "◎";
        if (has(s, "telegram")) return "➤";
        if (has(s, "gmail", "mail", "email", "surat")) return "✉";
        if (has(s, "clock", "jam", "alarm")) return "◷";
        if (has(s, "maps", "peta")) return "⌖";
        if (has(s, "music", "musik", "spotify")) return "♫";
        if (has(s, "calculator", "kalkulator")) return "＋";
        return null;
    }

    private static boolean has(String value, String... keys) {
        for (String key : keys) if (value.contains(key)) return true;
        return false;
    }

    private static int tileColor(String label, boolean start) {
        String s = normalized(label);
        int a = 0xFF;
        if (has(s, "phone", "telepon", "whatsapp", "message", "pesan")) return start ? 0xFF52D69A : 0xFF079D77;
        if (has(s, "camera", "kamera")) return start ? 0xFF9EAAB8 : 0xFF45566F;
        if (has(s, "photo", "foto", "gallery", "galeri", "instagram")) return start ? 0xFFFFA0C4 : 0xFFE84D85;
        if (has(s, "setting", "pengaturan", "setelan")) return start ? 0xFFBCC7D4 : 0xFF65758B;
        if (has(s, "calendar", "kalender")) return start ? 0xFFFFFFFF : 0xFFF2F6FA;
        if (has(s, "youtube", "video")) return start ? 0xFFFF6A69 : 0xFFD72535;
        if (has(s, "tiktok")) return start ? 0xFF47515E : 0xFF111827;
        if (has(s, "facebook", "telegram", "maps", "peta", "chrome", "browser", "internet")) return start ? 0xFF7CCCFB : 0xFF267AC6;
        if (has(s, "mail", "gmail", "email")) return start ? 0xFF8DD5FF : 0xFF2786D8;
        return start ? 0xFFE8F5FF : 0xFF8ECBEF;
    }

    private static int glyphColor(String label) {
        if (has(normalized(label), "calendar", "kalender")) return 0xFFE14C54;
        if (has(normalized(label), "tiktok", "setting", "pengaturan")) return Color.WHITE;
        return Color.WHITE;
    }
}
