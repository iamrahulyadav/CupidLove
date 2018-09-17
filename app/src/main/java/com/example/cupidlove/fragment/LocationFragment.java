package com.example.cupidlove.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kaushal Parmar on 1/1/2018.
 */


public class LocationFragment extends Fragment implements OnMapReadyCallback, PlaceSelectionListener, ResponseListener {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private TextView tvSearchLocation;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = LocationFragment.class.getSimpleName();
    private Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private PlaceDetectionClient mPlaceDetectionClient;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private static final int M_MAX_ENTRIES = 5;
    private GeoDataClient mGeoDataClient;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private ViewGroup view;
    PlaceAutocompleteFragment autocompleteFragment;
    private MapView map;
    private TextView tvDone, tvSelectedPlace;
    private Double currentlat, currentLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_location, container, false);
        tvDone = view.findViewById(R.id.tvDone);
        tvSelectedPlace = view.findViewById(R.id.tvSelectedPlace);
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        ((BaseActivity) getActivity()).showSearch();
        ((BaseActivity) getActivity()).settvTitle(getActivity().getString(R.string.location));

        ((BaseActivity) getActivity()).ivSearch = (ImageView) getActivity().findViewById(R.id.ivSearch);
        ((BaseActivity) getActivity()).ivSearch.setVisibility(View.VISIBLE);

        ((BaseActivity) getActivity()).ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        SupportMapFragment mapFragment = (((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)));

        mapFragment.getMapAsync(this);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location loc = mMap.getMyLocation();
                Log.e("Current Selected Lat:", currentlat + "");
                Log.e("Current Selected Lng:", currentLng + "");
                Toast.makeText(getActivity(), "Location Save Successfully", Toast.LENGTH_LONG).show();
//                Prefs.putString("lat", currentlat + "");
//                Prefs.putString("lng", currentLng + "");
                SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();
                pre.putString(RequestParamUtils.LATITUDE, currentlat + "");
                pre.putString(RequestParamUtils.LONGITUDE, currentLng + "");
                pre.commit();

                userUpdateLatLong();

                Constant.currentLatitude = currentlat;
                Constant.currentLongitude = currentLng;
            }
        });

        return view;
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.layout_location_info,
                        (FrameLayout) view.findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My LocationList layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        if (((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "").equals("") || ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "").equals("")) {
            getDeviceLocation();
        } else {
            currentlat = Double.parseDouble(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, ""));
            currentLng = Double.parseDouble(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, ""));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentlat, currentLng), DEFAULT_ZOOM));
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {

                addresses = geocoder.getFromLocation(currentlat, currentLng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = "None"; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String knownName = "None";
                if (addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    knownName = addresses.get(0).getFeatureName();
                    tvSelectedPlace.setText(addresses.get(0).getLocality() + " " + addresses.get(0).getCountryCode());
                } else {
                    tvSelectedPlace.setText("none");
                }
                mMap.addMarker(new MarkerOptions()
                        .title(knownName)
                        .position(new LatLng(currentlat,
                                currentLng))
                        .snippet(address));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationManager service = (LocationManager)
                        getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = service.getBestProvider(criteria, false);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                Location location = service.getLastKnownLocation(provider);
                if (location == null) {
                    location = mMap.getMyLocation();
                }
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                currentlat = userLocation.latitude;
                currentLng = userLocation.longitude;

                List<Address> addresses = null;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(currentlat, currentLng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = "None"; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String knownName = "None";
                    if (addresses.size() > 0) {
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        knownName = addresses.get(0).getFeatureName();
                        tvSelectedPlace.setText(addresses.get(0).getLocality() + " " + addresses.get(0).getCountryCode());
                    } else {
                        tvSelectedPlace.setText("none");
                    }
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .title(knownName)
                            .position(new LatLng(currentlat,
                                    currentLng))
                            .snippet(address));


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);


            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                    if (addresses.size() > 0) {
                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String knownName = addresses.get(0).getFeatureName();

                                        mMap.addMarker(new MarkerOptions()
                                                .title(knownName)
                                                .position(new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()))
                                                .snippet(address));
                                    } else {
                                        mMap.addMarker(new MarkerOptions()
                                                .title("None")
                                                .position(new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()))
                                                .snippet("None"));
                                    }
                                    currentlat = mLastKnownLocation.getLatitude();
                                    currentLng = mLastKnownLocation.getLongitude();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.e(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == getActivity().RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getContext(), data);

                Log.e(TAG, "Place Selected: " + place.getName());
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(place.getLatLng().latitude,
                                place.getLatLng().longitude), DEFAULT_ZOOM));
                mMap.addMarker(new MarkerOptions()
                        .title(place.getName() + "")
                        .position(place.getLatLng())
                        .snippet(place.getAddress() + ""));
                currentlat = place.getLatLng().latitude;
                currentLng = place.getLatLng().longitude;

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(currentlat, currentLng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    tvSelectedPlace.setText(addresses.get(0).getLocality() + " " + addresses.get(0).getCountryCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    @Override
    public void onPlaceSelected(Place place) {

        Log.e(TAG, "Place Selected: " + place.getName());
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(place.getLatLng().latitude,
                        place.getLatLng().longitude), DEFAULT_ZOOM));
        mMap.addMarker(new MarkerOptions()
                .title(place.getName() + "")
                .position(place.getLatLng())
                .snippet(place.getAddress() + ""));
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(getActivity(), "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    //TODO: update latitude and longitude api call
    public void userUpdateLatLong() {

        try {
            RequestParams params = new RequestParams();

            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("location_lat", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0"));
            params.put("location_long", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0"));

            Debug.e("userUpdateLatLong", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            ((BaseActivity) getActivity()).showProgress("");
            asyncHttpClient.post(new URLS().USER_UPDATE_LAT_LONG, params, new ResponseHandler(getActivity(), this, "userUpdateLatLong"));
        } catch (Exception e) {
            Debug.e("userUpdateLatLong Exception", e.getMessage());
        }
    }

    @Override
    public void onResponse(String response, String methodName) {

        ((BaseActivity) getActivity()).dismissProgress();
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("userUpdateLatLong")) {
            Log.e(methodName + "Response is ", response);
            //update lat long
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getBoolean("error")) {
                    //error
                } else {
                    //lat long updated
                }
            } catch (JSONException e) {
                Log.e("error", e.getMessage());
            }
        }
    }
}
