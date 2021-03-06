<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
    .pagehead {
        background: none;
        margin-top: -192px;
    }

    #content {
        padding-left: 10px;
        padding-right: 60px;
        padding-bottom: 0px;
        overflow: visible;
    }

    #header .parchment-head {
        position: relative;
        margin: 0;
        width: 0;
        height: 0;
        padding: 0;
        background: none;
    }

    .topbox {
        margin-top: -39px !important;
    }

    #header {
        height: 20px !important;
    }
</style>
<script type="text/javascript">

    $(document).ready(function () {
        $(function () {
            var width = 335;

            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            }
        });
    });
</script>

<div class="pagehead"
     style="background-image: url('http://static.eaw1805.com/images/site/HallOfFamePanelsBig.png');
            background-position: 0px 0px;
            background-repeat: no-repeat;
            float: left;
            margin-top: 30px;
            clear: both;
            background-size: 560px 520px;
            margin-left: 168px;
            width: 560px;
            height: 520px;">

<div style="padding-top: 40px; padding-left: 30px;">

<form:form commandName="paypalTransaction" method="POST">
<h1 style="font-size: 38px;
                       width: 410px;
                       text-align: center;">Request Receipt</h1>

<div class="formbody">

<label for="payerName"
       style="font-size: 16px;
                clear:both;
                padding-top: 5px;
                color: #444446;">Full Name:</label>

<div>
    <form:input path="payerName" id="payerName"
                class="customInput"
                tabindex="1"
                style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        width: 440px;
                        height: 20px;"/>
    <spring:bind path="payerName">
        <c:if test="${status.error}">
            <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
                 src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>
</div>

<label for="payerAddress"
       style="font-size: 16px;
            padding-top: 5px;
            color: #444446;">Postal Address:</label>

<div>
    <form:input path="payerAddress" id="payerAddress"
                class="customInput"
                tabindex="2"
                style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        width: 440px;
                        height: 20px;"/>
    <spring:bind path="payerAddress">
        <c:if test="${status.error}">
            <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
                 src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>
</div>


<label for="payerCity"
       style="font-size: 16px;
            padding-top: 5px;
            color: #444446;">City:</label>

<div>
    <form:input path="payerCity" id="payerCity"
                class="customInput"
                tabindex="3"
                style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        width: 440px;
                        height: 20px;"/>
    <spring:bind path="payerCity">
        <c:if test="${status.error}">
            <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
                 src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>
</div>

<label for="payerPOCode"
       style="font-size: 16px;
                padding-top: 5px;
                color: #444446;">Postal Code:</label>

<div>
    <form:input path="payerPOCode" id="payerPOCode"
                class="customInput"
                tabindex="4"
                style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        width: 200px;
                        height: 20px;"/>
    <spring:bind path="payerPOCode">
        <c:if test="${status.error}">
            <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
                 src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>
</div>

<label for="payerCountry"
       style="font-size: 16px;
                clear:both;
                float:left;
                padding-top: 5px;
                color: #444446;">Location:</label>

<div>
<form:select path="payerCountry" id="payerCountry"
             class="customInput"
             tabindex="5"
             style="font-size: 14px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 1px;
                        width: 448px;
                        height: 26px;">
<form:option value="" selected="selected">Select Country</form:option>
<form:option value="United States">United States</form:option>
<form:option value="United Kingdom">United Kingdom</form:option>
<form:option value="Afghanistan">Afghanistan</form:option>
<form:option value="Albania">Albania</form:option>
<form:option value="Algeria">Algeria</form:option>
<form:option value="American Samoa">American Samoa</form:option>
<form:option value="Andorra">Andorra</form:option>
<form:option value="Angola">Angola</form:option>
<form:option value="Anguilla">Anguilla</form:option>
<form:option value="Antarctica">Antarctica</form:option>
<form:option value="Antigua and Barbuda">Antigua and Barbuda</form:option>
<form:option value="Argentina">Argentina</form:option>
<form:option value="Armenia">Armenia</form:option>
<form:option value="Aruba">Aruba</form:option>
<form:option value="Australia">Australia</form:option>
<form:option value="Austria">Austria</form:option>
<form:option value="Azerbaijan">Azerbaijan</form:option>
<form:option value="Bahamas">Bahamas</form:option>
<form:option value="Bahrain">Bahrain</form:option>
<form:option value="Bangladesh">Bangladesh</form:option>
<form:option value="Barbados">Barbados</form:option>
<form:option value="Belarus">Belarus</form:option>
<form:option value="Belgium">Belgium</form:option>
<form:option value="Belize">Belize</form:option>
<form:option value="Benin">Benin</form:option>
<form:option value="Bermuda">Bermuda</form:option>
<form:option value="Bhutan">Bhutan</form:option>
<form:option value="Bolivia">Bolivia</form:option>
<form:option value="Bosnia and Herzegovina">Bosnia and Herzegovina</form:option>
<form:option value="Botswana">Botswana</form:option>
<form:option value="Bouvet Island">Bouvet Island</form:option>
<form:option value="Brazil">Brazil</form:option>
<form:option value="British Indian Ocean Territory">British Indian Ocean Territory</form:option>
<form:option value="Brunei Darussalam">Brunei Darussalam</form:option>
<form:option value="Bulgaria">Bulgaria</form:option>
<form:option value="Burkina Faso">Burkina Faso</form:option>
<form:option value="Burundi">Burundi</form:option>
<form:option value="Cambodia">Cambodia</form:option>
<form:option value="Cameroon">Cameroon</form:option>
<form:option value="Canada">Canada</form:option>
<form:option value="Cape Verde">Cape Verde</form:option>
<form:option value="Cayman Islands">Cayman Islands</form:option>
<form:option value="Central African Republic">Central African Republic</form:option>
<form:option value="Chad">Chad</form:option>
<form:option value="Chile">Chile</form:option>
<form:option value="China">China</form:option>
<form:option value="Christmas Island">Christmas Island</form:option>
<form:option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</form:option>
<form:option value="Colombia">Colombia</form:option>
<form:option value="Comoros">Comoros</form:option>
<form:option value="Congo">Congo</form:option>
<form:option value="Congo, The Democratic Republic of The">Congo, The Democratic Republic of The</form:option>
<form:option value="Cook Islands">Cook Islands</form:option>
<form:option value="Costa Rica">Costa Rica</form:option>
<form:option value="Cote D'ivoire">Cote D'ivoire</form:option>
<form:option value="Croatia">Croatia</form:option>
<form:option value="Cuba">Cuba</form:option>
<form:option value="Cyprus">Cyprus</form:option>
<form:option value="Czech Republic">Czech Republic</form:option>
<form:option value="Denmark">Denmark</form:option>
<form:option value="Djibouti">Djibouti</form:option>
<form:option value="Dominica">Dominica</form:option>
<form:option value="Dominican Republic">Dominican Republic</form:option>
<form:option value="Ecuador">Ecuador</form:option>
<form:option value="Egypt">Egypt</form:option>
<form:option value="El Salvador">El Salvador</form:option>
<form:option value="Equatorial Guinea">Equatorial Guinea</form:option>
<form:option value="Eritrea">Eritrea</form:option>
<form:option value="Estonia">Estonia</form:option>
<form:option value="Ethiopia">Ethiopia</form:option>
<form:option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</form:option>
<form:option value="Faroe Islands">Faroe Islands</form:option>
<form:option value="Fiji">Fiji</form:option>
<form:option value="Finland">Finland</form:option>
<form:option value="France">France</form:option>
<form:option value="French Guiana">French Guiana</form:option>
<form:option value="French Polynesia">French Polynesia</form:option>
<form:option value="French Southern Territories">French Southern Territories</form:option>
<form:option value="Gabon">Gabon</form:option>
<form:option value="Gambia">Gambia</form:option>
<form:option value="Georgia">Georgia</form:option>
<form:option value="Germany">Germany</form:option>
<form:option value="Ghana">Ghana</form:option>
<form:option value="Gibraltar">Gibraltar</form:option>
<form:option value="Greece">Greece</form:option>
<form:option value="Greenland">Greenland</form:option>
<form:option value="Grenada">Grenada</form:option>
<form:option value="Guadeloupe">Guadeloupe</form:option>
<form:option value="Guam">Guam</form:option>
<form:option value="Guatemala">Guatemala</form:option>
<form:option value="Guinea">Guinea</form:option>
<form:option value="Guinea-bissau">Guinea-bissau</form:option>
<form:option value="Guyana">Guyana</form:option>
<form:option value="Haiti">Haiti</form:option>
<form:option value="Heard Island and Mcdonald Islands">Heard Island and Mcdonald Islands</form:option>
<form:option value="Holy See (Vatican City State)">Holy See (Vatican City State)</form:option>
<form:option value="Honduras">Honduras</form:option>
<form:option value="Hong Kong">Hong Kong</form:option>
<form:option value="Hungary">Hungary</form:option>
<form:option value="Iceland">Iceland</form:option>
<form:option value="India">India</form:option>
<form:option value="Indonesia">Indonesia</form:option>
<form:option value="Iran, Islamic Republic of">Iran, Islamic Republic of</form:option>
<form:option value="Iraq">Iraq</form:option>
<form:option value="Ireland">Ireland</form:option>
<form:option value="Israel">Israel</form:option>
<form:option value="Italy">Italy</form:option>
<form:option value="Jamaica">Jamaica</form:option>
<form:option value="Japan">Japan</form:option>
<form:option value="Jordan">Jordan</form:option>
<form:option value="Kazakhstan">Kazakhstan</form:option>
<form:option value="Kenya">Kenya</form:option>
<form:option value="Kiribati">Kiribati</form:option>
<form:option value="Korea, Democratic People's Republic of">Korea, Democratic People's Republic of</form:option>
<form:option value="Korea, Republic of">Korea, Republic of</form:option>
<form:option value="Kuwait">Kuwait</form:option>
<form:option value="Kyrgyzstan">Kyrgyzstan</form:option>
<form:option value="Lao People's Democratic Republic">Lao People's Democratic Republic</form:option>
<form:option value="Latvia">Latvia</form:option>
<form:option value="Lebanon">Lebanon</form:option>
<form:option value="Lesotho">Lesotho</form:option>
<form:option value="Liberia">Liberia</form:option>
<form:option value="Libyan Arab Jamahiriya">Libyan Arab Jamahiriya</form:option>
<form:option value="Liechtenstein">Liechtenstein</form:option>
<form:option value="Lithuania">Lithuania</form:option>
<form:option value="Luxembourg">Luxembourg</form:option>
<form:option value="Macao">Macao</form:option>
<form:option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</form:option>
<form:option value="Madagascar">Madagascar</form:option>
<form:option value="Malawi">Malawi</form:option>
<form:option value="Malaysia">Malaysia</form:option>
<form:option value="Maldives">Maldives</form:option>
<form:option value="Mali">Mali</form:option>
<form:option value="Malta">Malta</form:option>
<form:option value="Marshall Islands">Marshall Islands</form:option>
<form:option value="Martinique">Martinique</form:option>
<form:option value="Mauritania">Mauritania</form:option>
<form:option value="Mauritius">Mauritius</form:option>
<form:option value="Mayotte">Mayotte</form:option>
<form:option value="Mexico">Mexico</form:option>
<form:option value="Micronesia, Federated States of">Micronesia, Federated States of</form:option>
<form:option value="Moldova, Republic of">Moldova, Republic of</form:option>
<form:option value="Monaco">Monaco</form:option>
<form:option value="Mongolia">Mongolia</form:option>
<form:option value="Montserrat">Montserrat</form:option>
<form:option value="Morocco">Morocco</form:option>
<form:option value="Mozambique">Mozambique</form:option>
<form:option value="Myanmar">Myanmar</form:option>
<form:option value="Namibia">Namibia</form:option>
<form:option value="Nauru">Nauru</form:option>
<form:option value="Nepal">Nepal</form:option>
<form:option value="Netherlands">Netherlands</form:option>
<form:option value="Netherlands Antilles">Netherlands Antilles</form:option>
<form:option value="New Caledonia">New Caledonia</form:option>
<form:option value="New Zealand">New Zealand</form:option>
<form:option value="Nicaragua">Nicaragua</form:option>
<form:option value="Niger">Niger</form:option>
<form:option value="Nigeria">Nigeria</form:option>
<form:option value="Niue">Niue</form:option>
<form:option value="Norfolk Island">Norfolk Island</form:option>
<form:option value="Northern Mariana Islands">Northern Mariana Islands</form:option>
<form:option value="Norway">Norway</form:option>
<form:option value="Oman">Oman</form:option>
<form:option value="Pakistan">Pakistan</form:option>
<form:option value="Palau">Palau</form:option>
<form:option value="Palestinian Territory, Occupied">Palestinian Territory, Occupied</form:option>
<form:option value="Panama">Panama</form:option>
<form:option value="Papua New Guinea">Papua New Guinea</form:option>
<form:option value="Paraguay">Paraguay</form:option>
<form:option value="Peru">Peru</form:option>
<form:option value="Philippines">Philippines</form:option>
<form:option value="Pitcairn">Pitcairn</form:option>
<form:option value="Poland">Poland</form:option>
<form:option value="Portugal">Portugal</form:option>
<form:option value="Puerto Rico">Puerto Rico</form:option>
<form:option value="Qatar">Qatar</form:option>
<form:option value="Reunion">Reunion</form:option>
<form:option value="Romania">Romania</form:option>
<form:option value="Russian Federation">Russian Federation</form:option>
<form:option value="Rwanda">Rwanda</form:option>
<form:option value="Saint Helena">Saint Helena</form:option>
<form:option value="Saint Kitts and Nevis">Saint Kitts and Nevis</form:option>
<form:option value="Saint Lucia">Saint Lucia</form:option>
<form:option value="Saint Pierre and Miquelon">Saint Pierre and Miquelon</form:option>
<form:option value="Saint Vincent and The Grenadines">Saint Vincent and The Grenadines</form:option>
<form:option value="Samoa">Samoa</form:option>
<form:option value="San Marino">San Marino</form:option>
<form:option value="Sao Tome and Principe">Sao Tome and Principe</form:option>
<form:option value="Saudi Arabia">Saudi Arabia</form:option>
<form:option value="Senegal">Senegal</form:option>
<form:option value="Serbia and Montenegro">Serbia and Montenegro</form:option>
<form:option value="Seychelles">Seychelles</form:option>
<form:option value="Sierra Leone">Sierra Leone</form:option>
<form:option value="Singapore">Singapore</form:option>
<form:option value="Slovakia">Slovakia</form:option>
<form:option value="Slovenia">Slovenia</form:option>
<form:option value="Solomon Islands">Solomon Islands</form:option>
<form:option value="Somalia">Somalia</form:option>
<form:option value="South Africa">South Africa</form:option>
<form:option
        value="South Georgia and The South Sandwich Islands">South Georgia and The South Sandwich Islands</form:option>
<form:option value="Spain">Spain</form:option>
<form:option value="Sri Lanka">Sri Lanka</form:option>
<form:option value="Sudan">Sudan</form:option>
<form:option value="Suriname">Suriname</form:option>
<form:option value="Svalbard and Jan Mayen">Svalbard and Jan Mayen</form:option>
<form:option value="Swaziland">Swaziland</form:option>
<form:option value="Sweden">Sweden</form:option>
<form:option value="Switzerland">Switzerland</form:option>
<form:option value="Syrian Arab Republic">Syrian Arab Republic</form:option>
<form:option value="Taiwan, Province of China">Taiwan, Province of China</form:option>
<form:option value="Tajikistan">Tajikistan</form:option>
<form:option value="Tanzania, United Republic of">Tanzania, United Republic of</form:option>
<form:option value="Thailand">Thailand</form:option>
<form:option value="Timor-leste">Timor-leste</form:option>
<form:option value="Togo">Togo</form:option>
<form:option value="Tokelau">Tokelau</form:option>
<form:option value="Tonga">Tonga</form:option>
<form:option value="Trinidad and Tobago">Trinidad and Tobago</form:option>
<form:option value="Tunisia">Tunisia</form:option>
<form:option value="Turkey">Turkey</form:option>
<form:option value="Turkmenistan">Turkmenistan</form:option>
<form:option value="Turks and Caicos Islands">Turks and Caicos Islands</form:option>
<form:option value="Tuvalu">Tuvalu</form:option>
<form:option value="Uganda">Uganda</form:option>
<form:option value="Ukraine">Ukraine</form:option>
<form:option value="United Arab Emirates">United Arab Emirates</form:option>
<form:option value="United Kingdom">United Kingdom</form:option>
<form:option value="United States">United States</form:option>
<form:option value="United States Minor Outlying Islands">United States Minor Outlying Islands</form:option>
<form:option value="Uruguay">Uruguay</form:option>
<form:option value="Uzbekistan">Uzbekistan</form:option>
<form:option value="Vanuatu">Vanuatu</form:option>
<form:option value="Venezuela">Venezuela</form:option>
<form:option value="Viet Nam">Viet Nam</form:option>
<form:option value="Virgin Islands, British">Virgin Islands, British</form:option>
<form:option value="Virgin Islands, U.S.">Virgin Islands, U.S.</form:option>
<form:option value="Wallis and Futuna">Wallis and Futuna</form:option>
<form:option value="Western Sahara">Western Sahara</form:option>
<form:option value="Yemen">Yemen</form:option>
<form:option value="Zambia">Zambia</form:option>
<form:option value="Zimbabwe">Zimbabwe</form:option>
</form:select>
<spring:bind path="payerCountry">
    <c:if test="${status.error}">
        <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
             src="http://static.eaw1805.com/images/site/error.jpeg"
             width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
    </c:if>
</spring:bind>
</div>


<label class='submit_btn'
       style="padding-top: 20px;">
    <input name="lang" value="en" type="hidden">
    <input name="agreed" value="true" type="hidden">
    <input name="change_lang" value="0" type="hidden">
    <input name="submit"
           type="image"
           src="http://static.eaw1805.com/images/site/paypal/RequestReceiptoff.png"
           onmouseover="this.src='http://static.eaw1805.com/images/site/paypal/RequestReceipthov.png'"
           onmouseout="this.src='http://static.eaw1805.com/images/site/paypal/RequestReceiptoff.png'"
           style="font-size: 22px;
                                  padding-left: 0px;
                                  width: 321px;
                                  height: 56px;"
           tabindex="6"
           type="submit"
           value="Request Receipt"/>
</label>

<div style="padding-top: 20px;
                    clear: both;
                    width: 450px;
                    font-size: 14px;
                    text-align: justify;">
    We will send you the payment receipt via Standard Postal Services.</strong>
</div>
</div>
</form:form>
</div>
</div>
