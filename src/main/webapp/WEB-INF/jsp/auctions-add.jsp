<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

    <h2><span class="center-block label label-default">Add auction</span></h2><br>

    <select multiple="multiple" id="my-select" name="my-select[]">
        <c:forEach items="${phonesAvailable}" var="phone">
            <option
                    value='${phone.phoneId}'
                    <c:if test="${phone.selected}">selected</c:if> >${phone.phoneName}</option>
        </c:forEach>
    </select>

    <div class="center-block" style="width: 250px">
        <p>
            <font color="red">${errorMessage}</font>
        </p>
        <form action="/auctions-add" method="POST">
            <fieldset class="form-group hidden">
                <input id="phone_in_auction_id" name="phones_in_auction" type="text" class="form-control" />
            </fieldset>

            <fieldset class="form-group">
                <label>Auction End Datetime</label> <input required name="datetime_end" type="text" value="${datetime_end}" class="form-control" placeholder="dd-MM-yyyy HH:mm:ss"/>
            </fieldset>
            <fieldset class="form-group">
                <label>Additional Info</label> <input name="additional_info" type="text" value="${additional_info}" class="form-control" />
            </fieldset>
            <fieldset class="form-group">
                <label>Starting Price</label> <input required name="starting_price" type="text" value="${starting_price}" class="form-control" />
            </fieldset>
            <fieldset class="form-group">
                <label>Target Price</label> <input required name="target_price" type="text" value="${target_price}" class="form-control" />
            </fieldset>
        <button type="submit" class="btn btn-success center-block">Create auction</button>
        </form>
    </div>

<%@ include file="common/footer.jspf"%>

<script src="../../js/jquery.multi-select.js" type="text/javascript"></script>
<script>
    $('#my-select').multiSelect({
        selectableHeader: "<div class='dropdown-header'>Available phones</div>",
        selectionHeader: "<div class='dropdown-header'>Phones added to auction</div>",
        afterInit: function(container) {
            this.selectedPhones = [];
            var that = this;
            $('#my-select option:selected').each(function(el, val) {
                that.selectedPhones.push(val.value);
            });
            document.getElementById("phone_in_auction_id").value = this.selectedPhones;
        },

        afterSelect: function(values){
            this.selectedPhones.push(values[0]);
            document.getElementById("phone_in_auction_id").value = this.selectedPhones;
        },

        afterDeselect: function(values){
            for( var i = 0; i < this.selectedPhones.length; i++){
                if ( this.selectedPhones[i] === values[0]) {
                    this.selectedPhones.splice(i, 1);
                }
            }
            document.getElementById("phone_in_auction_id").value = this.selectedPhones;
        }
    });
</script>