package com.askar.port.controller;

import com.askar.port.entity.Delivery;
import com.askar.port.entity.Port;
import com.askar.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoadController {

    private static final Logger LOGGER = LogManager.getLogger(LoadController.class);
    private Port port;
    private Condition isEmpty;
    private Lock lock = new ReentrantLock();

    public LoadController(Port port) {
        this.port = port;
        isEmpty = port.getLock().newCondition();
    }

    public void loadFromPort(Port port, Ship ship, int containerCount) {
        port.getLock().lock();
        try {
            while (port.getContainerCount() < ship.getCapacity()) {
                try {
                    LOGGER.info("Port is free,waiting for a container delivering " + ship.getModel());
                    isEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (ship.getContainerCount() <= ship.getCapacity()) {
                port.unloadContainers(containerCount);
                ship.loadContainers(containerCount);
            }
            LOGGER.info("Finished loading to ship " + ship.getModel());
            isEmpty.signalAll();
        } finally {
            port.getLock().unlock();
        }
    }


    public void deliverToPort(Port port, int containerAmount) {
        port.getLock().lock();
        try {
            while (port.getContainerCount() > 1000) {
                isEmpty.await();
            }
            while (port.getContainerCount() < Port.CAPACITY) {
                port.loadContainers(containerAmount);
            }
            LOGGER.info("Started Delivering containers to port");
            System.out.println(port.getContainerCount());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("Delivering to port finished");
            isEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            port.getLock().unlock();
        }
    }
}
