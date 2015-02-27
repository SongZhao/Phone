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
import com.example.jing.phone.enums.AvailableCommandNames;


public class DtcNumberObdCommand extends ObdCommand {


    private int codeCount = 0;
    private boolean milOn = false;


    public DtcNumberObdCommand() {
        super("01 01");
    }


    public DtcNumberObdCommand(DtcNumberObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        final int mil = buffer.get(2);
        milOn = (mil & 0x80) == 128;
        codeCount = mil & 0x7F;
    }


    @Override
    public String getFormattedResult() {
        final String res = milOn ? "MIL is ON" : "MIL is OFF";
        return new StringBuilder().append(res).append(codeCount).append(" codes").toString();
    }


    public int getTotalAvailableCodes() {
        return codeCount;
    }


    public boolean getMilOn() {
        return milOn;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.DTC_NUMBER.getValue();
    }


}
