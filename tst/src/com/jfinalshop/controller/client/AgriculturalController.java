package com.jfinalshop.controller.client;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.util.TextUtils;
import org.apache.poi.ss.formula.functions.T;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.entity.ProductImage;
import com.jfinalshop.model.AgriculturalProduction;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.client.CommonSTUC;


@ControllerBind(controllerKey="/client/agricultural")
public class AgriculturalController extends Controller{

	private final int MAXSize =4*1024*1024;
	
	/**
	 * 入驻提交
	 * @param
	 *       name 企业/品牌名称
	 *       areaid 地区id
	 *       areaname 所在地区
	 *       address  企业/店铺地址
	 *       product_type 产品类型
	 *       major_good 主营产品
	 *       product_certification 产品认证
	 *       certification_url 4.0农业可视认证
	 *       business_licence 营业执照
	 *       other_licence 其他证书
	 *       base_picture 基地照片
	 *       vender_picture 厂家照片
	 *       store_picture 店铺照片
	 *       product_images 产品图片
	 *       linkman 联系人
	 *       telephone 联系电话
	 *       remark 备注
	 *       create_time 创建时间
	 *       update_time 更新时间
	 * @return
	 *       无
	 */
	/*
	 * 初始化--商家入驻
	 */
	public void init() {
		Map<String,Object> result=new HashMap<>();			
		try {
						List<ProductCategory> categorys=ProductCategory.dao.find("select id,name from product_category ");
						result.put("category", categorys);
						result.put("status", CommonSTUC.Status.Success.getIndex());
					}catch(Exception e) {
						result.put("status", CommonSTUC.Status.Error.getIndex());
					}finally {
						this.renderJson(result);
					}
	}
	
	public void submit() {
	  Map<String,Object> result=new HashMap<>();
		try {
			String mid=this.getPara("mid");
			String storeType=this.getPara("storeType");
			String name=this.getPara("name");
			String address=this.getPara("address");
			String product_type=this.getPara("pruductType");
			String major_good=this.getPara("mainOperation");
			String linkman=this.getPara("owner");
			String mobile=this.getPara("telephone");
			String remark=this.getPara("markMsg");
			String lon=this.getPara("lon");
			String lat=this.getPara("lat");
			String detail=this.getPara("detail");
			SesStore ss=new SesStore();
			ss.setTypeId(Integer.valueOf(storeType));
			ss.setSname(name);
			ss.setAddr(address);
			ss.setProductType(product_type);
			ss.setMainOperation(major_good);
			ss.setOwner(linkman);
			ss.setContactMobile(mobile);
			ss.setMarkMsg(remark);
			ss.setScore(BigDecimal.valueOf(5));
			ss.setJoindate(new Date());
			ss.setLng(StringUtils.isEmpty(lon)?0:Float.valueOf(lon));
			ss.setLat(StringUtils.isEmpty(lat)?0:Float.valueOf(lat));
			ss.setStatus(SesStore.Status.Verify.ordinal());
			ss.setMemberId(Long.valueOf(mid));
			ss.setDetail(detail);
			if(ss.save()) {
				result.put("status", CommonSTUC.Status.Success.getIndex());
				result.put("id",ss.getId() );
			}else {
				result.put("status",CommonSTUC.Status.Fail.getIndex());
			}
	}catch(Exception e) {
		e.printStackTrace();
		  result.put("status", CommonSTUC.Status.Error.getIndex());
	}finally {
		this.renderJson(result);
	}
	}	
	
	public void upload() {
		this.renderJsp("/WEB-INF/template/admin/clientmanager/test.jsp");
	}
	
	public void upLoadImg() {
		Map<String,Object> result=new HashMap<>();
		long size=2048*1024*2;
		try {
			List<UploadFile> files=this.getFiles();
			String id=this.getPara("id");
			SesStore ss=SesStore.dao.findById(id);
			if(ss==null||ss.getSname()==null) {
				result.put("status", CommonSTUC.Status.NoExist.getIndex());
				return;
			}
			String base=this.getRequest().getServletContext().getRealPath("");
			String middle="/upload/store/merchant/";
			if(!new File(base+middle).exists()) {
				new File(base+middle).mkdirs();
			}
			String storeImg="";
			String operationImg="";
			for(UploadFile file:files) {
				if(file.getFile().length()>size) {
					result.put(file.getParameterName(), CommonSTUC.Status.Fail.getIndex());
					result.put("status", CommonSTUC.Status.InComplete.getIndex());
					continue;
				}
				String filename=UUID.randomUUID().toString();
				File of=file.getFile();
				String fn=of.getName();
				String last=fn.substring(fn.lastIndexOf("."));
				File nf=new File(base+middle+filename+last);
				of.renameTo(nf);
				if(of.length()>nf.length()) {
					result.put(file.getParameterName(), CommonSTUC.Status.Fail.getIndex());
					result.put("status", CommonSTUC.Status.InComplete.getIndex());
					continue;
				}
				of.delete();
				if(file.getParameterName().contains("operationLicence")) {
					String subfile=","+middle+filename+last;
					operationImg+=subfile;
				}else if(file.getParameterName().contains("storeImg")) {
					String subfile=","+middle+filename+last;
					storeImg=storeImg+subfile;
				}
				ss.setOperationLicence(operationImg);
				ss.setStoreImg(storeImg);
				result.put(file.getParameterName(), CommonSTUC.Status.Success.getIndex());
			}
			ss.update();
			result.put("status", CommonSTUC.Status.Success.getIndex());
		}catch(Exception e) {
			e.printStackTrace();
			result.put("status", CommonSTUC.Status.Error.getIndex());
		}finally {
			this.renderJson("result",result);
		}
	}	
	
	public void getProgress() {
		Map<String,Object> result=new HashMap<>();
		try {
			   String mid=this.getPara("mid");
			   List<SesStore> ss=SesStore.dao.find("select  status   from ses_store where member_id=?",mid);
			  if(ss.size()<=0) {
				  result.put("status", CommonSTUC.Status.NoExist.getIndex());
				  return;
			  }
			  result.put("status", CommonSTUC.Status.Success.getIndex());
			  result.put("storeProgress",ss.get(0).getStatus());
		}catch(Exception e) {
			result.put("status", CommonSTUC.Status.Error.getIndex());
		}finally {
			this.renderJson(result);
		}
		
	}
	public void myStore() {
		  Map<String,Object> result=new HashMap<>();
		  try {
			  String mid=this.getPara("mid");
			  	List<Record> ss=Db.find("select  s.*,c.name from ses_store s,product_category  where  s.product_type=c.id and  member_id=?",mid);
			  		if(ss.size()<=0) {
			  			result.put("status",CommonSTUC.Status.NoExist.getIndex());
			  			return;
			  		}
			  		String area="";
			  		String areaId=ss.get(0).get("area_id");
			  		while(true) {
			  			  Area a=Area.dao.findById(areaId);
			  			 if(a==null||TextUtils.isEmpty(a.getName()))break;
			  			 area=a.getName()+area;
			  			 areaId=a.getParentId()+"";
			  		}
			  		ss.get(0).set("area", area);
			  		result.put("store", ss.get(0));
		  }catch(Exception e) {
			  result.put("status", CommonSTUC.Status.Error.getIndex());
		  }finally {
			  this.renderJson(result);
		  }
	}
		
}
