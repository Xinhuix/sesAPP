package com.jfinalshop.controller.usercenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.model.CommonProblem;

import cn.com.infosec.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;
import freemarker.template.utility.StringUtil;
@ControllerBind(controllerKey="/OnlineService")
public class OnlineService extends Controller{
	/**
	 * 常见问题
	 */
	public void CommonProblems() {
		Map<String, Object> map = new HashMap<>();
		try {
			
			List<CommonProblem> commonProblem =CommonProblem.dao.find("select a.problem  , b.problem as bproblem,b.answer,b.answerimgs \r\n" + 
					"from common_problem a left join common_problem b on b.parent_id = a.id\r\n" + 
					"where b.parent_id is not null ");
			map.put("commonProblem", commonProblem);
			map.put("stauts", 1);
			this.renderJson(map);	
		} catch (Exception e) {
			map.put("stauts", 0);
			this.renderJson(map);
		}
	
	}
	
	
	/**
	 * 导入
	 */
	public void Inalluser() {
      		UploadFile uploadFile= getFile();
      	if(uploadFile==null) {
      		this.renderJson("error");
      		return;
      	}	
      	try {
      		
      		 File file=uploadFile.getFile();//获取文件对象
      		 
      		 FileInputStream inputStream = new FileInputStream(file);
      		
      		 XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
      		 //表
      		 XSSFSheet sheet = workbook.getSheet("Sheet1");
      		
      		 int lastRowNum = sheet.getLastRowNum();
      		
      	    //定义一个list来接收数值
            List<CommonProblem> addCommonProblem = new ArrayList<CommonProblem>();
            //定义一个list来接收修改数值
            List<CommonProblem> updateCommonProblem = new ArrayList<CommonProblem>();
            //创建时间  
      		Date date= new Date();
      		List<CommonProblem> list =CommonProblem.dao.find("select id,version from common_problem");
            for(int i =1 ;i<=lastRowNum; i++) {
              XSSFRow	row= sheet.getRow(i);
              //添加对象
              CommonProblem add = new CommonProblem();
              //修改对象
              CommonProblem updete = new CommonProblem();
              //是否修改对象
              int isadddispdate =0;
              //是否相同对象
              int isIdTheSame =0;
              //格式化
              if(row.getCell(0)==null||row.getCell(1)==null) {
            	 this.renderJson("id和版本不能为空,id和版本不能为空,id和版本不能为空！！！");
            	 return;
              }
              row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
              row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
             
              
              //默认值
              Integer sjkVersion =0;
              Long  sjkId=0L;
            //如果当row长度大于数据库查询的长度,那么赋默认值
              if(i>list.size()) {
            	   sjkVersion=0;
            	   sjkId=0L;
              }else {
                  //数据库id
                  sjkId = list.get(i-1).getId();
                  //数据库版本号
                  sjkVersion = Integer.valueOf(list.get(i-1).getVersion());
			 }
              
        	  //文档id
              Long    rowid = Long.valueOf(row.getCell(0).getStringCellValue());
              //文档版本
              Integer  oldVersion = Integer.valueOf(row.getCell(1).getStringCellValue());
              
              
              //判断是否修改
              if(rowid==sjkId&&oldVersion!=sjkVersion) {
        		  System.out.println("修改"+i);
        		  isadddispdate++;
        	  } 
              //判断是否相同
              if(rowid==sjkId) {
            	  System.out.println("相同"+i);
            	  isIdTheSame++;
              }
              
                //判断是否修改
            	if(isadddispdate!=0) {
            		System.out.println("进入修改"+i);
            		//修改id
            	   if(row.getCell(0)==null) {
                   this.renderJson("id怎么能为空呢？嗯？脑子瓦塔拉？");
                   return;
                   }else {
                   updete.setId(rowid);
                   updete.setModifyDate(date);
				   }
            		 
            		//版本
                   if(row.getCell(1)==null) {
                   this.renderJson("版本怎么能为空呢？嗯？脑子瓦塔拉？");
                   return;
                   }else {
                   updete.setVersion(String.valueOf(oldVersion));
            	   }
                     
                    //问题
                   if(row.getCell(2)==null) {
                   this.renderJson("问题怎么能不写呢？嗯？");
                   return;
                   }else {
                   updete.setProblem(row.getCell(2).getStringCellValue());
                   }
                     
                    //答案
                    if(row.getCell(3)==null) {
                   	 System.out.println("答案为空"+i);
                    }else {
                    updete.setAnswer(row.getCell(3).getStringCellValue());
                    } 
                    
                   //对应父id
                    if(row.getCell(4)==null) {
                  	  System.out.println("空父id"+i);
                    }else {
                  	row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                    updete.setParentId(row.getCell(4).getStringCellValue());
                    }
              	
                   //图片
                    if(row.getCell(5)==null) {
                  	  System.out.println("图片为空"+i);
                    }else {
                    updete.setAnswerimgs(row.getCell(5).getStringCellValue());
                    }
            	}
            	
              //判断是否添加 	
              if(isIdTheSame==0) { 
            	System.out.println("进入添加"+i);
            	//修改id
         	    if(row.getCell(0)==null) {
                this.renderJson("id怎么能为空呢？嗯？脑子瓦塔拉？");
                return;
                }else {
                	System.out.println("添加id");
                add.setId(rowid);
                add.setCreateDate(date);
                add.setModifyDate(date);
				}
         		 
         		//版本
                if(row.getCell(1)==null) {
                this.renderJson("版本怎么能为空呢？嗯？脑子瓦塔拉？");
                return;
                }else {
                add.setVersion(String.valueOf(oldVersion));
         	   }
                  
                 //问题
                if(row.getCell(2)==null) {
                this.renderJson("问题怎么能不写呢？嗯？");
                return;
                }else {
                add.setProblem(row.getCell(2).getStringCellValue());
                }
                  
                 //答案
                if(row.getCell(3)==null) {
                	 System.out.println("答案为空"+i);
                }else {
                add.setAnswer(row.getCell(3).getStringCellValue());
                } 
                 
                //对应父id
                if(row.getCell(4)==null) {
               	  System.out.println("空父id"+i);
                }else {
               	row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                add.setParentId(row.getCell(4).getStringCellValue());
                }
           	
                //图片
                if(row.getCell(5)==null) { 
               	  System.out.println("图片为空"+i);
                }else {
                add.setAnswerimgs(row.getCell(5).getStringCellValue());
                }
              }
            
            	//把添加的值存入集合
                if(add.getId()==null) {
          	      System.out.println("添加为空"+i);
                }else {
          	    addCommonProblem.add(add);
                }
                //把要修改的存入集合
                if(updete.getId()==null) {
                 System.out.println("修改为空"+i);
                }else {
          	    updateCommonProblem.add(updete);
                }
            }
            System.out.println("这是添加的集合:"+addCommonProblem); 
            System.out.println("这是修改的集合:"+updateCommonProblem);
          
         
            
           // String sql= "insert into user(id, create_date,modify_date,version,answerimgs,problem,answer,parent_id) values(?, ?, ? , ? , ? , ? , ? , ?)";
          //  Db.batch(sql, "id, create_date,modify_date,version,answerimgs,problem,answer,parent_id", addCommonProblem,500);
             CommonProblem com = new CommonProblem();
            //判断添加集合是否有值
             if(addCommonProblem.size()!=0) {
             for(int i = 0; i < addCommonProblem.size();i ++) {
            	com.setId(addCommonProblem.get(i).getId());
            	com.setCreateDate(addCommonProblem.get(i).getCreateDate());
            	com.setModifyDate(addCommonProblem.get(i).getModifyDate());
            	com.setVersion(addCommonProblem.get(i).getVersion());
            	com.setProblem(addCommonProblem.get(i).getProblem());
            	if(addCommonProblem.get(i).getAnswer()!=null) {
            		com.setAnswer(addCommonProblem.get(i).getAnswer());
            	}else {
            		com.setAnswer(null);
				}
            	if(addCommonProblem.get(i).getAnswerimgs()!=null) {
            		com.setAnswerimgs(addCommonProblem.get(i).getAnswerimgs());
            	}else {
            		com.setAnswerimgs(null);
				}
            	if(addCommonProblem.get(i).getParentId()!=null) {
            		com.setParentId(addCommonProblem.get(i).getParentId());
            	}else {
					com.setParentId(null);
				}
            	com.save();
               }
             }
             //判断修改集合是否有值
             if(updateCommonProblem.size()!=0) {
                 for(int i = 0; i < updateCommonProblem.size();i ++) {
                	 com.setId(updateCommonProblem.get(i).getId());
                	//修改时间
                	com.setModifyDate(updateCommonProblem.get(i).getModifyDate());
                	//版本号
                	com.setVersion(updateCommonProblem.get(i).getVersion());
                	//问题
                	com.setProblem(updateCommonProblem.get(i).getProblem());
                	//答案
                	if(updateCommonProblem.get(i).getAnswer()!=null) {
                		com.setAnswer(updateCommonProblem.get(i).getAnswer());
                	}/*else {
                		com.setAnswer(null);
    				}*/
                	if(updateCommonProblem.get(i).getAnswerimgs()!=null) {
                		com.setAnswerimgs(updateCommonProblem.get(i).getAnswerimgs());
                	}/*else {
                		com.setAnswerimgs(null);
    				}*/
                	if(updateCommonProblem.get(i).getParentId()!=null) {
                		com.setParentId(updateCommonProblem.get(i).getParentId());
                	}/*else {
    					com.setParentId(null);
    				}*/
                	com.update();
                   }
                 } 
            
            this.renderJson(addCommonProblem);
        	} catch (Exception e) {
      		System.out.println(e);
      		e.printStackTrace();
      		 this.renderJson("错误"+e);
		
		}
      	
	}
	
	//导出
	public void ProblemInformation() throws IOException{
		try {
	    List<Record> list = Db.find("select * from common_problem");
	    String[] header={"id","创建时间","更新时间","版本号","图片","问题","答案","父id"};
	    String[] columns={"id","create_date","modify_date","version","answerimgs","problem","answer","parent_id"};
	    Render poirender = PoiRender.me(list).fileName("ProblemInformation.xls").headers(header).sheetName("在线客服表").columns(columns);
        this.render(poirender);
		} catch (Exception e) {
		this.renderJson("导出失败");
		}
	}
	
	
	
	
	
}
