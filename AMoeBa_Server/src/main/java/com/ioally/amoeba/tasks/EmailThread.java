package com.ioally.amoeba.tasks;

public abstract class EmailThread extends Thread {

    public abstract void send();

    public abstract EmailThread to(String to);

    public abstract void run();
}
