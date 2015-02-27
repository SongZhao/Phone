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
import com.example.jing.phone.enums.FuelTrim;


public class FuelTrimObdCommand extends ObdCommand {


    private float fuelTrimValue = 0.0f;
    private final FuelTrim bank;


    public FuelTrimObdCommand(final FuelTrim bank) {
        super(bank.buildObdCommand());
        this.bank = bank;
    }


    private float prepareTempValue(final int value) {
        return new Double((value - 128) * (100.0 / 128)).floatValue();
    }


    @Override
    protected void performCalculations() {
        fuelTrimValue = prepareTempValue(buffer.get(2));
    }


    @Override
    public String getFormattedResult() {
        return String.format("%.2f%s", fuelTrimValue, "%");
    }


    public final float getValue() {
        return fuelTrimValue;
    }


    public final String getBank() {
        return bank.getBank();
    }


    @Override
    public String getName() {
        return bank.getBank();
    }


}
