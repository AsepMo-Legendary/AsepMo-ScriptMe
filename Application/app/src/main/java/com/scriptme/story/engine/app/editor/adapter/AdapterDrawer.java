package com.scriptme.story.engine.app.editor.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.settings.theme.Theme;

public class AdapterDrawer extends ArrayAdapter<File> {

    private final Callbacks callbacks;
    // Layout Inflater
    private final LayoutInflater inflater;
    // List of file details
    private final List<File> files;
    private String selectedPath = "";
    private Theme theme;
    
    public AdapterDrawer(Context context, List<File> files, Callbacks callbacks) {
        super(context, R.layout.item_file_list, files);
        this.files = files;
        this.inflater = LayoutInflater.from(context);
        this.callbacks = callbacks;
        this.theme = new Theme();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_drawer_list, parent, false);
            final ViewHolder hold = new ViewHolder();
            hold.nameLabel = (TextView) convertView.findViewById(android.R.id.text1);
            hold.cancelButton = (ImageView) convertView.findViewById(R.id.button_remove_from_list);
            convertView.setTag(hold);

            final String fileName = files.get(position).getName();
            hold.nameLabel.setText(fileName);
            hold.nameLabel.setTextColor(theme.getTextColor());
            hold.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean closeOpenedFile = TextUtils.equals(selectedPath, files.get(position).getAbsolutePath());
                    callbacks.CancelItem(position, closeOpenedFile);
                    if (closeOpenedFile)
                        selectedPath = "";

                }
            });

            if (TextUtils.equals(selectedPath, files.get(position).getAbsolutePath())) {
                hold.nameLabel.setTypeface(null, Typeface.BOLD);
            } else {
                hold.nameLabel.setTypeface(null, Typeface.NORMAL);
            }

        } else {
            final ViewHolder hold = ((ViewHolder) convertView.getTag());
            final String fileName = files.get(position).getName();
            hold.nameLabel.setText(fileName);
            hold.nameLabel.setTextColor(theme.getTextColor());
            
            hold.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean closeOpenedFile = TextUtils.equals(selectedPath, files.get(position).getAbsolutePath());
                    callbacks.CancelItem(position, closeOpenedFile);
                    if (closeOpenedFile)
                        selectedPath = "";
                }
            });

            if (TextUtils.equals(selectedPath, files.get(position).getAbsolutePath())) {
                hold.nameLabel.setTypeface(null, Typeface.BOLD);
            } else {
                hold.nameLabel.setTypeface(null, Typeface.NORMAL);
            }
        }
        return convertView;
    }

    public void selectView(String selectedPath) {
        //callbacks.ItemSelected(selectedPath);
        this.selectedPath = selectedPath;
        notifyDataSetChanged();
    }

    public interface Callbacks {
        void CancelItem(int position, boolean andCloseOpenedFile);

        //void ItemSelected(String path);
    }

    public static class ViewHolder {

        // Name of the file
        public TextView nameLabel;

        public ImageView cancelButton;
    }
}
