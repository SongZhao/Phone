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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.jing.phone.commands.ObdCommand;
import com.example.jing.phone.commands.SpeedObdCommand;
import com.example.jing.phone.enums.AvailableCommandNames;


public class FuelEconomyObdCommand extends ObdCommand {


    protected float kml = -1.0f;


    public FuelEconomyObdCommand() {
        super("");
    }


    @Override
    protected void performCalculations() { }


    @Override
    public void run(InputStream in, OutputStream out) throws IOException, InterruptedException {

        final FuelConsumptionRateObdCommand fuelConsumptionCommand = new FuelConsumptionRateObdCommand();
        fuelConsumptionCommand.run(in, out);

        final SpeedObdCommand speedCommand = new SpeedObdCommand();
        speedCommand.run(in, out);

        kml = (100 / speedCommand.getMetricSpeed()) * fuelConsumptionCommand.getLitersPerHour();

    }


    @Override
    public String getFormattedResult() {
        return useImperialUnits ? String.format("%.1f %s", getMilesPerUKGallon(), "mpg")
                : String.format("%.1f %s", kml, "1/100km");
    }


    public float getLitersPer100Km() {
        return kml;
    }


    public float getMilesPerUSGallon() {
        return 235.2f / kml;
    }


    public float getMilesPerUKGallon() {
        return 282.5f / kml;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_ECONOMY.getValue();
    }


}
