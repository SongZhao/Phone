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


package com.example.jing.phone.commands.temperature;


import com.example.jing.phone.commands.ObdCommand;
import com.example.jing.phone.commands.SystemOfUnits;


public abstract class TemperatureObdCommand extends ObdCommand implements SystemOfUnits {


    private float temperature = 0.0f;


    public TemperatureObdCommand(String cmd) {
        super(cmd);
    }


    public TemperatureObdCommand(TemperatureObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        temperature = buffer.get(2) - 40;
    }


    @Override
    public String getFormattedResult() {
        return useImperialUnits ? String.format("%.1f%s", getImperialUnit(), "F")
                : String.format("%.0f%s", temperature, "C");
    }


    public float getTemperature() {
        return temperature;
    }


    public float getImperialUnit() {
        return temperature * 1.8f + 32;
    }


    public float getKelvin() {
        return temperature + 273.15f;
    }


    @Override
    public abstract String getName();


}
