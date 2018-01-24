package com.aerofs.takehometest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements GitHubConstants {
    //Page value keeps track of the page of each query
    private static int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                EditText text = (EditText) findViewById(R.id.enter_user);
                hideKeyboard(MainActivity.this);
                searchUser(text.getText().toString(), page);
            }
        });

        Button nextPageButton = (Button) findViewById(R.id.next_page);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.enter_user);
                searchUser(text.getText().toString(), ++page);
            }
        });

        Button prevPageButton = (Button)findViewById(R.id.prev_page);
        prevPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page != 1) {
                    EditText text = (EditText) findViewById(R.id.enter_user);
                    searchUser(text.getText().toString(), --page);
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "This is the first page", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

//    public View.OnClickListener onClickListener(final String login, int page) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchUser(login, page);
//            }
//        };
//    }

    //Creates a new service then implements the onResponse/onFailure methods
    //On response will make sure the response body has content then builds a listview from the response body
    //Otherwise an error message will display if the response is empty
    public void searchUser(final String login, int page) {
        GitHubService gitHubService = GitHubServiceGenerator.createService(GitHubService.class);
        if(!login.isEmpty()) {
            final Call<List<Repository>> call = gitHubService.getRepos(login.toLowerCase().trim(), page);
            final TextView textView = (TextView) findViewById(R.id.result_view);
            call.enqueue(new Callback<List<Repository>>() {
                @Override
                public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                    if(response.body() != null) {
                        setListViewAdapter(response.body());
                        if(response.body().size() > 0) {
                            textView.setText("Returned " + response.body().size() + " repositories from " + login);
                        } else {
                            textView.setText("No more results.");
                        }
                    } else {
                        try {
                            textView.setText("Sorry " + response.errorBody().string());
                        } catch (IOException e) {
                            displayInvalidInputToast();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Repository>> call, Throwable t) {
                    textView.setText("Something went wrong: " + t.getMessage());

                }
            });
        } else {
            displayInvalidInputToast();
        }
    }

    public void displayInvalidInputToast() {
        Toast toast = Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setListViewAdapter(final List<Repository> repositories) {
        ArrayAdapter<Repository> repositoryArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, repositories);
        final ListView lv = (ListView) findViewById(R.id.rep_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                  @Override
                                  public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                      Intent intent = new Intent(Intent.ACTION_VIEW);
                                      Repository item = (Repository) lv.getItemAtPosition(position);
                                      intent.setData(Uri.parse(item.getHtml_url()));
                                      startActivity(intent);
                                  }
                              });
                lv.setAdapter(repositoryArrayAdapter);
    }


    //extracted from StackOverflow, hides the keyboard once the user presses the search button
    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if(view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
