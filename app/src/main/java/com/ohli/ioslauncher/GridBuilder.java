package com.ohli.ioslauncher;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import java.util.List;

public final class GridBuilder {
    private GridBuilder() { }

    public static void addApps(Context context, LinearLayout parent, List<AppEntry> apps,
                               int columns, boolean compact) {
        parent.removeAllViews();
        int rowCount = (apps.size() + columns - 1) / columns;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            for (int column = 0; column < columns; column++) {
                int index = rowIndex * columns + column;
                LinearLayout.LayoutParams cell = new LinearLayout.LayoutParams(0,
                        Ui.dp(context, compact ? 86 : 96), 1f);
                if (index < apps.size()) {
                    row.addView(ShortcutView.create(context, apps.get(index), compact), cell);
                } else {
                    row.addView(new android.view.View(context), cell);
                }
            }
            parent.addView(row, new LinearLayout.LayoutParams(-1, Ui.dp(context, compact ? 86 : 96)));
        }
    }
}
