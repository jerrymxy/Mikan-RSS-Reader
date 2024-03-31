package com.jerrymxy.mikanrssreader;

import com.jerrymxy.mikanrssreader.downloader.Downloader;
import com.jerrymxy.mikanrssreader.parser.RSSParser;
import com.jerrymxy.mikanrssreader.utils.Globals;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String option, url;
        if (args.length == 1) {
            url = args[0];
            option = "1";
        }
        else if (args.length == 2) {
            url = args[0];
            option = args[1];
        } else {
            System.out.println("输入蜜柑计划RSS地址：");
            Scanner input = new Scanner(System.in);
            url = input.nextLine();
            System.out.println("输入动作：");
            System.out.println("1 - 获取磁力链接");
            System.out.println("2 - 下载种子文件");
            option = input.nextLine();
        }
        process(option, url);
    }

    public static void process(String option, String url) {
        RSSParser rssParser = new RSSParser(url);

        if (Globals.OPTIONS_MAGNET.equals(option)) {
            List<String> result = rssParser.getMagnetURI();
            for (String ele : result) {
                System.out.println(ele);
            }
        } else {
            List<String> result = rssParser.getTorrentUrl();
            Downloader downloader = new Downloader(result, rssParser.getTitle());
            downloader.download();
        }
    }
}