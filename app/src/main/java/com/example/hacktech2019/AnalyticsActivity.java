package com.example.hacktech2019;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class AnalyticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        Intent gInt = getIntent();

        ArrayList<String> times = gInt.getStringArrayListExtra(MainActivity.GRAPH_KEY);
        ArrayList<String> bacs = gInt.getStringArrayListExtra(MainActivity.BAC_KEY);

        GraphView graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {});
        for (int i = 0; i < times.size(); i++) {
            Double time = Double.parseDouble(times.get(i));
            Double bac = Double.parseDouble(bacs.get(i));
            series.appendData(new DataPoint(time, bac), true, 100, false);
        }
        graph.addSeries(series);
    }

    public void onClick(View view) {
        finish();
    }
}
