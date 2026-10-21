package com.example.cst2335lab5;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    public TodoAdapter(Context context, List<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Todo item = getItem(position);

        LinearLayout row;

        if (convertView == null) {
            row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(20, 20, 20, 20);
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView text = new TextView(getContext());
            text.setId(View.generateViewId());
            text.setTextSize(20);
            row.addView(text);

            convertView = row;
        } else {
            row = (LinearLayout) convertView;
        }

        TextView text = (TextView) row.getChildAt(0);
        text.setText(item.getText());

        if (item.isUrgent()) {
            row.setBackgroundColor(Color.RED);
            text.setTextColor(Color.WHITE);
        } else {
            row.setBackgroundColor(Color.WHITE);
            text.setTextColor(Color.BLACK);
        }

        return row;
    }
}
