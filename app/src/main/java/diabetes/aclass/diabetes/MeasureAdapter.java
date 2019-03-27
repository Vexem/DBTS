package diabetes.aclass.diabetes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;

import java.util.List;

import diabetes.aclass.model.MeasurementEntity;

public class MeasureAdapter extends RecyclerView.Adapter<MeasureAdapter.MeasureViewHolder> {


    private Context mCtx;
    private List<MeasurementEntity> MeasureList;

    public MeasureAdapter(Context mCtx, List<MeasurementEntity> productList) {
        this.mCtx = mCtx;
        this.MeasureList = productList;
    }

    @Override
    public MeasureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_list, null);
        return new MeasureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeasureViewHolder holder, int position) {
        MeasurementEntity measure = MeasureList.get(position);

        //loading the image
        holder.textViewTitle.setText(String.valueOf(measure.getDate()));
        holder.textViewShortDesc.setText(String.valueOf(measure.getCreated_atTime()));
        holder.textViewRating.setText(String.valueOf(measure.getValue()));
        holder.textViewPrice.setText(String.valueOf(measure.getId()));
    }

    @Override
    public int getItemCount() {
        return MeasureList.size();
    }

    class MeasureViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public MeasureViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
           // imageView = itemView.findViewById(R.id.imageView);
        }
    }
}