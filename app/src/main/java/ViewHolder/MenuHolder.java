package ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import RVRC.GEQ1917.G34.android.diningmania.ItemClickListener;
import RVRC.GEQ1917.G34.android.diningmania.R;

public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tv_choice;
    public TextView tv_content;
    public ImageView iv_foodImage;

    private ItemClickListener itemClickListener;

    public MenuHolder(@NonNull View itemView) {
        super(itemView);
        tv_choice = itemView.findViewById(R.id.tv_food_choice);
        tv_content = itemView.findViewById(R.id.tv_food_content);
        iv_foodImage = itemView.findViewById(R.id.iv_food_choice_background);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
