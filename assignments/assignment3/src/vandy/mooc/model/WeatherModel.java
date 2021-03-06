package vandy.mooc.model;

import java.lang.ref.WeakReference;
import java.util.List;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericServiceConnection;
import vandy.mooc.model.aidl.WeatherCall;
import vandy.mooc.model.aidl.WeatherData;
import vandy.mooc.model.aidl.WeatherRequest;
import vandy.mooc.model.aidl.WeatherResults;
import vandy.mooc.model.services.WeatherServiceAsync;
import vandy.mooc.model.services.WeatherServiceSync;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * This class plays the "Model" role in the Model-View-Presenter (MVP)
 * pattern by defining an interface for providing data that will be
 * acted upon by the "Presenter" and "View" layers in the MVP pattern.
 * It implements the MVP.ProvidedModelOps so it can be created/managed
 * by the GenericPresenter framework.
 */
public class WeatherModel
       implements MVP.ProvidedModelOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final static String TAG = 
        WeatherModel.class.getSimpleName();

    /**
     * A WeakReference used to access methods in the Presenter layer.
     * The WeakReference enables garbage collection.
     */
    protected WeakReference<MVP.RequiredPresenterOps> mPresenter;

    /**
     * Location we're trying to get current weather for.
     */
    private String mLocation;

    // TODO -- define ServiceConnetions to connect to the
    // WeatherServiceSync and WeatherServiceAsync.
    private GenericServiceConnection<WeatherCall> mWeatherServiceConnectionSync;
    private GenericServiceConnection<WeatherRequest> mWeatherServiceConnectionAsync;

    /**
     * Hook method called when a new WeatherModel instance is created
     * to initialize the ServicConnections and bind to the WeatherService*.
     * 
     * @param presenter
     *            A reference to the Presenter layer.
     */
    @Override
    public void onCreate(MVP.RequiredPresenterOps presenter) {
        // Set the WeakReference.
        mPresenter = new WeakReference<>(presenter);

        // TODO -- you fill in here to initialize the WeatherService*.
        mWeatherServiceConnectionSync = new GenericServiceConnection<WeatherCall>(WeatherCall.class);
        mWeatherServiceConnectionAsync = new GenericServiceConnection<WeatherRequest>(WeatherRequest.class);

        // Bind to the services.
        bindService();
    }

    /**
     * Hook method called to shutdown the Presenter layer.
     */
    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        // Don't bother unbinding the service if we're simply changing
        // configurations.
        if (isChangingConfigurations)
            Log.d(TAG,
                  "Simply changing configurations, no need to destroy the Service");
        else
            unbindService();
    }

    /**
     * The implementation of the WeatherResults AIDL Interface, which
     * will be passed to the Weather Web service using the
     * WeatherRequest.getCurrentWeather() method.
     * 
     * This implementation of WeatherResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private final WeatherResults.Stub mWeatherResults =
        new WeatherResults.Stub() {
            /**
             * This method is invoked by the WeatherServiceAsync to
             * return the results back.
             */
            @Override
            public void sendResults(final WeatherData weatherResults)
                throws RemoteException {
                // Pass the results back to the Presenter's
                // displayResults() method.
                // TODO -- you fill in here.
            	mPresenter.get().displayResults(weatherResults, null);
            }

            /**
             * This method is invoked by the WeatherServiceAsync to
             * return error results back.
             */
            @Override
            public void sendError(final String reason)
                throws RemoteException {
                // Pass the results back to the Presenter's
                // displayResults() method.
                // TODO -- you fill in here.
            	mPresenter.get().displayResults(null, reason);
            }
	};

    /**
     * Initiate the service binding protocol.
     */
    private void bindService() {
        Log.d(TAG,
              "calling bindService()");

        // Launch the Weather Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the WeatherService* if they aren't already
        // bound.

        // TODO -- you fill in here.
        if (mWeatherServiceConnectionSync.getInterface() == null) {
        	Intent syncServinceIntent = WeatherServiceSync.makeIntent(mPresenter.get().getApplicationContext());
        	mPresenter.get().getApplicationContext().bindService(syncServinceIntent, mWeatherServiceConnectionSync, Context.BIND_AUTO_CREATE);
        }
        
        if (mWeatherServiceConnectionAsync.getInterface() == null) {
        	Intent asyncServinceIntent = WeatherServiceAsync.makeIntent(mPresenter.get().getApplicationContext());
        	mPresenter.get().getApplicationContext().bindService(asyncServinceIntent, mWeatherServiceConnectionAsync, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * Initiate the service unbinding protocol.
     */
    private void unbindService() {
        Log.d(TAG,
              "calling unbindService()");

        // TODO -- you fill in here to unbind from the WeatherService*.
        if (mWeatherServiceConnectionSync.getInterface() != null) {
        	mPresenter.get().getApplicationContext().unbindService(mWeatherServiceConnectionSync);
        }

        if (mWeatherServiceConnectionAsync.getInterface() != null) {
        	mPresenter.get().getApplicationContext().unbindService(mWeatherServiceConnectionAsync);
    	}
    }

    /**
     * Initiate the asynchronous weather lookup.
     */
    public boolean getWeatherAsync(String location) {
        // TODO -- you fill in here.
        final WeatherRequest weatherRequest = 
        		mWeatherServiceConnectionAsync.getInterface();

        if (weatherRequest != null) {
            try {
                // Invoke a one-way AIDL call that doesn't block the
                // caller.  Results are returned via the sendResults()
                // or sendError() methods of the AsyncResultsImpl
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
            	weatherRequest.getCurrentWeather(location, mWeatherResults);
            	
            	return true;
            } catch (RemoteException e) {
                Log.e(TAG,
                      "RemoteException:" 
                      + e.getMessage());
            }
        } else 
            Log.d(TAG,
                  "weatherRequest was null.");
        
        return false;
    }

    /**
     * Initiate the synchronous weather lookup.
     */
    public WeatherData getWeatherSync(String location) {
        // TODO -- you fill in here.
        try {
            final WeatherCall weatherCall = 
                mWeatherServiceConnectionSync.getInterface();

            if (weatherCall != null) {
                // Invoke a two-way AIDL call, which blocks the
                // caller.
            	
            	List<WeatherData> weatherData = weatherCall.getCurrentWeather(location);
            	
            	if (weatherData != null && weatherData.size() > 0) {
            		return weatherData.get(0);
            	}
            }
            else {
                Log.d(TAG, "weatherCall was null.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
