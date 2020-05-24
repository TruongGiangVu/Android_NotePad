package com.giangvu.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NoteListAdapter extends ArrayAdapter<NoteItem> {
    Context context;
    int layout;
    ArrayList<NoteItem> list;

    public NoteListAdapter(@NonNull Context context, int resource, ArrayList<NoteItem> list) {
        super(context, resource, list);
        this.context = context;
        this.layout = resource;
        this.list = list;
    }

    public class ViewHolder {
        TextView txtTitle, txtContent,txtId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.txtId =(TextView) convertView.findViewById(R.id.noteId);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.noteTitle);
            holder.txtContent = (TextView) convertView.findViewById(R.id.noteContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NoteItem item = list.get(position);
        holder.txtId.setText(item.getId()+"");
        holder.txtTitle.setText(item.getTitle());
        if (item.getContent().length() > 20)
            holder.txtContent.setText(item.getContent().substring(0, 20)+" ...");
        else
            holder.txtContent.setText(item.getContent());
        return convertView;
    }
    public int getSize(){
        return this.list.size();
    }
    public int getLastId(){
        return  list.get(getSize()-1).getId();
    }
    public int newId(){
        return getLastId()+1;
    }
}
