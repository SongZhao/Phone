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


package com.example.jing.phone.commands.fuel;


import com.example.jing.phone.commands.ObdCommand;
import com.example.jing.phone.enums.AvailableCommandNames;


public class FuelLevelObdCommand extends ObdCommand {


    private float fuelLevel = 0f;


    public FuelLevelObdCommand() {
        super("01 2F");
    }


    @Override
    protected void performCalculations() {
        fuelLevel = 100.0f * buffer.get(2) / 255.0f;
    }


    @Override
    public String getFormattedResult() {
        return String.format("%.1f", fuelLevel);
    }


    public float getFuelLevel() {
        return fuelLevel;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_LEVEL.getValue();
    }


}
