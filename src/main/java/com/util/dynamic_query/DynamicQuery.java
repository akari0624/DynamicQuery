package com.util.dynamic_query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.util.dynamic_query.models.entity.SQLGeneratorParameter;


/**
 * 依照前端傳來的參數在BaseSQL後面加上不固定數量的 where xxx = ? and.....，提供使用者能動態數量的?的SQL，以讓使用者能創出PreparedStatement
 * 並再提供一個方法讓使用者拼接完Order by條件後，再把參數 setObject進?裡
 * @author Morris
 * @since  2018/09/06
 * @version 1.0.0
 */
public interface DynamicQuery {

	/**
	 * 
	 * @param baseSQL
	 * @param whereConditionParameterList
	 * @param isInStringFormatForm  baseSQL是不是StringFormat模式(有%s) 是的話表示使用者想要加入where條件的狀況不是在SQL尾端，而是在中間，會有這種情境通常是因為要加入where條件的地方是在一個子查詢裡面
	 * , 例如要做Rownum 取第X筆到第X+N筆的時候。使用者必須自己判斷他是不是這種狀況
	 * @return
	 */
	public String generateMutipleWhereClauseConditionSQL(String baseSQL, List<SQLGeneratorParameter> whereConditionParameterList, boolean isInStringFormatForm);
		
	/**
	 * 幫助使用者加上order by條件，沒有要order by就不用call這個method。注意!!!這個Order by是加在SQL String的最後面，如果你的Order by是要加在子查詢裡，那就不能用這一個method。
	 * @param baseSQLWithMultipleWhereCondition 前一個方法組好where條件之後的SQL
	 * @param orderbyWhatColumns  要依照哪些欄位來order by , 使用參數列表, 所以可以動態加多個
	 * 
	 * @return  加上Order by之後的SQL String
	 */
	public String addOrderByToThisSQL(String baseSQLWithMultipleWhereCondition, String... orderbyWhatColumns);
	
	
	/**
	 * 如果你是用DBUtils的話你用不到這個method, 只有JDBC都自己來的人才會用到
	 * @param pstmt  已經給好有?的SQL，然後準備對?放入參數的PreparedStatement 
	 * @return  對?加好參數的 PreparedStatement，參數在上一個方法generateMutipleWhereClauseConditionSQL時就已經被存在屬性裡，所以不用再提供。所以注意 call這裡的時候必須跟call  generateMutipleWhereClauseConditionSQL時用的是同一個instance。
	 * @throws SQLException
	 * @see   generateMutipleWhereClauseConditionSQL(String baseSQL, List<SQLGeneratorParameter> whereConditionParameterList)
	 *
	 */
	public PreparedStatement setParameterToThisPstmtAndReturn(PreparedStatement pstmt) throws SQLException;
	
	
	/**
	 * 幫使用者把SQL印出來，但是參數的部分僅限於它後來沒有又自己加別的東西進去，這個Method能印出來的，僅限於DynamicQuery 能夠掌握的部分
	 * @param yourFinalSQLString
	 */
	public void helpPrintSQLToDebug(String yourFinalSQLString); 
	
	/**
	 * 把真的有把XX = ?  組進SQL裡的部分的那些SQLGeneratorParameter的value以Object[]的型態 return回去，方便跟別的JDBC library合作(如Apache DBUtils)
	 * 
	 * @return Object[]，裡面包含XX = ?的?裡應該要被pstme.setXXX(...)的東西
	 */
	public Object[] getOKParameterValuesInArray();
	
	/**
	 * 把真的有把XX = ?  組進SQL裡的部分的那些SQLGeneratorParameter的value以Object[]的型態 return回去，方便跟別的JDBC library合作(如Apache DBUtils),
	 * 用List型態傳回去是因為方便使用者再add 然後再自己 .toArray()  傳給Apache DBUtils的QueryRunner, 注意:那些後來自己加進去這個方法回傳的List裡的參數, 在helpPrintSQLToDebug(...)裡, 目前的架構上 會印不出來
	 * @return  List of Object
	 * @see   helpPrintSQLToDebug;
	 */
	public List<Object>  getOKParameterValuesInList();
}
