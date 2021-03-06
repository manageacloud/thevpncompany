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
    <title>Login | TheVPNCompany</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <form method="post"
                          action-xhr="/login/xhr"
                          target="_top" id="login">
                        <input type="hidden" name="${_csrf.parameterName}" value="${tokenBug}"/>
                        <div class="login">
                            <h1>Sign In</h1>
                            <div class="control-group">
                                <label>Email</label>
                                <input class="f-c user-valid valid" autocomplete="username" name="username" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" title="First and last name" placeholder="Email" type="email">
                            </div>
                            <div class="control-group">
                                <label>Password</label>
                                <input class="f-c user-valid valid" autocomplete="current-password" name="password" pattern="\w+" title="First and last name" placeholder="Password" type="password">
                            </div>
                            <div submitting>
                                <template type="amp-mustache">
                                    <div class="a a-i t-c" role="alert">
                                        Form submitting... Thank you for waiting.
                                    </div>
                                </template>
                            </div>
                            <div submit-success>
                                <template type="amp-mustache">
                                    Logging completed.<br>
                                    To continue, please access the <a class="b b-d" href="/dashboard">dashboard</a>
                                </template>
                            </div>
                            <div submit-error>
                                <template type="amp-mustache">
                                    <div class="a a-d t-c" role="alert">
                                        Oops! {{message}}
                                    </div>
                                </template>
                            </div>
                            <div id="submit-wrapper" class="r control-group" >
                                <div class="c-l-12">
                                    <button type="submit" class="b b-p b-l b-b">Sign In</button>
                                </div>
                            </div>
                            <div class="t-c font-14 more-options">
                                <p>
                                    <a href="/reset-password">Forgot your password</a>
                                    <span class="separator">|</span>
                                    <a href="/support">Need Help ?</a>
                                </p>
                                <p>New User?<br><a href="/get-started">Get The<b>VPN</b>Company</a></p>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>
