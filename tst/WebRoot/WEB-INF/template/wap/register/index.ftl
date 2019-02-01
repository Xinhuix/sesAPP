[#include "/wap/include/header.ftl" /]
<script type="text/javascript" src="${base}/statics/js/jfinalshop.validate.js?v=2.6.0.161014"></script>
<script type="text/javascript" src="${base}/statics/js/jquery.cookie.js?v=2.6.0.161014"></script>
	<div class="mui-content">
	    <div class="padding bg-white login-wrap">
	    	<form class="padding-small" action="${base}/wap/register/submit.jhtml" name="register" method="post">
	    	 	<div class="list">
<input type="tel" class="input"  name="mobile" placeholder="请输入手机号" datatype="mobile" ajaxurl="${base}/wap/register/checkUsername.jhtml" nullmsg="请输入手机号"/>
		        </div>
		        <div class="list">
		        	<input type="password" name="password" class="input" placeholder="请输入登陆密码" datatype="*" ajaxurl="" nullmsg="请输入登陆密码"/>
		        </div>
		        <div class="list">
		        	<input type="password" name="pwdconfirm" class="input" placeholder="请确认您的密码" datatype="*" nullmsg="请输入确认密码" recheck="password" />
		        </div>
				<div class="list relative">
		        	<input type="text" class="number" placeholder="请输入手机验证码" name="vcode" disabled="disabled" nullmsg="请输入验证码"/>
		        	<button class="mui-btn mui-btn-primary ver-code" id="sendsms">发送验证码</button>
		        </div>
				<div class="list">
		        	<input type="text" name="email" class="input" placeholder="请输入邮箱（必填）" datatype="email" nullmsg="请输入邮箱"  />
		        </div>
		        
		       <div class="list">
<input type="text" name="reccode" value="${reccode!}" class="input" placeholder="请输入邀请码（选填）" ajaxurl="${base}/wap/register/checkreccode.jhtml" nullmsg="请输入正确的邀请码"/>
		        </div>
		        
				<input type="hidden" name="url_forward" value="/wap/member.jhtml">
		        <input type="submit" class="mui-btn full line78" value="立即注册" />
		        <a class="mui-btn full margin-top margin-bottom mui-btn-danger" href="${base}/wap/login/login.jhtml"  style="color: #259b24;">已有账号？登录</a>
		    </form>
	    </div>
		<div id="cli_dialog_div"></div>
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
var register = $("form[name=register]").Validform({
	showAllError:true,
	ajaxPost:true,
	beforeCheck: function() {
		if($("input[name=vcode]").val() == '') {
			$.tips({
				content: '验证码不能为空'
			});
			$("input[name=vcode]").focus();
			return false;
		}
	},
	callback:function(ret) {
		if(ret.status == 0) {
			$.tips({
				icon:'error',
				content:ret.message,
				callback:function() {
					return false;
				}
			});
		}else{
			$.tips({
				icon:'success',
				content:ret.message
			});
			window.location.href=ret.referer;
		}
	}
});
 $("input[name=reccode]").bind('blur',function(){
	var ajaxurl = $("input[name=reccode]").attr('ajaxurl');
	if($("input[name=reccode]").val() != '') {
		$.post(ajaxurl,{'reccode':$(this).val()},function(data){
			$.tips({content: data.message});
		},'json');
	}
}) 
/*仿刷新：检测是否存在cookie*/
if($.cookie("reg_captcha")){
	reget($.cookie("reg_captcha"));
}
//发送验证码
$("#sendsms").bind("click",function(){
	var mobile = $('input[name="mobile"]').val();
	var checkurl = "${base}/wap/register/checkUsername.jhtml";
	$.post(checkurl,{name:'mobile',param:mobile},function(ret){
		if(ret.status == 1){
			var ajaxurl="${base}/wap/register/send.jhtml";
			$.post(ajaxurl,{'mobile':mobile},function(data){
				//$.tips({content:'测试环境验证码：' + data});
			},'json');
			$("input[name=vcode]").removeAttr("readonly disabled");
			reget(5);
		}else{
			$.tips({content:ret.message});
		}
	},'json');
})
function reget(count){
	  var mobj = $('input[name="mobile"]');
		var btn = $("#sendsms");
		var count = count;
		var resend = setInterval(function(){
			count--;
			if (count > 0){
				btn.text(count+"s后再试");
				mobj.attr('readonly',true);
				$.cookie("reg_captcha", count, {path: '/', expires: (1/86400)*count});
			}else {
				clearInterval(resend);
				mobj.removeAttr('disabled readonly');
				btn.text("重获验证码").removeAttr('disabled').css({'cursor':'','background':'#259b24'});
			}
		}, 1000);

  btn.attr('disabled',true).css({'cursor':'not-allowed','background':'#989898'});
}
</script>