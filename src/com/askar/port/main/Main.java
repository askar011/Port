package com.askar.port.main;

import com.askar.port.controller.DeliveryController;
import com.askar.port.controller.LoadController;
import com.askar.port.entity.Delivery;
import com.askar.port.entity.Port;
import com.askar.port.entity.ServiceType;
import com.askar.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Semaphore;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Port port = new Port("Askar", 2200);
        LoadController loadController = new LoadController();
        Semaphore semaphore = new Semaphore(2);
        DeliveryController deliveryController = new DeliveryController();
        Thread t = new Thread(new Delivery(port));
        t.start();
        new Ship(0, String.valueOf(1), ServiceType.LOAD, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(0, String.valueOf(2), ServiceType.LOAD, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(0, String.valueOf(3), ServiceType.LOAD, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(0, String.valueOf(4), ServiceType.LOAD, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(200, String.valueOf(5), ServiceType.DELIVERY, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(300, String.valueOf(6), ServiceType.MULTISERVICE, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(300, String.valueOf(7), ServiceType.DELIVERY, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(430, String.valueOf(8), ServiceType.MULTISERVICE, 500, port, loadController, deliveryController, semaphore).start();
        new Ship(420, String.valueOf(9), ServiceType.DELIVERY, 500, port, loadController, deliveryController, semaphore).start();

        try {
            t.join(150000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("CLosing port and delivering");
        port.stopPort();
    }
}
