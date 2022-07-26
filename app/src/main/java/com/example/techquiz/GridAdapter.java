package com.example.techquiz;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class GridAdapter extends BaseAdapter {
    private List<String> categoryList;

    public GridAdapter(List<String> categoryList)
    {
        this.categoryList=categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout,parent,false);
        }
        else
        {
            view=convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(parent.getContext(),SetsActivity.class);
                intent.putExtra("CATEGORY",categoryList.get(position));
                intent.putExtra("CATEGORY_ID",position+1);
                parent.getContext().startActivity(intent);
            }
        });
        ((TextView) view.findViewById(R.id.gridName)).setText(categoryList.get(position));
        Random rd=new Random();
        int color= Color.argb(255,rd.nextInt(255),rd.nextInt(255),rd.nextInt(255));
        view.setBackgroundColor(color);
        return view;
    }
}
