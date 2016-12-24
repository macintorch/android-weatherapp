package ainor.com.my.myweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    Button checkButton;
    EditText cityNameEditText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;

                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Could not find weather", Toast.LENGTH_LONG);

            }


            return "Failed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String message = "";

                JSONObject jsonObject = null;

                jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

               JSONArray array = new JSONArray(weatherInfo);

                Log.i("Result content", result);
               Log.i("Weather content", weatherInfo);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);

                    String main ="";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    Log.i("main", jsonPart.getString("main"));

                    Log.i("description", jsonPart.getString("description"));

                    if (main != "" && description != "") {
                        message +=  main +" : " + description + "\r\n";
                    }
                }

                if (message != "") {
                    resultTextView.setText(message);
                } else {
                    Toast.makeText(getApplicationContext(),"Could not find weather", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Could not find weather", Toast.LENGTH_LONG);
            }

        }
    }

    public void checkWeather(View view) {

        Log.i("City Name", cityNameEditText.getText().toString());

        // to hide the keyboard

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mgr.hideSoftInputFromWindow(cityNameEditText.getWindowToken(),0);

        DownloadTask task = new DownloadTask();

        String result = null;

        try {
            result = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ cityNameEditText.getText().toString()+",uk&appid=8fc9866e66c062de7b3f59d611f2b023").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkButton = (Button) findViewById(R.id.checkButton);
        cityNameEditText = (EditText) findViewById(R.id.cityNameEditText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

    }

}
