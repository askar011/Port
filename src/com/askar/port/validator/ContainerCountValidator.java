package com.askar.port.validator;

import com.askar.port.entity.Ship;

public class ContainerCountValidator {

    public static boolean countIsValid(Ship ship , int containerCount){
        return containerCount <= ship.getCapacity();
    }
}
