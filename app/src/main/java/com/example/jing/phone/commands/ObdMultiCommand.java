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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class ObdMultiCommand {


    private ArrayList<ObdCommand> commands;


    public  ObdMultiCommand() {
        this.commands = new ArrayList<ObdCommand>();
    }


    public void add(ObdCommand command) {
        this.commands.add(command);
    }


    public void remove(ObdCommand command) {
        this.commands.remove(command);
    }


    public void sendCommands(InputStream in, OutputStream out) throws IOException, InterruptedException {
        for (ObdCommand command : commands)
            command.run(in, out);
    }


    public String getFormattedResult() {
        StringBuilder res = new StringBuilder();
        for (ObdCommand command : commands)
            res.append(command.getFormattedResult()).append(",");
        return res.toString();
    }


}
