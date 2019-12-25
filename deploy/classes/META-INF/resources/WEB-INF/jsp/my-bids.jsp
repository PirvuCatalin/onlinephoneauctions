<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<h2>
    <span class="center-block label label-default">My Bids</span>
</h2>

<div class="container">
    <c:forEach items="${auctions}" var="auction">
    <div class="panel panel-default">
        <div class="panel-heading">${auction.auction_title}</div>
        <div class="panel-body">
            <table>
                <tbody>
                <tr>
                    <td>
                        <div>
                            <b>Seller:</b>&nbsp;<div class="label label-info">${auction.auction_seller}</div>
                        </div>
                    </td>
                    <td>
                        <div style="margin-left: 75px;">
                            <b>End of Auction:</b>&nbsp;<div
                                class="label label-warning">${auction.auction_datetime_end}</div>
                        </div>
                    </td>
                    <td>
                        <div style="margin-left: 400px;">
                            <b>Current Price Bidded:</b>&nbsp;<div
                                class="label label-success">${auction.auction_current_price_bidded}</div>
                        </div>
                    </td>
                    </td>
                </tr>
                </tbody>
            </table>
            <br>
            <c:forEach items="${auction.bids}" var="bid">

                <div class="panel panel-success">
                    <div class="panel-body">
                        <table>
                            <tbody>
                            <tr>
                                <td>
                                    <div>
                                        <b>Amount of bid:</b>&nbsp;
                                        <div class="label label-success">${bid.price_bidded}</div>
                                    </div>
                                </td>
                                <td>
                                    <div style="margin-left: 250px;">
                                        <b>Date of bid:</b>&nbsp;
                                        <div class="label label-warning">${bid.datetime_bidded}</div>
                                    </div>
                                </td>
                                <td>
                                    <h4>
                                        <c:choose>
                                            <c:when test="${bid.winner}">
                                                <span style="margin-left: 300px;"
                                                      class="label label-success">WINNER</span>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${bid.highest}">
                                                    <span style="margin-left: 300px;" class="label label-warning">HIGHEST BID</span>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </h4>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </c:forEach>
            <a type="button" class="btn btn-success" href="/auction?id=${auction.auction_info_id}">View auction</a>

            <c:if test="${auction.canBeReviewed.equals(true)}">
                <span class="alert alert-warning" style="float:right;">
                    You won this auction.
                    <br>
                    Now you have the possibility of reviewing the seller (e.g. items were delivered ASAP, etc.):
<%--                    <a class="btn btn-success" style="margin-top: -20px;" href="/auction?id=${auction}&manuallyDo=true">--%>
<%--                        Review--%>
<%--                    </a>--%>
                    <button type="button" class="btn btn-info btn-lg" style="margin-top: -20px;" data-toggle="modal"
                            data-target="#myModal_${auction.auction_info_id}">Review</button>
                </span>

                <div class="modal fade" id="myModal_${auction.auction_info_id}" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Review ${auction.auction_seller}</h4>
                            </div>
                            <div class="modal-body">
                                <b>Stars:</b>
                                <br>
                                <fieldset class="rating">
                                    <input type="radio" id="star5_${auction.auction_info_id}" name="rating" value="5" /><label class = "full" for="star5_${auction.auction_info_id}" title="Awesome - 5 stars"></label>
                                    <input type="radio" id="star4_${auction.auction_info_id}" name="rating" value="4" /><label class = "full" for="star4_${auction.auction_info_id}" title="Pretty good - 4 stars"></label>
                                    <input type="radio" id="star3_${auction.auction_info_id}" name="rating" value="3" /><label class = "full" for="star3_${auction.auction_info_id}" title="Meh - 3 stars"></label>
                                    <input type="radio" id="star2_${auction.auction_info_id}" name="rating" value="2" /><label class = "full" for="star2_${auction.auction_info_id}" title="Kinda bad - 2 stars"></label>
                                    <input type="radio" id="star1_${auction.auction_info_id}" name="rating" value="1" /><label class = "full" for="star1_${auction.auction_info_id}" title="Sucks big time - 1 star"></label>
                                </fieldset>
                                <br>
                                <br>
                                <b>Review:</b>
                                <br>
                                <input id="review_${auction.auction_info_id}" name="review" type="text"
                                       class="form-control"
                                       placeholder="Review..."/>
                            </div>
                            <div class="modal-footer">
                                <button class="js-sendReviewButton btn btn-info"
                                        data-auctionid="${auction.auction_info_id}" type="button" data-dismiss="modal">
                                    Send Review
                                </button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>
            </c:if>
        </div>
    </div>
    </c:forEach>
    <%@ include file="common/footer.jspf" %>
    <script>
        $(".js-sendReviewButton").click(function (el) {
            var auctionId = $(this).data("auctionid");
            var review = document.getElementById("review_" + auctionId).value;

            var stars = 0;
            for(var i = 1; i <= 5; i++) {
                var starIsChecked = document.getElementById("star" + i + "_" + auctionId).checked;
                if(starIsChecked) {
                    stars = i;
                    break;
                }
            }

            $.ajax({
                url: "/reviews/add?auctionId=" + auctionId + "&stars=" + stars + "&review=" + review,
                type: 'GET',
                success: function (response) {
                },
                complete: function() {
                    location.reload();
                }
            });
        });
    </script>
