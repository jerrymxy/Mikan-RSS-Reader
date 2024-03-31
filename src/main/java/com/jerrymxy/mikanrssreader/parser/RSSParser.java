package com.jerrymxy.mikanrssreader.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jerrymxy.mikanrssreader.utils.Globals;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.XmlReader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RSSParser {
    private final String url;
    private SyndFeed feed;

    public RSSParser(String url) {
        this.url = url;
        this.parse();
    }

    /**
     * 获取RSS，使用ROME解析
     */
    public void parse() {
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request);
                 InputStream stream = response.getEntity().getContent()) {
                System.out.println("Connecting...");
                SyndFeedInput input = new SyndFeedInput();
                feed = input.build(new XmlReader(stream));
                System.out.println("标题： " + feed.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取磁力链接
     */
    public List<String> getMagnetURI() {
        List<String> result = new ArrayList<>();
        List<SyndEntry> syndEntries = feed.getEntries();
        for (SyndEntry entry : syndEntries) {
            String link = entry.getLink();
            String curMagnetUri;
            if (link.startsWith(Globals.MIKANANIME_URL)) {
                curMagnetUri = Globals.MAGNET_PREFIX + link.substring(Globals.MIKANANIME_URL.length());
            } else if (link.startsWith(Globals.MIKANANIME_BACKUP_URL)) {
                curMagnetUri = Globals.MAGNET_PREFIX + link.substring(Globals.MIKANANIME_BACKUP_URL.length());
            } else {
                System.err.println("Invalid entry: " + entry.getTitle() + ", link: " + link);
                continue;
            }
            result.add(curMagnetUri);
        }
        return result;
    }

    /**
     * 获取种子链接
     */
    public List<String> getTorrentUrl() {
        List<String> result = new ArrayList<>();
        List<SyndEntry> syndEntries = feed.getEntries();
        for (SyndEntry entry : syndEntries) {
            result.add(entry.getEnclosures().getFirst().getUrl());
        }
        return result;
    }

    public String getTitle() {
        return feed.getTitle();
    }
}
