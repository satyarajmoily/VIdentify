package com.howaboutthis.satyaraj.videntify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private List<Message> messageArrayList;
    private EditText inputMessage;
    private Map<String,Object> context = new HashMap<>();
    private String workspace_id;
    private boolean initialRequest;
    private String conversation_username;
    private String conversation_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Ask me to do Anything");
        Context mContext = getApplicationContext();
        workspace_id = mContext.getString(R.string.workspace_id);
        conversation_username = mContext.getString(R.string.conversation_username);
        conversation_password = mContext.getString(R.string.conversation_password);
        inputMessage = findViewById(R.id.input_message);
         ImageButton send = findViewById(R.id.send_button);
        recyclerView =  findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();

        mAdapter = new ChatAdapter(messageArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        inputMessage.setText("");
        this.initialRequest = true;
        sendMessage();

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {

        final String inputmessage = this.inputMessage.getText().toString().trim();
        if(!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            Log.d("Message","Sending a message to Watson Conversation Service");

        }
        else
        {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("100");
            this.initialRequest = false;

        }

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

        final Thread thread = new Thread(new Runnable(){
            public void run() {

                boolean check;
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL("http://www.google.com").openConnection());
                    httpURLConnection.setRequestProperty("User-Agent", "Test");
                    httpURLConnection.setRequestProperty("Connection", "close");
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.connect();
                    check = (httpURLConnection.getResponseCode() == 200);
                } catch (IOException e) {
                    e.printStackTrace();
                    check = false;
                }
                if (check) {
                    try {

                        ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2017_02_03);
                        service.setUsernameAndPassword(conversation_username, conversation_password);
                        MessageRequest newMessage = new MessageRequest.Builder().inputText(inputmessage).context(context).build();
                        MessageResponse response = service.message(workspace_id, newMessage).execute();

                        //Passing Context of last conversation
                        if (response.getContext() != null) {
                            context.clear();
                            context = response.getContext();
                        }
                        Message outMessage = new Message();
                        if (response != null) {
                            if (response.getOutput() != null && response.getOutput().containsKey("text")) {

                                ArrayList responseList = (ArrayList) response.getOutput().get("text");
                                if (null != responseList && responseList.size() > 0) {
                                    outMessage.setMessage((String) responseList.get(0));
                                    outMessage.setId("2");
                                }
                                messageArrayList.add(outMessage);
                            }

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    if (mAdapter.getItemCount() > 1) {
                                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(ConversationActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
