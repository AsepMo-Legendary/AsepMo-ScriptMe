package com.scriptme.story.engine.app.folders.explorer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;
import com.scriptme.story.engine.app.folders.explorer.file.MimeTypes;
import com.scriptme.story.engine.app.folders.explorer.listener.OnCheckedChangeListener;
import com.scriptme.story.engine.app.folders.explorer.model.FileItemModel;
import com.scriptme.story.engine.app.folders.explorer.widget.IconImageView;
import com.scriptme.story.engine.app.listeners.OnItemClickListener;
import com.scriptme.story.engine.app.utils.StringUtils;
import com.scriptme.story.engine.widget.FastScrollRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileListItemAdapter extends RecyclerView.Adapter<FileListItemAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private final String year;
    private final SparseIntArray checkedArray;
    private BaseFile[] data;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnItemClickListener onItemClickListener;
    private BaseFile[] mOriginalValues;
    private int itemCount;
    private Context context;

    public FileListItemAdapter(Context context) {
        this.context = context;
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        checkedArray = new SparseIntArray();
    }

    public void setData(BaseFile[] data) {
        this.data = data;
        itemCount = data.length;
        mOriginalValues = data.clone();
        notifyDataSetChanged();
    }

    public void filter(CharSequence filterText) {
        if (mOriginalValues == null)
            return;

        if (TextUtils.isEmpty(filterText)) {
            data = mOriginalValues;
            itemCount = mOriginalValues.length;
            notifyDataSetChanged();
            return;
        }

        data = new BaseFile[mOriginalValues.length];

        filterText = filterText.toString().toLowerCase();
        int index = 0;
        for (BaseFile path : mOriginalValues) {
            if (path.getName().toLowerCase().contains(filterText)) {
                data[index++] = path;
            }
        }
        itemCount = index;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        BaseFile file = getItem(position);
        char c = file.getName().charAt(0);

        if ((c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                ) {
            return String.valueOf(c);
        }

        return "#";
    }

    public BaseFile getItem(int position) {
        return data[position];
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

	@Override
	public FileListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int id) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        return new FileListItemAdapter.ViewHolder(itemView);
	}


	
    @Override
    public void onBindViewHolder(final FileListItemAdapter.ViewHolder holder, final int position) {
        BaseFile path = data[position];

        MimeTypes mimeTypes = MimeTypes.getInstance();
        Resources res = holder.itemView.getResources();
        int color, icon;
        if (path.isDirectory()) {
            color = R.color.engine_explore_type_folder;
            icon = R.drawable.ic_folder_black;

        } else if (mimeTypes.isImageFile(path)) {
            color = R.color.engine_explore_type_media;
            icon = R.drawable.ic_file_image;

        } else if (mimeTypes.isVideoFile(path)) {
            color = R.color.engine_explore_type_media;
            icon = R.drawable.ic_file_video;

        } else if (mimeTypes.isAudioFile(path)) {
            color = R.color.engine_explore_type_media;
            icon = R.drawable.ic_file_music;

        } else if (mimeTypes.isAPKFile(path)) {
            color = R.color.engine_explore_type_apk;
            icon = R.drawable.ic_file_cad;

        } else if (mimeTypes.isArchive(path)) {
            color = R.color.engine_explore_type_archive;
            icon = R.drawable.ic_file_zip;

        } else if (mimeTypes.isCodeFile(path)) {
            color = R.color.engine_explore_type_code;
            icon = R.drawable.ic_file_code;

        } else {
            color = R.color.engine_explore_type_code;
            String extension = path.getExtension();
            switch (extension.toLowerCase()) {
                default:
                    if (mimeTypes.isTextFile(path)) {
                        color = R.color.engine_explore_type_text;
                        icon = R.drawable.ic_file_text;
                    } else {
                        color = R.color.engine_explore_type_file;
                        icon = R.drawable.ic_file;
                    }
                    break;
            }
        }

        holder.iconImageView.setDefaultImageResource(icon);
        holder.iconImageView.setDefaultBackgroundColor(res.getColor(color));

        FileItemModel item = new FileItemModel();
        item.setName(path.getName());
        item.setExt(icon == 0 && color == R.color.engine_explore_type_file ? path.getExtension() : "");
        item.setDate(getDate(path.lastModified()));
        item.setSecondLine(path.isFile() ? StringUtils.formatSize(path.length()) : "");
        holder.setItem(item);

        holder.iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleChecked(position, holder);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggleChecked(position, holder);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedArray != null && checkedArray.size() > 0) {
                    toggleChecked(position, holder);
                    return;
                }
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position, v);
            }
        });

        boolean isChecked = isChecked(position);
        setViewCheckedStatus(isChecked, holder);
    }

    private void setViewCheckedStatus(boolean isChecked, FileListItemAdapter.ViewHolder holder) {
        holder.iconImageView.setChecked(isChecked);

        if (!isChecked) {
            holder.iconImageView.setSelected(false);
            holder.extTextView.setVisibility(View.VISIBLE);
        } else {
            holder.iconImageView.setSelected(true);
            holder.extTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleChecked(int position, FileListItemAdapter.ViewHolder holder) {
        boolean isChecked = isChecked(position);
        if (isChecked) {
            checkedArray.delete(position);
        } else {
            checkedArray.put(position, 1);
        }

        setViewCheckedStatus(!isChecked, holder);

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(getItem(position), position, !isChecked);
            onCheckedChangeListener.onCheckedChanged(checkedArray.size());
        }

    }

    public boolean isChecked(int position) {
        return checkedArray.get(position) == 1;
    }

    private String getDate(long f) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = (sdf.format(f));
        if (date.substring(0, 4).equals(year))
            date = date.substring(5);
        return date;
    }

    public void checkAll(boolean checked) {
        if (checked) {
            int count = getItemCount();
            for (int i = 0; i < count; i++) {
                checkedArray.put(i, 1);
            }
        } else {
            checkedArray.clear();
        }

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(checkedArray.size());
        }

        notifyDataSetChanged();
    }

	public static class ViewHolder extends RecyclerView.ViewHolder {

        public IconImageView iconImageView;
        public AppCompatTextView extTextView;
		public AppCompatTextView nameTextView;
		public AppCompatTextView dateTextView;
		public AppCompatTextView secondLineTextView;
		private FileItemModel item;
        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = (IconImageView) itemView.findViewById(R.id.iconImageView);
            extTextView = (AppCompatTextView) itemView.findViewById(R.id.extTextView);
			
			nameTextView = (AppCompatTextView) itemView.findViewById(R.id.nameTextView);
			
			dateTextView = (AppCompatTextView) itemView.findViewById(R.id.dateTextView);
			
			secondLineTextView = (AppCompatTextView) itemView.findViewById(R.id.secondLineTextView);
			
        }

		public void setItem(FileItemModel item){
			extTextView.setText(item.getExt());
			nameTextView.setText(item.getName());
			dateTextView.setText(item.getDate());
			secondLineTextView.setText(item.getSecondLine());
		}
    }
	
}
