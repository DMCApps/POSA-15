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
    	// No array to begin.
    	
    	List<WeatherData> weatherDataItems = new ArrayList<WeatherData>();

        while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
        	weatherDataItems.add(parseJsonWeatherData(reader));
        }      
    	
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
            default:
                reader.skipValue();
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
    			break;
    		case Weather.icon_JSON:
    			weather.setIcon(reader.nextString());
    			break;
    		case Weather.description_JSON:
    			weather.setDescription(reader.nextString());
    			break;
    		case Weather.main_JSON:
    			weather.setMain(reader.nextString());
    			break;
            default:
                reader.skipValue();
                break;
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
    	reader.beginObject();
    	Main main = new Main();
    	
    	while (reader.hasNext()) {
    		String name = reader.nextName();
    		switch(name) {
    		case Main.humidity_JSON:
    			main.setHumidity(reader.nextLong());
    			break;
    		case Main.pressure_JSON:
    			main.setPressure(reader.nextDouble());
    			break;
    		case Main.temp_JSON:
    			main.setTemp(reader.nextDouble());
    			break;
            default:
                reader.skipValue();
                break;
    		}
    	}
    	
    	reader.endObject();
    	return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
    	reader.beginObject();
    	Wind wind = new Wind();

    	while (reader.hasNext()) {
    		String name = reader.nextName();
	    	switch(name) {
		    	case Wind.deg_JSON:
		    		wind.setDeg(reader.nextDouble());
		    		break;
		    	case Wind.speed_JSON:
		    		wind.setSpeed(reader.nextDouble());
		    		break;
	            default:
	                reader.skipValue();
	                break;
	    	}
    	}
    	
    	reader.endObject();
    	return wind;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader)
        throws IOException {
        // TODO -- you fill in here.
    	reader.beginObject();
    	Sys system = new Sys();

    	while (reader.hasNext()) {
    		String name = reader.nextName();
	    	switch(name) {
	    	case Sys.country_JSON:
	    		system.setCountry(reader.nextString());
	    		break;
	    	case Sys.message_JSON:
	    		system.setMessage(reader.nextDouble());
	    		break;
	    	case Sys.sunrise_JSON:
	    		system.setSunrise(reader.nextLong());
	    		break;
	    	case Sys.sunset_JSON:
	    		system.setSunset(reader.nextLong());
	    		break;
            default:
                reader.skipValue();
                break;
	    	}
    	}
    	

    	reader.endObject();
    	return system;
    }
}
