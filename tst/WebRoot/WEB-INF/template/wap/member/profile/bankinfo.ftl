[#include "/wap/include/header.ftl" /]
[#if cur??&&cur=='1']
<script>$.tips({content: '提交成功!'});</script>[/#if]
		<div class="mui-content">
			<ul class="mui-table-view layout-list-common user-info-detail margin-none">
				<form id="inputForm" action="update.jhtml" method="post" style="padding-top:.2rem;padding-bottom:.2rem;">
						
						<style>
						
						table.input { width:90%; margin:5%}
						table.input  th {text-align:right; padding-right:.2rem; width:29%;}
						</style>
						
						<table class="input">
							<tr>
								<th>
									${message("Member.nickname")}:
								</th>
								<td>
									<input type="text" name="nickname" class="text" value="${member.nickname}" maxlength="200" />
								</td>
							</tr>
							[@member_attribute_list]
								[#list memberAttributes as memberAttribute]
									<tr>
										<th>
											[#if memberAttribute.isRequired]<span class="requiredField">*</span>[/#if]${memberAttribute.name}:
										</th>
										<td>
											[#if memberAttribute.typeName == "name"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.name}" maxlength="200" />
											[#elseif memberAttribute.typeName == "gender"]
												<span class="fieldSet">
													[#list genders as gender]
														<label>
															<input type="radio" name="memberAttribute_${memberAttribute.id}" value="${gender}"[#if gender == member.genderName] checked="checked"[/#if] />${message("Member.Gender." + gender)}
														</label>
													[/#list]
												</span>
											[#elseif memberAttribute.typeName == "birth"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.birth}" onfocus="WdatePicker();" />
											[#elseif memberAttribute.type == "area"]
												<span class="fieldSet">
													<input type="hidden" id="areaId" name="memberAttribute_${memberAttribute.id}" value="${(member.area.id)!}" treePath="${(member.area.treePath)!}" />
												</span>
											[#elseif memberAttribute.typeName == "address"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.address}" maxlength="200" />
											[#elseif memberAttribute.typeName == "zipCode"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.zipCode}" maxlength="200" />
											[#elseif memberAttribute.typeName == "phone"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.phone}" maxlength="200" />
											[#elseif memberAttribute.typeName == "mobile"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.mobile}" maxlength="200" />
											[#elseif memberAttribute.typeName == "text"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.getAttributeValue(memberAttribute)}" maxlength="200" />
											[#elseif memberAttribute.typeName == "select"]
												<select name="memberAttribute_${memberAttribute.id}">
													<option value="">${message("shop.common.choose")}</option>
													[#list memberAttribute.optionsConverter as option]
														<option value="${option}"[#if option == member.getAttributeValue(memberAttribute)] selected="selected"[/#if]>
															${option}
														</option>
													[/#list]
												</select>
											[#elseif memberAttribute.typeName == "checkbox"]
												<span class="fieldSet">
													[#list memberAttribute.options as option]
														<label>
															<input type="checkbox" name="memberAttribute_${memberAttribute.id}" value="${option}"[#if (member.getAttributeValue(memberAttribute)?seq_contains(option))!] checked="checked"[/#if] />${option}
														</label>
													[/#list]
												</span>
											[/#if]
										</td>
									</tr>
								[/#list]
							[/@member_attribute_list]
							
						</table>
						<div style="text-align:center;"><input type="submit" class="mui-btn  mui-btn-primary hd-h4 line78" value="${message("shop.member.submit")}" style="color:#fff; width:40%; margin:.1rem; border:solid 1px #259b24" />
									<a href="${base}/wap/member/profile/edit.jhtml" class="mui-btn  mui-btn-primary hd-h4 line78" value="${message("shop.member.submit")}" style="color:#259b24; background-color:#fff; width:40%; margin:.1rem; border:solid 1px #259b24">${message("shop.member.back")}</a></div>
					</form>
				
				
			</ul>
		</div>
		<footer class="footer posi">
			<div class="mui-text-center copy-text">
				<span></span>
			</div>
		</footer>
		<div id="cli_dialog_div"></div>
		
		
		<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
	</body>

</html>
		<link type="text/css" rel="stylesheet" href="${base}/statics/js/upload/uploader.css?v=2.6.0.161014">
		<script type="text/javascript" src="${base}/statics/js/upload/uploader.js?v=2.6.0.161014"></script>
		<script>
			var uploader = WebUploader.create({
				auto: true,
				chunked: false,
				fileVal: 'upfile',
				// 允许上传的类型
				accept: {
					title: '图片文件',
					extensions: 'jpg,jpeg,png,gif,bmp',
					mimeType: 'image/*'
				},
				// 指定选择文件的按钮容器
				pick: {
					id: '#file-avatar',
					multiple: false
				},
				// swf文件路径
				swf: '${base}/statics/js/upload/uploader.swf',
				// 文件接收服务端
				server: '/wap/member/upload.jhtml',
				// 附加参数
				formData: {
					file: 'upfile',
					//upload_init: '08cbU0bW1SwiOAWtMjtn1/Px3CAi69lIcgmcGLnLKcTQ7I1vBfxGtApuWNbsPWwD/N8+oJa+zSA/93AWQK2Guu9fHCdSzW3su8BTmKRTycD1ksXQaDMcInRgyDMIcMK4Z98aqLWWzBW2XSskSxBvd0T6vu0USQFoQ3umKTFCGmUNxJirxpC7WBLpG9xzCkF8vj4xiOY'
				},
				// 压缩图片
				compress: {
					width: 408,
					height: 408,
					allowMagnify: false
				}
			});
			uploader.onFileQueued = function(file) {
				$.tips({
					content: '图片上传中...'
				});
			}
			uploader.onUploadError = function(file, reason) {
				$.tips({
					content: '上传失败'
				});
			}
			uploader.onUploadSuccess = function(file, response) {
				if(response.status == 1) {
					/* if(response.result.width < 200 || response.result.height < 200) {
						$.tips({
							content: '图片分辨率至少是200*200'
						});
						return false;
					} */
					window.location.reload();
					/* var ajaxurl = '/index.php?m=member&c=account&a=avatar';
					$.post(ajaxurl, {avatar: response.result.url, w: response.result.width, h: response.result.height}, function(ret) {
						if(ret.status == 1) {
							window.location.href = ret.referer;
						} else {
							$.tips({
								content: ret.message
							});
							return false;
						}
					}, 'json'); */
					return true;
				} else {
					$.tips({
						content: response.message
					});
					return false;
				}
			}
		</script>
