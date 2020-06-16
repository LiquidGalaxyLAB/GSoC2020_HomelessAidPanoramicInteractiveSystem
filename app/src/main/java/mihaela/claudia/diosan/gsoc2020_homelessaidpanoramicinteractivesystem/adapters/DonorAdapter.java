package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Homeless;


public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorAdapterHolder> implements Filterable {
    private List<Homeless> homelessData;
    private List<Homeless> homelessList;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Homeless> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredList.addAll(homelessList);
            }else{
                for (Homeless homeless : homelessList){
                    if (homeless.getHomelessUsername().toLowerCase().contains(constraint.toString().trim()) ||
                            homeless.getHomelessAddress().toLowerCase().contains(constraint.toString().trim()) ||
                            homeless.getHomelessNeed().toLowerCase().contains(constraint.toString().trim())){
                            filteredList.add(homeless);

                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
               homelessData.clear();
               homelessData.addAll((Collection<? extends Homeless>) results.values);
               notifyDataSetChanged();
        }
    };


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

   public DonorAdapter(List<Homeless> homelessData){
        super();
       this.homelessData = homelessData;
       this.homelessList = new ArrayList<>(homelessData);
   }

    @NonNull
    @Override
    public DonorAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeless_card_custom_view, parent, false);
        DonorAdapterHolder holder = new DonorAdapterHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonorAdapterHolder donorHolder, int position) {

        Homeless homeless = homelessData.get(position);

      donorHolder.username.setText(homeless.getHomelessUsername());
      donorHolder.locationAddress.setText(homeless.getHomelessAddress());
      donorHolder.need.setText(homeless.getHomelessNeed());
        Glide
                .with(donorHolder.itemView.getContext())
                .load(homeless.getImage())
                .placeholder(R.drawable.no_profile_image)
                .into(donorHolder.profileImageView);

    }


    @Override
    public int getItemCount() {
        return homelessData.size();
    }


    static class DonorAdapterHolder extends RecyclerView.ViewHolder{
        ImageView profileImageView;
        TextView username;
        TextView locationAddress;
        TextView need;

        private DonorAdapterHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);


            profileImageView = itemView.findViewById(R.id.profile_image_donor_card);
            username = itemView.findViewById(R.id.homeless_username_tv_donor_card);
            need = itemView.findViewById(R.id.homeless_need_tv_donor_card);
            locationAddress = itemView.findViewById(R.id.homeless_locationAddress_tv_donor_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                listener.onItemClick(position);
                            }
                    }
                }
            });
        }
    }

}
