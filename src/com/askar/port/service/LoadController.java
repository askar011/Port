package com.askar.port.service;

import com.askar.port.entity.Delivery;
import com.askar.port.entity.Port;
import com.askar.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class LoadController {

    private static final Logger LOGGER = LogManager.getLogger(LoadController.class);
    private Port port;
    private Condition isEmpty;

    public LoadController(Port port) {
        this.port = port;
        isEmpty = port.getLock().newCondition();
    }

    public void loadFromPort(Port port, Ship ship, int containerCount) {
        port.getLock().lock();
        try {
            while (ship.getContainerCount() <= ship.getCapacity()) {
                port.unloadContainers(containerCount);
                ship.loadContainers(containerCount);
                while (port.getContainerCount() < Delivery.MIN_CONTAINER_AMOUNT) {
                    try {
                        LOGGER.info("Port is free,waiting for a container delivering " + ship.getModel());
                        isEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            LOGGER.info("Finished loading to ship " + ship.getModel());
        } finally {
            port.getLock().unlock();
        }
    }


    public void deliverToPort(Port port, int containerAmount) {
        port.getLock().lock();
        try {
            LOGGER.info("Started Delivering containers to port");
            while (port.getContainerCount() < Port.CAPACITY) {
                port.loadContainers(containerAmount);
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("Delivering to port finished");
            isEmpty.signal();
        } finally {
            port.getLock().unlock();
        }
    }
}
