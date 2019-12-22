<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="container">
    <label>Filter by seller:</label>
    <form action="/reviews" method="GET" style="max-width: 400px">
        <fieldset style="display: inline" class="form-group">
            <input id="sellerName" name="sellerName" type="text" value="${sellerName}" class="form-control"/>
        </fieldset>
        <button style="display: inline" type="submit" class="btn btn-success center-block">Filter</button>
    </form>

    <table class="table table-striped">
        <caption>Reviews:</caption>
        <c:if test="${not empty reviews}">
            <thead>
            <tr>
                <th>Seller</th>
                <th>Auction</th>
                <th>Reviewer</th>
                <th>Auction End Datetime</th>
                <th>Buy Price</th>
                <th>Stars</th>
                <th>Review</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${reviews}" var="review">
                <tr>
                    <td>${review.seller}</td>
                    <td>${review.auction}</td>
                    <td>${review.buyer}</td>
                    <td>${review.auctionEndDatetime}</td>
                    <td>${review.buyPrice}&euro;</td>
                    <td>${review.stars}</td>
                    <td>${review.review}</td>
                    <td>
                        <sec:authorize access="hasAuthority('ADMIN')">
                            <a type="button" class="btn btn-warning"
                               href="/reviews/delete?id=${review.id}">Delete</a>
                        </sec:authorize>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </c:if>
    </table>
    <c:if test="${empty reviews}">
        None.
    </c:if>


<%@ include file="common/footer.jspf" %>