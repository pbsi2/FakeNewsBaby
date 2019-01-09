package com.pbsi2.fakenewsbaby;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

//@SuppressWarnings("ALL")
public class NewsActivity extends AppCompatActivity
        implements NewsAdapter.ClickAdapterListener,
        LoaderManager.LoaderCallbacks<ArrayList<BadNews>>
        //, View.OnClickListener
{
    private final int LOADER_ID = 0;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private GetNews myGetNews;
    private String TAG = NewsActivity.class.getSimpleName();
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar nTopToolbar;
    private LoaderManager.LoaderCallbacks<ArrayList<BadNews>> pCallbacks;
    private String pUrl;
    private CoordinatorLayout coordLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pUrl = MainActivity.guardianUrl;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);
        nTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.newsview);
        mRecyclerView.setHasFixedSize(true);
        // For LINEAR
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        // For a GRID
        //mLayoutManager = new GridLayoutManager(this,5);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        actionModeCallback = new ActionModeCallback();
        pCallbacks = this;
        // Build the Loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @Override
    public Loader<ArrayList<BadNews>> onCreateLoader(int id, Bundle args) {
        // Create a new Loader with the following query parameters.
        Toast.makeText(getApplicationContext(),
                "CreateLoader",
                Toast.LENGTH_SHORT).show();
        return new GetNews(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BadNews>> loader, ArrayList<BadNews> data) {
        if (data == null) {
            setContentView(R.layout.error_layout);
            nTopToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(nTopToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            TextView textView = findViewById(R.id.errorView);
            textView.setText(R.string.null_data);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Finished Loader",
                    Toast.LENGTH_SHORT).show();
            NewsAdapter mAdapter = new NewsAdapter(this, data, this);
            mRecyclerView.setAdapter(mAdapter);
            Toast.makeText(getApplicationContext(),
                    "Adapter done or redone",
                    Toast.LENGTH_SHORT).show();
        }
        // The view now displays the queried data.
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BadNews>> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.clearSelections();
    }

    @Override
    public void onRowClicked(int position) {
        enableActionMode(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
            actionMode = null;
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void selectAll() {
        mAdapter.selectAll();
        int count = mAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
        actionMode = null;
    }

    private void deleteRows() {
        actionMode = null;
    }

    private void updateColoredRows() { actionMode = null; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Toast.makeText(getApplicationContext(),
                    "REFRESH REQUESTED",
                    Toast.LENGTH_SHORT).show();
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            Toast.makeText(getApplicationContext(),
                    "REFRESH DONE",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_news, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.d("Clicked Action", "here");
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected rows
                    deleteRows();
                    mode.finish();
                    return true;
                case R.id.action_color:
                    updateColoredRows();
                    mode.finish();
                    return true;
                case R.id.action_select_all:
                    selectAll();
                    return true;
                case R.id.action_refresh:
                    onCreateLoader(14, null);
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, pCallbacks);
                    Toast.makeText(getApplicationContext(),
                            "REFRESH DONE",
                            Toast.LENGTH_SHORT).show();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
        }
    }
}