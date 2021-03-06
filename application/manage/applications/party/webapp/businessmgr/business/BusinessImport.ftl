<script type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/upload/ajaxupload.js</@ofbizContentUrl>"></script>

<!-- 内容start -->
<div class="box box-info">
	<div class="box-body">
		<!-- 按钮组start -->
		<div class="row m-b-10">
			<div class="col-sm-6 dp-tables_btn">
				<button id="downTemplate" type="button" class="btn btn-primary">下载模板</button>
	  			<button id="inventoryImport" type="button" class="btn btn-primary">商家导入</button>
			</div>
		</div><!-- 按钮组end -->

		<!-- 分割线start -->
		<div class="cut-off-rule bg-gray"></div><!-- 分割线end -->
		
		<!-- 注意事项start -->
		<div class="row" id="hint">
			<div class="col-xs-12">
				<div class="box" style="border-top:0px">
		            <div class="box-header">
		              <h3 class="box-title"><icon class="fa fa-exclamation"></icon>注意事项</h3>
		            </div><!-- /.box-header -->
		            <div class="box-body no-padding" align="left">
		              <table class="table table-condensed" >
		                <tbody>
		                <tr>
		                  <td style="text-align:left">1.请先下载导入模板Excel，按照模板内容填写</td>
		                </tr>
		                <tr>
		                  <td style="text-align:left">2.请勿调整模板Excel中的sheet页位置及名称</td>
		                </tr>
		                <tr>
		                  <td style="text-align:left">3.请仔细检查模版需要的编号字段是否存在，避免不必要的错误</td>
		                </tr>
		                <tr>
		                  <td style="text-align:left">4.如若有大量数据需要导入，建议分批次进行，可以提升导入效率</td>
		                </tr>
		              </tbody></table>
		            </div><!-- /.box-body -->
		        </div>
			</div>
		</div><!-- 注意事项end -->

		
	</div> 
</div><!-- 内容end --> 

<!-- 导入弹出框start -->
<div id="modal_import"  class="modal fade " tabindex="-1" role="dialog" aria-labelledby="modal_import_title">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="modal_import_title">商家导入</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-sm-2">${uiLabelMap.SelectFile}:</label>
						<div class="col-sm-10 uploadFile">
							<input type="text" class="form-control w-p80" style="float: left" disabled id="doc">
							<input type="hidden" id="hidFileName" />
							<input type="button" class="btn btn-default m-l-5" id="btnUpload" value="${uiLabelMap.Upload}" />
						</div>                
					</div>
		        </form>
			</div>
			<div class="modal-footer">
				<button id="upload" type="button" class="btn btn-primary">${uiLabelMap.Import}</button>
				<button id="cancel" type="button" class="btn btn-default" data-dismiss="modal">${uiLabelMap.BrandCancel}</button>
			</div>
		</div>
	</div>
</div><!-- 导入弹出框end -->

<!-- 导入错误提示框start -->
<div id="modal_error"  class="modal fade " tabindex="-1" role="dialog" aria-labelledby="modal_error_title">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="modal_error_title">${uiLabelMap.Error}</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<!-- 错误提示start -->
					<div class="row" id="error_list">
						<div class="col-xs-12">
							<div class="box" style="border-top:0px">
					            <div class="box-header">
					              <h3 class="box-title" style="color: red">
					              	<icon class="fa fa-times-circle"></icon>${uiLabelMap.ImportFail}
					              </h3>
					            </div><!-- /.box-header -->
					            <div class="box-body no-padding" style="border: 1px solid #ddd;overflow-y: auto;height: 350px;" align="left">
					              <table class="table table-condensed" >
					                <tbody>
					              	</tbody>
					              </table>
					            </div><!-- /.box-body -->
					        </div>
						</div>
					</div><!-- 错误提示end -->
		        </form>
			</div>
			<div class="modal-footer">
				<button id="importAgain" type="button" class="btn btn-primary">${uiLabelMap.ImportAgain}</button>
				<button id="cancel" type="button" class="btn btn-default" data-dismiss="modal">${uiLabelMap.BrandCancel}</button>
			</div>
		</div>
	</div>
</div><!-- 导入错误提示框end -->

<!-- 提示弹出框start -->
<div id="modal_msg"  class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="modal_msg_title">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="modal_msg_title">${uiLabelMap.FacilityOptionMsg}</h4>
      </div>
      <div class="modal-body">
        <h4 id="modal_msg_body"></h4>
      </div>
      <div class="modal-footer">
        <button id="ok" type="button" class="btn btn-primary" data-dismiss="modal">${uiLabelMap.FacilityOk}</button>
      </div>
    </div>
  </div>
</div><!-- 提示弹出框end -->
 
<!-- script区域start --> 
<script>
	// 创建一个上传参数
	var uploadOption =
	{
	    action: "businessImport",    // 提交目标
	    name: "file",            // 服务端接收的名称
	    autoSubmit: false,        // 是否自动提交
	    // 选择文件之后…
	    onChange: function (file, extension) 
	    {
	        $('#doc').val(file);
	    },
	    // 开始上传文件
	    onSubmit: function (file, extension)
	    {
	        if (!(extension && /^(xls|XLS|xlsx|XLSX)$/.test(extension))) {  
                alert("只支持xls或xlsx格式文件！");  
                return false;  
            }
	    },
	    // 上传完成之后
	    onComplete: function (file, response) 
	    {
	    	var data = eval('(' + response + ')')
	    	if(data.success){
	    		$('#modal_import').modal('hide');
	    		//设置提示弹出框内容
				$('#modal_msg #modal_msg_body').html(data.successMsg);
				$('#modal_msg').modal();
	    	}else{
	    		$('#error_list table tbody').empty();
	    		$('#error_list .box-header .box-title small').empty();
				$.each(data.errorMsg, function(){     
					$('#error_list table tbody').append("<tr><td style='text-align:left;color:red'>"+this.msg+"</td></tr>");    
				});
				$('#error_list .box-header .box-title').append("<small style='color:red'>（共"+data.errorMsg.length+"条错误）</small>");
				$('#modal_import').modal('hide');
				$('#modal_error').modal();  
	    	}
	    }
	}

	$(function(){
		//下载模板按钮点击事件
		$('#downTemplate').click(function(){
			window.location="<@ofbizContentUrl>/images/importTemplate/Business/BusinessTemplate.xlsx</@ofbizContentUrl>";
		});

		//导入按钮点击事件
		$('#inventoryImport').click(function(){
			$('#doc').val("");
			$('#modal_import').modal();
		});

		// 初始化图片上传框
		var au = new AjaxUpload($('#btnUpload'), uploadOption);

		//导入按钮点击事件
		$('#upload').click(function(){
			au.submit();
		});
		
		//重新导入按钮点击事件
		$('#importAgain').click(function(){
			$('#modal_error').modal("hide");  
			$('#inventoryImport').trigger("click");
		});
	});
</script><!-- script区域end -->
