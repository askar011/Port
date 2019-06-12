package com.askar.port.entity;

import com.askar.port.service.DeliveryController;
import com.askar.port.service.LoadController;

public class Delivery implements Runnable {

    private Port port;
    public final static int MIN_CONTAINER_AMOUNT = 250;
    private final static int DELIVER_CONTAINER_COUNT = 200;
    private LoadController loadController;
    private DeliveryController deliveryController;
    private Thread loadContainers, deliverContainers;

    public Delivery(Port port, LoadController loadController, DeliveryController deliveryController) {
        this.port = port;
        this.loadController = loadController;
        this.deliveryController = deliveryController;
    }

    @Override
    public void run() {

    }

    private void loadContainers() {
        loadContainers = new Thread(new Runnable() {
            @Override
            public void run() {
                deliveryController.loadFromPort(port, DELIVER_CONTAINER_COUNT);
            }
        });
        loadContainers.start();
    }

    private void deliverContainers() {
        deliverContainers = new Thread(() -> loadController.deliverToPort(port, DELIVER_CONTAINER_COUNT));
        deliverContainers.start();
    }

}
