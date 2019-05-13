package com.e.api;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import api.EmployeeAPI;
import model.Employee;
import model.EmployeeCUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateEmployeeActivity extends AppCompatActivity {
    private Button btnSearch, btnUpdate, btnDelete;
    private EditText etNo, etName, etAge, etSalary;
    private final static String BASE_URL = "http://dummy.restapiexample.com/api/v1/";

    Retrofit retrofit;
    EmployeeAPI employeeAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        etNo = findViewById(R.id.etID);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etSalary = findViewById(R.id.etSalary);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee();
            }
        });
    }

    private void createInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employeeAPI = retrofit.create(EmployeeAPI.class);
    }

    private void deleteEmployee() {
        createInstance();


        new AlertDialog.Builder(this).setTitle("Title").setMessage("Do you really want Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int whichButton){
                        Call<Void> voidCall = employeeAPI.deleteEmployee(Integer.parseInt(etNo.getText().toString()));
                        voidCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(UpdateEmployeeActivity.this, "Successful Deleted;", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(UpdateEmployeeActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton(android.R.string.no, null).show();

    }

    private void updateEmployee() {
        createInstance();
        EmployeeCUD employee = new EmployeeCUD(
                etName.getText().toString(),
                Float.parseFloat(etSalary.getText().toString()),
                Integer.parseInt(etAge.getText().toString())
        );

        Call<Void> voidCall = employeeAPI.updateEmployee(Integer.parseInt(etNo.getText().toString()), employee);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateEmployeeActivity.this, "Update Successful;", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateEmployeeActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadData() {
        createInstance();
        Call<Employee> listCall = employeeAPI.getEmployeeByID(Integer.parseInt(etNo.getText().toString()));
        listCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                etName.setText(response.body().getEmployee_name());
                etAge.setText(Integer.toString(response.body().getEmployee_age()));
                etSalary.setText(Float.toString(response.body().getEmployee_salary()));
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(UpdateEmployeeActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
