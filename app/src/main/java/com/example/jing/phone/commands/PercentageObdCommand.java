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


public abstract class PercentageObdCommand extends ObdCommand {


    private float percentage = 0f;


    public PercentageObdCommand(String command) {
        super(command);
    }


    public PercentageObdCommand(PercentageObdCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        percentage = (buffer.get(2) * 100.0f) / 255.0f;
    }


    @Override
    public String getFormattedResult() {
        return String.format("%.1f", percentage);
    }


    public float getPercentage() {
        return percentage;
    }


}
