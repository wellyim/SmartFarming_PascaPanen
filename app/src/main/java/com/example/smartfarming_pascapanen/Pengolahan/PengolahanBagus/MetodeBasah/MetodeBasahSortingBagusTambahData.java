package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.PengolahanBagus;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.Sorting.DashboardSorting;
import com.example.smartfarming_pascapanen.RawData.Sorting.InputDataSorting;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MetodeBasahSortingBagusTambahData extends AppCompatActivity {
    TextView txtIDSorting,txtNamaPengguna, txtBeratSorting;
    EditText IdFermentasi, tglAwalProses, tglAkhirProses;
    Button btnTambahData, btnHapusData;
    Dialog dialogPopUp, infoPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengolahan_metode_basah_sorting_bagus_tambah_data);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");
        String id_sorting = i.getStringExtra("id_sorting");
        String berat_sorting = i.getStringExtra("berat_sorting");
        String id_panen = i.getStringExtra("id_panen");

        IdFermentasi = findViewById(R.id.id_fermentasi);
        txtIDSorting = findViewById(R.id.id_sorting);
        txtNamaPengguna = findViewById(R.id.nama_pengguna);
        txtBeratSorting = findViewById(R.id.info_berat_sorting);
        tglAwalProses = findViewById(R.id.tanggal_mulai_fermentasi);
        tglAkhirProses = findViewById(R.id.tanggal_akhir_fermentasi);
        btnTambahData = findViewById(R.id.tambah_proses);
        btnHapusData = findViewById(R.id.hapus_data);

        dialogPopUp = new Dialog(this);
        infoPopUp = new Dialog(this);

        String GenIdFermentasi = "F" + new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());;
        String dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        IdFermentasi.setText(GenIdFermentasi);
        txtIDSorting.setText(id_sorting);
        txtNamaPengguna.setText(nama_pengguna);
        txtBeratSorting.setText(berat_sorting);
        tglAwalProses.setText(dateNow);
        tglAkhirProses.setText(dateNow);

        btnTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_fermentasi = IdFermentasi.getText().toString();
                String id_sorting = txtIDSorting.getText().toString();
                String id_panen_sorting = id_panen;
                String id_pengguna_sorting = id_pengguna;
                String nama_pengguna_sorting = txtNamaPengguna.getText().toString();
                String berat_sorting = txtBeratSorting.getText().toString();
                String tanggal_mulai_fermentasi = tglAwalProses.getText().toString();
                String tanggal_akhir_fermentasi = tglAkhirProses.getText().toString();
                //setError if empty
                if (id_fermentasi.isEmpty()) {
                    IdFermentasi.setError("ID Fermentasi tidak boleh kosong!");
                } else if (tanggal_mulai_fermentasi.isEmpty()){
                    tglAwalProses.setError("Tanggal Awal Proses tidak boleh kosong!");
                } else if (tanggal_akhir_fermentasi.isEmpty()) {
                    tglAkhirProses.setError("Tanggal Akhir Proses tidak boleh kosong!");
                } else {
                    InputDataFermentasi(id_fermentasi, id_sorting, id_panen_sorting, id_pengguna_sorting, berat_sorting, tanggal_mulai_fermentasi, tanggal_akhir_fermentasi, nama_pengguna_sorting);
                }
            }
        });
    }

    private void InputDataFermentasi(String id_fermentasi, String id_sorting, String id_panen_sorting, String id_pengguna_sorting, String berat_sorting, String tanggal_mulai_fermentasi, String tanggal_akhir_fermentasi, String nama_pengguna_sorting) {
        Button simpan, batal;
        dialogPopUp.setContentView(R.layout.component_dialog);
        batal = dialogPopUp.findViewById(R.id.batal);
        simpan = dialogPopUp.findViewById(R.id.simpan_data);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPopUp.dismiss();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.localhost) + "=inputdatafermentasibaru";
                RequestQueue queue = Volley.newRequestQueue(MetodeBasahSortingBagusTambahData.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoPopup(view, id_pengguna_sorting, nama_pengguna_sorting,status, pesan);
                                dialogPopUp.dismiss();
                            } else if (status.equals("0")) {
                                ShowInfoPopup(view, id_pengguna_sorting, nama_pengguna_sorting, status, pesan);
                                dialogPopUp.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MetodeBasahSortingBagusTambahData.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_fermentasi", id_fermentasi);
                        params.put("id_sorting_bagus", id_sorting);
                        params.put("id_panen", id_panen_sorting);
                        params.put("id_pengguna", id_pengguna_sorting);
                        params.put("berat_awal_proses", berat_sorting);
                        params.put("tanggal_awal_proses", tanggal_mulai_fermentasi);
                        params.put("tanggal_akhir_proses", tanggal_akhir_fermentasi);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
        dialogPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogPopUp.show();
    }

    private void ShowInfoPopup(View view,String id_pengguna_sorting, String nama_pengguna_sorting, String status, String pesan) {
        Button ok;
        TextView textInfo;
        infoPopUp.setContentView(R.layout.component_info);
        ok = infoPopUp.findViewById(R.id.ok);
        textInfo = infoPopUp.findViewById(R.id.showinfo);

        textInfo.setText(pesan);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopUp.dismiss();
                //settimeout
                if (status.equals("1")){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(MetodeBasahSortingBagusTambahData.this, PengolahanBagus.class);
                                    intent.putExtra("id_pengguna", id_pengguna_sorting);
                                    intent.putExtra("nama_pengguna", nama_pengguna_sorting);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                }
            }
        });
        infoPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        infoPopUp.show();
    }
}