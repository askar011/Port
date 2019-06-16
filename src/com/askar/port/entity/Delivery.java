package com.askar.port.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Delivery implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Delivery.class);
    private Port port;
    private static final int MIN_CONTAINER_AMOUNT = 500;
    private static final int MAX_CONTAINER_AMOUNT = 2400;
    private static final int DELIVER_CONTAINER_AMOUNT = 1000;
    private static final int WAITING_TIME = 5;

    public Delivery(Port port) {
        this.port = port;
    }

    @Override
    public void run() {
        while (port.isPortRun()) {
            if (port.getContainerCount() < MIN_CONTAINER_AMOUNT) {
                port.getLock().lock();
                LOGGER.info("Port is free, wait for delivering containers to port");
                try {
                    port.loadContainers(DELIVER_CONTAINER_AMOUNT);
                    TimeUnit.SECONDS.sleep(WAITING_TIME);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } finally {
                    port.getLock().unlock();
                }
            }
            if (port.getContainerCount() > MAX_CONTAINER_AMOUNT) {
                port.getLock().lock();
                LOGGER.info("Port is full, wait for loading containers from port");
                try {
                    TimeUnit.SECONDS.sleep(WAITING_TIME);
                    port.unloadContainers(DELIVER_CONTAINER_AMOUNT);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } finally {
                    port.getLock().unlock();
                }
            }
        }
    }
}
