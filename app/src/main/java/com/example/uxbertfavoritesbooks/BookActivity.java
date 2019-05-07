package com.example.uxbertfavoritesbooks;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uxbertfavoritesbooks.bookdata.Book;
import com.example.uxbertfavoritesbooks.bookdata.BookDatabase;
import com.example.uxbertfavoritesbooks.utils.MyNotification;

import java.util.Calendar;

public class BookActivity extends AppCompatActivity {

    private EditText tileEditText;
    private EditText authorEditText;
    private EditText detailEditText;
    private EditText releaseDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tileEditText = findViewById(R.id.title_editText);
        authorEditText = findViewById(R.id.author_editText);
        detailEditText = findViewById(R.id.detail_editText);
        releaseDateEditText = findViewById(R.id.releaseDate_editText);
    }

    public void openPicker(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                releaseDateEditText.setText(dayOfMonth + "-" + (month + 1)  + "-" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void addFavorite(View view) {
        if (requiredDataPresent()) {
            Book book = new Book(tileEditText.getText().toString(), authorEditText.getText().toString(),
                    releaseDateEditText.getText().toString(), detailEditText.getText().toString(), false);
            new DbInsertBookAsyncTask().execute(new Pair(this, book));
        } else
            Toast.makeText(this, "Required data not present", Toast.LENGTH_SHORT).show();
    }

    private boolean requiredDataPresent() {
        boolean goodToGo = false;
        if (!tileEditText.getText().equals("") && !authorEditText.getText().equals("") && !detailEditText.getText().equals("") && !releaseDateEditText.getText().equals(""))
            goodToGo = true;
        return goodToGo;
    }

    public class DbInsertBookAsyncTask extends AsyncTask<Pair<Context, Book>, Void, Void> {

        @Override
        protected Void doInBackground(Pair<Context, Book>[] pairs) {
            Context context = pairs[0].first;
            Book book = pairs[0].second;
            BookDatabase.getInstance(context).getBookDao().insert(book);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(BookActivity.this, "Favorite add success!", Toast.LENGTH_SHORT).show();
            BookActivity.this.finish();
        }
    }
}
