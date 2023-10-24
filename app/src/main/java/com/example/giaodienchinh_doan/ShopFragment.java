package com.example.giaodienchinh_doan;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodienchinh_doan.NewProductsAdapter;
import com.example.giaodienchinh_doan.NewProductsModel;
import com.example.giaodienchinh_doan.R;
import com.example.giaodienchinh_doan.ShowAllActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    RecyclerView newProductRecycleview;
    TextView newProductsShowAll;
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;
    FirebaseFirestore db;

    public ShopFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        db = FirebaseFirestore.getInstance();

        newProductRecycleview = root.findViewById(R.id.new_product_rec);
        newProductsShowAll=root.findViewById(R.id.newProducts_see_all);

        newProductsShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        //New Products
        newProductRecycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecycleview.setAdapter(newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                NewProductsModel newProductsModel=document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });

        return root;
    }
}