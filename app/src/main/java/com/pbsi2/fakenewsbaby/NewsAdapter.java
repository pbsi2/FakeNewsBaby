package com.pbsi2.fakenewsbaby;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private static int currentSelectedIndex = -1;
    private Context mContext;
    private ArrayList<BadNews> mNews;
    private ClickAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Context mContext, ArrayList<BadNews> news, ClickAdapterListener listener) {

        this.mContext = mContext;
        mNews = news;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View itemViews = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_items_layout, parent, false);
        return new MyViewHolder(itemViews);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        LinearLayout linearLayout = holder.linearLayout;

        TextView stitle = linearLayout.findViewById(R.id.news_text);
        stitle.setText(mNews.get(position).getTitle());

        TextView ssection = linearLayout.findViewById(R.id.section_text);
        ssection.setText(mNews.get(position).getSection());

        TextView slink = linearLayout.findViewById(R.id.link_text);
        final String myUrl = mNews.get(position).getLink();
        final String surl = "<a href=\""
                + myUrl
                + "\">"
                + myUrl
                + "</a>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            slink.setText(Html.fromHtml(surl, Html.FROM_HTML_MODE_COMPACT));
        } else {
            slink.setText(Html.fromHtml(surl));
        }

        //slink.setText(Html.fromHtml(surl, Html.FROM_HTML_MODE_COMPACT));
        TextView sAuthorName = linearLayout.findViewById(R.id.author_text);
        sAuthorName.setText(mNews.get(position).getAuthor());

        TextView stype = linearLayout.findViewById(R.id.type_text);
        stype.setText(mNews.get(position).getType());

        TextView sdate = linearLayout.findViewById(R.id.date_text);
        String dateTemp = mNews.get(position).getDate();
        sdate.setText(dateTemp.substring(0, Math.min(dateTemp.length(), 10)));

        slink.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(myUrl);
                Intent sBrowser = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(view.getContext(), sBrowser, null);
            }

        });
        stitle.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(myUrl);
                Intent sBrowser = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(view.getContext(), sBrowser, null);
            }

        });
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mNews.size();
    }


    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void selectAll() {

        for (int i = 0; i < getItemCount(); i++)
            selectedItems.put(i, true);
        notifyDataSetChanged();

    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }


    public interface ClickAdapterListener {

        void onRowClicked(int position);

        void onRowLongClicked(int position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        // each data item is just a string in this case
        //public LinearLayout mLinearLayout;
        public LinearLayout linearLayout;

        public MyViewHolder(View v) {

            super(v);

            linearLayout = v.findViewById(R.id.linearLayout);
            TextView slink = v.findViewById(R.id.link_text);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }

}