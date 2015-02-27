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


import com.example.jing.phone.commands.PercentageObdCommand;
import com.example.jing.phone.enums.AvailableCommandNames;


public class ThrottlePositionObdCommand extends PercentageObdCommand {


    public ThrottlePositionObdCommand() {
        super("01 11");
    }


    public ThrottlePositionObdCommand(ThrottlePositionObdCommand other) {
        super(other);
    }


    @Override
    public String getName() {
        return AvailableCommandNames.THROTTLE_POS.getValue();
    }


}