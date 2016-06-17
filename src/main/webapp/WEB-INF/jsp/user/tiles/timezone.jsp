<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<form:select path="timezone" id="timezone"
             class="customInput"
             style="width: 15.4em;">
    <form:option oldtitle="[UTC - 12] Baker Island Time" value="-12">[UTC - 12] Baker Island Time</form:option>
    <form:option oldtitle="[UTC - 11] Niue Time, Samoa Standard Time" value="-11">[UTC - 11] Niue Time, Samoa
        Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 10] Hawaii-Aleutian Standard Time, Cook Island Time" value="-10">[UTC - 10]
        Hawaii-Aleutian Standard Time, Cook Island Time
    </form:option>
    <form:option oldtitle="[UTC - 9:30] Marquesas Islands Time" value="-9.5">[UTC - 9:30] Marquesas Islands
        Time
    </form:option>
    <form:option oldtitle="[UTC - 9] Alaska Standard Time, Gambier Island Time" value="-9">[UTC - 9] Alaska
        Standard Time, Gambier Island Time
    </form:option>
    <form:option oldtitle="[UTC - 8] Pacific Standard Time" value="-8">[UTC - 8] Pacific Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 7] Mountain Standard Time" value="-7">[UTC - 7] Mountain Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 6] Central Standard Time" value="-6">[UTC - 6] Central Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 5] Eastern Standard Time" value="-5">[UTC - 5] Eastern Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 4:30] Venezuelan Standard Time" value="-4.5">[UTC - 4:30] Venezuelan
        Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 4] Atlantic Standard Time" value="-4">[UTC - 4] Atlantic Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 3:30] Newfoundland Standard Time" value="-3.5">[UTC - 3:30] Newfoundland
        Standard Time
    </form:option>
    <form:option oldtitle="[UTC - 3] Amazon Standard Time, Central Greenland Time" value="-3">[UTC - 3]
        Amazon Standard Time, Central Greenland Time
    </form:option>
    <form:option oldtitle="[UTC - 2] Fernando de Noronha Time, South Georgia &amp; the South Sandwich Islands Time"
                 value="-2">[UTC - 2] Fernando de Noronha Time, South Georgia &amp; the South Sandwich
        Islands Time
    </form:option>
    <form:option oldtitle="[UTC - 1] Azores Standard Time, Cape Verde Time, Eastern Greenland Time"
                 value="-1">[UTC - 1] Azores Standard Time, Cape Verde Time, Eastern Greenland Time
    </form:option>
    <form:option oldtitle="[UTC] Western European Time, Greenwich Mean Time" value="0">[UTC] Western European
        Time, Greenwich Mean Time
    </form:option>
    <form:option oldtitle="[UTC + 1] Central European Time, West African Time" value="1" selected="selected">
        [UTC + 1] Central European Time, West African Time
    </form:option>
    <form:option oldtitle="[UTC + 2] Eastern European Time, Central African Time" value="2">[UTC + 2] Eastern
        European Time, Central African Time
    </form:option>
    <form:option oldtitle="[UTC + 3] Moscow Standard Time, Eastern African Time" value="3">[UTC + 3] Moscow
        Standard Time, Eastern African Time
    </form:option>
    <form:option oldtitle="[UTC + 3:30] Iran Standard Time" value="3.5">[UTC + 3:30] Iran Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 4] Gulf Standard Time, Samara Standard Time" value="4">[UTC + 4] Gulf
        Standard Time, Samara Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 4:30] Afghanistan Time" value="4.5">[UTC + 4:30] Afghanistan Time</form:option>
    <form:option oldtitle="[UTC + 5] Pakistan Standard Time, Yekaterinburg Standard Time" value="5">[UTC + 5]
        Pakistan Standard Time, Yekaterinburg Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 5:30] Indian Standard Time, Sri Lanka Time" value="5.5">[UTC + 5:30] Indian
        Standard Time, Sri Lanka Time
    </form:option>
    <form:option oldtitle="[UTC + 5:45] Nepal Time" value="5.75">[UTC + 5:45] Nepal Time</form:option>
    <form:option oldtitle="[UTC + 6] Bangladesh Time, Bhutan Time, Novosibirsk Standard Time" value="6">[UTC
        + 6] Bangladesh Time, Bhutan Time, Novosibirsk Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 6:30] Cocos Islands Time, Myanmar Time" value="6.5">[UTC + 6:30] Cocos
        Islands Time, Myanmar Time
    </form:option>
    <form:option oldtitle="[UTC + 7] Indochina Time, Krasnoyarsk Standard Time" value="7">[UTC + 7] Indochina
        Time, Krasnoyarsk Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 8] Chinese Standard Time, Australian Western Standard Time, Irkutsk Standard Time"
                 value="8">[UTC + 8] Chinese Standard Time, Australian Western Standard Time, Irkutsk
        Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 8:45] Southeastern Western Australia Standard Time" value="8.75">[UTC +
        8:45] Southeastern Western Australia Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 9] Japan Standard Time, Korea Standard Time, Chita Standard Time"
                 value="9">[UTC + 9] Japan Standard Time, Korea Standard Time, Chita Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 9:30] Australian Central Standard Time" value="9.5">[UTC + 9:30] Australian
        Central Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 10] Australian Eastern Standard Time, Vladivostok Standard Time"
                 value="10">[UTC + 10] Australian Eastern Standard Time, Vladivostok Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 10:30] Lord Howe Standard Time" value="10.5">[UTC + 10:30] Lord Howe
        Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 11] Solomon Island Time, Magadan Standard Time" value="11">[UTC + 11]
        Solomon Island Time, Magadan Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 11:30] Norfolk Island Time" value="11.5">[UTC + 11:30] Norfolk Island
        Time
    </form:option>
    <form:option oldtitle="[UTC + 12] New Zealand Time, Fiji Time, Kamchatka Standard Time" value="12">[UTC +
        12] New Zealand Time, Fiji Time, Kamchatka Standard Time
    </form:option>
    <form:option oldtitle="[UTC + 12:45] Chatham Islands Time" value="12.75">[UTC + 12:45] Chatham Islands
        Time
    </form:option>
    <form:option oldtitle="[UTC + 13] Tonga Time, Phoenix Islands Time" value="13">[UTC + 13] Tonga Time,
        Phoenix Islands Time
    </form:option>
    <form:option oldtitle="[UTC + 14] Line Island Time" value="14">[UTC + 14] Line Island Time</form:option>
</form:select>