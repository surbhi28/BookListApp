package com.example.pallvi.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Activity Context, List<Book> books){
        super(Context,0,books);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView img = (ImageView) listItemView.findViewById(R.id.book_img);

        Glide.with(getContext()).load(currentBook.getImage()).into(img);

        TextView name = (TextView) listItemView.findViewById(R.id.title_view);

        name.setText(currentBook.getTitle());

        TextView author = (TextView)listItemView.findViewById(R.id.auth_name);

        author.setText(currentBook.getAuthor());

        TextView price = (TextView)listItemView.findViewById(R.id.book_price);

        price.setText(currentBook.getPrice());

        return listItemView;
    }


}