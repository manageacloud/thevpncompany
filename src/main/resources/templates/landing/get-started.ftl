<#import "amp_framework.ftl" as amp />
<#import "parts/_chooseplan.ftl" as p />
<#assign tokenBug = _csrf.token>

<#assign schema>
<script type="application/ld+json">

    {
        "@context": "http://schema.org",
        "@type": "SoftwareApplication",
        "@id": "https://thevpncompany.com.au/get-started",
        "name": "TheVPNCompany - VPN Services specialized in Australia",
        "operatingSystem": "all",
        "url": "https://thevpncompany.com.au",
        <#--"aggregateRating": {
            "@type": "AggregateRating",
            "ratingValue": "4.53",
            "reviewCount": "3"
        },-->
        "offers": {
            "@type": "AggregateOffer",
            "offeredBy": {
                "@type": "Organization",
                "name":"TheVPNCompany"
            },
            "highPrice": "11.95",
            "lowPrice": "7.95",
            "priceCurrency": "AUD",
            "priceSpecification": [
                {
                    "@type": "UnitPriceSpecification",
                    <#--"price": "11.95",-->
                    "price": "5.95",
                    "priceCurrency": "AUD",
                    "name": "TheVPNCompany Monthly Subscription",
                    "referenceQuantity": {
                        "@type": "QuantitativeValue",
                        "value": "1",
                        "unitCode": "MON"
                    }
                },
                {
                    "@type": "UnitPriceSpecification",
                    <#--"price": "59.70",-->
                    "price": "29.70",
                    "priceCurrency": "AUD",
                    "name": "TheVPNCompany 6 Months Subscription",
                    "referenceQuantity": {
                        "@type": "QuantitativeValue",
                        "value": "6",
                        "unitCode": "MON"
                    }
                },
                {
                    "@type": "UnitPriceSpecification",
                    <#--"price": "95.40",-->
                    "price": "47.40",
                    "priceCurrency": "AUD",
                    "name": "TheVPNCompany Yearly Subscription",
                    "referenceQuantity": {
                        "@type": "QuantitativeValue",
                        "value": "1",
                        "unitCode": "ANN"
                    }
                }
            ]
        },
        "creator": {
           "@type":"Organization",
           "name":"TheVPNCompany",
           "url":"https://www.thevpncompany.com.au",
           "logo":"https://www.thevpncompany.com.au/static/the-vpn-company-logo.png",
        }
    }

</script>
</#assign>

<#assign extra_css>
    ${p.choosePlanCss}

    .step {
        margin-top: 50px;
    }

    input.cc {
        width: 5rem;
        display: inline;
    }

    select.month {
        width: 120px;
        display: inline;
    }

    select.year {
        width: 120px;
        display: inline;
    }

    .control-group {
        margin-bottom: 10px;
    }

    button[type=submit] {
        font-size:16px;
        border-radius: .25rem;
    }
    label {
    margin-bottom: 5px;
    display: block;
    }
</#assign>

<#assign extra_js>
    <script async custom-element="amp-selector" src="https://cdn.ampproject.org/v0/amp-selector-0.1.js"></script>
    <script async custom-element="amp-form" src="https://cdn.ampproject.org/v0/amp-form-0.1.js"></script>
    <script async custom-element="amp-bind" src="https://cdn.ampproject.org/v0/amp-bind-0.1.js"></script>
    <script async custom-template="amp-mustache" src="https://cdn.ampproject.org/v0/amp-mustache-0.2.js"></script>
</#assign>


<#assign head>
    <title>Sign Up | TheVPNCompany</title>
    ${p.headerPlanCss}
</#assign>

<@amp.html head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Choose your TheVPNCompany plan</h1>
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <form method="post"
              action-xhr="/get-started/xhr"
              on="submit:submit-wrapper.hide;submit-error:submit-wrapper.show"
              <#--verify-xhr="/get-started/action-xhr"-->
              target="_top" id="new_user">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="r ">
                <div class="c-l-12">
                    <h2>1 - Select a plan that works for you</h2>
                    <@p.choosePlanForm/>
                </div>
            </div>
            <div class="r step readdable">
                <div class="c-l-12">
                    <h2>2 - Enter your email address</h2>
                    <p class="fine-print">Privacy guarantee: We do not share your information and will contact you only as needed to provide our service. </p>
                    <label>
                        <input class="f-c" name="name" pattern="\w+ \w+.*" title="First and last name" placeholder="First and last name"  type="text">
                    </label>
                    <label>
                        <input class="f-c"
                               placeholder="your.email@yourcompany.com"
                                name="email"
                               type="text"
                               <#--on="change:new_user.submit"-->>
                    </label>
                </div>
            </div>
            <div class="r step readdable">
                <div class="c-l-12">
                    <div id="credit_card_form">
                        <h2>3 - Select your  preferred method of payment</h2>

                        <@p.creditCardForm/>
                    </div>

                    <@p.submitButtonForm label="Join Now"/>

                </div>
            </div>
        </form>
    </section>
</@amp.html>

