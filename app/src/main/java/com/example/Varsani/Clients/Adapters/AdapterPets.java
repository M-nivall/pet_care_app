package com.example.Varsani.Clients.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.PetsModel;
import com.example.Varsani.R;

import java.util.List;

public class AdapterPets extends RecyclerView.Adapter<AdapterPets.ViewHolder> {

    private Context context;
    private List<PetsModel> list;

    public AdapterPets(Context context, List<PetsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PetsModel model = list.get(position);

        holder.txtPetName.setText(model.getPetName());
        holder.txtSpecies.setText("Species: " + model.getSpecies());
        holder.txtBreed.setText("Breed: " + model.getBreed());
        holder.txtGender.setText("Gender: " + model.getGender());
        holder.txtDOB.setText("DOB: " + model.getDob());
        holder.txtWeight.setText("Weight: " + model.getWeight() + " KG");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPetName, txtSpecies, txtBreed, txtGender, txtDOB, txtWeight;

        public ViewHolder(View itemView) {
            super(itemView);

            txtPetName = itemView.findViewById(R.id.txtPetName);
            txtSpecies = itemView.findViewById(R.id.txtSpecies);
            txtBreed = itemView.findViewById(R.id.txtBreed);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtDOB = itemView.findViewById(R.id.txtDOB);
            txtWeight = itemView.findViewById(R.id.txtWeight);
        }
    }
}
