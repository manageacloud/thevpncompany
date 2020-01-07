<#import "../amp_framework.ftl" as amp />

<#assign extra_css>
    a.b {
    margin-left: 8px;
    margin-right: 8px;
    }
</#assign>

<#assign head>
    <title>Guide to install TheVPNCompany for Mac OS X</title>
</#assign>

<@amp.html head=head extra_css=extra_css>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Guide to install VPN for Mac OS X</h1>
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <div class="r ">
            <div class="c-l-12">
                <h2>1 - Download Tunnelblick</h2>
                <p>
                    The first thing you need to do to configure The<b>VPN</b>Company is to
                    <a class="b b-d" href="https://tunnelblick.net/release/Latest_Tunnelblick_Stable.dmg">download Tunnelblick for Mac OS X</a>
                </p>
                <h2>2 - Install Tunnelblick</h2>
                <p>
                    Start the installation by clicking in the file that you just downloaded.
                    Go through the installation process.
                </p>
                <h2>3 - Download the configuration you want</h2>
                <p>
                    Access to your <a class="b b-d" href="/dashboard/mac/ovpn">The<b>VPN</b>Company account</a> and download
                    the configuration file.
                </p>
                <h2>4 - Import the configuration file</h2>
                <p>
                    Double click in the tblk file. Alternatively you can  <b>hold Ctrl</b> label and <b>Open</b>.
                </p>
                <p>
                    If you see a message that says "<b>Install Configuration For All Users</b" choose <b>Only Me</b>.
                </p>
                <h2>5 - Connect to The<b>VPN</b>Company</h2>
                <p>
                    Tunnelblick should be in the activity tray in the top right corner of your screen. Click the icon to connect to the configuration file that you downloaded.
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

