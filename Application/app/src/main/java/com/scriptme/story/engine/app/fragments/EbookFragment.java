package com.scriptme.story.engine.app.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.folders.ebook.PDFView;
import com.scriptme.story.engine.app.folders.ebook.listener.OnLoadCompleteListener;
import com.scriptme.story.engine.app.folders.ebook.listener.OnPageChangeListener;
import com.scriptme.story.engine.app.folders.ebook.listener.OnPageErrorListener;
import com.scriptme.story.engine.app.folders.ebook.scroll.DefaultScrollHandle;
import com.scriptme.story.engine.app.folders.ebook.PdfDocument;

public class EbookFragment extends Fragment implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

	private static final String TAG = EbookFragment.class.getSimpleName();
	
    private static final String EXTRA_TEXT = "text";
    private Context mContext;
	private AppCompatActivity mActivity;
    private PDFView pdfView;
    private Uri uri;
    private Integer pageNumber = 0;
    private String pdfFileName;
    private String text;

    public static EbookFragment newInstance(String text) {
        EbookFragment fragment = new EbookFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        text = getArguments().getString(EXTRA_TEXT);

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_ebook, container, false);
	}
	
	@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
		mActivity = (AppCompatActivity)getActivity();
        pdfView = (PDFView)view.findViewById(R.id.pdfView);
		pdfView.setBackgroundColor(Color.LTGRAY);
        
		displayFromAsset(text);
    }
	
    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(assetFileName)
			.defaultPage(pageNumber)
			.onPageChange(this)
			.enableAnnotationRendering(true)
			.onLoad(this)
			.scrollHandle(new DefaultScrollHandle(mContext))
			.spacing(10) // in dp
			.onPageError(this)
			.load();
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName(uri);

        pdfView.fromUri(uri)
			.defaultPage(pageNumber)
			.onPageChange(this)
			.enableAnnotationRendering(true)
			.onLoad(this)
			.scrollHandle(new DefaultScrollHandle(mContext))
			.spacing(10) // in dp
			.onPageError(this)
			.load();
    }
    
	@Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

	@Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }
	
    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
	
}
