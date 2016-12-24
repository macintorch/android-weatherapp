package ainor.com.my.myweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Failed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = null;

                jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

               JSONArray array = new JSONArray(weatherInfo);

                Log.i("Result content", result);
               Log.i("Weather content", weatherInfo);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);

                    Log.i("main", jsonPart.getString("main"));

                    Log.i("description", jsonPart.getString("description"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void checkWeather(View view) {

        Log.i("City Name", cityNameEditText.getText().toString());

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

    }

}
