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
import com.example.jing.phone.enums.FuelType;


public class FindFuelTypeObdCommand extends ObdCommand {


    private int fuelType = 0;


    public FindFuelTypeObdCommand() {
        super("01 51");
    }


    public FindFuelTypeObdCommand(FindFuelTypeObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        fuelType = buffer.get(2);
    }


    @Override
    public String getFormattedResult() {

        try {
            return FuelType.fromValue(fuelType).getDescription();
        } catch (Exception e) {
            return "-";
        }

    }


    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_TYPE.getValue();
    }


}
