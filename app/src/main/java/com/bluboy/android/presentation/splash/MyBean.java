package com.bluboy.android.presentation.splash;

public class MyBean {

    private String latestVersion;
    private String latestVersionCode;
    private String releaseNotes[];
    private String content;
    private String url;
    private double versioncode;

     public String getLatestVersion() {
            return latestVersion;
        }

        public void setLatestVersion(String latestVersion) {
            this.latestVersion = latestVersion;
        }

        public String getLatestVersionCode() {
            return latestVersionCode;
        }

        public void setLatestVersionCode(String latestVersionCode) {
            this.latestVersionCode = latestVersionCode;
        }

        public String[] getReleaseNotes() {
            return releaseNotes;
        }

        public void setReleaseNotes(String[] releaseNotes) {
            this.releaseNotes = releaseNotes;
        }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(double versioncode) {
        this.versioncode = versioncode;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "latestVersion='" + latestVersion + '\'' +
                "latestVersionCode='" + latestVersionCode + '\'' +
                "releaseNotes='" + releaseNotes + '\'' +
                "content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", versioncode=" + versioncode +
                '}';
    }
}