/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package com.example.jing.phone.commands.control;


import com.example.jing.phone.commands.ObdCommand;
import com.example.jing.phone.commands.SystemOfUnits;
import com.example.jing.phone.enums.AvailableCommandNames;


public class DistanceTraveledSinceCodesClearedObdCommand extends ObdCommand implements SystemOfUnits {


    private int km = 0;


    public DistanceTraveledSinceCodesClearedObdCommand() {
        super("01 31");
    }


    public DistanceTraveledSinceCodesClearedObdCommand(DistanceTraveledSinceCodesClearedObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        km = buffer.get(2) * 256 + buffer.get(3);
    }


    @Override
    public String getFormattedResult() {
        return String.format("%.2f%s", (float) km, "km");
    }


    @Override
    public float getImperialUnit() {
        return new Double(km * 0.621371192).floatValue();
    }


    public int getKm() {
        return km;
    }


    public void setKm(int km) {
        this.km = km;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.DISTANCE_TRAVELED_AFTER_CODES_CLEARED.getValue();
    }


}
