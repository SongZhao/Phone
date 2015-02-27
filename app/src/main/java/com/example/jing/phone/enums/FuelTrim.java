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


package com.example.jing.phone.enums;


import java.util.HashMap;
import java.util.Map;


public enum FuelTrim {


    SHORT_TERM_BANK_1(0x06, "Short Term Fuel Trim Bank 1"),
    LONG_TERM_BANK_1(0x07, "Long Term Fuel Trim Bank 1"),
    SHORT_TERM_BANK_2(0x08, "Short Term Fuel Trim Bank 2"),
    LONG_TERM_BANK_2(0x09, "Long Term Fuel Trim Bank 2");


    private final int value;
    private final String bank;

    private static Map<Integer, FuelTrim> map = new HashMap<Integer, FuelTrim>();
    static {
        for (FuelTrim error : FuelTrim.values())
            map.put(error.getValue(), error);
    }


    private FuelTrim(final int value, final String bank) {
        this.value = value;
        this.bank = bank;
    }


    public int getValue() {
        return value;
    }


    public String getBank() {
        return bank;
    }


    public static FuelTrim fromValue(final int value) {
        return map.get(value);
    }


    public final String buildObdCommand() {
        return new String("01 " + value);
    }


}
