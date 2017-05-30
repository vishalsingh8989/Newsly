package com.kepler.news.newsly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kepler.news.newsly.helper.RoundedTransformation;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class FoldingCellListAdapter extends BaseAdapter {


    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private ArrayList<NewsStory> productsList = null;
    private ArrayList<NewsStory> newProductsList = null;
    private static String DESCRIPTION        = "description";
    private static String SOURCE             = "source";
    private static String  SOURCENAME        = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RoundedTransformation transformation = null;


    private int[] mycolors = {
            R.color.n,R.color.d,R.color.e,R.color.q,
            R.color.b,R.color.f,R.color.g,R.color.h,
            R.color.o,R.color.i,R.color.c,R.color.j,R.color.k,
            R.color.b, R.color.l,R.color.m,R.color.p,

    };

    public FoldingCellListAdapter(Context context, ArrayList<NewsStory> productsList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsList = productsList;
        this.transformation = new RoundedTransformation(15, 0);
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
        cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            cell = (FoldingCell) mLayoutInflater.inflate(R.layout.main_cell_item, parent, false);
            // binding view parts to view holder
            viewHolder.description = (TextView) cell.findViewById(R.id.description);
            viewHolder.author = (TextView) cell.findViewById(R.id.author);
            viewHolder.title = (TextView) cell.findViewById(R.id.title);
            viewHolder.source = (TextView) cell.findViewById(R.id.source);
            viewHolder.sourceMini = (TextView)cell.findViewById(R.id.sourceMini);
            viewHolder.image = (ImageView)cell.findViewById(R.id.urltoimage);
            viewHolder.side_bar = (LinearLayout)cell.findViewById(R.id.side_bar);
            viewHolder.side_bar1 = (LinearLayout)cell.findViewById(R.id.side_bar1);
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
        viewHolder.description.setText(productsList.get(position).getDescription().replaceAll("^\"|\"$", ""));
        viewHolder.source.setText("Source: "+productsList.get(position).getSourceName());
        viewHolder.sourceMini.setText("Source: "+productsList.get(position).getSourceName());
        viewHolder.title.setText(productsList.get(position).getTitle().replaceAll("^\"|\"$", ""));
        viewHolder.author.setText("Author: "+productsList.get(position).getAuthor());

        GradientDrawable gradientDrawable = (GradientDrawable)viewHolder.side_bar.getBackground();
//        shapeDrawable.getPaint().setColor(ContextCompat.getColor(mContext,colors[position%colors.length]));

        gradientDrawable.setColor(mContext.getResources().getColor(mycolors[position%mycolors.length]));
        viewHolder.side_bar.setBackground(gradientDrawable);
        viewHolder.side_bar1.setBackground(gradientDrawable);



        Picasso.with(mContext)
                .load(productsList.get(position).getUrltoimage())
                .into(viewHolder.image);

        return cell;
    }




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
        productsList.addAll(entries);

        this.notifyDataSetChanged();
    }
    public ArrayList<NewsStory> getDateEntries()
    {

        return newProductsList;
    }

    private static class ViewHolder {
        LinearLayout side_bar;
        LinearLayout side_bar1;
        TextView description;
        TextView title;
        TextView author;
        TextView source;
        TextView sourceMini;
        ImageView image;

    }
}
