<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<style>
        .btn-file {
            position: relative;
            overflow: hidden;
        }

        .btn-file input[type=file] {
            position: absolute;
            top: 0;
            right: 0;
            min-width: 100%;
            min-height: 100%;
            font-size: 100px;
            text-align: right;
            filter: alpha(opacity = 0);
            opacity: 0;
            outline: none;
            background: white;
            cursor: inherit;
            display: block;
        }
</style>

<div class="container">
    <div>
        <div class="table-responsive">
            <table class="table table-striped" id="tblFileList">
                <thead>
                <tr>
                    <th>Download File</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${listOfEntries}" var="entry">
                    <tr>
                        <td><a href="${entry.href}" text="${entry.hrefText}">file</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>


    <div class="page-header">
        <h1>Upload/Download Application</h1>
    </div>

    <div class="row">
        <div class="col-md-12">
            <form method="POST" enctype="multipart/form-data" action="/files/upload">
                <div class="row border">

                    <div class="col-md-5 text-left">
                        <div class="input-group">
                            <label class="input-group-btn"> <span
                                    class="btn btn-primary"> Browse&hellip; <input
                                    type="file" name="file" accept=".txt" id="customFile"
                                    style="display: none;" />
								</span>
                            </label> <input type="text" class="form-control" id="customFileHolder"
                                            readonly="true" />
                        </div>
                        <span class="help-block"> Try selecting a <em>text</em>
								file to upload.
							</span>
                    </div>
                    <div class="col-md-2">
                        <input type="submit" id="btnUploadFile" value="Upload"
                               class="btn btn-primary" />
                    </div>
                    <div class="col-md-5 text-left">

                        <c:if test="${not empty message}">
                        <div text="${message}" class="alert alert-success" role="alert" id="divUploadSuccess">
                            Upload Success.
                        </div>
                        </c:if>

                    </div>
                </div>

            </form>
        </div>
    </div>

    <div class="row">
        <div id="fileList"></div>
    </div>

</div>


<input type="hidden" value="/files/list" id="urlFileList" />


<%@ include file="common/footer.jspf" %>
<script type="text/javascript" src="js/upload.js"></script>
