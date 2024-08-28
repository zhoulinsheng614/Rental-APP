package com.example.forum;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {
    private List<House> houseList;
    public HouseAdapter(List<House> houseList) {
        this.houseList = houseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for individual house listings in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_cardview, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data for the house at the current position
        House house = houseList.get(position);

        // Random background image selection for house listings

        int[] imageResources = {
                R.drawable.houseappearence1,
                R.drawable.houseappearence2,
                R.drawable.houseappearence3,
                R.drawable.houseappearence4,
                R.drawable.houseappearence5,
                R.drawable.houseappearence6,
                R.drawable.houseappearence7,
                R.drawable.houseappearence8,
                R.drawable.houseappearence9,
                R.drawable.houseappearence10,

        };


        Random random = new Random();
        int aa=random.nextInt(imageResources.length);
        int bb=aa+1;
        int randomImageResource = imageResources[aa];
        holder.houimage.setAlpha(0.5f);
        holder.houimage.setImageResource(randomImageResource);


        // Set the image with a slight opacity for visual effect
        holder.textViewTitle.setText(house.getCity()+" "+house.getSuburb());
        holder.textViewDescription.setText(house.getStreet()+" "+house.getStreetNumber()+" $"+house.getPrice()+" "+house.getXbxb()+" Bedroom");

        // Set an OnClickListener for each house item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity to display detailed information about the selected house
                System.out.println(house.getStreet());
                    Intent intent = new Intent(v.getContext(), House_Detail_Page.class);
                    intent.putExtra("houseData", (Serializable) house);
                    intent.putExtra("imageid",bb);
                    v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        ImageView houimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextViews and ImageView for house details
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            houimage=itemView.findViewById(R.id.houimage);

        }
    }
}