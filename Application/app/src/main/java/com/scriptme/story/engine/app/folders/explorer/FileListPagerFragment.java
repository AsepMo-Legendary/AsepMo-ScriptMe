package com.scriptme.story.engine.app.folders.explorer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.adapters.decoration.HorizontalDividerItemDecoration;
import com.scriptme.story.engine.app.folders.explorer.adapter.FileListItemAdapter;
import com.scriptme.story.engine.app.folders.explorer.adapter.PathButtonAdapter;
import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;
import com.scriptme.story.engine.app.folders.explorer.listener.FileListResultListener;
import com.scriptme.story.engine.app.folders.explorer.listener.OnClipboardPasteFinishListener;
import com.scriptme.story.engine.app.folders.explorer.file.FileListSorter;
import com.scriptme.story.engine.app.listeners.OnItemClickListener;
import com.scriptme.story.engine.app.tasks.JecAsyncTask;
import com.scriptme.story.engine.app.tasks.TaskListener;
import com.scriptme.story.engine.app.tasks.TaskResult;
import com.scriptme.story.engine.app.utils.UIUtils;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.widget.FastScrollRecyclerView;

public class FileListPagerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, FileExplorerView, ExplorerContext, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String EXTRA_PATH = "path";

	private RecyclerView pathScrollView;
    private FileListItemAdapter adapter;
    private BaseFile path;
	private SwipeRefreshLayout explorerSwipeRefreshLayout;
	private FastScrollRecyclerView recyclerView;
    private AppCompatTextView emptyLayout;
	private PathButtonAdapter pathAdapter;
    private ScanFilesTask task;
    private FileExplorerAction action;

    public static FileListPagerFragment newFragment(BaseFile path) {
        FileListPagerFragment f = new FileListPagerFragment();
        Bundle b = new Bundle();
        b.putParcelable(EXTRA_PATH, path);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        path = getArguments().getParcelable(EXTRA_PATH);
        return inflater.inflate(R.layout.file_explorer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pathScrollView = view.findViewById(R.id.pathScrollView);
		explorerSwipeRefreshLayout = view.findViewById(R.id.explorer_swipe_refresh_layout);
		recyclerView = view.findViewById(R.id.recyclerView);
		emptyLayout = view.findViewById(R.id.emptyLayout);
		
		action = new FileExplorerAction(getContext(), this, ((FileExplorerActivity) getActivity()).getFileClipboard(), this);
        adapter = new FileListItemAdapter(getContext());
        adapter.setOnCheckedChangeListener(action);
        adapter.setOnItemClickListener(this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                emptyLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                    }
                });

            }
        });

        pathScrollView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pathAdapter = new PathButtonAdapter();
        pathAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                pathScrollView.scrollToPosition(pathAdapter.getItemCount() - 1);
            }
        });
        pathAdapter.setPath(path);
        pathAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                BaseFile file = pathAdapter.getItem(position);
                switchToPath(file);
            }
        });
        pathScrollView.setAdapter(pathAdapter);

        explorerSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).margin(getResources().getDimensionPixelSize(R.dimen.engine_dp_70), 0).build());
        explorerSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                explorerSwipeRefreshLayout.setRefreshing(true);
            }
        });
        //EditorPreference.getInstance(getContext()).registerOnSharedPreferenceChangeListener(this);

        view.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EditorPreference.getInstance(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        if (action != null) {
            action.destroy();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        // 不能加在onPause，因为请求Root UI会导致pause
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.paste_menu) {
            final FileClipboard fileClipboard = ((FileExplorerActivity) getActivity()).getFileClipboard();
            fileClipboard.paste(getContext(), getCurrentDirectory(), new OnClipboardPasteFinishListener() {
                @Override
                public void onFinish(int count, String error) {
                    onRefresh();
                    fileClipboard.showPasteResult(getContext(), count, error);
                }
            });
            item.setVisible(false);
        } else if (item.getItemId() == R.id.add_folder_menu) {
            action.doCreateFolder();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        UpdateRootInfo updateRootInfo = new UpdateRootInfo() {

            @Override
            public void onUpdate(BaseFile f) {
                path = f;
            }
        };
        task = new ScanFilesTask(getActivity(), path, updateRootInfo);
        task.setTaskListener(new TaskListener<BaseFile[]>() {
            @Override
            public void onCompleted() {
                if (explorerSwipeRefreshLayout != null)
                    explorerSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(BaseFile[] result) {
                if (adapter != null)
                    adapter.setData(result);
            }

            @Override
            public void onError(Exception e) {
                if (explorerSwipeRefreshLayout == null)
                    return;
                explorerSwipeRefreshLayout.setRefreshing(false);
                UIUtils.toast(getContext(), e);
            }
        });
        task.execute();
    }

    @Override
    public void onItemClick(int position, View view) {
        BaseFile file = adapter.getItem(position);
        if (!((FileExplorerActivity) getActivity()).onSelectFile(file)) {
            if (file.isDirectory()) {
                switchToPath(file);
            }
        }
    }

    public boolean onBackPressed() {
        BaseFile parent = path.getParentFile();
        if (parent == null || parent.getPath().startsWith(path.getPath())) {
            switchToPath(parent);
            return true;
        }
        return false;
    }

    public void switchToPath(BaseFile file) {
        if (file.canRead()) {
            path = file;
            pathAdapter.setPath(file);
            Theme.getInstance().setLastOpenPath(file.getPath());
            onRefresh();
        } else {
            Toast.makeText(getContext(), R.string.engine_explore_cannot_read_folder, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return ((AppCompatActivity) getActivity()).startSupportActionMode(callback);
    }

    @Override
    public void setSelectAll(boolean checked) {
        adapter.checkAll(checked);
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void filter(String query) {
        if (adapter != null) {
            adapter.filter(query);
        }
    }

    @Override
    public BaseFile getCurrentDirectory() {
        return path;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        onRefresh();
    }

    private interface UpdateRootInfo {
        void onUpdate(BaseFile path);
    }

    private static class ScanFilesTask extends JecAsyncTask<Void, Void, BaseFile[]> {
        private final UpdateRootInfo updateRootInfo;
        private final Context context;
        private final boolean isRoot = false;
        private BaseFile path;

        private ScanFilesTask(Context context, BaseFile path, UpdateRootInfo updateRootInfo) {
            this.context = context.getApplicationContext();
            this.path = path;
            this.updateRootInfo = updateRootInfo;
        }

        @Override
        protected void onRun(final TaskResult<BaseFile[]> taskResult, Void... params) throws Exception {
            Theme preferences = Theme.getInstance();
            final boolean showHiddenFiles = preferences.isShowHiddenFiles();
            final int sortType = preferences.getFileSortType();
            updateRootInfo.onUpdate(path);
            path.listFiles(new FileListResultListener() {
                @Override
                public void onResult(BaseFile[] result) {
                    if (result.length == 0) {
                        taskResult.setResult(result);
                        return;
                    }
                    if (!showHiddenFiles) {
                        List<BaseFile> list = new ArrayList<>(result.length);
                        for (BaseFile file : result) {
                            if (file.getName().charAt(0) == '.') {
                                continue;
                            }
                            list.add(file);
                        }
                        result = new BaseFile[list.size()];
                        list.toArray(result);
                    }
                    Arrays.sort(result, new FileListSorter(true, sortType, true));
                    taskResult.setResult(result);
                }
            });
        }
    }
}
