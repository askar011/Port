package com.askar.port.entity;

import com.askar.port.controller.DeliveryController;
import com.askar.port.controller.LoadController;
import com.askar.port.validator.ContainerCountValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Ship implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Ship.class);
    private int capacity;
    private String model;
    private ServiceType serviceType;
    private AtomicInteger containerCount = new AtomicInteger();
    private Port port;
    private Semaphore semaphore;
    private LoadController loadController;
    private static final int LOAD_CONTAINER_AMOUNT = 50;
    private DeliveryController deliveryController;

    public Ship(int containerCount, String model, ServiceType serviceType, int capacity, Port port,
                LoadController loadController, DeliveryController deliveryController, Semaphore semaphore) {
        this.capacity = capacity;
        this.model = model;
        this.serviceType = serviceType;
        this.setContainerCount(containerCount);
        this.port = port;
        this.loadController = loadController;
        this.deliveryController = deliveryController;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            LOGGER.info("Ship " + model + " came to the port");
            if (serviceType.toString().equals("LOAD")) {
                LOGGER.info("Ship " + model + " is loading");
                loadController.loadFromPort(port, this, LOAD_CONTAINER_AMOUNT);
            } else if (serviceType.toString().equals("DELIVERY")) {
                LOGGER.info("Ship " + model + " is delivering");
                deliveryController.deliverToPort(port, this);
            } else {
                LOGGER.info("Ship " + model + " is delivering and loading");
                deliveryController.deliverToPort(port, this);
                loadController.loadFromPort(port, this, LOAD_CONTAINER_AMOUNT);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        LOGGER.info(model + " finished and leaving the port");
        semaphore.release();
    }

    private void setContainerCount(int containerCount) {
        if (ContainerCountValidator.countIsValid(this, containerCount)) {
            this.containerCount.set(containerCount);
        } else {
            throw new IllegalArgumentException("Containers must be less than ship capacity");
        }
    }


    public ServiceType getServiceType() {
        return serviceType;
    }

    public void loadContainers(int containerCount) {
        this.containerCount.getAndAdd(containerCount);
    }

    public String getModel() {
        return model;
    }

    public void deliverContainers(int containerCount) {
        int temp = this.containerCount.get() - containerCount;
        this.containerCount.set(temp);
    }

    public int getContainerCount() {
        return containerCount.get();
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return containerCount == ship.containerCount &&
                capacity == ship.capacity &&
                Objects.equals(model, ship.model) &&
                serviceType == ship.serviceType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + model.hashCode();
        result = prime * result + capacity;
        return result;
    }

}
