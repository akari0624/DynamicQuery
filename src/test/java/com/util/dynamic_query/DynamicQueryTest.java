package com.util.dynamic_query;

import java.util.ArrayList;
import java.util.List;

import com.util.dynamic_query.DynamicQuery;
import com.util.dynamic_query.models.entity.SQLGeneratorParameter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



public class DynamicQueryTest{

	
	private static final String GET_EUDCFONT_BY_ROWNUMBER_BASESQL = "select * from ( select *, row_number() over(order by big5) as rn from EUDCFONT %s) as A where A.rn between ? and ?";
	
	private static final String COUNT_ROWCOUNT_BASE = "select  count(*) from EUDCFONT ";
	
	
	private static final String GET_All_EUDCFONT_BASESQL = "select *  from EUDCFONT";
	
	private DynamicQuery dq;
	

	@Before 
	public void doTestInit(){


		 dq = new DynamicQueryImpl();


	}
	// 假設查詢參數中 使用者只填入了county跟stroke
  @Test
	public void testIsGenerateProperTwoCoditionSQL(){

		      String county = "F";
		      Integer stroke = 12;
					String prefix = "";
					String cangjie = null;
					String phonetic = null;
					String cns_code = "";
		
		List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
		
		// like有三種狀況，前面要模糊比對，後面要模糊比對，前後都要 
    // 在SQLGeneratorParameter裡有一個  wrapValueWithPercentSign方法，可以指定是這三種狀況裡的哪一種
		argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
		argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));
		argumentsList.add(new SQLGeneratorParameter("prefix", "=", prefix));
		
		argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		
		
		String sql = dq.generateMutipleWhereClauseConditionSQL(COUNT_ROWCOUNT_BASE, argumentsList, false);
	
		List<Object> okParameters = dq.getOKParameterValuesInList();

		assertEquals(String.format("%s where cty = ? and chars = ?", COUNT_ROWCOUNT_BASE), sql);
    assertArrayEquals(okParameters.toArray(), new Object[]{"F", 12});
	}

		// 假設查詢參數中 使用者只填入了county跟stroke，然後prefix這個欄位就算不輸入也要帶入空字串作為查詢條件
		@Test
		public void testIsGenerateProperThreeCoditionSQL_prefixQueryConditionNeedToBeEmptyString(){
	
						String county = "F";
						Integer stroke = 12;
						String prefix = "";
						String cangjie = null;
						String phonetic = null;
						String cns_code = "";
			
			List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
			
			// like有三種狀況，前面要模糊比對，後面要模糊比對，前後都要 
			// 在SQLGeneratorParameter裡有一個  wrapValueWithPercentSign方法，可以指定是這三種狀況裡的哪一種
			argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
			argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));

			// 假設prefix這個欄位  就算使用者傳入空字串 也會成為合理的查詢條件  就在後面再加上  .valueToBeAssignedNeedToBeEmptyString()
			argumentsList.add(new SQLGeneratorParameter("prefix", "=", prefix).valueToBeAssignedNeedToBeEmptyString());
			
			argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
			argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
			argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
			
			
			
			String sql = dq.generateMutipleWhereClauseConditionSQL(COUNT_ROWCOUNT_BASE, argumentsList, false);
		
			List<Object> okParameters = dq.getOKParameterValuesInList();
	
			assertEquals(String.format("%s where cty = ? and chars = ? and prefix = ?", COUNT_ROWCOUNT_BASE), sql);
			assertArrayEquals(okParameters.toArray(), new Object[]{"F", 12, ""});
		}

			// 假設查詢參數中 使用者只填入了county跟stroke，然後prefix這個欄位 如果帶入null 也要把null作為查詢條件
			@Test
			public void testIsGenerateProperThreeCoditionSQL_prefixQueryConditionNeedToBeNull(){
		
							String county = "F";
							Integer stroke = 12;
							String prefix = null;
							String cangjie = null;
							String phonetic = null;
							String cns_code = "";
				
				List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
				
				// like有三種狀況，前面要模糊比對，後面要模糊比對，前後都要 
				// 在SQLGeneratorParameter裡有一個  wrapValueWithPercentSign方法，可以指定是這三種狀況裡的哪一種
				argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
				argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));
	
				// 假設prefix這個欄位  就算使用者傳入null 也會成為合理的查詢條件  就在後面再加上  .valueToBeAssignNeedToBeNull()
				argumentsList.add(new SQLGeneratorParameter("prefix", "=", prefix).valueToBeAssignNeedToBeNull());
				
				argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
				argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
				argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
				
				
				
				String sql = dq.generateMutipleWhereClauseConditionSQL(COUNT_ROWCOUNT_BASE, argumentsList, false);
			
				List<Object> okParameters = dq.getOKParameterValuesInList();
		
				assertEquals(String.format("%s where cty = ? and chars = ? and prefix = ?", COUNT_ROWCOUNT_BASE), sql);
				assertArrayEquals(okParameters.toArray(), new Object[]{"F", 12, null});
			}


			// 假設查詢參數中 使用者只填入了county跟stroke，然後prefix這個欄位 如果帶入null 也要把null作為查詢條件, 但如果prefix不是null 就還是用傳入的值
			@Test
			public void testIsGenerateProperThreeCoditionSQL_prefixQueryConditionCanBeNull(){
		
							String county = "F";
							Integer stroke = 12;
							String prefix = "刀";
							String cangjie = null;
							String phonetic = null;
							String cns_code = "";
				
				List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
				
				// like有三種狀況，前面要模糊比對，後面要模糊比對，前後都要 
				// 在SQLGeneratorParameter裡有一個  wrapValueWithPercentSign方法，可以指定是這三種狀況裡的哪一種
				argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
				argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));
	
				// 假設prefix這個欄位  就算使用者傳入null 也會成為合理的查詢條件  就在後面再加上  .valueToBeAssignNeedToBeNull()
				argumentsList.add(new SQLGeneratorParameter("prefix", "=", prefix).valueToBeAssignNeedToBeNull());
				
				argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
				argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
				argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
				
				
				
				String sql = dq.generateMutipleWhereClauseConditionSQL(COUNT_ROWCOUNT_BASE, argumentsList, false);
			
				List<Object> okParameters = dq.getOKParameterValuesInList();
		
				assertEquals(String.format("%s where cty = ? and chars = ? and prefix = ?", COUNT_ROWCOUNT_BASE), sql);
				assertArrayEquals(okParameters.toArray(), new Object[]{"F", 12, "刀"});
			}
	
	@Test
	// 一個用 DB的rownum函數 來做分頁的查詢方式的test case
	public void  testIsGenerateCorrectRowNumberSQL(){

    // 假設這個分頁查詢handler裡，前端有可能會傳來這些參數，但是有些可能有輸入，有些可能沒有，因為這是個複合查詢(composite query)的使用情境
		        int startRow = 1; 
			      int endRow = 5;
			      String county = "E";
			      Integer stroke = 14;
            String prefix = "山";
            String cangjie = "花鳥風月";
            String phonetic = "ㄅ";
            String cns_code = null;
		
		List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
		
		argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
		argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));
		argumentsList.add(new SQLGeneratorParameter("prefix", "=", prefix));

		argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		String tempSQL = dq.generateMutipleWhereClauseConditionSQL(GET_EUDCFONT_BY_ROWNUMBER_BASESQL, argumentsList, true);
		
		
	     List<Object> okParameterValues = dq.getOKParameterValuesInList();
		
			 // 再放入第幾筆到第幾筆
		  okParameterValues.add(startRow);
		  okParameterValues.add(endRow);
		
				// 有必要的話，可以把SQL print在log上
				dq.helpPrintSQLToDebug(tempSQL+String.format(",筆數  %s -%s", startRow, endRow));
			
       String theWhereClause = " where cty = ? and chars = ? and prefix = ? and chajei like ? and phon like ?";

/* select * from ( select *, row_number() over(order by big5) as rn from EUDCFONT %s) as A where A.rn between ? and ?
  %s 會被換成 where  cty = ? chars = ? ..... 根據使用者先前給的指示，有合理的值的查詢條件才會被加入where子句中，過程中完全不用自己去寫 if(!"".equals(cty) || cty != null){...}  這樣的程式碼

 再使用 Apache DbUtils 或 Spring JDBCTemplate之類的library進行DB操作
  QueryRunner run = new QueryRunner( aDataSource );
  run.query(tempSQL, rs -> {.....}, okParameterValues.toArray());
*/
      assertEquals(String.format(GET_EUDCFONT_BY_ROWNUMBER_BASESQL, theWhereClause), tempSQL);
		  assertArrayEquals(okParameterValues.toArray(), new Object[]{county, stroke, prefix, String.format("%%%s%%", cangjie), String.format("%%%s%%", phonetic), startRow, endRow});

	}

@Test	
public void queryAllRareWordsData(){

	String county = "E";
	Integer stroke = 14;
	String prefix = "山";
	String cangjie = "花鳥風月";
	String phonetic = null;
	String cns_code = "CK1234";
		
		
     List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
		
		argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
		argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));
		argumentsList.add(new SQLGeneratorParameter("prefix", "=", prefix));
		
		argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		
		
		String sql = dq.generateMutipleWhereClauseConditionSQL(GET_All_EUDCFONT_BASESQL, argumentsList, false);
		
		System.out.println(sql);

		
		List<Object> okParameterValues = dq.getOKParameterValuesInList();
				
		assertEquals(String.format("%s where cty = ? and chars = ? and prefix = ? and chajei like ? and cns like ?", GET_All_EUDCFONT_BASESQL), sql);
		assertArrayEquals(okParameterValues.toArray(), new Object[]{county, stroke, prefix, String.format("%%%s%%", cangjie), String.format("%%%s%%", cns_code)});
	
		 
		
	}

	
}
