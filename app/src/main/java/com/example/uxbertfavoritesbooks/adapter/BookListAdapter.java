package com.example.uxbertfavoritesbooks.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uxbertfavoritesbooks.R;
import com.example.uxbertfavoritesbooks.bookdata.Book;
import com.example.uxbertfavoritesbooks.bookdata.BookDatabase;
import com.example.uxbertfavoritesbooks.utils.MyNotification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookItemViewHolder> {

    public interface OnBookItemListener {
        void onClickDelete(Book book);
    }

    private List<Book> bookList;
    private Context context;
    private OnBookItemListener listener;

    public BookListAdapter(Context context, OnBookItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.book_item_layout, viewGroup, false);
        return new BookItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookItemViewHolder bookItemViewHolder, int i) {
        final Book book = bookList.get(i);

        final String bookName = book.bookName;
        String bookAuthor = book.bookAuthor;
        final String releaseDate = book.releaseDate;
        final String bookDetails = book.bookDetails;

        bookItemViewHolder.bookTitle.setText(bookName);
        bookItemViewHolder.bookAuthor.setText(bookAuthor);
        bookItemViewHolder.bookDate.setText(releaseDate);
        bookItemViewHolder.bookDetail.setText(bookDetails);

        if(book.notificationActive) {
            bookItemViewHolder.notification.setImageDrawable(context.getDrawable(R.drawable.ic_notifications_active_red_24dp));
            showNotificationIfReleaseDateReached(releaseDate, bookName, bookDetails);
        } else {
            bookItemViewHolder.notification.setImageDrawable(context.getDrawable(R.drawable.ic_notifications_black_24dp));
        }

        bookItemViewHolder.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView notImg = (ImageView) v;
                if(!book.notificationActive) {
                    book.notificationActive = true;
                    notImg.setImageDrawable(context.getDrawable(R.drawable.ic_notifications_active_red_24dp));
                    showNotificationIfReleaseDateReached(releaseDate, bookName, bookDetails);
                } else {
                    book.notificationActive = false;
                    notImg.setImageDrawable(context.getDrawable(R.drawable.ic_notifications_black_24dp));
                }
                new DbUpdateNotificationActiveAsyncTask().execute(new Pair<Context, Book>(context, book));
            }
        });

        bookItemViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DbDeleteBookAsyncTask().execute(new Pair<Context, Book>(context, book));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (bookList == null) return 0;
        return bookList.size();
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    private class DbUpdateNotificationActiveAsyncTask extends AsyncTask<Pair<Context, Book>, Void, Void> {

        @Override
        protected Void doInBackground(Pair<Context, Book>[] pairs) {
            Context context = pairs[0].first;
            Book book = pairs[0].second;
            BookDatabase.getInstance(context).getBookDao().updateNotificationActive(book.id, !book.notificationActive);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    public class DbDeleteBookAsyncTask extends AsyncTask<Pair<Context, Book>, Void, Book> {

        @Override
        protected Book doInBackground(Pair<Context, Book>[] pairs) {
            Context context = pairs[0].first;
            Book book = pairs[0].second;
            BookDatabase.getInstance(context).getBookDao().delete(book);
            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            listener.onClickDelete(book);
        }
    }

    private void showNotificationIfReleaseDateReached(String releaseDate, String title, String details) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date today = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            Date date = simpleDateFormat.parse(releaseDate);

            if (date.compareTo(today) >= 0)
                MyNotification.createNotification(context, title, details);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class BookItemViewHolder extends RecyclerView.ViewHolder {

        private TextView bookTitle;
        private TextView bookDate;
        private TextView bookAuthor;
        private TextView bookDetail;
        private ImageView notification;
        private ImageView delete;

        public BookItemViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title);
            bookDate = itemView.findViewById(R.id.book_date);
            bookAuthor = itemView.findViewById(R.id.book_Author);
            bookDetail = itemView.findViewById(R.id.book_detail);
            notification = itemView.findViewById(R.id.notification_imageView);
            delete = itemView.findViewById(R.id.delete_imageView);
        }
    }
}