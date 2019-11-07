<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<h2><span class="center-block label label-default">
    <% if (request.getParameter("id") != null) {
        out.print("Edit auction");
    } else {
        out.print("Add auction");
    } %>
</span></h2><br>

<div class="container text-center" style="display: flex; align-items: flex-end; width: 1300px;">
    <div>
        <select multiple="multiple" id="my-select" name="my-select[]">
            <c:forEach items="${phonesAvailable}" var="phone">
                <option
                        value='${phone.phoneId}'
                        <c:if test="${phone.selected}">selected</c:if> >${phone.phoneName}</option>
            </c:forEach>
        </select>
    </div>

    <div style="margin-left: 130px; width: 250px;">
        <p>
            <font color="red">${errorMessage}</font>
        </p>
        <% if (request.getParameter("id") != null) { %>
        <form action="<%= "/auctions-add?id=" + request.getParameter("id") %>" method="POST" class="center-block"
              style="max-width: 400px">
                <% } else {%>
            <form action="/auctions-add" method="POST" class="center-block" style="max-width: 400px">
                <% }%>
                <fieldset class="form-group hidden">
                    <input id="phone_in_auction_id" name="phones_in_auction" type="text" class="form-control"/>
                </fieldset>

                <fieldset class="form-group">
                    <label>Auction End Datetime</label> <input required name="datetime_end" type="text"
                                                               value="${datetime_end}" class="form-control"
                                                               placeholder="dd-MM-yyyy HH:mm:ss"/>
                </fieldset>
                <fieldset class="form-group">
                    <label>Additional Info</label> <input name="additional_info" type="text" value="${additional_info}"
                                                          class="form-control"/>
                </fieldset>
                <fieldset class="form-group">
                    <label>Starting Price</label> <input required name="starting_price" type="text"
                                                         value="${starting_price}" class="form-control"/>
                </fieldset>
                <fieldset class="form-group">
                    <label>Target Price</label> <input required name="target_price" type="text" value="${target_price}"
                                                       class="form-control"/>
                </fieldset>
                <button type="submit" class="btn btn-success center-block">
                    <% if (request.getParameter("id") != null) {
                        out.print("Edit auction");
                    } else {
                        out.print("Create auction");
                    } %>
                </button>
            </form>
    </div>

    <div style="margin-left: 130px;">
        <h3><span class="center-block label label-default">Create other phone</span></h3>
        <% if (request.getParameter("id") != null) { %>
        <form action="<%= "/auctions-add?id=" + request.getParameter("id") + "&phoneAdd=true" %>" method="POST"
              style="max-width: 400px">
                <% } else {%>
            <form action="/auctions-add?phoneAdd=true" method="POST" style="max-width: 400px">
                <% }%>
                <fieldset class="form-group hidden">
                    <input id="phone_in_auction_id2" name="phones_in_auction" type="text" class="form-control"/>
                </fieldset>

                <fieldset class="form-group hidden">
                    <label>Auction End Datetime</label> <input name="datetime_end" type="text"
                                                               value="${datetime_end}" class="form-control"
                                                               placeholder="dd-MM-yyyy HH:mm:ss"/>
                </fieldset>
                <fieldset class="form-group hidden">
                    <label>Additional Info</label> <input name="additional_info" type="text" value="${additional_info}"
                                                          class="form-control"/>
                </fieldset>
                <fieldset class="form-group hidden">
                    <label>Starting Price</label> <input name="starting_price" type="text"
                                                         value="${starting_price}" class="form-control"/>
                </fieldset>
                <fieldset class="form-group hidden">
                    <label>Target Price</label> <input name="target_price" type="text" value="${target_price}"
                                                       class="form-control"/>
                </fieldset>
                <fieldset class="form-group">
                    <label>Phone Brand</label> <input required name="phone_brand" type="text" value="${phone_brand}"
                                                      class="form-control"/>
                </fieldset>
                <fieldset class="form-group">
                    <label>Phone Model</label> <input required name="phone_model" type="text"
                                                      value="${phone_model}" class="form-control"/>
                </fieldset>
                <button type="submit" class="btn btn-success center-block">Create phone</button>
            </form>
    </div>

</div>

<%@ include file="common/footer.jspf" %>

<script src="../../js/jquery.multi-select.js" type="text/javascript"></script>
<script>
    $('#my-select').multiSelect({
        selectableHeader: "<div class='dropdown-header'>Available phones</div>",
        selectionHeader: "<div class='dropdown-header'>Phones added to auction</div>",
        afterInit: function (container) {
            this.selectedPhones = [];
            var that = this;
            $('#my-select option:selected').each(function (el, val) {
                that.selectedPhones.push(val.value);
            });
            document.getElementById("phone_in_auction_id").value = this.selectedPhones;
            document.getElementById("phone_in_auction_id2").value = this.selectedPhones;
        },

        afterSelect: function (values) {
            this.selectedPhones.push(values[0]);
            document.getElementById("phone_in_auction_id").value = this.selectedPhones;
            document.getElementById("phone_in_auction_id2").value = this.selectedPhones;
        },

        afterDeselect: function (values) {
            for (var i = 0; i < this.selectedPhones.length; i++) {
                if (this.selectedPhones[i] === values[0]) {
                    this.selectedPhones.splice(i, 1);
                }
            }
            document.getElementById("phone_in_auction_id").value = this.selectedPhones;
            document.getElementById("phone_in_auction_id2").value = this.selectedPhones;
        }
    });
</script>