package com.example.uxbertfavoritesbooks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.uxbertfavoritesbooks.adapter.BookListAdapter;
import com.example.uxbertfavoritesbooks.bookdata.Book;
import com.example.uxbertfavoritesbooks.bookdata.BookDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BookListAdapter.OnBookItemListener {

    private RecyclerView recyclerView;
    private BookListAdapter adapter;

    private TextView noFavTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noFavTextView = findViewById(R.id.no_fav_textView);
        recyclerView = findViewById(R.id.book_recyclerView);
        adapter = new BookListAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DbSelectBookAsyncTask().execute(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(this, BookActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickDelete(Book book) {
        new DbSelectBookAsyncTask().execute(this);
    }

    public class DbSelectBookAsyncTask extends AsyncTask<Context, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(Context... context) {
            return BookDatabase.getInstance(context[0]).getBookDao().getAllBooks();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            if (adapter != null) {
                if (!books.isEmpty()) {
                    noFavTextView.setVisibility(View.GONE);
                    adapter.setBookList(books);
                }
            }
        }
    }
}
