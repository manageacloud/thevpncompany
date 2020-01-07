<#assign security = JspTaglibs["http://www.springframework.org/security/tags"]>
<#macro css_framework>
    <#compress>
    <style amp-custom>

<#--


The VPN Company Framework


-->

    .section-main {
    padding-top: 100px;
    padding-bottom: 100px;
    }
    .section-main h1 {
    font-size: 34px;
    }
    .section-main p.lead {
    font-size: 19px;
    }
    .section-main p {
    font-size: 16px;
    }
    .section-blue  {
    background-color: #4582ec;
    color: white;
    }
    .section-blue .b-p {
    color: #ffffff;
    background-color: #4582ec;
    border-color: #ffffff;
    }
    .section-blue .b-d {
        background-color: #4582ec;
    }
    .section-blue a {
        color: #ffffff;
    }
    .section-blue .b-d {
        color: #ffffff;
        border-color: #ffffff;
    }
    ,green {
        color: green;
     }

     div.isvpn {
         padding: 0.20rem 1.25rem;
         font-size: 0.75em;
     }
     .font-14 {
         font-size: 14px;
     }

<#--

    framework css

-->
    body {
    font-family: "Raleway";
    color: rgb(51, 51, 51);
    }
    .logo {
    float: left;
    }
    .logo amp-img {
        margin-top: 8px;
    }

    .menu-burger {
    float: right;
    width: 30px;
    height: 30px;
    margin-top: 14px;
    margin-right: 19px;
    }

    a {
    color: #447CC3;
    text-decoration: none;
    }

    h2 {
        font-size: 20px;
    }

<#--  navbar  -->
    .n-c ul {
    padding-right: 50px;
    }
    .n-c ul li {
    list-style-type: none;
    }
    .n-c ul li a {
    line-height: 36px;
    }
<#--  container  -->
    .c{
    margin-right: auto;
    margin-left: auto;
    padding-left: 15px;
    padding-right: 15px;
    }
<#--  container fluid   -->
    .c-f {
    clear:both;
    margin-right: auto;
    margin-left: auto;
    padding-left: 15px;
    padding-right: 15px;

    }

<#-- list style -->
    .l-none {
        padding: 0;
    }

    .l-none li {
        list-style-type: none;
    }

<#--  text center  -->
    .t-c {
    text-align: center;
    }
    .t-j {
    text-align: justify;
    }

    footer {
        background-color: #4582ec;
        height: 100px;
        text-align: center;
        color: white;
        padding-top: 30px;
        padding-bottom: 30px;
        clear:both;
    }
    footer a {
    color: white;
    margin-left: 15px;
    margin-right: 15px;
    }

<#-- buttons -->
    button.b {
        font-size: 16px;
    }
    .b {
    cursor: pointer;
    border: 1px solid transparent;
    display: inline-block;
    padding: 6px 1%;
    }

    button.disabled, a.disabled {
        color:gray;
        border: 1px solid gray;
    }

    .b-paragraph {
        margin-left: 8px;
        margin-right: 8px;
    }

<#-- btn-primary: b-p -->
    .b-p {
    background-color: #4582ec;
    border-color: #4582ec;
    color: white;
    }
<#-- btn-default: b-d -->
    .b-d {
        color: #4582ec;
        border-color: #4582ec;
        background-color: #ffffff;
    }
<#-- btn-lg: b-l -->
    .b-l {
        width: 100%;
        text-align: center;
        padding-top: 15px;
        padding-bottom: 15px;
    }

<#-- btn-block: b-b -->
    .b-b {
        width: 100%;
        padding-left: 0;
        padding-right: 0;
        text-align: center;
    }



    <#-- a: alert a-d: alert-danger -->
    .a {
        position: relative;
        padding: .75rem 1.25rem;
        margin-bottom: 1rem;
        border: 1px solid transparent;
        border-radius: .25rem;
    }

    .a-d {
        color: #721c24;
        background-color: #f8d7da;
        border-color: #f5c6cb;
    }

    <#-- alert-info: a-i-->
    .a-i {
        color: rgba(69, 130, 236);
        background-color: rgba(69, 130, 236, 0.05);
        border-color: rgba(69, 130, 236, 0.50);
    }

    <#-- alert-success: a-i-->
    .a-s {
        color: #155724;
        background-color: #d4edda;;
        border-color: #c3e6cb;
    }


.clear {
        clear:both;
    }


<#-- visibility hidden-xs: h-xs, visible-xs: v-xs-->
    .h-xs {
    display: none;
    }

    .v-xs {
    display: initial;
    }

<#-- text text-bold: t-b-->
    .t-b {
    font-weight: bold;
    }
<#-- text strikethough-->
    .t-s {
        text-decoration: line-through;
    }
<#-- forms. form-control f-c-->
    .f-c::placeholder {
        color: #c4c4c4;
        opacity: 1;
    }
    .f-c::placeholder {
        color: ##c4c4c4;
        opacity: 1;
    }
    .f-c {
        -webkit-box-sizing: border-box; /* Safari/Chrome, other WebKit */
        -moz-box-sizing: border-box;    /* Firefox, other Gecko */
        box-sizing: border-box;         /* Opera/IE 8+ */
        display: block;
        width: 100%;
        height: calc(1.5em + .75rem + 2px);
        padding: .375rem 15px;
        font-size: 1rem;
        font-weight: 400;
        line-height: 1.5;
        color: #495057;
        background-color: #fff;
        background-clip: padding-box;
        border: 1px solid #ced4da;
        border-radius: .25rem;
        transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;
    }

    <#--  row  -->
    .r {
        <#--
        margin-left: -15px;
        margin-right: -15px;
        -->
        clear:both;
        display: -ms-flexbox;
        display: flex;
        -ms-flex-wrap: wrap;
        flex-wrap: wrap;
    }
    .readdable {
        max-width: 600px;
        text-align: justify;
    }

<#-- columns -->
    .c-l-4,.c-l-6, .c-l-12, {
        padding-left: 15px;
        padding-right: 15px;
    }
    .c-l-4, .c-l-6, .c-l-12 {
        width: 100%;
    }

<#-- mobiles xs -->
    @media (min-width: 380px) {
        .b-l {
            padding: 15px 85px;
            width: auto;
        }
        .b-b {
            padding: 15px 0;
            width: 100%;
        }
    }

<#-- mobiles sm -->
    @media (min-width: 768px) {
        .c-l-4 {
            -ms-flex-preferred-size: 0;
            flex-basis: 0;
            -ms-flex-positive: 4;
            flex-grow: 4;
            max-width: 100%;
            padding: 0 15px;
        }


        .c {
        width: 750px;
        }

    <#--  navbar  -->
        .n-c ul {
        float: right;       <#-- menu left of right -->
        padding-left: 0;
        padding-right: 0;
        }
        .n-c ul li {
        float: left;
        width: auto;
        margin-right: 25px;
        list-style-type: none;
        }
        .n-c ul li a {
        line-height: 36px;
        }
        .n-c a.get-started {
        float: right;
        line-height: 26px;
        margin-right: 0;
        margin-top: 13px;
        }

    <#-- visibility -->
        .h-xs {
        display: initial;
        }

        .v-xs {
        display: none;
        }

    }
<#-- tablet md -->
    @media (min-width: 992px) {
    .c{
    width: 970px;
    }
    }
<#-- desktop lg -->
    @media (min-width: 1200px) {

        .c-l-6 {
            -ms-flex-preferred-size: 0;
            flex-basis: 0;
            -ms-flex-positive: 6;
            flex-grow: 6;
            max-width: 100%;
        }
        .c-l-6:not(:last-child){
            margin-right: 15px;
        }
        .c-l-o-3 {
            margin-left: 25%;
        }


    }

    </#compress>
<#nested />


    </style>
</#macro>

<#macro footer>
    <footer>
<#--        <p>-->
<#--            <a href="/about">About</a>-->
<#--            <a href="/support">Contact Us</a>-->
<#--            <a href="/docs">Disclaimer</a>-->
<#--        <p>-->
        <p>
            <span class="h-xs">©${.now?string('yyyy')} <a href="https://manageacloud.com">ManageaCloud</a> Services Pty Ltd</span>
            <#--<a href="/privacy-policy">Privacy Policy</a>-->
            <#--<a href="/terms-and-conditions">Terms and Conditions</a>-->
        </p>

    </footer>
</#macro>

<#macro menu>

    <@security.authorize access="isAuthenticated()">
        <ul>
            <li><a href="/dashboard">Devices</a></li>
            <li><a href="/dashboard/location">Locations</a></li>
            <li><a href="/dashboard/support">Need Help ?</a></li>
            <li><a href="/dashboard/account">My Account</a></li>
        </ul>
    </@security.authorize>

    <@security.authorize access="! isAuthenticated()">
        <a class="b b-p get-started" href="/get-started">GET STARTED</a>
        <ul>
            <li><a href="/what-is-vpn">What is VPN</a></li>
            <li><a href="/products">Products</a></li>
            <li><a href="/support">Support</a></li>
            <li><a href="/login">My Account</a></li>
        </ul>
    </@security.authorize>

</#macro>

<#macro html head extra_css="" extra_js="" schema="" is_amp=true>
<!doctype html>
<html <#if is_amp>⚡</#if>>
<head>
    <meta charset="utf-8">
    ${head}
    <meta name="viewport" content="width=device-width,minimum-scale=1,initial-scale=1">
    <link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Raleway' type='text/css' media='all' />
    <link rel="canonical" href="${domain}${path_info}">

    <@css_framework>
        ${extra_css}
    </@css_framework>

<#--    <script type="application/ld+json">-->
<#--        {-->
<#--            "@context": "http://schema.org",-->
<#--            "@type": "NewsArticle",-->
<#--            "headline": "Article headline",-->
<#--        }-->
<#--    </script>-->
    <style amp-boilerplate>body{-webkit-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-moz-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-ms-animation:-amp-start 8s steps(1,end) 0s 1 normal both;animation:-amp-start 8s steps(1,end) 0s 1 normal both}@-webkit-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-moz-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-ms-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-o-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}</style><noscript><style amp-boilerplate>body{-webkit-animation:none;-moz-animation:none;-ms-animation:none;animation:none}</style></noscript>

    <#--could we just load amp sidebar ??-->
    <script async src="https://cdn.ampproject.org/v0.js"></script>
    <#if !is_dev>
        <script async custom-element="amp-analytics" src="https://cdn.ampproject.org/v0/amp-analytics-0.1.js"></script>
    </#if>

    ${extra_js}
    <#--<script async custom-element="amp-sidebar" src="https://cdn.ampproject.org/v0/amp-sidebar-0.1.js"></script>-->
    <script async custom-element="amp-sidebar" src="https://cdn.ampproject.org/v0/amp-sidebar-0.1.js"></script>
</head>

<body>

    <#if isvpn>
        <div class="isvpn a a-s t-c">
            <span class="h-xs"><b>VPN UP</b>: You are protected by <b>TheVPNCompany</b></span>
            <span class="v-xs"><b>VPN UP</b>: You are protected</span>
        </div>
    </#if>

    <div class="c">
        <a href="/" class="logo">
            <amp-img width="206" height="50"  src="/static/the-vpn-company-logo.png"/>
        </a>

        <nav class="n-c h-xs">
            <@menu/>
        </nav>
    </div>

    <div role="button" tabindex="1" class="menu-burger v-xs" on="tap:sidebar.toggle">
        <svg id="menu" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="100%" height="100%">
            <path d="M2.133 13.867h59.733c1.178 0 2.133 0.955 2.133 2.133v2.133c0 1.178-0.955 2.133-2.133 2.133h-59.733c-1.178 0-2.133-0.955-2.133-2.133v-2.133c0-1.178 0.955-2.133 2.133-2.133z"></path>
            <path d="M2.133 28.8h59.733c1.178 0 2.133 0.955 2.133 2.133v2.133c0 1.178-0.955 2.133-2.133 2.133h-59.733c-1.178 0-2.133-0.955-2.133-2.133v-2.133c0-1.178 0.955-2.133 2.133-2.133z"></path>
            <path d="M2.133 43.733h59.733c1.178 0 2.133 0.955 2.133 2.133v2.133c0 1.178-0.955 2.133-2.133 2.133h-59.733c-1.178 0-2.133-0.955-2.133-2.133v-2.133c0-1.178 0.955-2.133 2.133-2.133z"></path>
        </svg>
    </div>


    <amp-sidebar id="sidebar"
                 class="sample-sidebar"
                 layout="nodisplay"
                 side="left">

        <nav class="n-c">
            <@menu/>
        </nav>

    </amp-sidebar>



<#nested />
<@footer/>
<#if !is_dev>
    <amp-analytics type="gtag" data-credentials="include">
        <script type="application/json">
            {
                "vars" : {
                    "gtag_id": "UA-48211069-2",
                    "config" : {
                        "UA-48211069-2": { "groups": "default" }
                    }
                }
            }
        </script>
    </amp-analytics>
</#if>
</body>
</html>
</#macro>
