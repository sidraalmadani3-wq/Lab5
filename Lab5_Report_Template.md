# CST2335 Lab 5 Report

Name: Sidra Al Madani

## Code summary
The app uses `TodoDatabase`, which extends `SQLiteOpenHelper`, to create and access the SQLite table. Todos are loaded when the activity opens. Adding a todo inserts its text and urgency into the database. Long-pressing an item deletes it from both the list and database. The `printCursor` method logs the database version, column count, column names, result count, and rows using the `Lab5Cursor` tag.

## Emulator screenshots
Insert 3–5 real screenshots from your emulator:
1. App opened with saved todos.
2. Adding a normal todo.
3. Adding an urgent todo.
4. Long-press deletion.
5. Logcat showing `Lab5Cursor` output.

## GitHub repository
Paste your own repository URL here after publishing the project.
