<#assign updatePasswordCss>
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

<#macro updatePassword>
    <div class="login">
        <h1>Update Your Password</h1>
        <div class="control-group">
            <label>Password</label>
            <input class="f-c user-valid valid" name="password" pattern="\w+" placeholder="Password" type="password">
        </div>
        <div class="control-group">
            <label>Repeat Password</label>
            <input class="f-c user-valid valid" name="password2" pattern="\w+" placeholder="Password" type="password">
        </div>

        <div submitting>
            <template type="amp-mustache">
                <div class="a a-i t-c" role="alert">
                    Form submitting...,<br>Thank you for waiting.
                </div>
            </template>
        </div>
        <div submit-success>
            <template type="amp-mustache">
                <div class="a a-s t-c" role="alert">
                    The password has been update. Please <a href="/login">login</a>.
                </div>
            </template>
        </div>
        <div submit-error>
            <template type="amp-mustache">
                <div class="a a-d t-c" role="alert">
                    Oops! {{message}}<br>
                    Please try to <a href="/reset-password">request the password</a> again.
                    Alternatively, you can contact <a href="/support">support</a>
                </div>
            </template>
        </div>
        <div id="submit-wrapper" class="r control-group" >
            <div class="c-l-12">
                <button type="submit" class="b b-p b-l b-b">Change Password</button>
            </div>
        </div>
        <@security.authorize access="! isAuthenticated()">
            <div class="t-c font-14 more-options">
                <p>
                    <a href="/login">Sign In</a>
                    <span class="separator">|</span>
                    <a href="/reset-password">Need Help ?</a>
                </p>
                <p>New User?<br><a href="/get-started">Get The<b>VPN</b>Company</a></p>
            </div>
        </@security.authorize>
    </div>

</#macro>