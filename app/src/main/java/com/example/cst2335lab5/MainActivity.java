package com.example.cst2335lab5;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.graphics.Color;
import android.app.AlertDialog;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private TodoDatabase database;
    private final ArrayList<Todo> todos = new ArrayList<>();
    private TodoAdapter adapter;
    private EditText todoInput;
    private Switch urgentSwitch;
    private ListView todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new TodoDatabase(this);
        buildInterface();
        loadTodos();
    }

    private void buildInterface() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, 0, 0, 0);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(20, 40, 20, 40);
        header.setBackgroundColor(Color.parseColor("#4B0082"));

        TextView heading = new TextView(this);
        heading.setText(R.string.app_name);
        heading.setTextSize(26);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setTextColor(Color.WHITE);
        heading.setGravity(Gravity.CENTER);

        header.addView(heading);
        root.addView(header);

        todoList = new ListView(this);
        adapter = new TodoAdapter(this, todos);
        todoList.setAdapter(adapter);

        root.addView(todoList, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        controls.setGravity(Gravity.CENTER_VERTICAL);
        controls.setPadding(16, 16, 16, 16);

        todoInput = new EditText(this);
        todoInput.setSingleLine(true);
        todoInput.setHint(R.string.todo_hint);
        controls.addView(todoInput, new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        urgentSwitch = new Switch(this);
        urgentSwitch.setText(R.string.urgent);
        controls.addView(urgentSwitch);

        Button addButton = new Button(this);
        addButton.setText(R.string.add);
        controls.addView(addButton);

        root.addView(controls);
        setContentView(root);

        addButton.setOnClickListener(v -> addTodo());

        todoList.setOnItemLongClickListener((parent, view, position, id) -> {
            Todo selected = todos.get(position);

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Delete");
            dialog.setMessage("Are you sure you want to delete this item?");

            dialog.setPositiveButton("Yes", (d, w) -> {
                database.deleteTodo(selected.getId());
                todos.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show();
                printCursor(database.getTodos());
            });

            dialog.setNegativeButton("No", (d, w) -> d.dismiss());

            dialog.show();
            return true;
        });
    }

    private void addTodo() {
        String text = todoInput.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.enter_todo, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean urgent = urgentSwitch.isChecked();
        long id = database.addTodo(text, urgent);
        if (id != -1) {
            todos.add(new Todo(id, text, urgent));
            adapter.notifyDataSetChanged();
            todoInput.setText("");
            urgentSwitch.setChecked(false);
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            printCursor(database.getTodos());
        }
    }

    private void loadTodos() {
        Cursor cursor = database.getTodos();
        printCursor(cursor);
        todos.clear();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_ID));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_TEXT));
                boolean urgent = cursor.getInt(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_URGENT)) == 1;
                todos.add(new Todo(id, text, urgent));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void printCursor(Cursor cursor) {
        Log.d("Lab5Cursor", "Database version: " + database.getReadableDatabase().getVersion());
        Log.d("Lab5Cursor", "Number of columns: " + cursor.getColumnCount());
        for (String column : cursor.getColumnNames()) {
            Log.d("Lab5Cursor", "Column: " + column);
        }
        Log.d("Lab5Cursor", "Number of results: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (i > 0) row.append(", ");
                    row.append(cursor.getColumnName(i)).append("=")
                            .append(cursor.getString(i));
                }
                Log.d("Lab5Cursor", "Row: " + row);
            } while (cursor.moveToNext());
        }
        cursor.moveToPosition(-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) database.close();
    }
}
