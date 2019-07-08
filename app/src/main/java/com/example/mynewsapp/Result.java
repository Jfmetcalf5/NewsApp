package com.example.mynewsapp;

class Result {

    Result(String sectionName, String webTitle, String webUrl, String webPublicationDate, String author) {
        _sectionName = sectionName;
        _webTitle = webTitle;
        _webUrl = webUrl;
        _webPublicationDate = webPublicationDate;
        _author = author;
    }

    String getSectionName() {
        return _sectionName;
    }
    String getwebTitle() { return _webTitle; }
    String getWebUrl() {
        return _webUrl;
    }
    String getWebPublicationDate() { return _webPublicationDate; }
    String getAuthor() { return _author; }

    private String _sectionName;
    private String _webTitle;
    private String _webUrl;
    private String _webPublicationDate;
    private String _author;

}
