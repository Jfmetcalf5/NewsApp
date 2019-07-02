package com.example.mynewsapp;

public class Result {

    public Result(String sectionName, String webTitle, String webUrl) {
        _sectionName = sectionName;
        _webTitle = webTitle;
        _webUrl = webUrl;
    }

    public String getSectionName() {
        return _sectionName;
    }
    public String getwebTitle() {
        return _webTitle;
    }
    public String getWebUrl() {
        return _webUrl;
    }

    String _sectionName;
    String _webTitle;
    String _webUrl;

}
