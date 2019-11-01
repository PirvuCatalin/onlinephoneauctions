<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
    <div>
        <a type="button" class="btn btn-success" href="/auctions-add">Add new auction</a>
    </div>
    <p>
        <font color="green">${errorMessage}</font>
    </p>
    <h2><label style="margin-left: 50px;">Currently active auctions:</label></h2>

        <c:forEach items="${auctions}" var="auction">
            <div class="panel panel-primary">
                <div class="panel-heading">${auction.title}</div>
                <div class="panel-body">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Seller: </th><td style="vertical-align:bottom">${auction.seller_name}</td>
                            <th>Additional Info:</th><td style="vertical-align:bottom">${auction.additional_info}</td>
                        </tr>
                        <tr>
                            <th>Datetime Start: </th><td style="vertical-align:bottom">${auction.datetime_start}</td>
                            <th>Datetime End: </th><td style="vertical-align:bottom">${auction.datetime_end}</td>
                        </tr>

                        <tr>
                            <th>Starting Price</th><td style="vertical-align:bottom">${auction.starting_price}&euro;</td>
                            <th>Target Price</th><td style="vertical-align:bottom">${auction.target_price}&euro;</td>
                            <th>Current Bidded Price</th><td style="vertical-align:bottom">${auction.current_price_bidded}&euro;</td>
                        </tr>

                        </thead>
                        <tbody>
                        <%--            <tr>--%>
                        <%--                <td>${auction.id}</td>--%>
                        <%--                <td><fmt:formatDate pattern="dd/MM/yyyy"--%>
                        <%--                                    value="${todo.targetDate}" /></td>--%>
                        <%--                <td>${todo.done}</td>--%>
<%--                                        <td><a type="button" class="btn btn-primary"--%>
<%--                                               href="/update-todo?id=${todo.id}">Edit</a> <a type="button"--%>
<%--                                                                                             class="btn btn-warning" href="/delete-todo?id=${todo.id}">Delete</a>--%>
<%--                                        </td>--%>
                        <%--            </tr>--%>
                        </tbody>
                    </table>
                    <c:if test="${user_id == auction.seller_id}">
                        <a type="button" class="btn btn-primary" href="/auctions-add?id=${auction.id}">Edit</a>
                        <a type="button" class="btn btn-warning" href="/auctions/delete?id=${auction.id}">Delete</a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
</div>
<%@ include file="common/footer.jspf"%>