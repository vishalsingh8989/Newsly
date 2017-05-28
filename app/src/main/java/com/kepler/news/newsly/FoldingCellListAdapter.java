package com.kepler.news.newsly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class FoldingCellListAdapter extends BaseAdapter {


    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private ArrayList<NewsStory> productsList = null;
    private static String DESCRIPTION        = "description";
    private static String SOURCE             = "source";
    private static String  SOURCENAME        = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public FoldingCellListAdapter(Context context, ArrayList<NewsStory> productsList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsList = productsList;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int i) {
        return productsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        FoldingCell cell;

        NewsStory item = (NewsStory) getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            cell = (FoldingCell) mLayoutInflater.inflate(R.layout.main_cell_item, parent, false);
            // binding view parts to view holder
            viewHolder.description = (TextView) cell.findViewById(R.id.description);
            viewHolder.source = (TextView) cell.findViewById(R.id.source);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.description.setText(productsList.get(position).getDescription());
        viewHolder.source.setText(productsList.get(position).getSourceName());


//        // set custom btn handler for list item from that item
//        if (item.getRequestBtnClickListener() != null) {
//            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
//        }
////        } else {
////            // (optionally) add "default" handler if no handler found in item
////            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
////        }


        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public void upDateEntries(ArrayList<NewsStory> entries) {
        productsList = entries;
        this.notifyDataSetChanged();
    }
    // View lookup cache
    private static class ViewHolder {
        TextView description;
        TextView source;

    }
}
