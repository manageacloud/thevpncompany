<#import "../dashboard_template.ftl" as t />
<#import "../../parts/_chooseplan.ftl" as p />

<#assign extra_css>
    ${p.choosePlanCss}

    .step {
        margin-top: 50px;
    }

    .control-group {
        margin-bottom: 10px;
    }

    button[type=submit] {
        font-size:16px;
    }
    label {
        margin-bottom: 5px;
        display: block;
    }

    .margin-bottom-button {
        margin-bottom: 150px;
    }
</#assign>

<#assign extra_js>
    <script async custom-element="amp-selector" src="https://cdn.ampproject.org/v0/amp-selector-0.1.js"></script>
    <script async custom-element="amp-form" src="https://cdn.ampproject.org/v0/amp-form-0.1.js"></script>
    <script async custom-element="amp-bind" src="https://cdn.ampproject.org/v0/amp-bind-0.1.js"></script>
    <script async custom-template="amp-mustache" src="https://cdn.ampproject.org/v0/amp-mustache-0.2.js"></script>
</#assign>


<#assign head>
    <title>Cancel The<b>VPN</b>Company Plan</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <form method="post"
              action="/dashboard/account/cancel/xhr"
              action-xhr="/dashboard/account/cancel/xhr"
              on="submit:submit-wrapper.hide;submit-error:submit-wrapper.show"
              target="_top" id="new_user">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="r readdable">
                <div class="c-l-12">
                    <h2>We are sorry to see you go !</h2>
                    <p>The<b>VPN</b>Company protects your privacy on Internet. If you cancel the subscription,
                        you won't be protected any more!</p>
                </div>
            </div>
            <div class="r step readdable">
                <div class="c-l-12">
                    <div submitting>
                        <template type="amp-mustache">
                            <div class="a a-i t-c" role="alert">
                                Cancelling subscription ... Thank you for waiting
                            </div>
                        </template>
                    </div>
                    <div submit-success>
                        <template type="amp-mustache">
                            Your subscription has been successfully cancelled<br>
                        </template>
                    </div>
                    <div submit-error>
                        <template type="amp-mustache">
                            <div class="a a-d t-c" role="alert">
                                Oops! {{message}}
                            </div>
                        </template>
                    </div>

                    <div id="submit-wrapper" class="r control-group">
                        <div class="c-l-12">
                            <button type="submit" class="b b-d">Cancel Subscription</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        </div>
    </section>
</@t.dashboardTemplate>

