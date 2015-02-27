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

import com.example.jing.phone.exceptions.*;


public abstract class ObdCommand {


    protected ArrayList<Integer> buffer = null;
    protected String cmd = null;
    protected String rawData = null;
    protected boolean useImperialUnits = true;


    private Class[] ERROR_CLASSES = {
            UnableToConnectException.class,
            BusInitException.class,
            MisunderstoodCommandException.class,
            NoDataException.class,
            StoppedException.class,
            UnknownObdErrorException.class
    };


    public ObdCommand(String command) {
        this.cmd = command;
        this.buffer = new ArrayList<Integer>();
    }


    private ObdCommand() { }


    public ObdCommand(ObdCommand other) {
        this(other.cmd);
    }


    public void run(InputStream in, OutputStream out) throws IOException, InterruptedException {
        sendCommand(out);
        readResult(in);
    }


    protected void sendCommand(OutputStream out) throws IOException, InterruptedException {
        cmd += "\r";
        out.write(cmd.getBytes());
        out.flush();
        Thread.sleep(200);
    }


    protected void resendCommand(OutputStream out) throws IOException, InterruptedException {
        out.write("\r".getBytes());
        out.flush();
    }


    protected void readResult(InputStream in) throws IOException {
        readRawData(in);
        checkForErrors();
        fillBuffer();
        performCalculations();
    }


    protected abstract void performCalculations();


    protected void fillBuffer() {

        rawData = rawData.replaceAll("\\s", "");
        if (!rawData.matches("([0-9A-F]{2})+"))
            throw new NonNumericResponseException(rawData);

        buffer.clear();

        int begin = 0;
        int end = 2;
        while (end <= rawData.length()) {
            buffer.add(Integer.decode("0x" + rawData.substring(begin, end)));
            begin = end;
            end += 2;
        }

    }


    protected void readRawData(InputStream in) throws IOException {

        byte b = 0;
        StringBuilder res = new StringBuilder();

        while((char) (b = (byte) in.read()) != '>')
            res.append((char) b);

        rawData = res.toString().trim();
        rawData = rawData.substring(rawData.lastIndexOf(13) + 1);

    }


    protected void checkForErrors() {

        for (Class<? extends ObdResponseException> errorClass : ERROR_CLASSES) {

            ObdResponseException messageError;

            try {
                messageError = errorClass.newInstance();
                messageError.setCommand(this.cmd);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (messageError.isError(rawData))
                throw messageError;

        }

    }


    public String getResult() {
        return rawData;
    }


    public abstract String getFormattedResult();


    protected ArrayList<Integer> getBuffer() {
        return buffer;
    }


    public boolean useImperialUnits() {
        return useImperialUnits;
    }


    public void useImperialUnits(boolean isImperial) {
        this.useImperialUnits = isImperial;
    }


    public abstract String getName();


}
