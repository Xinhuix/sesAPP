[#include "/wap/include/header.ftl" /]
<style>
			body {
				background-color: #f5f5f5;
			}
		</style>
		<div class="mui-content">
			<ul class="member-address mui-table-view layout-list-common margin-none mui-clearfix" style="background-color: #f2f2f2;">
				[#if pages?? && pages.list?has_content]
					[#list pages.list as receiver]
			        <li class="address-list">
						
						[#if receiver.isDefault]
							<a href="${base}/wap/member/receiver/edit.jhtml?id=${receiver.id}" class="mui-block mui-navigate-right padding-big-right">
				        	<div class="address-text">
				        		<span class="address_c_tit">${receiver.consignee}　${receiver.phone}</span>
			        			
				        		<p class="address_info">[默认]${receiver.area_name}  ${receiver.address} [#if receiver.zipCode?has_content]  邮编：${receiver.zipCode}[/#if]</p>
								
				        	</div>
							</a>
							<div class="setaddress">
							<div class="hd-radio item-radio">
			    				<input name="isdefault" type="radio" checked="checked">
			    			</div> 默认地址
							</div>
						[#else]
							<a href="${base}/wap/member/receiver/edit.jhtml?id=${receiver.id}" class="mui-block mui-navigate-right padding-big-right">
							<div class="address-text" data-event="default">
				        		<span class="address_c_tit">${receiver.consignee}　${receiver.phone}</span>
			        		
				        		<p class="address_info">${receiver.area_name}  ${receiver.address} [#if receiver.zipCode?has_content]  邮编：${receiver.zipCode}[/#if]</p>
								
				        	</div>
							</a>
							<div class="setaddress">
							<div class="hd-radio item-radio">
			    				<input name="isdefault" type="radio" >
			    			</div> 设为默认
							</div>
						[/#if]
						
			        </li>
			        [/#list]
				[/#if]
		    </ul>
	    	<div class="padding-lr margin-top">
	    		<a href="${base}/wap/member/receiver/add.jhtml" class="mui-btn mui-btn-primary full hd-h4 line78" style="color:#fff">添加新收货地址</a>
	    	</div>			
		</div>
		<footer class="footer posi">
			<div class="mui-text-center copy-text">
				<span></span>
			</div>
		</footer>

		<script type="text/javascript" src="${base}/statics/js/order.js?v=2.6.0.161014"></script>
		<script>
			/* 添加新收货地址 */
			mui("body").on("tap", '#address_add', function() {
				window.location.href = '/wap/member/receiver/add.jhtml';
			})
		</script>
		<div id="cli_dialog_div"></div>
		<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
	</body>

</html>