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


public enum ObdProtocols {


    AUTO('0'),
    SAE_J1850_PWM('1'),
    SAE_J1850_VPW('2'),
    ISO_9141_2('3'),
    ISO_14230_4_KWP('4'),
    ISO_14230_4_KWP_FAST('5'),
    ISO_15765_4_CAN('6'),
    ISO_15765_4_CAN_B('7'),
    ISO_15765_4_CAN_C('8'),
    ISO_15765_4_CAN_D('9'),
    SAE_J1939_CAN('A'),
    USER1_CAN('B'),
    USER2_CAN('C');


    private final char value;


    private ObdProtocols(char value) {
        this.value = value;
    }


    public char getValue() {
        return value;
    }


}
