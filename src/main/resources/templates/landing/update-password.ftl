<#import "dashboard/dashboard_template.ftl" as t />
<#import "parts/_password.ftl" as p />
<#assign tokenBug = _csrf.token>

<#assign extra_css>
    ${p.updatePasswordCss}
</#assign>

<#assign extra_js>
    <script async custom-element="amp-selector" src="https://cdn.ampproject.org/v0/amp-selector-0.1.js"></script>
    <script async custom-element="amp-form" src="https://cdn.ampproject.org/v0/amp-form-0.1.js"></script>
    <script async custom-element="amp-bind" src="https://cdn.ampproject.org/v0/amp-bind-0.1.js"></script>
    <script async custom-template="amp-mustache" src="https://cdn.ampproject.org/v0/amp-mustache-0.2.js"></script>
</#assign>

<#assign head>
    <title>Update your password | TheVPNCompany</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <form method="post"
                          action-xhr="/update-password/xhr"
                          on="submit:submit-wrapper.hide;submit-error:submit-wrapper.show;"
                          target="_top" id="login">
                        <input type="hidden" name="${_csrf.parameterName}" value="${tokenBug}"/>
                        <input type="hidden" name="token" value="${token}"/>
                        <@p.updatePassword/>
                    </form>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>
