import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.SimpleTimeZone;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);

        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try{
            HttpURLConnection conn = fetchApiResponse(urlString);

            if(conn.getResponseCode() != 200){
                System.out.println("Unable to connect to API Server");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexofCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weather_code = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertweather_code((long) weather_code.get(index));

            JSONArray relative_Humidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relative_Humidity.get(index);

            JSONArray wind_speed_Data = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) wind_speed_Data.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public static JSONArray getLocationData(String locationName){
        locationName = locationName.replace(" ","+");

        //API from here
        String url = "https://geocoding-api.open-meteo.com/v1/search?name="+locationName+"&count=10&language=en&f";

        try{
            HttpURLConnection conn = fetchApiResponse(url);

            if (conn.getResponseCode() != 200){
                System.out.println("Could not connect to API Server");
                return null;
            }
            else{
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf((resultJson)));

                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

                return locationData;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static  HttpURLConnection fetchApiResponse(String url){
        try {

            URL realurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realurl.openConnection();


            conn.setRequestMethod("GET");
            conn.connect();

            return conn;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    private static int findIndexofCurrentTime(JSONArray timeList)
    {
        String currentTime = getCurrentTime();

        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);

            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }

        return 0;
    }
    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'HH':00'");

        String formatedDateTime = currentDateTime.format(formatter);
        return formatedDateTime;
    }
    private static String convertweather_code(long weather_code)
    {
        String weatherCondition  = "";
        if (weather_code == 0){
            weatherCondition = "Clear";
        } else if (weather_code <= 3L && weather_code > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weather_code >= 5L && weather_code <= 67L) || (weather_code >= 80L && weather_code <= 99L)) {
            weatherCondition = "Rain";
        } else if (weather_code >= 71L && weather_code<= 77L){
            weatherCondition = "Snow";
        }
        return  weatherCondition;
    }
}
