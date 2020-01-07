<#import "amp_framework.ftl" as amp />
<#import "parts/_choosedevice.ftl" as cd />

<#assign extra_css>
    .device {
    width: 25%;
    margin-bottom: 15px;
    text-align: center;
    }
    .b-d:hover {
    color: #ffffff;
    background-color: #4582ec;
    }
    .section-intro a {
    margin-right: 8px;
    }
</#assign>

<#assign head>
    <title>Products | TheVPNCompany</title>
</#assign>

<@amp.html head=head extra_css=extra_css>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="readdable">
                    <h1>Configure The<b>VPN</b>Company</h1>
                    <p>The<b>VPN</b>Company offers safe, easy-to-use VPN app
                        for each of your favorite devices. You can connect up to five devices
                        with one single account.</p>
                    <p>Start using The<b>VPN</b>Company in two simple steps:</p>
                    <p>1 - <a class="b b-d" href="/get-started">Sign Up</a> in The<b>VPN</b>Company</p>
                    <p>2 - <a class="b b-d" href="#choose-device">Download and configure</a> the software</p>
                </div>
            </div>

        </div>
    </section>
    <section class="c section-main" id="choose-device">
        <div class="r ">
            <div class="c-l-12">
                <@cd.chooseDeviceOptions/>
            </div>
        </div>
    </section>
</@amp.html>

