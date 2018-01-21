package com.howaboutthis.satyaraj.videntify;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import clarifai2.dto.prediction.Concept;

public class DetailedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Concept> listItem;

    DetailedAdapter(List<Concept> list) {
        this.listItem = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detailed_card_view, parent, false);
            return new ViewItem(v);
        }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Concept listItemEvent = listItem.get(position);
        ViewItem viewItem = (ViewItem) holder;

        viewItem.concept.setText(listItemEvent.name());

        @SuppressLint("DefaultLocale") String formattedValue = String.format("%.1f", (listItemEvent.value() * 100));
        viewItem.value.setText(String.valueOf(formattedValue +"%"));


    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    private class ViewItem extends RecyclerView.ViewHolder{

        TextView concept;
        TextView value;

        ViewItem(View itemView){
            super(itemView);

            concept = itemView.findViewById(R.id.concept_text_view);
            value = itemView.findViewById(R.id.value_text_view);

        }

    }
}
