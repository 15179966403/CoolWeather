package com.example.myapplication.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ListAdapter extends ArrayAdapter<String> {
    /**
     * 布局资源id
     */
    private int resourceId;

    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_name=(TextView) view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText(name);
        return view;
    }

    class ViewHolder{
        TextView tv_name;
    }
}
