<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="container">
    <div>
        <a type="button" class="btn btn-success" href="/auctions-add">Add new auction</a>
    </div>
    <p>
        <font color="green">${errorMessage}</font>
    </p>

    <sec:authorize access="hasAuthority('ADMIN')">
        <h2><label style="margin-left: 50px;">Auctions to be validated:</label></h2>
        <c:if test="${empty notValidateAuctions}">
            None.
        </c:if>
        <c:forEach items="${notValidateAuctions}" var="notValidatedAuction">
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
                    <a type="button" class="btn btn-primary" href="/auctions/validate?id=${notValidatedAuction.id}">Validate</a>
                    <c:if test="${user_id == notValidatedAuction.seller_id}">
                        <a type="button" class="btn btn-primary"
                           href="/auctions-add?id=${notValidatedAuction.id}">Edit</a>
                        <a type="button" class="btn btn-warning" href="/auctions/delete?id=${notValidatedAuction.id}">Delete</a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </sec:authorize>


    <h2><label style="margin-left: 50px;">Currently active auctions:</label></h2>
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
                <c:if test="${user_id == auction.seller_id}">
                    <a type="button" class="btn btn-primary" href="/auctions-add?id=${auction.id}">Edit</a>
                    <a type="button" class="btn btn-warning" href="/auctions/delete?id=${auction.id}">Delete</a>
                </c:if>
            </div>
        </div>
    </c:forEach>

    <sec:authorize access="hasAuthority('ADMIN')">
        <h2><label style="margin-left: 50px;">Ended auctions:</label></h2>
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
                    <c:if test="${user_id == inactiveAuction.seller_id}">
                        <a type="button" class="btn btn-primary" href="/auctions-add?id=${inactiveAuction.id}">Edit</a>
                        <a type="button" class="btn btn-warning"
                           href="/auctions/delete?id=${inactiveAuction.id}">Delete</a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </sec:authorize>
</div>
<%@ include file="common/footer.jspf" %>