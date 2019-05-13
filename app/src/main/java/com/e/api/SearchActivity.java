package com.e.api;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import api.EmployeeAPI;
import model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private TextView tvData;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvData = findViewById(R.id.tvData);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Validate()){
                    loadData();
//                }
            }
        });

    }

    public void loadData(){
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://dummy.restapiexample.com/api/v1/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

        EmployeeAPI employeeAPI = retrofit.create(EmployeeAPI.class);
        Call<Employee> listCall = employeeAPI.getEmployeeByID(Integer.parseInt(etSearch.getText().toString()));

        listCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Toast.makeText(SearchActivity.this,response.body().toString(), Toast.LENGTH_SHORT).show();
                String content = "";
                content += "Id : "+response.body().getId()+"\n";
                content += "Name : "+response.body().getEmployee_name()+"\n";
                content +="Age : "+ response.body().getEmployee_age()+"\n";
                content +="Salary : "+response.body().getEmployee_salary()+"\n";

                tvData.setText(content);
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error ",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
