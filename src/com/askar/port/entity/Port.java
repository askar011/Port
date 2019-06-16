package com.askar.port.entity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    private String name;
    private static final int CAPACITY = 3000;
    private AtomicInteger containerCount = new AtomicInteger();
    private Lock lock = new ReentrantLock();
    private AtomicBoolean portRun = new AtomicBoolean();

    public Port(String name, int containerCount) {
        this.name = name;
        this.containerCount.set(containerCount);
        portRun.set(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContainerCount() {
        return containerCount.get();
    }

    public Lock getLock() {
        return lock;
    }

    public boolean isPortRun() {
        return portRun.get();
    }

    public void stopPort() {
        portRun.set(false);
    }

    public void setContainerCount(int containerCount) {
        this.containerCount.set(containerCount);
    }

    public void loadContainers(int containerCount) {
        this.containerCount.getAndAdd(containerCount);
    }

    public void unloadContainers(int containerCount) {
        int temp = this.containerCount.get() - containerCount;
        this.containerCount.set(temp);
    }

}
