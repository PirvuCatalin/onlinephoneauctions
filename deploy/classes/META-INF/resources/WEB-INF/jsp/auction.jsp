<jsp:useBean id="auction" scope="request" type="com.onlinephoneauctions.dto.AuctionInfoDTO"/>
<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="container">
    <h2>
        <span class="center-block label label-default">Auction Info</span>
    </h2>
    <br>
    <table class="table">
        <tr>
            <td>
                <label>Seller:</label>
                <p></p>
                <h3><span class="label label-info">${auction.seller_name}</span></h3>
            </td>
            <td>
                <div style="margin-left: 25px;">
                    <label>Items:</label>
                    <p></p>
                    <h3><span class="label label-info">${auction.title}</span></h3>
                </div>
            </td>
            <td>
                <div style="margin-left: 25px; width: 110px;">
                    <label>Starting Price:</label>
                    <p></p>
                    <h3><span class="label label-success pull-right">${auction.starting_price}&euro;</span></h3>
                </div>
            </td>
            <td>
                <div style="margin-left: 25px; width: 110px;">
                    <label>Target Price:</label>
                    <p></p>
                    <h3><span class="label label-success pull-right">${auction.target_price}&euro;</span></h3>
                </div>
            </td>
            <td>
                <div style="margin-left: 25px; width: 110px;">
                    <label>Current Price:</label>
                    <p></p>
                    <h3><span class="label label-success pull-right">${auction.current_price_bidded}&euro;</span></h3>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div style="max-width: 450px;">
                    <label>Additional Info:</label>
                    <p></p>
                    <h4><span class="alert-warning" style="width: 150px;">${auction.additional_info}</span></h4>
                </div>
            </td>
            <td></td>
            <td>
                <div style="margin-left: 25px;">
                    <label>Starting Time:</label>
                    <p></p>
                    <h3><span class="label label-warning">${auction.datetime_start}</span></h3>
                </div>
            </td>
            <td>
                <div style="margin-left: 25px;">
                    <label>Ending Time:</label>
                    <p></p>
                    <h3><span class="label label-warning">${auction.datetime_end}</span></h3>
                </div>
            </td>
        </tr>
    </table>
    <div>

    </div>
    <br>
    <c:if test="${errorMessage != null}">
        <span class="alert alert-danger">${errorMessage}</span>
    </c:if>
    <br>
    <br>
    <c:if test="${canBid != null}">
        <label for="bidForm">Please enter the bid amount:</label>
        <form class="form-inline" id="bidForm" action="/auction" method="GET">
            <fieldset class="form-group">
                <input required name="bidAmount" class="form-control" style="width: 250px;" type="number"/>
            </fieldset>
            <fieldset class="form-group">
                <input hidden name="id" class="hidden form-control" value="${auction.id}"/>
            </fieldset>
            <button type="submit" class="btn btn-success">Bid</button>
        </form>
    </c:if>

    <c:if test="${canBeManuallyDone != null && not empty bids}">
        <span class="alert alert-danger" style="float:left;">
            The auction has ended, but currently no one bid more than or equal to the target price.
            <br>
            By pressing the following button you will manually pick the highest bidder as the winner:
            <a class="btn btn-success" style="margin-top: -25px;" href="/auction?id=${auction.id}&manuallyDo=true">
                Pick Highest Bidder
            </a>
        </span>
    </c:if>
    <c:if test="${canBeManuallyDone != null && empty bids}">
        <span class="alert alert-danger" style="float:left;">
            The auction couldn't complete as it has ended without any bids!
        </span>
    </c:if>
    <br><br><br><br>
    <h2>
        <span class="center-block label label-default">
            Bids
        </span>
    </h2>
    <c:if test="${auctionEnded != null}">
        <h2><span class="pull-right navbar-fixed-bottom label label-danger">This auction has ended!</span></h2>
    </c:if>
    <br>
    <c:if test="${bids != null}">
        <table class="table table-striped">
            <thead>
            <tr>
                <td><b>Bidder Name</b></td>
                <td><b>Time of Bid</b></td>
                <td><b>Amount Bidded</b></td>
            </tr>
            </thead>
            <c:forEach items="${bids}" var="bid">
                <tr>
                    <td>
                            ${bid.buyer_name}
                    </td>
                    <td>
                            ${bid.datetime_bidded}
                    </td>
                    <td>
                            ${bid.price_bidded}&euro;
                        <c:choose>
                            <c:when test="${bid.winner}">
                                <span class="label label-success">WINNER</span>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${bid.highest}">
                                    <span class="label label-warning">HIGHEST BID</span>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${bids == null}">
        No bids at the moment.
    </c:if>
    <br>

</div>

<%@ include file="common/footer.jspf" %>

