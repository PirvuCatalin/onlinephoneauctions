<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="container">

    <button class="btn btn-block btn-default" data-toggle="collapse" data-target="#filter" value="FILTER...">
        <label style="font-size: large">Filter...</label>
    </button>

    <c:if test="${empty filtered}">
    <div id="filter" class="collapse"></c:if>
        <c:if test="${not empty filtered}">
        <div id="filter" class="collapse in"></c:if>
            <div class="container text-center" style="display: flex; align-items: flex-end; width: 1300px;">
                <div>
                    <select multiple="multiple" id="my-select" name="my-select[]">
                        <c:forEach items="${phonesAvailable}" var="phone">
                            <option
                                    value='${phone.phoneName}'
                                    <c:if test="${phone.selected}">selected</c:if> >${phone.phoneName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="margin-left: 30px; margin-bottom: 60px;">
                    <label for="startingPrice">Filter by starting price(&euro;):</label>
                    <b>${startingPriceMin}&nbsp;&nbsp;</b>
                    <input id="startingPrice" type="text" class="span2" value=""
                           data-slider-min="${startingPriceMin}"
                           data-slider-max="${startingPriceMax}"
                           data-slider-step="1"
                            <c:if test="${startingPriceLow == null}">
                                data-slider-value="[${startingPriceMin},${startingPriceMax}]"
                            </c:if>
                            <c:if test="${startingPriceLow != null}">
                                data-slider-value="[${startingPriceLow},${startingPriceHigh}]"
                            </c:if>

                    />
                    <b>&nbsp;&nbsp;${startingPriceMax}</b>
                    <p></p>

                    <label for="targetPrice">Filter by target price(&euro;):</label>
                    <b>&nbsp;&nbsp;&nbsp;${targetPriceMin}&nbsp;&nbsp;</b>
                    <input id="targetPrice" type="text" class="span2" value=""
                           data-slider-min="${targetPriceMin}"
                           data-slider-max="${targetPriceMax}"
                           data-slider-step="1"
                            <c:if test="${targetPriceLow == null}">
                                data-slider-value="[${targetPriceMin},${targetPriceMax}]"
                            </c:if>
                            <c:if test="${targetPriceLow != null}">
                                data-slider-value="[${targetPriceLow},${targetPriceHigh}]"
                            </c:if>
                    />
                    <b>&nbsp;&nbsp;${targetPriceMax}</b>
                    <p></p>

                    <label for="currentPrice">Filter by current price(&euro;):</label>
                    <b>&nbsp;&nbsp;&nbsp;${currentPriceMin}&nbsp;&nbsp;</b>
                    <input id="currentPrice" type="text" class="span2" value=""
                           data-slider-min="${currentPriceMin}"
                           data-slider-max="${currentPriceMax}"
                           data-slider-step="1"
                            <c:if test="${currentPriceLow == null}">
                                data-slider-value="[${currentPriceMin},${currentPriceMax}]"
                            </c:if>
                            <c:if test="${currentPriceLow != null}">
                                data-slider-value="[${currentPriceLow},${currentPriceHigh}]"
                            </c:if>
                    />
                    <b>&nbsp;&nbsp;${currentPriceMax}</b>
                </div>


                <form action="/auctions" method="POST" class="center-block"
                      style="margin-left: 30px; width: 200px; max-width: 400px;">
                    <label for="sellerNameId">Seller Name: </label>
                    <fieldset class="form-group" style="margin-bottom: 50px;">
                        <input id="sellerNameId" name="sellerName" type="text" class="form-control" placeholder="Seller Name"
                                <c:if test="${not empty sellerName}">value="${sellerName}"</c:if>
                        />
                    </fieldset>
                    <fieldset class="form-group hidden">
                        <input id="startingPriceId" name="startingPrice" type="text" class="form-control"/>
                    </fieldset>
                    <fieldset class="form-group hidden">
                        <input id="targetPriceId" name="targetPrice" type="text" class="form-control"/>
                    </fieldset>
                    <fieldset class="form-group hidden">
                        <input id="currentPriceId" name="currentPrice" type="text" class="form-control"/>
                    </fieldset>
                    <fieldset class="form-group hidden">
                        <input id="phone_in_auction_id" name="phones_in_auction" type="text" class="form-control"/>
                    </fieldset>
                    <button type="submit" class="btn btn-success">Filter auctions</button>
                </form>
            </div>
        </div>

        <br><br><br>

        <sec:authorize access="!hasAuthority('ADMIN')">
            <div>
                <a type="button" class="btn btn-success" href="/auctions-add">Add new auction</a>
            </div>
        </sec:authorize>
        <p>
            <font color="green">${errorMessage}</font>
        </p>

        <sec:authorize access="hasAuthority('ADMIN')">
            <h2><label style="margin-left: 50px;">Auctions to be validated:</label></h2>
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
                        <a type="button" class="btn btn-primary" href="/auctions/validate?id=${notValidatedAuction.id}">Validate</a>
                        <c:if test="${user_id == notValidatedAuction.seller_id}">
                            <a type="button" class="btn btn-primary"
                               href="/auctions-add?id=${notValidatedAuction.id}">Edit</a>
                            <a type="button" class="btn btn-warning"
                               href="/auctions/delete?id=${notValidatedAuction.id}">Delete</a>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </sec:authorize>


        <h2><label style="margin-left: 50px;">Currently active auctions:</label></h2>
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
                    <c:if test="${user_id == auction.seller_id}">
                        <a type="button" class="btn btn-primary" href="/auctions-add?id=${auction.id}">Edit</a>
                    </c:if>
                    <c:choose>
                        <c:when test="${user_id == auction.seller_id}">
                            <a type="button" class="btn btn-warning" href="/auctions/delete?id=${auction.id}">Delete</a>
                        </c:when>
                        <c:otherwise>
                            <sec:authorize access="hasAuthority('ADMIN')">
                                <a type="button" class="btn btn-warning" href="/auctions/delete?id=${auction.id}">Delete</a>
                            </sec:authorize>
                        </c:otherwise>
                    </c:choose>
                    <a type="button" class="btn btn-success" href="/auction?id=${auction.id}">View auction</a>

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
                            <a type="button" class="btn btn-primary"
                               href="/auctions-add?id=${inactiveAuction.id}">Edit</a>
                            <a type="button" class="btn btn-warning"
                               href="/auctions/delete?id=${inactiveAuction.id}">Delete</a>
                        </c:if>
                        <a type="button" class="btn btn-success" href="/auction?id=${inactiveAuction.id}">View auction</a>
                    </div>
                </div>
            </c:forEach>
        </sec:authorize>
    </div>

    <%@ include file="common/footer.jspf" %>

    <script src="../../js/jquery.multi-select.js" type="text/javascript"></script>
    <script src="../../js/bootstrap-slider.js" type="text/javascript"></script>
    <script>
        $(document).ready(function () {
            this.startingPriceSlider = new Slider('#startingPrice', {});
            this.targetPriceSlider = new Slider('#targetPrice', {});
            this.currentPriceSlider = new Slider('#currentPrice', {});
            $("#startingPriceId").val($("#startingPrice").val());
            $("#targetPriceId").val($("#targetPrice").val());
            $("#currentPriceId").val($("#currentPrice").val());
        });

        $("#startingPrice").on("slide", function (slideEvt) {
            $("#startingPriceId").val(slideEvt.value);
        });

        $("#targetPrice").on("slide", function (slideEvt) {
            $("#targetPriceId").val(slideEvt.value);
        });

        $("#currentPrice").on("slide", function (slideEvt) {
            $("#currentPriceId").val(slideEvt.value);
        });

        $('#my-select').multiSelect({
            selectableHeader: "<div class='dropdown-header'>Available phones</div>",
            selectionHeader: "<div class='dropdown-header'>Phones to filter by</div>",
            afterInit: function (container) {
                this.selectedPhones = [];
                var that = this;
                $('#my-select option:selected').each(function (el, val) {
                    that.selectedPhones.push(val.value);
                });
                document.getElementById("phone_in_auction_id").value = this.selectedPhones;
            },

            afterSelect: function (values) {
                this.selectedPhones.push(values[0]);
                document.getElementById("phone_in_auction_id").value = this.selectedPhones;
            },

            afterDeselect: function (values) {
                for (var i = 0; i < this.selectedPhones.length; i++) {
                    if (this.selectedPhones[i] === values[0]) {
                        this.selectedPhones.splice(i, 1);
                    }
                }
                document.getElementById("phone_in_auction_id").value = this.selectedPhones;
            }
        });
    </script>
