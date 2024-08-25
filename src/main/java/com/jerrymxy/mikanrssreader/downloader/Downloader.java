package com.jerrymxy.mikanrssreader.downloader;

import com.jerrymxy.mikanrssreader.utils.Globals;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Downloader {
    private final List<String> urls;
    private String filePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);

    public Downloader(List<String> urls) {
        this.urls = urls;
    }

    public Downloader(List<String> urls, String filePath) {
        this.urls = urls;
        this.filePath = filePath;
    }

    public void download() {
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        for (String url : urls) {
            try {
                Response response = Request.get(url).execute();
                String filename = url.substring(url.lastIndexOf("/") + 1);
                System.out.print("Downloading: " + filename + " ...");
                response.saveContent(new File("./" + filePath + "/" + filename));
                System.out.println(" Done!");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Deprecated
    public void download2() {
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
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
