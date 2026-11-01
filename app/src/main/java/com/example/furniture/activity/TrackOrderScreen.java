package com.example.furniture.activity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrackOrderScreen extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText etLocationA;
    private EditText etLocationB;
    private Button btnFindRoute;
    private TextView tvRouteInfo;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_track_order_screen;
    }

    @Override
    protected void init() {
        etLocationA = findViewById(R.id.et_location_a);
        etLocationB = findViewById(R.id.et_location_b);
        btnFindRoute = findViewById(R.id.btn_find_route);
        tvRouteInfo = findViewById(R.id.tv_route_info);

        // Pre-fill default demo values for user convenience
        if (etLocationA != null && etLocationA.getText().toString().isEmpty()) {
            etLocationA.setText("10.848054, 106.786647");
        }
        if (etLocationB != null && etLocationB.getText().toString().isEmpty()) {
            etLocationB.setText("10.776530, 106.700980");
        }

        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (btnFindRoute != null) {
            btnFindRoute.setOnClickListener(v -> findRoute());
        }
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Chỉ đường đơn hàng");
        }
    }

    @Override
    protected void data() {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Initial default location: Ho Chi Minh City
        LatLng defaultLocation = new LatLng(10.8231, 106.6297);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));

        // Auto find route if default locations pre-filled
        findRoute();
    }

    private void findRoute() {
        if (mMap == null) return;

        String locAStr = etLocationA.getText().toString().trim();
        String locBStr = etLocationB.getText().toString().trim();

        if (locAStr.isEmpty() || locBStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ địa điểm A và địa điểm B!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnFindRoute.setEnabled(false);
        btnFindRoute.setText("Đang tìm đường...");

        executorService.execute(() -> {
            LatLng latLngA = parseOrGeocodeLocation(locAStr);
            LatLng latLngB = parseOrGeocodeLocation(locBStr);

            if (latLngA == null || latLngB == null) {
                mainHandler.post(() -> {
                    btnFindRoute.setEnabled(true);
                    btnFindRoute.setText("Chỉ đường");
                    Toast.makeText(TrackOrderScreen.this, "Không tìm thấy tọa độ địa điểm đã nhập. Vui lòng thử lại!", Toast.LENGTH_LONG).show();
                });
                return;
            }

            // Fetch real road routing (Google Routes API v2 primary, OSRM secondary)
            RouteResult routeResult = fetchRoute(latLngA, latLngB);

            mainHandler.post(() -> {
                btnFindRoute.setEnabled(true);
                btnFindRoute.setText("Chỉ đường");

                mMap.clear();

                // Add Marker A
                mMap.addMarker(new MarkerOptions()
                        .position(latLngA)
                        .title("Điểm A: " + locAStr)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                // Add Marker B
                mMap.addMarker(new MarkerOptions()
                        .position(latLngB)
                        .title("Điểm B: " + locBStr)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                List<LatLng> polylinePoints = routeResult != null ? routeResult.points : new ArrayList<>();
                if (polylinePoints.isEmpty()) {
                    polylinePoints.add(latLngA);
                    polylinePoints.add(latLngB);
                }

                // Draw route line on map
                PolylineOptions lineOptions = new PolylineOptions()
                        .addAll(polylinePoints)
                        .width(14f)
                        .color(Color.parseColor("#1A73E8"))
                        .geodesic(true);

                mMap.addPolyline(lineOptions);

                // Show route info
                if (tvRouteInfo != null) {
                    tvRouteInfo.setVisibility(View.VISIBLE);
                    if (routeResult != null && routeResult.distanceKm > 0) {
                        tvRouteInfo.setText(String.format(Locale.getDefault(), "Khoảng cách đường đi: %.2f km | Dự kiến: %d phút",
                                routeResult.distanceKm, routeResult.durationMin));
                    } else {
                        float[] results = new float[1];
                        Location.distanceBetween(latLngA.latitude, latLngA.longitude, latLngB.latitude, latLngB.longitude, results);
                        float distanceKm = results[0] / 1000f;
                        tvRouteInfo.setText(String.format(Locale.getDefault(), "Khoảng cách thẳng: %.2f km", distanceKm));
                    }
                }

                // Zoom map camera to fit both points A and B with padding
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng point : polylinePoints) {
                    builder.include(point);
                }
                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            });
        });
    }

    private LatLng parseOrGeocodeLocation(String locStr) {
        if (locStr.contains(",")) {
            try {
                String[] parts = locStr.split(",");
                if (parts.length == 2) {
                    double lat = Double.parseDouble(parts[0].trim());
                    double lng = Double.parseDouble(parts[1].trim());
                    return new LatLng(lat, lng);
                }
            } catch (Exception ignored) {
            }
        }

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(locStr, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private RouteResult fetchRoute(LatLng start, LatLng end) {
        return fetchOsrmRoute(start, end);
    }

    private RouteResult fetchOsrmRoute(LatLng start, LatLng end) {
        try {
            String urlStr = String.format(Locale.US,
                    "https://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=polyline",
                    start.longitude, start.latitude, end.longitude, end.latitude);

            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "FurnitureApp/1.0 (Android)");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();

                JSONObject root = new JSONObject(builder.toString());
                JSONArray routes = root.optJSONArray("routes");
                if (routes != null && routes.length() > 0) {
                    JSONObject route = routes.getJSONObject(0);
                    double distanceMeters = route.optDouble("distance", 0);
                    double durationSeconds = route.optDouble("duration", 0);
                    String geometryStr = route.optString("geometry", "");

                    List<LatLng> decodedPoints = decodePolyline(geometryStr);
                    return new RouteResult(decodedPoints, distanceMeters / 1000.0, (int) Math.ceil(durationSeconds / 60.0));
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        if (encoded == null || encoded.isEmpty()) return poly;
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private static class RouteResult {
        final List<LatLng> points;
        final double distanceKm;
        final int durationMin;

        RouteResult(List<LatLng> points, double distanceKm, int durationMin) {
            this.points = points;
            this.distanceKm = distanceKm;
            this.durationMin = durationMin;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
