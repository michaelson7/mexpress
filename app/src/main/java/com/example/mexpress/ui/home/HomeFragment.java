package com.example.mexpress.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mexpress.DB_Handler;
import com.example.mexpress.R;
import com.example.mexpress.TripDetails;
import com.example.mexpress.adapter.adapter;
import com.example.mexpress.adapter.adapter1;
import com.example.mexpress.databinding.FragmentHomeBinding;
import com.example.mexpress.searchActivity;
import com.example.mexpress.tripModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView,recyclerView1;
    Button button4,button7;
    CardView cardView;
    ImageView imageView;
    adapter[] adapters = new adapter[1];
    com.example.mexpress.adapter.adapter1[] adapter1 = new adapter1[1];
    ArrayList<tripModel> list = new ArrayList<tripModel>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
        button7= root.findViewById(R.id.button7);
        cardView= root.findViewById(R.id.cardView);
        recyclerView= root.findViewById(R.id.recyclerView);
        recyclerView1= root.findViewById(R.id.recyclerView1);
        imageView= root.findViewById(R.id.imageView2);
        button4= root.findViewById(R.id.button4);

        //final TextView textView = binding.textHome;
        DB_Handler prcndbHandler = new DB_Handler(getContext());
        list = (ArrayList<tripModel>) prcndbHandler.getAllTrips();

        //get and display all trips
        adapters[0] = new adapter(getContext(), list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapters[0]);

        adapter1[0] = new adapter1(getContext(), list);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView1.setAdapter(adapter1[0]);

        Glide.with(getContext()).load("https://media.istockphoto.com/id/1354524757/photo/casual-african-american-woman-smiling-in-purple-studio-isolated-background.jpg?b=1&s=170667a&w=0&k=20&c=8MxQbHDUExcyfLm9RvxITgGWMyfqCftOv5is8p426lE=").into(imageView);;

        button4.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), TripDetails.class);
            myIntent.putExtra("hasData", false);
            startActivity(myIntent);
        });

        cardView.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), searchActivity.class);
            startActivity(myIntent);
        });

        button7.setOnClickListener(v -> {
            try {
                uploadListData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return root;
    }

    void uploadListData() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();
        JSONArray array=new JSONArray();

        for (int i = 0; i < list.size(); i++)
        {
            tripModel model = list.get(i);
            JSONObject obj=new JSONObject();
            obj.put("name", model.getTitle());
            obj.put("description", model.getDescription());
            array.put(obj);
        }

        try {
            postData.put("userId", "987654");
            postData.put("detailList",array);
            Log.e("POST DATA", postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://mocki.io/v1/c90e8bc9-9962-40bb-9e8c-027bc55aa10e", postData, response -> {
            try {
                String message = response.getString("message");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(getContext(),error.toString(), Toast.LENGTH_SHORT).show();
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 2, 1));
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}