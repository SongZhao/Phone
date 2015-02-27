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


package com.example.jing.phone.commands.pressure;


import com.example.jing.phone.commands.ObdCommand;
import com.example.jing.phone.commands.SystemOfUnits;


public abstract class PressureObdCommand extends ObdCommand implements SystemOfUnits {


    protected int tempValue = 0;
    protected int pressure = 0;


    public PressureObdCommand(String cmd) {
        super(cmd);
    }


    public PressureObdCommand(PressureObdCommand other) {
        super(other);
    }


    protected int preparePressureValue() {
        return buffer.get(2);
    }


    @Override
    protected void performCalculations() {
        pressure = preparePressureValue();
    }


    @Override
    public String getFormattedResult() {
        return useImperialUnits ? String.format("%.1f%s", getImperialUnit(), "psi")
                : String.format("%d%s", pressure, "kPa");
    }


    public int getMetricUnit() {
        return pressure;
    }


    public float getImperialUnit() {
        return new Double(pressure * 0.145037738).floatValue();
    }


}
