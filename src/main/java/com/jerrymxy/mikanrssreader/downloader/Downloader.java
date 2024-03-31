package com.jerrymxy.mikanrssreader.downloader;

import com.jerrymxy.mikanrssreader.utils.Globals;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Downloader {
    private List<String> urls;
    private String filePath;

    public Downloader(List<String> urls) {
        this.urls = urls;
    }

    public Downloader(List<String> urls, String filePath) {
        this.urls = urls;
        this.filePath = filePath;
    }

    public void download() {
        for (String url : urls) {
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(new HttpGet(url))) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    if (filePath == null || filePath.isBlank()) {
                        filePath = new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date());
                    }
                    File file = new File(filePath);
                    if (!file.exists() || !file.isDirectory()) {
                        file.mkdir();
                    }
                    String filename = response.getFirstHeader("Content-Disposition").getValue();
                    int startIndex = filename.indexOf(';');
                    int endIndex = filename.indexOf(';', startIndex + 1);
                    startIndex = filename.indexOf(Globals.FILENAME_PREFIX) + Globals.FILENAME_PREFIX.length();
                    filename = filename.substring(startIndex + 1, endIndex);
                    System.out.print("Downloading: " + filename + " ...");
                    try (FileOutputStream outputStream = new FileOutputStream("./" + filePath + "/" + filename)) {
                        entity.writeTo(outputStream);
                    }
                    System.out.println(" Done!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
