<#assign headerPlanCss>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Proxima" type="text/css" media='all'>
</#assign>

<#assign choosePlanCss>
    .price-box {
        color: #808080;
        margin-bottom: 1rem;
        margin: 15px 7px;
        background-color: rgba(69, 130, 236, 0.05);
    }
    .font-number {
        font-family: "Proxima";
    }
    .price-box h3 {
        color: rgb(51, 51, 51);
    }
    .price-box .original-price {
        margin-top: 35px;
        text-decoration: line-through;
        line-height: 0;
    }
    .price-box .price {
        font-size: 28px;
        font-weight: bold;
        line-height: 0.5;
        margin-bottom: 48px;
    }
    .fine-print {
        font-size: 12px;
        line-height: 1rem;
    }
    .plan-selector [option][selected] {
        outline: 7px solid #4e995f;
    }
</#assign>

<#macro choosePlanForm selectedPlan=3>
    <div class="a a-d t-c" role="alert">
        For limited time <b>50% OFF</b> in all our plans
    </div>


    <amp-selector layout="container" name="plan" class="plan-selector">
        <div class="r font-number">
            <div class="price-box c-l-4 t-c" option="1" <#if selectedPlan == 1>selected</#if>  on="tap:credit_card_form.show">
                <h3>1 Month</h3>
                <p class="original-price">$11.95 AUD</p>
                <p class="price">$5.95 AUD</p>
                <p class="fine-print">Billed <span class="t-s">$11.95</span> $5.95<br>every month<p>
                <p class="fine-print">30-day money-back guarantee</p>
            </div>
            <div class="price-box c-l-4 t-c" option="3" <#if selectedPlan == 3>selected</#if>  on="tap:credit_card_form.show">
                <h3>12 Months</h3>
                <p class="original-price">$7.95 AUD</p>
                <p class="price">$3.95 AUD</p>
                <p class="fine-print">Billed <span class="t-s">$95.4</span> $47.4<br>every 12 months<p>
                <p class="fine-print">30-day money-back guarantee</p>
            </div>
            <div class="price-box c-l-4 t-c" option="5" <#if selectedPlan == 5>selected</#if> on="tap:credit_card_form.hide">
                <h3>Forever</h3>
                <p class="original-price"><br></p>
                <p class="price">FREE</p>
                <p class="fine-print">500MB per month<p>
            </div>
        </div>
    </amp-selector>

    <div class="a a-i t-c clear ">
        All prices are in Australian Dollars
    </div>
</#macro>

<#macro submitButtonForm label css="">

    <div submitting>
        <template type="amp-mustache">
            <div class="a a-i t-c" role="alert">
                Form submitting... Thank you for waiting.
            </div>
        </template>
    </div>
    <div submit-success>
        <template type="amp-mustache">
            Success! Thanks for subscribing!<br>
            To continue, please access the <a class="b b-d" href="/user/dashboard">dashboard</a>
        </template>
    </div>
    <div submit-error>
        <template type="amp-mustache">
            <div class="a a-d t-c" role="alert">
                Oops! {{message}}
            </div>
        </template>
    </div>

    <div id="submit-wrapper" class="r control-group ${css} ">
        <div class="c-l-12">
            <button type="submit" class="b b-p b-l b-b">${label}</button>
        </div>
    </div>

</#macro>


<#macro creditCardForm>

    <#--<#assign is_dev=false>-->

    <div class="control-group">
        <label>Address</label>
        <input class="f-c" name="address" title="Address" placeholder="Unit 1 / Sydney Street"  type="text">

    </div>
    <div class="control-group">
        <div class="r">
            <div class="c-l-6">
                <label>City</label>
                <input class="f-c" name="city" pattern="\w+.*" title="City" placeholder="Randwick"  type="text">
            </div>
            <div class="c-l-6">
                <label>Country</label>
                <input class="f-c" name="country" pattern="\w+.*" title="Country" placeholder="Australia"  type="text">
            </div>
        </div>
    </div>
    <div class="control-group">
        <label>Card Number</label>
        <div class="controls">
            <div class="r">
                <div class="c-l-12">
                    <input class="f-c cc" name="cc1" autocomplete="off" maxlength="4" pattern="\d{4}" title="First four digits"   type="text" <#if is_dev>value="5520"</#if> placeholder="0000">
                    <input class="f-c cc" name="cc2" autocomplete="off" maxlength="4" pattern="\d{4}" title="Second four digits"  type="text" <#if is_dev>value="0000"</#if> placeholder="0000">
                    <input class="f-c cc" name="cc3" autocomplete="off" maxlength="4" pattern="\d{4}" title="Third four digits"   type="text" <#if is_dev>value="0000"</#if> placeholder="0000">
                    <input class="f-c cc" name="cc4" autocomplete="off" maxlength="4" pattern="\d{4}" title="Fourth four digits"  type="text" <#if is_dev>value="0000"</#if> placeholder="0000">
                </div>
                <div class="c-x-12 has-error">
                    <#if (errors["cc1"])??><span class="help-block">${errors["cc1"]}</span></#if>
                    <#if (errors["cc2"])??><span class="help-block">${errors["cc2"]}</span></#if>
                    <#if (errors["cc3"])??><span class="help-block">${errors["cc3"]}</span></#if>
                    <#if (errors["cc4"])??><span class="help-block">${errors["cc4"]}</span></#if>
                </div>
            </div>
        </div>
    </div>
    <div class="r control-group">
        <div class="c-l-12">
            <label>Card Expiry Date</label>
            <div class="r">
                <div class="c-l-12">
                    <select class="f-c month" name="month">
                        <option value="">Month</option>
                        <option value="01" >January</option>
                        <option value="02">February</option>
                        <option value="03">March</option>
                        <option value="04">April</option>
                        <option value="05" <#if is_dev>selected="selected"</#if>>May</option>
                        <option value="06">June</option>
                        <option value="07">July</option>
                        <option value="08">August</option>
                        <option value="09">September</option>
                        <option value="10">October</option>
                        <option value="11">November</option>
                        <option value="12">December</option>
                    </select>

                    <select class="f-c year" name="year">
                        <option value="">Year</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24" <#if is_dev>selected</#if>>24</option>
                        <option value="25">25</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="r">
            <div class="c-l-12 has-error">
                <#if (errors["month"])??><span class="help-block">${errors["month"]}</span></#if>
                <#if (errors["year"])??><span class="help-block">${errors["year"]}</span></#if>
            </div>
        </div>
    </div>
    <div class="r control-group">
        <div class="c-l-12">
            <label>Card CVV</label>
            <div class="r">
                <div class="c-l-12">
                    <input class="f-c cc" name="cvv" autocomplete="off" maxlength="3" pattern="\d{3}" title="Three digits at back of your card"  type="text" placeholder="000" <#if is_dev>value="123"</#if>>
                    <#if (errors["cvv"])??><span class="help-block">${errors["cvv"]}</span></#if>
                </div>
            </div>
        </div>
    </div>
</#macro>