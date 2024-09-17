package sh.now.afk.processing;

import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import processing.core.PApplet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Plot extends PApplet {
    public static int C = 1;
    private List<Map<String, String>> data;
    float minLat;
    float maxLat;
    float minLng;
    float maxLng;
    int padding = 50;
    private Map<Integer, List<Map<String, String>>> zoneData;

    @Override
    public void settings() {
        noLoop();
        size(1000, 1000);
        try {
            data = CsvUtil.getReader(CsvReadConfig.defaultConfig().setContainsHeader(true))
                    .readFromStr(Files.readString(Paths.get("data-with-population-geo.csv")))
                    .getRows()
                    .stream()
                    .filter(x -> !x.getByName("area").contains("（含"))
                    .map(CsvRow::getFieldMap)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        minLat = data.stream().map(x -> x.get("lat")).min(Comparator.comparingDouble(Float::parseFloat)).map(Float::parseFloat).get();
        maxLat = data.stream().map(x -> x.get("lat")).max(Comparator.comparingDouble(Float::parseFloat)).map(Float::parseFloat).get();
        minLng = data.stream().map(x -> x.get("lng")).min(Comparator.comparingDouble(Float::parseFloat)).map(Float::parseFloat).get();
        maxLng = data.stream().map(x -> x.get("lng")).max(Comparator.comparingDouble(Float::parseFloat)).map(Float::parseFloat).get();

        zoneData = data.stream().collect(Collectors.groupingBy(x -> ((int) (Float.parseFloat(x.get("lng")) + 7.5)) / 15));
    }

    @Override
    public void setup() {
        background(0);
        noStroke();
        fill(255);
        drawPoints();
        draw120();
        drawZone();
    }

    private void drawZone() {
        float rightOf8 = 127.5f;
        for (int i = 0; i < 5; i++) {
            float lngRight = rightOf8 - 15 * i;
            float x = map(lngRight, minLng, maxLng, padding, height - padding);
            stroke(128);
            line(x, -padding, x, height);
            stroke(255);
            x = map(lngRight + 5, minLng, maxLng, padding, height - padding);
            int timezoneId = 9 - i;
            textSize(50);
            int baseLine = height / 5 * 4;
            text(timezoneId + "", x, baseLine);
            textSize(30);
            text(zoneData.get(timezoneId).size() + "", x, baseLine + 50);
            textSize(12);
            stroke(255, 0, 0);
            text(zoneData.get(timezoneId).stream().map(c -> c.get("population"))
                         .map(c -> c.equals("") ? "0" : c) //no data for jinmen
                         .mapToInt(Integer::parseInt).sum() + "", x, baseLine + 70);
        }
    }

    private void draw120() {
        fill(255);
        float xc = map(120f, minLng, maxLng, padding, height - padding);
        stroke(255, 0, 0);
        line(xc, -padding, xc, height);
    }

    private void drawPoints() {
        for (var area : data) {
            float y = map(Float.parseFloat(area.get("lat")), minLat, maxLat, height - padding, padding);
            float x = map(Float.parseFloat(area.get("lng")), minLng, maxLng, padding, width - padding);

            C = 4;
            colorMode(RGB);
            if (area.get("city").equals("北京市")) {
                fill(255, 0, 0);
            } else if (area.get("city").equals("上海市")) {
                fill(0, 255, 0);
            } else if (area.get("city").equals("西安市")) {
                fill(0, 0, 255);
            } else if (area.get("city").equals("洛阳市")) {
                fill(255, 255, 0);
            } else if (area.get("city").equals("广州市")) {
                fill(0, 255, 255);
            } else {
                fill(255);
                C = 2;
            }
            ellipse(x, y, C, C);
//            point(x, y);
        }
    }

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{""}, new Plot());
    }
}
