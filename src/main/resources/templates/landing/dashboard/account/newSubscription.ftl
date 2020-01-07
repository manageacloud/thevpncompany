<#-- @ftlvariable name="currentPlan" type="Integer" -->
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
    ${p.headerPlanCss}
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c section-main">
        <form method="post"
              action-xhr="/dashboard/account/subscription/new/xhr"
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
                    <div id="credit_card_form">
                        <h2>3 - Select your  preferred method of payment</h2>

                        <@p.creditCardForm/>
                    </div>

                    <@p.submitButtonForm label="Join Now"/>

                </div>
            </div>
        </form>
    </section>
</@t.dashboardTemplate>

