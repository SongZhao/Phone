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


package com.example.jing.phone.commands;


import com.example.jing.phone.enums.AvailableCommandNames;


public class SpeedObdCommand extends ObdCommand implements SystemOfUnits {


    private int metricSpeed = 0;


    public SpeedObdCommand() {
        super("01 0D");
    }


    public SpeedObdCommand(SpeedObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        metricSpeed = buffer.get(2);
    }


    @Override
    public String getFormattedResult() {
        return useImperialUnits ? String.format("%.2f", getImperialUnit())
                : String.format("%d", getMetricSpeed());
    }


    public int getMetricSpeed() {
        return metricSpeed;
    }


    public float getImperialSpeed() {
        return getImperialUnit();
    }


    @Override
    public float getImperialUnit() {
        return new Double(metricSpeed * 0.621371192).floatValue();
    }


    @Override
    public String getName() {
        return AvailableCommandNames.SPEED.getValue();
    }


}
