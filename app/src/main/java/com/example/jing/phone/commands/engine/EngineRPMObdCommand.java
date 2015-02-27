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


package com.example.jing.phone.commands.engine;


import com.example.jing.phone.commands.ObdCommand;
import com.example.jing.phone.enums.AvailableCommandNames;


public class EngineRPMObdCommand extends ObdCommand {


    private int rpm = -1;


    public EngineRPMObdCommand() {
        super("01 0C");
    }


    public EngineRPMObdCommand(EngineRPMObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        rpm = (buffer.get(2) * 256 + buffer.get(3)) / 4;
    }


    @Override
    public String getFormattedResult() {
        return String.format("%d", rpm);
    }


    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RPM.getValue();
    }


    public int getRPM() {
        return rpm;
    }


}
