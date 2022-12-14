<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<!--pagination-->
	<link rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	$(function(){
		$("#createActivityBtn").click(function () {
			// initial
			// reset the form
			$("#createActivityForm").get(0).reset();
			// show modal window
			$("#createActivityModal").modal("show");
		});

		$("#saveCreateActivityBtn").click(function () {
			// collect params
			var owner=$("#create-marketActivityOwner").val();
			var name=$.trim($("#create-marketActivityName").val());
			var startDate=$("#create-startTime").val();
			var endDate=$("#create-endTime").val();
			var cost=$.trim($("#create-cost").val());
			var description = $.trim($("#create-describe").val());
			// verify form
			if(owner=="") {
				alert("Owner can't be null");
				return;
			}
			if(name=="") {
				alert("Name can't be null");
				return;
			}
			if(startDate!=""&&endDate!="") {
				if(endDate<startDate){
					alert("End date can't be smaller than start date.");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("The cost must not be negative.");
				return;
			}
			$.ajax({
				url:'workbench/activity/saveCreateActivity.do',
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						$("#createActivityModal").modal("hide");
						// show the first page
						queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
					}else{
						alert(data.message);
						$("#createActivityModal").modal("show");
					}
				}
			});
		});

		$(".mydate").datetimepicker({
			language:'en',
			format:'yyyy-mm-dd',
			minView:'month',
			autoclose:true,
			initialDate:new Date(),
			todayBtn:true,
			clearBtn:true
		});

		queryActivityByConditionForPage(1,10);

		$("#queryActivityBtn").click(function () {
			queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
		});
		
		$("#checkAll").click(function () {
			$("#tBody input[type='checkbox']").prop("checked",this.checked);

		});

		$("#tBody").on("click","input[type='checkbox']",function () {
			if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
				$("#checkAll").prop("checked",true);
			} else{
				$("#checkAll").prop("checked",false);
			}
		});

		$("#deleteActivityBtn").click(function () {
			// get checked boxes
			var checkedIds=$("#tBody input[type='checkbox']:checked");
			if(checkedIds.size()==0) {
				alert("Please choose the activities that you want to delete.");
				return;
			}
			
			if(window.confirm("Are you sure about this operation?")){
				var ids="";
				$.each(checkedIds, function () {
					ids+="id="+this.value+"&";
				});
				ids=ids.substr(0,ids.length-1);
				$.ajax({
					url:'workbench/activity/deleteActivityIds.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data) {
						if(data.code=="1") {
							queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
						} else {
							alert(data.message);
						}
					}
				});
			}
		});

		$("#editActivityBtn").click(function () {
			var checkedIds=$("#tBody input[type='checkbox']:checked");
			if(checkedIds.size()==0) {
				alert("Please choose one activity to modify.");
				return;
			}
			if(checkedIds.size()>1) {
				alert("You can only change one at one time.")
				return;
			}
			var id = checkedIds[0].value;

			$.ajax({
				url:'workbench/activity/queryActivityById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					// put the info into the modal window
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description);

					$("#editActivityModal").modal("show");
				}
			});
		});

		// update
		$("#saveEditActivityBtn").click(function () {
			var id=$("#edit-id").val();
			var owner=$("#edit-marketActivityOwner").val();
			var name=$.trim($("#edit-marketActivityName").val());
			var startDate=$("#edit-startTime").val();
			var endDate=$("#edit-endTime").val();
			var cost=$.trim($("#edit-cost").val());
			var description=$.trim($("#edit-describe").val());

			// verify form
			if(owner=="") {
				alert("Owner can't be null");
				return;
			}
			if(name=="") {
				alert("Name can't be null");
				return;
			}
			if(startDate!=""&&endDate!="") {
				if(endDate<startDate){
					alert("End date can't be smaller than start date.");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("The cost must not be negative.");
				return;
			}

			// send request
			$.ajax({
				url:"workbench/activity/saveEditActivity.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1") {
						$("#editActivityModal").modal("hide");
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption','currentPage'),
								$("#demo_pag1").bs_pagination('getOption','rowsPerPage'))
					} else {
						alert(data.message);
						$("#editActivityModal").modal("show");
					}
				}
			});

		});

		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/exportAllActivities.do"
		});
		
		$("#importActivityBtn").click(function () {
			var activityFileName = $("#activityFile").val();
			var suffix = activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase();
			if(suffix != "xls") {
				alert("Only accept .xls file!");
				return;
			}
			var activityFile = $("#activityFile")[0].files[0];
			if(activityFile.size > 5*1024*1024) {
				alert("The file size cannot be larger than 5MB.");
				return;
			}

			var formData = new FormData();
			formData.append("activityFile", activityFile);

			$.ajax({
				url:'workbench/activity/importActivity.do',
				data:formData,
				processData:false,
				contentType:false,
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code == "1") {
						alert("Successfully import " + data.retData + " records.");
						$("#importActivityModal").modal("hide");
						queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination("getOption", 'rowsPerPage'));
					} else {
						alert(data.message);
						$("#importActivityModal").modal("show");
					}
				}
			});
		});
	});

	function queryActivityByConditionForPage(pageNo, pageSize) {
		// load parameters
		var name=$("#query-name").val();
		var owner=$("#query-owner").val();
		var startDate=$("#query-startDate").val();
		var endDate=$("#query-endDate").val();
		// default
		// var pageNo=1;
		// var pageSize=10;

		$.ajax({
			url:'workbench/activity/queryActivityByConditionForPage.do',
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data) {
				// $("#totalRowsB").text(data.totalRows);
				var htmlStr="";
				$.each(data.activityList,function (index,obj) {
					htmlStr+="<tr class=\"active\">";
					htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"  /></td>";
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.startDate+"</td>";
					htmlStr+="<td>"+obj.endDate+"</td>";
					htmlStr+="</tr>";
				});
				$("#tBody").html(htmlStr);

				$("#checkAll").prop("checked",false);

				var totalPages = 1;
				if(data.totalRows%pageSize==0){
					totalPages = data.totalRows/pageSize;
				} else{
					totalPages = parseInt(data.totalRows/pageSize) + 1;
				}


				$("#demo_pag1").bs_pagination({
					currentPage:pageNo,
					rowsPerPage:pageSize,
					totalPages:totalPages,
					totalRows:data.totalRows,
					visiblePageLinks:5,
					showGoToPage:false,
					showRowsPerPage:true,
					showRowsInfo:true,

					onChangePage: function(event, pageObj) {
						queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		});
	}
	
</script>
</head>
<body>

	<!-- ????????????????????????????????? -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">??????????????????</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="createActivityForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="u">
									  <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">??????</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">??????</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">??????</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- ????????????????????????????????? -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">??????????????????</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id"> <!-- hidden id -->
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="?????????">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">????????????Marketing??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">??????</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">??????</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- ????????????????????????????????? -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">??</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">??????????????????</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        ??????????????????????????????<small style="color: gray;">[?????????.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>????????????</h3>
                        <ul>
                            <li>???????????????Excel????????????????????????XLS????????????</li>
                            <li>?????????????????????????????????????????????</li>
                            <li>????????????????????????????????????5MB???</li>
                            <li>?????????????????????????????????????????????yyyy-MM-dd?????????</li>
                            <li>????????????????????????????????????????????????yyyy-MM-dd HH:mm:ss????????????</li>
                            <li>?????????????????????????????????UTF-8 (?????????)????????????????????????????????????????????????????????????????????????</li>
                            <li>??????????????????????????????????????????????????????????????????????????????</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">??????</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">??????</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>??????????????????</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">??????</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">?????????</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">????????????</div>
					  <input class="form-control" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">????????????</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">??????</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> ??????</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> ??????</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> ??????</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> ??????????????????????????????</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> ????????????????????????????????????</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> ????????????????????????????????????</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll" /></td>
							<td>??????</td>
                            <td>?????????</td>
							<td>????????????</td>
							<td>????????????</td>
						</tr>
					</thead>
					<tbody id="tBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detdetail.jsp>?????????</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detdetail.jsp>?????????</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>

					</tbody>
				</table>
				<div id="demo_pag1"></div>
			</div>


			
<%--			<div style="height: 50px; position: relative;top: 30px;">--%>
<%--				<div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">???<b id="totalRowsB">50</b>?????????</button>--%>
<%--				</div>--%>
<%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">??????</button>--%>
<%--					<div class="btn-group">--%>
<%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--							10--%>
<%--							<span class="caret"></span>--%>
<%--						</button>--%>
<%--						<ul class="dropdown-menu" role="menu">--%>
<%--							<li><a href="#">20</a></li>--%>
<%--							<li><a href="#">30</a></li>--%>
<%--						</ul>--%>
<%--					</div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">???/???</button>--%>
<%--				</div>--%>
<%--				<div style="position: relative;top: -88px; left: 285px;">--%>
<%--					<nav>--%>
<%--						<ul class="pagination">--%>
<%--							<li class="disabled"><a href="#">??????</a></li>--%>
<%--							<li class="disabled"><a href="#">?????????</a></li>--%>
<%--							<li class="active"><a href="#">1</a></li>--%>
<%--							<li><a href="#">2</a></li>--%>
<%--							<li><a href="#">3</a></li>--%>
<%--							<li><a href="#">4</a></li>--%>
<%--							<li><a href="#">5</a></li>--%>
<%--							<li><a href="#">?????????</a></li>--%>
<%--							<li class="disabled"><a href="#">??????</a></li>--%>
<%--						</ul>--%>
<%--					</nav>--%>
<%--				</div>--%>
<%--			</div>--%>
			
		</div>
		
	</div>
</body>
</html>