<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container">
    <div class="center-block" style="width: 250px">
        <p>
            <font color="red">${errorMessage}</font>
        </p>

        <form action="/register" method="POST">
            <fieldset class="form-group">
                <label>Username</label> <input required name="username" type="text" value="${username}"
                                               class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Password</label> <input required name="password" type="password"
                                               class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Confirm Password</label> <input required name="passwordConfirm" type="password"
                                                       class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Name</label> <input required name="name" type="text" value="${name}"
                                           class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Birthday</label> <input required name="birthday" type="text" value="${birthday}"
                                               class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Address Detail</label> <input required name="address_detail" type="text"
                                                     value="${address_detail}"
                                                     class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>City</label> <input required name="city" type="text" value="${city}"
                                           class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Country</label> <input required name="country" type="text" value="${country}"
                                              class="form-control"/>
            </fieldset>
            <button type="submit" class="btn btn-success center-block">Create Account</button>
            <br>Already have an account? <a href="/login">Login.</a>
        </form>
    </div>
</div>

<%@ include file="common/footer.jspf" %>