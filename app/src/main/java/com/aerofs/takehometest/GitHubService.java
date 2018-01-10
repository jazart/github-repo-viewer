package com.aerofs.takehometest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by jazart on 1/5/2018.
 * creating the service and retrofit builder
 */

public interface GitHubService {

    @GET("users/{owner}/repos?/page=")
    Call<List<Repository>> getRepos(
            @Path("owner") String owner, @Query("page") int page);

}
