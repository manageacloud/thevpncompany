<#-- @ftlvariable name="location" type="com.manageacloud.vpn.model.Location" -->
<#-- @ftlvariable name="active" type="com.manageacloud.vpn.model.Location" -->
<#import "dashboard_template.ftl" as t />
<#assign tokenBug = _csrf.token>

<#assign head>
    <title>Locations | TheVPNCompany</title>
</#assign>

<#assign extra_js>
    <script async custom-element="amp-selector" src="https://cdn.ampproject.org/v0/amp-selector-0.1.js"></script>
    <script async custom-element="amp-form" src="https://cdn.ampproject.org/v0/amp-form-0.1.js"></script>
    <script async custom-element="amp-bind" src="https://cdn.ampproject.org/v0/amp-bind-0.1.js"></script>
    <script async custom-template="amp-mustache" src="https://cdn.ampproject.org/v0/amp-mustache-0.2.js"></script>
</#assign>

<#assign extra_css>

    .device {
        width: 100%;
        margin-bottom: 15px;
        text-align: center;
    }
    .b-d:hover {
        color: #ffffff;
        background-color: #4582ec;
    }
    button.disabled:hover {
        color:gray;
        border: 1px solid gray;
        background-color: white;
    }
    .active, .active:hover  {
        color: #155724;;
        border-color: #c3e6cb;;
        background-color: #d4edda;
    }

    @media (min-width: 370px) {
        .device {
            width: 25%;
            margin-bottom: 15px;
            text-align: center;
        }
        .b-d:hover {
            color: #ffffff;
            background-color: #4582ec;
        }
        a.disabled:hover {
            color:gray;
            border: 1px solid gray;
            background-color: white;
        }
        .active:hover {
            color: #155724;;
            border-color: #c3e6cb;;
            background-color: #d4edda;
        }


    }

</#assign>


<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Available Locations</h1>

                    <div class="readdable">
                        <#if status_success??>
                            <div class="a a-s t-c" role="alert">
                                The location has been updated. <br>
                                Please restart the VPN connections in your devices to start using the new location right away.
                            </div>
                        </#if>

                        <#if status_building??>
                            <div class="a a-s t-c" role="alert">
                                The selected location is very busy and we are creating more servers just for you.
                                This process can take a couple of minutes.<br>
                                You will receive an email notification when this location
                                is ready to use.
                            </div>
                        </#if>

                        <#if status_error??>
                            <div class="a a-d t-c" role="alert">
                                There was an error while changing location. Please contact support.
                            </div>
                        </#if>
                    </div>


                    <p class="readdable">
                        Choose the connection that you would like to connet to Internet.
                        This setting will affect to all the devices that you have configured,
                        and it might take up to two minutes.
                    </p>

                    <form method="post"
                          action-xhr="/dashboard/location/xhr"
                          target="_top" id="location">
                        <input type="hidden" name="${_csrf.parameterName}" value="${tokenBug}"/>

                        <div class="readdable">
                            <div submitting>
                                <template type="amp-mustache">
                                    <div class="a a-i t-c" role="alert">
                                        Updating ... Thank you for waiting.
                                    </div>
                                </template>
                            </div>
                            <div submit-success>
                                <template type="amp-mustache">
                                    Request Completed.
                                </template>
                            </div>
                            <div submit-error>
                                <template type="amp-mustache">
                                    <div class="a a-d t-c" role="alert">
                                        Oops! {{message}}
                                    </div>
                                </template>
                            </div>
                        </div>

                        <#assign parentRegion = "">
                        <#list locations as location>
                            <#if parentRegion != location.getParentRegion()>
                                <#assign parentRegion = location.getParentRegion()/>
                                <h2>${parentRegion}</h2>
                            </#if>
                            <button type="submit" name="iso" value="${location.getIso()}" class="device b b-d <#if active.getIso() == location.getIso()>active</#if>">${location.getName()}</button>
                        </#list>
                    </form>

                    <p class="readdable">
                    You can use the tool <a class="b b-d b-paragraph" href="/is-vpn-up">Is Vpn Up ?</a> to confirm the location
                    that you are currently using.
                    </p>

                    <p>
                        <a href="/support/chat">Would you like to request another location ?</a>
                    </p>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>

