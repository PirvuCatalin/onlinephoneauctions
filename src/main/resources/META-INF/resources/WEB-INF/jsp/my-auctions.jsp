<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<div class="container">
    <p>
        <font color="green">${errorMessage}</font>
    </p>

    <h2><label style="margin-left: 50px;">My auctions to be validated by admin:</label></h2>
    <c:if test="${empty notValidatedAuctions}">
        None.
    </c:if>
    <c:forEach items="${notValidatedAuctions}" var="notValidatedAuction">
        <div class="panel panel-primary">
            <div class="panel-heading">${notValidatedAuction.title}</div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Seller:</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.seller_name}</td>
                        <th>Additional Info:</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.additional_info}</td>
                    </tr>
                    <tr>
                        <th>Datetime Start:</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.datetime_start}</td>
                        <th>Datetime End:</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.datetime_end}</td>
                    </tr>

                    <tr>
                        <th>Starting Price</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.starting_price}&euro;</td>
                        <th>Target Price</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.target_price}&euro;</td>
                        <th>Current Bidded Price</th>
                        <td style="vertical-align:bottom">${notValidatedAuction.current_price_bidded}&euro;</td>
                    </tr>

                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <a type="button" class="btn btn-primary"
                   href="/auctions-add?id=${notValidatedAuction.id}&returnToMyAuctions=true">Edit</a>
                <a type="button" class="btn btn-warning"
                   href="/auctions/delete?id=${notValidatedAuction.id}&returnToMyAuctions=true">Delete</a>
            </div>
        </div>
    </c:forEach>


    <h2><label style="margin-left: 50px;">My active auctions:</label></h2>
    <c:if test="${empty auctions}">
        None.
    </c:if>
    <c:forEach items="${auctions}" var="auction">
        <div class="panel panel-primary">
            <div class="panel-heading">${auction.title}</div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Seller:</th>
                        <td style="vertical-align:bottom">${auction.seller_name}</td>
                        <th>Additional Info:</th>
                        <td style="vertical-align:bottom">${auction.additional_info}</td>
                    </tr>
                    <tr>
                        <th>Datetime Start:</th>
                        <td style="vertical-align:bottom">${auction.datetime_start}</td>
                        <th>Datetime End:</th>
                        <td style="vertical-align:bottom">${auction.datetime_end}</td>
                    </tr>

                    <tr>
                        <th>Starting Price</th>
                        <td style="vertical-align:bottom">${auction.starting_price}&euro;</td>
                        <th>Target Price</th>
                        <td style="vertical-align:bottom">${auction.target_price}&euro;</td>
                        <th>Current Bidded Price</th>
                        <td style="vertical-align:bottom">${auction.current_price_bidded}&euro;</td>
                    </tr>

                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <a type="button" class="btn btn-primary" href="/auctions-add?id=${auction.id}&returnToMyAuctions=true">Edit</a>
                <a type="button" class="btn btn-warning"
                   href="/auctions/delete?id=${auction.id}&returnToMyAuctions=true">Delete</a>
                <a type="button" class="btn btn-success" href="/auction?id=${auction.id}">View auction</a>
            </div>
        </div>
    </c:forEach>

    <h2><label style="margin-left: 50px;">My ended auctions:</label></h2>
    <c:if test="${empty inactiveAuctions}">
        None.
    </c:if>
    <c:forEach items="${inactiveAuctions}" var="inactiveAuction">
        <div class="panel panel-primary">
            <div class="panel-heading">${inactiveAuction.title}</div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Seller:</th>
                        <td style="vertical-align:bottom">${inactiveAuction.seller_name}</td>
                        <th>Additional Info:</th>
                        <td style="vertical-align:bottom">${inactiveAuction.additional_info}</td>
                    </tr>
                    <tr>
                        <th>Datetime Start:</th>
                        <td style="vertical-align:bottom">${inactiveAuction.datetime_start}</td>
                        <th>Datetime End:</th>
                        <td style="vertical-align:bottom">${inactiveAuction.datetime_end}</td>
                    </tr>

                    <tr>
                        <th>Starting Price</th>
                        <td style="vertical-align:bottom">${inactiveAuction.starting_price}&euro;</td>
                        <th>Target Price</th>
                        <td style="vertical-align:bottom">${inactiveAuction.target_price}&euro;</td>
                        <th>Current Bidded Price</th>
                        <td style="vertical-align:bottom">${inactiveAuction.current_price_bidded}&euro;</td>
                    </tr>

                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <a type="button" class="btn btn-success" href="/auction?id=${inactiveAuction.id}">View auction</a>
                <c:if test="${inactiveAuction.is_successfully_done.equals(false)}">
                    <span class="alert alert-danger" style="float:right; margin-right: 600px;">
                        The auction couldn't complete successfully.
                        <br>
                        Please visit the auction's page for more information!
                    </span>
                </c:if>
            </div>

        </div>
    </c:forEach>
</div>
<%@ include file="common/footer.jspf" %>