package com.example.jing.phone.commands.pressure;


import com.example.jing.phone.enums.AvailableCommandNames;


public class FuelPressureObdCommand extends PressureObdCommand {


    public FuelPressureObdCommand() {
        super("01 0A"); // super("010A");
    }


    public FuelPressureObdCommand(FuelPressureObdCommand other) {
        super(other);
    }


    @Override
    protected final int preparePressureValue() {
        return buffer.get(2) * 3;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_PRESSURE.getValue();
    }


}
