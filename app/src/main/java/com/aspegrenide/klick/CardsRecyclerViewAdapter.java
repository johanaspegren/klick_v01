package com.aspegrenide.klick;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardsRecyclerViewAdapter extends RecyclerView.Adapter<CardsRecyclerViewAdapter.ViewHolder> {

    private List<CardDetails> cardDetails;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    CardsRecyclerViewAdapter(Context context, List<CardDetails> data) {
        this.mInflater = LayoutInflater.from(context);
        this.cardDetails = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_details, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = cardDetails.get(position).getName();
        String uri = cardDetails.get(position).getUri();
        String action = cardDetails.get(position).getAction();
        String imgUrl = cardDetails.get(position).getImgUrl();
        if (imgUrl != null) {
            int drawableId = context.getResources().getIdentifier(imgUrl, "drawable",
                    context.getPackageName());
            holder.imageView.setImageResource(drawableId);
        }
        holder.tvCardName.setText(name);
        holder.tvCardUri.setText(uri);
        holder.tvCardAction.setText(action);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return cardDetails.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCardName;
        TextView tvCardUri;
        TextView tvCardAction;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvName);
            tvCardUri = itemView.findViewById(R.id.tvUri);
            tvCardAction = itemView.findViewById(R.id.tvAction);
            imageView = itemView.findViewById(R.id.ivLogo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    CardDetails getItem(int id) {
        return cardDetails.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
