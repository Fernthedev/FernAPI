package com.github.fernthedev.fernapi.server.bungee.network;


public abstract class MessageRunnable implements Runnable {

    private boolean ran;



    public MessageRunnable() {

    }


    public void run() {
        if(!ran) {
            ran = true;
        }
    }


}
