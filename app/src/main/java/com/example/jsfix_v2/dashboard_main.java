package com.example.jsfix_v2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class dashboard_main extends AppCompatActivity {
    private CardView whatsapp, img1;
    ViewPager viewPager;
    int images[] = {R.drawable.pic1 , R.drawable.pic2, R.drawable.pic3};
    int currentPageCnt = 0;
    private String jsonURL = "https://maxisolutech.com/JSFIX_V2/api-routes.php";
    private final int jsoncode = 1;
    private RecyclerView recyclerView;
    ArrayList<ProductModel> productModelArrayList;
    private ProductAdapter productAdapter;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);
        recyclerView = findViewById(R.id.recycler);
        fetchJSON();

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new slider_adapter(images, dashboard_main.this));

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPageCnt == images.length){
                    currentPageCnt = 0;
                }
                viewPager.setCurrentItem(currentPageCnt++, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2500, 2500);

        whatsapp = (CardView) findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesan = "Testing whatsapp";

                Intent kirimWA = new Intent(Intent.ACTION_SEND);
                kirimWA.setType("text/plain");
                kirimWA.putExtra(Intent.EXTRA_TEXT, pesan);
                kirimWA.putExtra("jid", "6282112927843" + "@s.whatsapp.net");
                kirimWA.setPackage("com.whatsapp");

                startActivity(kirimWA);
            }
        });

    }
    @SuppressLint("StaticFieldLeak")
    private void fetchJSON(){

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(jsonURL);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("hasill",result);
                onTaskCompleted(result,jsoncode);
            }
        }.execute();
    }

    public void onTaskCompleted(String response, int serviceCode) {
        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:

                if (isSuccess(response)) {
                    removeSimpleProgressDialog();  //will remove progress dialog
                    productModelArrayList = getInfo(response);
                    productAdapter = new ProductAdapter(this,productModelArrayList);
                    recyclerView.setAdapter(productAdapter);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener(){
                        
                        public void onClick(View view, int position) {
                            Toast.makeText(dashboard_main.this, productModelArrayList.get(position).getImgURL(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(dashboard_main.this, web_view.class);
                            intent.putExtra("link_url", productModelArrayList.get(position).getUrl());
                            startActivity(intent);
                        }

                        
                        public void onLongClick(View view, int position) {

                        }
                    }));

                }else {
                    Toast.makeText(dashboard_main.this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<ProductModel> getInfo(String response) {
        ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {

                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    ProductModel playersModel = new ProductModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    playersModel.setUrl(dataobj.getString("link"));
                    playersModel.setImgURL(dataobj.getString("gambar"));
                    productModelArrayList.add(playersModel);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productModelArrayList;
    }

    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("status").equals("true")) {
                return true;
            } else {

                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}

