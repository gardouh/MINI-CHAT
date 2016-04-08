package com.github.jmsoft.socketclient;

import android.content.Context;
import android.os.AsyncTask;

import com.github.jmsoft.socketclient.Const.ChatCst;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import socketclient.lg.com.socketclient.R;

/**
 * Async task to connect to server
 */
public class Connection extends AsyncTask<Void, String, Void> {

    //Socket for connecting the client to server
    private Socket sSocket;
    private InetAddress ia;
    private int mPort;
    private Context context;
    private List<String> messages;
    private ChatCst mChatCst;

    public Connection(InetAddress ia, int mPort, Context context, List<String> messages, ChatCst mChatCst){
        this.ia = ia;
        this.mPort = mPort;
        this.context = context;
        this.messages = messages;
        this.mChatCst = mChatCst;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            sSocket = new Socket(ia, mPort);

            //Add connected to server message to UI
            publishProgress(context.getResources().getString(R.string.connected_to_server));

            //Listen to messages
            while (true) {
                DataInputStream dis = new DataInputStream(
                        sSocket.getInputStream());
                final String string = dis.readUTF();

                publishProgress(string);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        messages.add(values[0]);
        mChatCst.notifyDataSetChanged();
    }

    public Socket getsSocket() {
        return sSocket;
    }
}
