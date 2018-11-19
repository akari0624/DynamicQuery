package com.util.dynamic_query.models.entity;

public class SQLGeneratorParameter {
	
	
	private String columnName;
	
	private String operator;
	
	private Object valueToBeAssigned; 
	
	/**
	 * 
	 */
	private boolean valueToBeAssignedNeedToBeEmptyString = false;
	
	private PercentSignEnum percentSignEnum = null;
	
	private boolean isValueToBeAssignNeefToBeNull = false;
	
	
	/**
	 * 
	 *
	 */
    public enum SpecialConditionEnum {

		無參數
	}
    
    
    public enum PercentSignEnum {

  		前,後,前後
  	}
	
	/**
	 * 
	 * @param columnName - where條件時  table的欄位名稱
	 * @param operator - like, =, <, >.....那些      
	 * @param valueToBeAssigned - 值
	 * 
	 * 有些where條件會是 valueToBeAssigned本來就不會有值的時候 如operator是 is null, is not null...那樣的狀況
	 */
	public SQLGeneratorParameter(String columnName, String operator, Object valueToBeAssigned) {
		
		this.columnName = columnName;
		this.operator = operator;
		this.valueToBeAssigned = valueToBeAssigned;
	}
	
	
	

	

	public String getColumnName() {
		return columnName;
	}




	public String getOperator() {
		return operator;
	}




	public Object getValueToBeAssigned() {
		return valueToBeAssigned;
	}


/**
 *  添加特殊狀況  ?的值必須是空字串時  用來call的函數  可串接
 */
	public SQLGeneratorParameter valueToBeAssignedNeedToBeEmptyString() {
		
		this.valueToBeAssignedNeedToBeEmptyString = true;
		
		return this;
	}
	
	public SQLGeneratorParameter wrapValueWithPercentSign(PercentSignEnum value) {
		
		this.percentSignEnum = value;
		
		return this;
		
	}

	/**
	 *  添加特殊狀況 	?的值必須是 null時  用來call的函數  可串接
	 */
    public SQLGeneratorParameter valueToBeAssignNeefToBeNull() {
		
		this.isValueToBeAssignNeefToBeNull = true;
		
		return this;
		
	}
	
    
   // 特殊狀況的 getter們
	public boolean getIsValueToBeAssignNeedToBeEmptyString(){
		
		return this.valueToBeAssignedNeedToBeEmptyString;
	}
	
	public PercentSignEnum getWrapValueWithPercentSign() {
		
		return this.percentSignEnum;
	}
	  
	public boolean getIsValueToBeAssignNeefToBeNull() {
		
		return this.isValueToBeAssignNeefToBeNull;
	}
	
	
	// % XXX % 之後 為了印出SQL 所以必須把% XXX % 送回OKParameter時在用的  也是個很有有副作用的method
	public void CAUTIONLY_resetValueToBeAssigned(Object value) {
		
		this.valueToBeAssigned = value;
		
	}
}
