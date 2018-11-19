package com.util.dynamic_query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.util.dynamic_query.models.entity.SQLGeneratorParameter;





@Service("dynamicQuery1")
public class DynamicQueryImpl implements DynamicQuery {
	
	private static final Logger _logger = LoggerFactory.getLogger(DynamicQuery.class);
	
	private List<SQLGeneratorParameter> okParameters = null;
	
	
	@Override
	public String generateMutipleWhereClauseConditionSQL(String baseSQL, List<SQLGeneratorParameter> whereConditionParameterList, boolean isInStringFormatForm) {
		
		
		  StringBuilder sb = new StringBuilder();
		
		  
		
		
		  
		  this.okParameters = new ArrayList<SQLGeneratorParameter>();
		  
		 // 檢查參數 
		  checkParameters(whereConditionParameterList);
		  
		
        if(this.okParameters.size()>0) {
			  
			  sb.append(" where");
		  }
		  
		// 開始組
		int a=0;
		int okParametersLength = okParameters.size();
		for(SQLGeneratorParameter p : this.okParameters){
			
			 sb.append(" ");
			  sb.append(p.getColumnName());
			  sb.append(" ");
			  sb.append(p.getOperator());
			  
			  Object okParamValue = p.getValueToBeAssigned();
			  if(okParamValue != SQLGeneratorParameter.SpecialConditionEnum.無參數) {
			    sb.append(" ?");
			  }
			  
			  
	          if(p.getWrapValueWithPercentSign() != null && okParamValue != null) {
					
					okParamValue = suitWithPercentSign(p.getWrapValueWithPercentSign(), (String)okParamValue);
					
					p.CAUTIONLY_resetValueToBeAssigned(okParamValue);
				}
	          
			  // 還不是最後一個之前就加上 and, 如果有需要用到or 的使用情境的話  就要再修改 建構出 SQLGeneratorParameter 時給的參數
			  if(a != okParametersLength -1){
			  sb.append(" and");
			  }
			  
			  a += 1;
			
		};
		 
		
		if(isInStringFormatForm) {
			
			return String.format(baseSQL, sb.toString());
		}
		
		
		    return baseSQL + sb.toString();
		
		
	}
	
	
	@Override
	public String addOrderByToThisSQL(String baseSQLWithMultipleWhereCondition, String... orderbyWhatColumns) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(baseSQLWithMultipleWhereCondition);
		sb.append(" order by ");
		
		for(String columnName : orderbyWhatColumns) {
			
			sb.append(columnName);
			sb.append(", ");
			
		}
		
	    String afterAddOrderby = sb.toString();
	    
	    int sqlStringLength = afterAddOrderby.length();
	    
	    /*去除掉最後一個逗點*/
	    afterAddOrderby = afterAddOrderby.substring(0, sqlStringLength-2);
		
	    
	    return afterAddOrderby;
	}
	
	
	
	@Override
	public PreparedStatement setParameterToThisPstmtAndReturn(PreparedStatement pstmt) throws SQLException {
		
		if(this.okParameters == null) {
			
			
			throw new RuntimeException("you must call generateMutipleWhereClauseConditionSQL(...) before invoke this method");
		}
		 
		
		try {
			
			 int pLength = this.okParameters.size();
			
			for(int a=0; a<pLength ; a+=1) {
				
				SQLGeneratorParameter currOKParam = this.okParameters.get(a);
				
				Object okParamValue = currOKParam.getValueToBeAssigned();
				
			
				
				pstmt.setObject(a+1, okParamValue);
			}
			
			
		} catch (SQLException e) {
			
			_logger.error("發生SQLException:{}", e.getMessage());
			e.printStackTrace();
			
			throw new SQLException(e.getMessage());
		}
		
		
		
		
		return pstmt;
		
	}
	
	
	@Override
	public void helpPrintSQLToDebug(String yourFinalSQLString) {
	  	
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" going to execute SQL:");
		sb.append(yourFinalSQLString);
		
		sb.append(" ...below is parameters:");
		
		int i =1;
		for(SQLGeneratorParameter p : okParameters) {
			
			sb.append(" ?"+i+"=" + p.getValueToBeAssigned());
			sb.append("|");
			i+=1;
		}
		
		
		_logger.info(sb.toString());
	}
	
	
	@Override
	public Object[] getOKParameterValuesInArray() {
		
		
		return okParameters.stream().map(p -> p.getValueToBeAssigned()).toArray();
	}
	
	@Override
	public List<Object>  getOKParameterValuesInList() {
		
		return okParameters.stream().map(p -> p.getValueToBeAssigned()).collect(Collectors.toList());
	}

	
	// 一個很有副作用的method...
	private void checkParameters(List<SQLGeneratorParameter> whereConditionParameterList) {
		
		int parameterListLength = whereConditionParameterList.size();
for(int i = 0; i<parameterListLength; i+=1 ) {
			
			SQLGeneratorParameter oneCondition = whereConditionParameterList.get(i);
			String columnName = oneCondition.getColumnName();
			String operator = oneCondition.getOperator(); 
			
			Object valueToBeAssigned = oneCondition.getValueToBeAssigned();
			
			// 把nested if 攤平的一種快速簡單有效但醜陋的方式
			if(columnName == null || "".equals(columnName)) {
				continue;
			}
			
			if(operator == null || "".equals(operator)) {
				continue;
			}
			
			if( !oneCondition.getIsValueToBeAssignNeefToBeNull() && valueToBeAssigned == null) {
				
				continue;
			}
			
			if( "".equals(valueToBeAssigned) && !oneCondition.getIsValueToBeAssignNeedToBeEmptyString()) {
				
				continue;
			}
			       
							
			
			  this.okParameters.add(oneCondition);
			  
			}
			
		}
	
	
	   private String suitWithPercentSign(SQLGeneratorParameter.PercentSignEnum condition, String value) {
		   
		   switch(condition){
			   
		   case 前:
			   return String.format("%%%s", value);
		
		   case 後:
			   return String.format("%s%%", value);
		
		   case 前後:
			   return String.format("%%%s%%", value); 
			   
			default:
				_logger.error("someThing wrong...........");
				 return "";
		   }
		   
		  
	   }
		
	}
	
