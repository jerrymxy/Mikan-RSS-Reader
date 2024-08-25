package com.jerrymxy.mikanrssreader.parser;

import java.util.ArrayList;
import java.util.List;

import com.jerrymxy.mikanrssreader.utils.Globals;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.XmlReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RSSParser {
    private final String url;
    private SyndFeed feed;

    private static final Logger LOGGER = LoggerFactory.getLogger(RSSParser.class);

    public RSSParser(String url) {
        this.url = url;
        this.parse();
    }

    /**
     * 获取RSS，使用ROME解析
     */
    public void parse() {
        try {
            Response response = Request.get(url).execute();
            System.out.println("Connecting...");
            SyndFeedInput input = new SyndFeedInput();
            feed = input.build(new XmlReader(response.returnContent().asStream()));
            System.out.println("标题： " + feed.getTitle());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 获取磁力链接
     */
    public List<String> getMagnetURI(String pattern) {
        List<String> result = new ArrayList<>();
        List<SyndEntry> syndEntries = feed.getEntries();
        for (SyndEntry entry : syndEntries) {
            if (StringUtils.isBlank(pattern) || StringUtils.contains(entry.getTitle(), pattern)) {
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
        }
        return result;
    }

    /**
     * 获取种子链接
     */
    public List<String> getTorrentUrl(String pattern) {
        List<String> result = new ArrayList<>();
        List<SyndEntry> syndEntries = feed.getEntries();
        for (SyndEntry entry : syndEntries) {
            if (StringUtils.isBlank(pattern) || StringUtils.contains(entry.getTitle(), pattern)) {
                result.add(entry.getEnclosures().getFirst().getUrl());
            }
        }
        return result;
    }

    public String getTitle() {
        return feed.getTitle();
    }
}
