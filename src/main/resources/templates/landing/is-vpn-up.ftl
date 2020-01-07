<#import "dashboard/dashboard_template.ftl" as t />
<#assign tokenBug = _csrf.token>

<#assign extra_css>
    .control-group {
        margin-bottom: 20px;
    }

    button[type=submit] {
        font-size:16px;
        border-radius: .25rem;
    }
    label {
        margin-bottom: 5px;
        display: block;
    }

    .login {
        max-width: 300px;
        margin-top: 50px;
    }

    .separator {
        margin-left: 10px;
        margin-right: 10px;
    }

    .more-options {
        margin-top: 35px;
    }

    @media (min-width: 380px) {
        .login {
            box-shadow: 0px 2px 5px 0px rgba(0,0,0,0.08),0px 5px 50px 0px rgba(0,0,0,0.08);
            margin: 68px auto;
            padding: 52px 47px 63px;

        }
    }

</#assign>

<#assign extra_js>
    <script async custom-element="amp-selector" src="https://cdn.ampproject.org/v0/amp-selector-0.1.js"></script>
    <script async custom-element="amp-form" src="https://cdn.ampproject.org/v0/amp-form-0.1.js"></script>
    <script async custom-element="amp-bind" src="https://cdn.ampproject.org/v0/amp-bind-0.1.js"></script>
    <script async custom-template="amp-mustache" src="https://cdn.ampproject.org/v0/amp-mustache-0.2.js"></script>
</#assign>

<#assign head>
    <title>Is your VPN up? | TheVPNCompany</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <div class="login">
                        <h1>Is your VPN up?</h1>
                        <#if vpn_ip??>
                            <div class="a a-s" role="alert">
                                Your VPN is up.
                            </div>
                            <p>You can <a href="/dashboard">configure more devices</a>.</p>
                        <#else>
                            <div class="a a-d t-c" role="alert">
                                You VPN is not up yet.
                            </div>
                            <ul class="l-none">
                                <li>- Did you <a href="/get-started">sign-up</a>?</li>
                                <li>- Did you <a href="/dashboard">configured your device</a>?</li>
                                <li>- Is this page loaded using the device that you are testing?</li>
                            </ul>

                        </#if>
                        <p>If you have additional problems or questions, please contact <a href="/support">support</a>.</p>
                    </div>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>
