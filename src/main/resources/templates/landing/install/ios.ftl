<#import "../amp_framework.ftl" as amp />

<#assign extra_css>
    a.b {
    margin-left: 8px;
    margin-right: 8px;
    }
</#assign>

<#assign head>
    <title>Guide to install OpenVPN Connect for IOS (iPhone and iPad)</title>
</#assign>

<@amp.html head=head extra_css=extra_css>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Guide to install OpenVPN Connect for IOS (iPhone and iPad)</h1>
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <div class="r ">
            <div class="c-l-12">
                <h2>1 - Download The<b>VPN</b>Company Connect</h2>
                <p>
                    The first thing you need to do to configure The<b>VPN</b>Company is to install
                    <a class="b b-d" href="https://itunes.apple.com/us/app/openvpn-connect/id590379981?mt=8">OpenVPN Connect</a>
                    from the Apple Store.
                </p>
                <h2>2 - Download the configuration you want</h2>
                <p>
                    Access to your <a class="b b-d" href="/dashboard/ios/ovpn">The<b>VPN</b>Company account</a> and download
                    the tblk configuration file.
                </p>
                <h2>4 - Open the configuration file</h2>
                <p>
                    Open the configuration file that you downloaded in step 2. You should be able to choose to <b>Open in "OpenVPN"</b>.
                    Now you can import the configuration profile by clicking on <b>Add</b>
                </p>
                <h2>5 - Connect to The<b>VPN</b>Company</h2>
                <p>
                    Connect by clicking on the grey toggle that appears next to the profile name.
                </p>
                <h2>6 - Finished</h2>
                <p>
                    You should be connected to The<b>VPN</b>Company safely and be able to browser the internet safely.
                    To make sure that everything was set up correctly, please use <a class="b b-d" href="/is-vpn-up">Is Vpn Up ?</a> tool.
                </p>

            </div>
        </div>
    </section>
</@amp.html>

