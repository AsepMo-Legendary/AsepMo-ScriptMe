package com.scriptme.story.engine.app.folders.explorer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;
import com.scriptme.story.engine.app.folders.explorer.file.LocalFile;
import com.scriptme.story.engine.app.folders.explorer.file.MimeTypes;
import com.scriptme.story.engine.app.folders.explorer.listener.BoolResultListener;
import com.scriptme.story.engine.app.folders.explorer.listener.OnClipboardPasteFinishListener;
import com.scriptme.story.engine.app.folders.explorer.listener.OnCheckedChangeListener;
import com.scriptme.story.engine.app.utils.UIUtils;

public class FileExplorerAction implements OnCheckedChangeListener, ActionMode.Callback, ShareActionProvider.OnShareTargetSelectedListener {
    private final FileExplorerView view;
    private final Context context;
    private final FileClipboard fileClipboard;
    private final ExplorerContext explorerContext;
    private ActionMode actionMode;
    private List<BaseFile> checkedList = new ArrayList<>();
    private ShareActionProvider shareActionProvider;
    private MenuItem renameMenu;
    private MenuItem shareMenu;

    public FileExplorerAction(Context context, FileExplorerView view, FileClipboard fileClipboard, ExplorerContext explorerContext) {
        this.view = view;
        this.context = context;
        this.fileClipboard = fileClipboard;
        this.explorerContext = explorerContext;
    }

    @Override
    public void onCheckedChanged(BaseFile file, int position, boolean checked) {
        if (checked) {
            checkedList.add(file);
        } else {
            checkedList.remove(file);
        }
    }

    @Override
    public void onCheckedChanged(int checkedCount) {
        if(checkedCount > 0) {
            if (actionMode == null)
                actionMode = view.startActionMode(this);
            actionMode.setTitle(context.getString(R.string.engine_explore_selected_x_items, checkedCount));
        } else {
            if(actionMode != null) {
                actionMode.finish();
                actionMode = null;
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        menu.add(0, R.id.action_select_all, 0, R.string.engine_explore_select_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, R.id.action_cut, 0, R.string.engine_explore_cut).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, R.id.action_copy, 0, R.string.engine_explore_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem pasteMenu = menu.add(0, R.id.action_paste, 0, R.string.engine_explore_paste);
        pasteMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        pasteMenu.setEnabled(fileClipboard.canPaste());

        renameMenu = menu.add(0, R.id.action_rename, 0, R.string.engine_explore_rename);
        renameMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        shareMenu = menu.add(0, R.id.action_share, 0, R.string.engine_explore_share);
        shareMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        shareActionProvider = new ShareActionProvider(context);
        shareActionProvider.setOnShareTargetSelectedListener(this);
        MenuItemCompat.setActionProvider(shareMenu, shareActionProvider);

        menu.add(0, R.id.action_delete, 0, R.string.engine_explore_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        shareMenu.setEnabled(canShare());
        renameMenu.setEnabled(checkedList.size()  == 1);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_select_all) {
            if (!item.isChecked()) {
                view.setSelectAll(true);
                item.setChecked(true);
                item.setTitle(R.string.engine_explore_cancel_select_all);
            } else {
                view.setSelectAll(false);
            }
        } else if (id == R.id.action_copy && !checkedList.isEmpty()) {
            fileClipboard.setData(true, checkedList);
            destroyActionMode();
        } else if (id == R.id.action_cut && !checkedList.isEmpty()) {
            fileClipboard.setData(false, checkedList);
            destroyActionMode();
        } else if (id == R.id.action_paste) {
            destroyActionMode();
            fileClipboard.paste(context, explorerContext.getCurrentDirectory(), new OnClipboardPasteFinishListener() {
                @Override
                public void onFinish(int count, String error) {
                    fileClipboard.showPasteResult(context, count, error);
                }
            });
        } else if (id == R.id.action_rename) {
            doRenameAction();
        } else if (id == R.id.action_share) {
            shareFile();
        } else if (id == R.id.action_delete) {
            doDeleteAction();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        shareActionProvider.setOnShareTargetSelectedListener(null);
        shareActionProvider = null;
        checkedList.clear();
        view.setSelectAll(false);
        renameMenu = null;
        shareMenu = null;
        actionMode = null;
    }

    public void destroy() {
        destroyActionMode();
    }

    private void destroyActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    private boolean canShare() {
        for (BaseFile file : checkedList) {
            if (!(file instanceof LocalFile) || !file.isFile())
                return false;
        }
        return true;
    }

    private void doRenameAction() {
        if (checkedList.size() != 1)
            return;

        final BaseFile file = checkedList.get(0);
        UIUtils.showInputDialog(context, R.string.engine_explore_rename, 0, file.getName(), 0, new UIUtils.OnShowInputCallback() {
            @Override
            public void onConfirm(CharSequence input) {
                if (TextUtils.isEmpty(input)) {
                    return;
                }
                if (file.getName().equals(input)) {
                    destroyActionMode();
                    return;
                }
                file.renameTo(file.getParentFile().newFile(input.toString()), new BoolResultListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (!result) {
                            UIUtils.toast(context, R.string.engine_explore_rename_fail);
                            return;
                        }
                        view.refresh();
                        destroyActionMode();
                    }
                });
            }
        });
    }

    private void shareFile() {
        if (checkedList.isEmpty() || shareActionProvider == null)
            return;

        Intent shareIntent = new Intent();
        if (checkedList.size() == 1) {
            File localFile = new File(checkedList.get(0).getPath());
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType(MimeTypes.getInstance().getMimeType(localFile.getPath()));
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(localFile));
        } else {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

            ArrayList<Uri> streams = new ArrayList<>();
            for (BaseFile file : checkedList) {
                if (!(file instanceof LocalFile))
                    throw new ExplorerException(context.getString(R.string.engine_explore_can_not_share_x, file + " isn't LocalFile"));

                streams.add(Uri.fromFile(new File(file.getPath())));
            }

            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, streams);
        }


        shareActionProvider.setShareIntent(shareIntent);
    }

    private void doDeleteAction() {
        for (BaseFile file : checkedList) {
            file.delete(null);
        }
        view.refresh();
        destroyActionMode();
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        destroyActionMode();
        return false;
    }

    public void doCreateFolder() {
        UIUtils.showInputDialog(context, R.string.engine_explore_create_folder, 0 ,null, 0, new UIUtils.OnShowInputCallback() {
            @Override
            public void onConfirm(CharSequence input) {
                if (TextUtils.isEmpty(input)) {
                    return;
                }
                BaseFile folder = explorerContext.getCurrentDirectory().newFile(input.toString());
                folder.mkdirs(new BoolResultListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (!result) {
                            UIUtils.toast(context, R.string.engine_explore_can_not_create_folder);
                            return;
                        }
                        view.refresh();
                        destroyActionMode();
                    }
                });
            }
        });
    }
}
