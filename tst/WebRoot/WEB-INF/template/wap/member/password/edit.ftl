[#include "/wap/include/header.ftl" /]
<script type="text/javascript" src="${base}/statics/js/jfinalshop.validate.js?v=2.6.0.161014"></script>
	<div class="mui-content">
	    <div class="padding bg-white form-wrap login-wrap">
	    	<form class="padding-small" name="resetpassword" action="${base}/wap/member/password/update.jhtml" method="POST">
			<div class="list">
		        <input type="password" class="input" name="oldpassword" placeholder="请输入当前密码" datatype="*" nullmsg="请输入当前密码"/>
			</div>
			<div class="list">
		        <input type="password" class="input" name="newpassword" placeholder="请输入新密码"  datatype="*" nullmsg="请输入新密码"/>
			</div>
			<div class="list">
		        <input type="password" class="input" name="newpassword1" placeholder="请再次输入新密码" datatype="*" nullmsg="请再次输入新密码" recheck="newpassword"/>
			</div>
		        <input type="submit" class="mui-btn full line78" value="保存密码" />
		    </form>
	    </div>
	</div>
	<footer class="footer posi">
		<div class="mui-text-center copy-text">
			<span></span>
		</div>
	</footer>
	<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
</body>
</html>
<script>
var resetpassword = $("form[name=resetpassword]").Validform({
	ajaxPost:true,
	callback:function(ret){
		if(ret.status == 0){
			$.tips({content:ret.message});
		}else{
			$.tips({content:ret.message});
			window.location.href=ret.referer;
		}
	}
});
</script>
