package shop.cottonkeyboards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String url = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    //could use a search box with this as a default value instead.
    private String match = "Aerodynamic Cotton Keyboard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClickUpdate(View v){
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView rev = (TextView)findViewById(R.id.revenue);
        final TextView sold = (TextView) findViewById(R.id.sold);

        // URL where the JSON info lives
        //String url = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";

        ///Send of a request and a handler
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Get the TextViews
                        //TextView rev = (TextView) findViewById(R.id.revenue);
                        //TextView sold = (TextView) findViewById(R.id.sold);
                        TextView errors = (TextView) findViewById(R.id.errorBox);
                        //Try to parse out the revenue
                        try {
                            JSONObject salesInfo = new JSONObject(response);
                            JSONArray salesList = salesInfo.getJSONArray("orders");
                            //accumulators
                            float totalRevenue = 0;
                            int totalKeyboards = 0;

                            String saleName; // name of item
                            JSONObject saleInfo; //info for full sale
                            JSONArray items; // all items in sale
                            JSONObject item; //individual item in sale

                            for (int i = 0; i < salesList.length(); i++){

                                saleInfo = salesList.getJSONObject(i);
                                items = saleInfo.getJSONArray("line_items");
                                // go through ordered items
                                for (int j = 0; j < items.length(); j++) {
                                    // No cancelled orders, so a some cheating.
                                    item = items.getJSONObject(j);
                                    saleName = item.getString("title");
                                    Log.i("name", saleName);
                                    if (saleName.equals(match)) {
                                        totalRevenue += item.getInt("quantity") * item.getDouble("price");
                                        totalKeyboards += item.getInt("quantity");
                                    }
                                }
                            }
                            rev.setText("$" + totalRevenue);
                            sold.setText("" + totalKeyboards);
                            //errors.setText("");
                        }
                        // Big cheat
                        catch(JSONException e) {
                            errors.setText("Something went wrong with the parsing.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView errors = (TextView)findViewById(R.id.errorBox);
                errors.setText("There was a problem with the request.");
            }
        });
        queue.add(request);
    }
}
