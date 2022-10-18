package com.vrd.gsaf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.adapters.viewHolders.NetworkingTablesViewHolder;
import com.vrd.gsaf.callbacks.NetworkingTablesCallback;
import com.vrd.gsaf.model.NetworkingTable;

import java.util.ArrayList;
import java.util.List;

public class NetworkingTablesAdapter extends RecyclerView.Adapter<NetworkingTablesViewHolder> {

    private final Context context;
    private final NetworkingTablesCallback callback;
    private List<NetworkingTable> itemList;

    public NetworkingTablesAdapter(Context context, List<NetworkingTable> itemList, NetworkingTablesCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public NetworkingTablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.tables_list_item, parent, false);
        return new NetworkingTablesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NetworkingTablesViewHolder holder, int position) {
        holder.setData(context, itemList.get(position), holder, position, callback);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void filteredList(ArrayList<NetworkingTable> filteredList) {
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    public void addItem(NetworkingTable item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        if (itemList != null && !itemList.isEmpty()) {
            int size = itemList.size();
            itemList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public List<NetworkingTable> getAllUsers() {
        return itemList;
    }
}