<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container" style="display: flex; justify-content: space-evenly; align-items: flex-end;">
    <div style="width: 250px;">
        <h2><span class="label label-default">User Info</span></h2><br>
        <p>
            <font color="red">${errorMessageUserInfo}</font>
        </p>
        <form action="/account/change-userinfo" method="POST">
            <fieldset class="form-group">
                <label>Name</label> <input required name="name" class="form-control" type="text"
                                           value="${name}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Birthday</label> <input required name="birthday" class="form-control" type="text"
                                               value="${birthday}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Address Detail</label> <input required name="address_detail" class="form-control" type="text"
                                                     value="${address_detail}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>City</label> <input required name="city" class="form-control" type="text"
                                           value="${city}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Country</label> <input required name="country" class="form-control" type="text"
                                              value="${country}"/>
            </fieldset>
            <button type="submit" class="btn btn-success center-block">Update Account</button>
        </form>
    </div>

    <div style="width: 250px;">
        <h2><span class="label label-default">Card Info</span></h2><br>
        <p>
            <font color="red">${errorMessageCardInfo}</font>
        </p>
        <form action="/account/change-card" method="POST">
            <fieldset class="form-group">
                <label>Card Number</label> <input required name="card_number" class="form-control" type="text"
                                                  value="${card_number}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Cardholder Name</label> <input required name="cardholder_name" class="form-control" type="text"
                                                      value="${cardholder_name}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Card Expiry Date</label> <input required name="card_expiry_date" class="form-control" type="text"
                                                       value="${card_expiry_date}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Card CVV</label> <input required name="card_cvv" class="form-control" type="text"
                                               value="${card_cvv}"/>
            </fieldset>
            <button type="submit" class="btn btn-success center-block">Update Card</button>
        </form>
    </div>


    <div style="width: 250px;">
        <h2><span class="label label-default">Account Info</span></h2><br>
        <p>
            <font color="red">${errorMessage}</font>
        </p>
        <form action="/account/change-password" method="POST">
            <fieldset class="form-group">
                <label>Username</label> <input name="username" class="form-control" type="text" readonly
                                               value="${username}"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Current Password</label> <input required name="currentPassword" type="password"
                                                       class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>New Password</label> <input required name="password" type="password"
                                                   class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Confirm New Password</label> <input required name="passwordConfirm" type="password"
                                                           class="form-control"/>
            </fieldset>
            <button type="submit" class="btn btn-success center-block">Change Password</button>
        </form>
    </div>
</div>

<%@ include file="common/footer.jspf" %>