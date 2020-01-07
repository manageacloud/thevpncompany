<#import "../dashboard_template.ftl" as t />
<#import "../../parts/_chooseplan.ftl" as p />

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
    <title>Get TheVPNCompany</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <form method="post"
              action="/dashboard/account/cc/xhr"
              action-xhr="/dashboard/account/cc/xhr"
              on="submit:submit-wrapper.hide;submit-error:submit-wrapper.show"
              target="_top" id="new_user">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="r readdable">
                <div class="c-l-12">
                    <h2>Change your credit card</h2>
                    <@p.creditCardForm/>
                </div>
            </div>
            <div class="r step readdable">
                <div class="c-l-12">
                    <@p.submitButtonForm label="Update Credit Card" css="margin-bottom-button"/>
                </div>
            </div>
        </form>
        </div>
    </section>
</@t.dashboardTemplate>

