package shop.cottonkeyboards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String url = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    //could use a search box with this as a default value instead.
    private String match = "Aerodynamic Cotton Keyboard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);

        // Fetch the TextViews for the info
        //final TextView rev = (TextView)findViewById(R.id.revenue);
        //final TextView sold = (TextView) findViewById(R.id.sold);

        // URL where the JSON info lives
        //String url = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";

        ///Send of a request and a handler
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Get the TextViews
                        TextView rev = (TextView) findViewById(R.id.revenue);
                        TextView sold = (TextView) findViewById(R.id.sold);
                        TextView errors = (TextView) findViewById(R.id.errorBox);
                        //Try to parse out the revenue
                        try {
                            JSONObject salesInfo = new JSONObject(response);
                            JSONArray salesList = salesInfo.getJSONArray("orders");
                            //accumulators
                            float totalRevenue = 0;
                            int totalKeyboards = 0;
                            // name to match against
                            //String match = "Aerodynamic Cotton Keyboard";
                            String saleName;
                            JSONObject saleInfo;
                            for (int i = 0; i < salesList.length(); i++){
                                saleInfo = salesList.getJSONObject(i);
                                saleName = saleInfo.getString("name");
                                // match with name string (regex seemed like overkill, but this
                                // seems like a mess of its own)
                                if(saleName.length()>=match.length() &&
                                        saleName.substring(0,match.length()).equals(match)){
                                    totalRevenue += saleInfo.getDouble("");
                                    totalKeyboards++;
                                }
                            }
                            rev.setText("$" + totalRevenue);
                        }
                        // Big cheat
                        catch(Exception e) {
                            errors.setText("Something went wrong with the parsing.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView errors = (TextView)findViewById(R.id.errorBox);
                errors.setText("Request went wrong.");
            }
        });

    }
    void onClickUpdate(){

    }
}
