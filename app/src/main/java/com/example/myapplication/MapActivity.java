package com.example.farmapsbeta;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.Polyline.OnClickListener;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private GeoPoint userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Configuración de OSMDroid
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Inicializar MapView
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        // Configuración inicial de ubicación
        userLocation = new GeoPoint(-33.4489, -70.6693); // Coordenadas de ejemplo (Santiago, Chile)

        // Comprobar y solicitar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            // Obtener la ubicación del usuario si se tienen permisos
            getUserLocation();
        }

        // Añadir marcadores de farmacias
        addMarker(new GeoPoint(-33.44834097985506, -70.63262276271865), "Farmacia 1", R.drawable.salud1);
        addMarker(new GeoPoint(-33.42726530010337, -70.62668648230218), "Farmacia 2", R.drawable.salud1);
        addMarker(new GeoPoint(-33.43023354843337, -70.63257500441654), "Farmacia 3", R.drawable.salud1);

        // Dibujar línea al hacer clic en un marcador
        mapView.getOverlays().add((OnClickListener) (overlay, mapView, eventPos) -> {
            drawLine(userLocation, eventPos);
            return true;
        });
    }

    private void getUserLocation() {
        // Obtener la ubicación actual del usuario
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                mapView.getController().setCenter(userLocation);
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addMarker(GeoPoint point, String title, int resourceId) {
        // Configurar y agregar el marcador en el mapa
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle(title);
        marker.setIcon(getResources().getDrawable(resourceId));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
    }

    private void drawLine(GeoPoint startPoint, GeoPoint endPoint) {
        // Dibujar una línea entre dos puntos en el mapa
        Polyline line = new Polyline();
        List<GeoPoint> points = new ArrayList<>();
        points.add(startPoint);
        points.add(endPoint);
        line.setPoints(points);
        line.setColor(0xFFFF0000); // Rojo
        line.setWidth(5f);
        mapView.getOverlays().add(line);
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
