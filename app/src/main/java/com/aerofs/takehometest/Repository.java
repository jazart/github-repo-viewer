package com.aerofs.takehometest;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.StringJoiner;

/**
 * Created by jazart on 1/5/2018.
 * This is the repository object model with the relevant info for repositories.
 *
 */

public class Repository {
    private String name;
    private String login;
    private String html_url;
    private int forks_count;
    private int stargazers_count;
    private int open_issues;

    public Repository(String name, String login, String html_url, int forks_count, int stargazers_count, int open_issues) {
        this.name = name;
        this.login = login;
        this.html_url = html_url;
        this.forks_count = forks_count;
        this.stargazers_count = stargazers_count;
        this.open_issues = open_issues;
    }

    public String getName() {
        return this.name;
    }

    public String getlogin() {
        return this.login;
    }

    public String getHtml_url() { return this.html_url; }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        return new StringJoiner(" ")
                .add(name + " - ")
                .add("Forks: " + forks_count)
                .add("Open Issues: " + open_issues)
                .add("Stars: " + stargazers_count)
                .toString();
    }
}
