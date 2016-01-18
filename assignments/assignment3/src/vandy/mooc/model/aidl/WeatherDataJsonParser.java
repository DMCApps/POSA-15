package vandy.mooc.model.aidl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import vandy.mooc.model.aidl.WeatherData.Main;
import vandy.mooc.model.aidl.WeatherData.Sys;
import vandy.mooc.model.aidl.WeatherData.Weather;
import vandy.mooc.model.aidl.WeatherData.Wind;
import android.util.JsonReader;
import android.util.JsonToken;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of WeatherData objects that contain this data.
 */
public class WeatherDataJsonParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
        this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<WeatherData> parseJsonStream(InputStream inputStream)
        throws IOException {

        // TODO -- you fill in here.
    	try (JsonReader reader =
                new JsonReader(new InputStreamReader(inputStream,
                                                     "UTF-8"))) {
       
    		return parseJsonWeatherDataArray(reader);
    	}
    }

    /**
     * Parse a Json stream and convert it into a List of WeatherData
     * objects.
     */
    public List<WeatherData> parseJsonWeatherDataArray(JsonReader reader)
        throws IOException {

        // TODO -- you fill in here.
    	reader.beginArray();
    	
    	List<WeatherData> weatherDataItems = new ArrayList<WeatherData>();

        while (reader.hasNext()) 
        	weatherDataItems.add(parseJsonWeatherData(reader));
        
        reader.endArray();
    	
    	return weatherDataItems;
    }

    /**
     * Parse a Json stream and return a WeatherData object.
     */
    public WeatherData parseJsonWeatherData(JsonReader reader) 
        throws IOException {
    	
        reader.beginObject();

        // TODO -- you fill in here.
    	WeatherData weatherData = new WeatherData();
    	
    	while (reader.hasNext()) {
    		String name = reader.nextName();
    		switch(name) {
    		case WeatherData.cod_JSON:
    			weatherData.setCod(reader.nextLong());
    			break;
    		case WeatherData.dt_JSON:
    			weatherData.setDate(reader.nextLong());
    			break;
    		case WeatherData.message_JSON:
    			weatherData.setMessage(reader.nextString());
    			break;
    		case WeatherData.name_JSON:
    			weatherData.setName(reader.nextString());
    			break;
    		case WeatherData.main_JSON:
    			weatherData.setMain(parseMain(reader));
    			break;
    		case WeatherData.sys_JSON:
    			weatherData.setSys(parseSys(reader));
    			break;
    		case WeatherData.weather_JSON:
    			weatherData.setWeathers(parseWeathers(reader));
    			break;
    		case WeatherData.wind_JSON:
    			weatherData.setWind(parseWind(reader));
    			break;
    		}
    	}
    	
        reader.endObject();

    	return weatherData;
    }
    
    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
    	reader.beginArray();
    	
    	List<Weather> weatherItems = new ArrayList<Weather>();

        while (reader.hasNext())  {
        	weatherItems.add(parseWeather(reader));
        }
        
        reader.endArray();
    	
    	return weatherItems;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
    	reader.beginObject();
    	Weather weather = new Weather();
    	
    	while (reader.hasNext()) {
    		String name = reader.nextName();
    		switch(name) {
    		case Weather.id_JSON:
    			weather.setId(reader.nextLong());
    		}
    	}
    	
    	reader.endObject();
    	return weather;
    }

    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader) 
        throws IOException {
        // TODO -- you fill in here.
    	return new Main();
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
    	return new Wind();
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader)
        throws IOException {
        // TODO -- you fill in here.
    	return new Sys();
    }
}
