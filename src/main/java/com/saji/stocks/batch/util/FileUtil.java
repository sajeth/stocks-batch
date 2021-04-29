package com.saji.stocks.batch.util;

import com.saji.stocks.mongo.pojos.StockData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static void main(String args[]) {
        try {
            FileUtil.parseFile("Equity.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<StockData> parseFile(String fileName) throws IOException {
        String line;
        Object[] vals;
        List<StockData> output = new ArrayList<>();
        InputStream inputStream = FileUtil.class.getResourceAsStream("/" + fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (int i = 0; (line = reader.readLine()) != null; i++) {
            if (i == 0) {
                i++;
                continue;
            }
            vals = Arrays.stream(line.split(",")).toArray();
            StockData data = new StockData(vals[2] + ".BO");
            data.setIndustry(vals[3].toString());
            if (null != vals[6]) {
                data.setFaceValue(Float.valueOf(vals[6].toString()));
            }
            data.setSector(vals[8].toString());
            output.add(data);
        }

        return output;
    }
}
