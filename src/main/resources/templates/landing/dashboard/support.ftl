<#import "dashboard_template.ftl" as t />

<#assign extra_css>

    .options div a {
        width: 100%;
    }
    .options div {
        margin-bottom: 11%;
    }

    @media (min-width: 430px) {

        .options {
            margin: 0px auto;
            padding: 52px 0px 63px;
        }

        .options div {
            width: 42%;
            float: left;
            margin-left: 5%;
            padding-bottom: 25px;
            box-shadow: 0px 2px 5px 0px rgba(0,0,0,0.08),0px 5px 50px 0px rgba(0,0,0,0.08);

        }
        .options div a {
            width: initial;
            padding-left: 7px;
            padding-right: 7px;
        }
    }

    @media (min-width: 667px) {
        .options {
            margin: 68px auto 213px;
            padding: 52px 47px 63px;
        }

        .options div:last-child {
            margin-left: 5%;
        }
    }

</#assign>

<#assign head>
    <title>VPN Support</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="options">
                <div class="c-l-12 box-50 t-c">
                    <h2>Talk to an Aussie </h2>
                    <a href="/support/chat" class="b b-d ">Live Chat</a>
                </div>
                <div class="box-50 t-c" >
                    <h2>Set up The<b>VPN</b>Company</h2>
                    <a href="/dashboard/configuration" class="b b-d ">Get Instructions</a>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>
