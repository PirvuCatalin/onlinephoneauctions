<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="container">
    <div class="center-block" style="width: 250px">
        <p>
            <font color="red">${errorMessage}</font>
        </p>

        <form action="login" method="POST">
            <fieldset class="form-group">
                <label>Username</label> <input required name="username" type="text"
                    class="form-control" />
            </fieldset>
            <fieldset class="form-group">
                <label>Password</label> <input required name="password" type="password"
                    class="form-control"/>
            </fieldset>
            <button type="submit" class="btn btn-success center-block">Login</button>
            <br>New? <a href="/register">Create an account.</a>
        </form>
    </div>
</div>

<%@ include file="common/footer.jspf"%>