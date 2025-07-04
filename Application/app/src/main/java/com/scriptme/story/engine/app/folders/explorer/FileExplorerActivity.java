package com.scriptme.story.engine.app.folders.explorer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;
import com.scriptme.story.engine.app.folders.explorer.file.LocalFile;
import com.scriptme.story.engine.app.folders.explorer.file.FileListSorter;
import com.scriptme.story.engine.app.folders.explorer.listener.OnClipboardDataChangedListener;
import com.scriptme.story.engine.app.folders.explorer.widget.FileNameEditText;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.app.settings.theme.ThemeSupportActivity;

public class FileExplorerActivity extends ThemeSupportActivity implements View.OnClickListener, OnClipboardDataChangedListener {
    public static final String EXTRA_HOME_PATH = "home_path";
    public static final String EXTRA_INIT_PATH = "dest_file";
    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_ENCODING = "encoding";

    public static final int MODE_PICK_FILE = 1;
    public static final int MODE_PICK_PATH = 2;

	private LinearLayout filenameLayout;
	private FileNameEditText filenameEditText;
	private AppCompatButton btnSelect;
	private AppCompatTextView fileEncodingTextView;

    private FileListPagerFragment mFileListPagerFragment;
    private int mMode;
    private String fileEncoding = null;
    private String mLastPath;
    private String mHomePath;
    private FileClipboard fileClipboard;
    private MenuItem mPasteMenu;
    private SearchView mSearchView;
    private Theme theme;
    public static void startPickFileActivity(Activity activity,
                                             @Nullable String destFile,
                                             @Nullable String homePath,
                                             int requestCode) {
        startPickFileActivity(activity, destFile, homePath, null, requestCode);
    }

    public static void startPickFileActivity(@NonNull Activity activity,
                                             @Nullable String destFile,
                                             @Nullable String homePath,
                                             @Nullable String encoding,
                                             int requestCode) {
        Intent it = new Intent(activity, FileExplorerActivity.class);
        it.putExtra(EXTRA_MODE, MODE_PICK_FILE);
        it.putExtra(EXTRA_INIT_PATH, destFile);
        it.putExtra(EXTRA_HOME_PATH, homePath);
        it.putExtra(EXTRA_ENCODING, encoding);
        activity.startActivityForResult(it, requestCode);
    }

    public static void startPickPathActivity(Activity activity, String destFile, String encoding, int requestCode) {
        Intent it = new Intent(activity, FileExplorerActivity.class);
        it.putExtra(EXTRA_MODE, MODE_PICK_PATH);
        it.putExtra(EXTRA_INIT_PATH, destFile);
        it.putExtra(EXTRA_ENCODING, encoding);
        activity.startActivityForResult(it, requestCode);
    }

    public static void startPickPathActivity(Fragment fragment, String destFile, String encoding, int requestCode) {
        Intent it = new Intent(fragment.getContext(), FileExplorerActivity.class);
        it.putExtra(EXTRA_MODE, MODE_PICK_PATH);
        it.putExtra(EXTRA_INIT_PATH, destFile);
        it.putExtra(EXTRA_ENCODING, encoding);
        fragment.startActivityForResult(it, requestCode);
    }

    @Nullable
    public static String getFile(Intent it) {
        return it.getStringExtra("file");
    }

    @Nullable
    public static String getFileEncoding(Intent it) {
        return it.getStringExtra("encoding");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = Theme.with(this);
		theme.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_MODE, MODE_PICK_FILE);
        mHomePath = intent.getStringExtra(EXTRA_HOME_PATH);
        if (TextUtils.isEmpty(mHomePath)) {
            mHomePath = Environment.getExternalStorageDirectory().getPath();
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {     
            mToolbar.setPopupTheme(theme.getPopupToolbarStyle());
            mToolbar.setBackgroundColor(theme.getBackgroundColor());
            mToolbar.setTitle(mMode == MODE_PICK_FILE ? R.string.engine_explore_select_file : R.string.engine_explore_select_path);
            setSupportActionBar(mToolbar);
		}	
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle(mMode == MODE_PICK_FILE ? R.string.engine_explore_select_file : R.string.engine_explore_select_path);

		filenameLayout  = findViewById(R.id.filename_layout);
		btnSelect  = findViewById(R.id.btn_select);
		filenameEditText  = findViewById(R.id.filename_editText);
		fileEncodingTextView = findViewById(R.id.file_encoding_textView);
        mLastPath = Theme.getInstance().getLastOpenPath();
        if (TextUtils.isEmpty(mLastPath)) {
            mLastPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            File file = new File(mLastPath);
            if (!file.exists() || !file.canRead()) {
                mLastPath = Environment.getExternalStorageDirectory().getPath();
            }
        }

        String initPath = intent.getStringExtra(EXTRA_INIT_PATH);
        if (!TextUtils.isEmpty(initPath)) {
            File dest = new File(initPath);
            mLastPath = dest.isFile() ? dest.getParent() : dest.getPath();
            filenameEditText.setText(dest.getName());
        } else {
            filenameEditText.setText(getString(R.string.engine_explore_untitled_file_name));
        }

        initFileView();
        btnSelect.setOnClickListener(this);
        fileEncodingTextView.setOnClickListener(this);

        String encoding = intent.getStringExtra("encoding");
        fileEncoding = encoding;
        if (TextUtils.isEmpty(encoding)) {
            encoding = getString(R.string.engine_explore_auto_detection_encoding);
        }
        fileEncodingTextView.setText(encoding);
        filenameLayout.setVisibility(mMode == MODE_PICK_FILE ? View.GONE : View.VISIBLE);

        getFileClipboard().setOnClipboardDataChangedListener(this);
    }

    private void initFileView() {
        BaseFile path = new LocalFile(mLastPath);
        mFileListPagerFragment = (FileListPagerFragment) getSupportFragmentManager()
            .findFragmentByTag(FileListPagerFragment.class.getName());
        if (mFileListPagerFragment == null) {
            mFileListPagerFragment = FileListPagerFragment.newFragment(path);
        }
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content, mFileListPagerFragment, FileListPagerFragment.class.getName())
            .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explore, menu);

        // Retrieve the SearchView and plug it into SearchManager
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (mFileListPagerFragment != null) {
                        mFileListPagerFragment.filter(newText);
                    }
                    return false;
                }
            });

        Theme preferences = Theme.getInstance();
        menu.findItem(R.id.show_hidden_files_menu).setChecked(preferences.isShowHiddenFiles());
        mPasteMenu = menu.findItem(R.id.paste_menu);

        int sortId;
        switch (preferences.getFileSortType()) {
            case FileListSorter.SORT_DATE:
                sortId = R.id.sort_by_datetime_menu;
                break;
            case FileListSorter.SORT_SIZE:
                sortId = R.id.sort_by_size_menu;
                break;
            case FileListSorter.SORT_TYPE:
                sortId = R.id.sort_by_type_menu;
                break;
            default:
                sortId = R.id.sort_by_name_menu;
                break;
        }

        menu.findItem(sortId).setChecked(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
        } else {
            try {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                String name = fragment.getClass().getName();
                if (name.contains("FileListPagerFragment")) {
                    FileListPagerFragment listFile = (FileListPagerFragment) getSupportFragmentManager().findFragmentById(R.id.content);
                    boolean isBack = listFile.onBackPressed();
                    super.onBackPressed(); 
                }
            } catch (ClassCastException e) {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Theme preferences = Theme.getInstance();
        int id = item.getItemId();
        if (id == R.id.show_hidden_files_menu) {
            item.setChecked(!item.isChecked());
            preferences.setShowHiddenFiles(item.isChecked());
        } else if (id == R.id.sort_by_name_menu) {
            item.setChecked(true);
            preferences.setFileSortType(FileListSorter.SORT_NAME);
        } else if (id == R.id.sort_by_datetime_menu) {
            item.setChecked(true);
            preferences.setFileSortType(FileListSorter.SORT_DATE);
        } else if (id == R.id.sort_by_size_menu) {
            item.setChecked(true);
            preferences.setFileSortType(FileListSorter.SORT_SIZE);
        } else if (id == R.id.sort_by_type_menu) {
            item.setChecked(true);
            preferences.setFileSortType(FileListSorter.SORT_TYPE);
        } else if (id == R.id.action_goto_home) {
            LocalFile home = new LocalFile(mHomePath);
            mFileListPagerFragment.switchToPath(home);
        }
        return super.onOptionsItemSelected(item);
    }

    boolean onSelectFile(BaseFile file) {
        if (file.isFile()) {
            if (mMode == MODE_PICK_FILE) {
                Intent it = new Intent();
                it.putExtra("file", file.getPath());
                it.putExtra("encoding", fileEncoding);
                setResult(RESULT_OK, it);
                finish();
            }
            return true;
        } else if (file.isDirectory()) {
            mLastPath = file.getPath();
            filenameEditText.setText(file.getPath());
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_select) {
            onSave();
        } else if (id == R.id.file_encoding_textView) {
            onShowEncodingList();
        }
    }

    private void onShowEncodingList() {
        SortedMap m = Charset.availableCharsets();
        Set k = m.keySet();

        int selected = 0;
        final String[] names = new String[m.size() + 1];
        names[0] = getString(R.string.engine_explore_auto_detection_encoding);
        Iterator iterator = k.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            String n = (String) iterator.next();
            if (n.equals(fileEncoding))
                selected = i;
            names[i++] = n;
        }

        new AlertDialog.Builder(this)
			.setTitle(R.string.engine_explore_encoding)
            .setSingleChoiceItems(names, selected, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fileEncodingTextView.setText(names[which]);
                    if (which > 0) {
                        fileEncoding = names[which];
                    }
                    dialog.dismiss();
                    ;
                }
            })
            .show();
    }

    private void onSave() {
        String fileName = filenameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(fileName)) {
            filenameEditText.setError(getString(R.string.engine_explore_cannot_be_empty));
            return;
        }
        if (TextUtils.isEmpty(mLastPath)) {
            filenameEditText.setError(getString(R.string.engine_explore_unknown_path));
            return;
        }

        File f = new File(mLastPath);
        if (f.isFile()) {
            f = f.getParentFile();
        }

        try {
            File newFile = new File(fileName);
            if (!newFile.exists()) {
                newFile = new File(f, fileName);
            }
            setResult(newFile);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setResult(File file) {
        Intent it = new Intent();
        it.putExtra("file", file.getPath());
        it.putExtra("encoding", fileEncoding);
        setResult(RESULT_OK, it);
        finish();
    }

    public FileClipboard getFileClipboard() {
        if (fileClipboard == null)
            fileClipboard = new FileClipboard();

        return fileClipboard;
    }

    @Override
    public void onClipboardDataChanged() {
        if (mPasteMenu == null)
            return;

        mPasteMenu.setVisible(getFileClipboard().canPaste());
    }
}
