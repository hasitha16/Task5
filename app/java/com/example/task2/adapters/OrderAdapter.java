package com.example.task2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2.R;
import com.example.task2.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderIdText.setText("Order ID: " + order.getOrderId());
        holder.orderDateText.setText("Date: " + order.getOrderDate());
        holder.orderStatusText.setText("Status: " + order.getOrderStatus());
        holder.orderTotalText.setText(String.format("Total: ₹%.2f", order.getTotalAmount()));
        holder.paymentMethodText.setText("Payment: " + order.getPaymentMethod());

        // Show number of items
        int itemCount = order.getItems() != null ? order.getItems().size() : 0;
        holder.itemCountText.setText(itemCount + " item(s)");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, orderDateText, orderStatusText, orderTotalText, paymentMethodText, itemCountText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            orderTotalText = itemView.findViewById(R.id.orderTotalText);
            paymentMethodText = itemView.findViewById(R.id.paymentMethodText);
            itemCountText = itemView.findViewById(R.id.itemCountText);
        }
    }
}