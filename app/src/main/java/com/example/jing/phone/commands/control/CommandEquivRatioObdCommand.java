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


public class CommandEquivRatioObdCommand extends ObdCommand {


    private double ratio = 0.00;


    public CommandEquivRatioObdCommand() {
        super("01 44");
    }


    public CommandEquivRatioObdCommand(CommandEquivRatioObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        int a = buffer.get(2);
        int b = buffer.get(3);
        ratio = (a * 256 + b) / 32768;
    }


    @Override
    public String getFormattedResult() {
        return String.format("%.1f%s", ratio, "%");
    }


    public double getRatio() {
        return ratio;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.EQUIV_RATIO.getValue();
    }


}
