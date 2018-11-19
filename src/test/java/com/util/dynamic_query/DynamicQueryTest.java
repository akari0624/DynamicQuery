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
	
  @Test
	public void testIsGenerateProperTwoCoditionSQL(){

		      String county = "F";
		      Integer stroke = 12;
					String prefix = "";
					String cangjie = null;
					String phonetic = null;
					String cns_code = "";
		
		List<SQLGeneratorParameter> argumentsList = new ArrayList<SQLGeneratorParameter>();
		
		argumentsList.add(new SQLGeneratorParameter("cty", "=", county));
		argumentsList.add(new SQLGeneratorParameter("chars", "=", stroke));
		argumentsList.add(new SQLGeneratorParameter("part", "=", prefix));
		if(cangjie != null) {
		argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		}
		argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		
		
		String sql = dq.generateMutipleWhereClauseConditionSQL(COUNT_ROWCOUNT_BASE, argumentsList, false);
	
		List<Object> okParameters = dq.getOKParameterValuesInList();

		assertEquals(String.format("%s where cty = ? and chars = ?", COUNT_ROWCOUNT_BASE), sql);
    assertArrayEquals(okParameters.toArray(), new Object[]{"F", 12});
	}
	
	@Test
	public void  testIsGenerateCorrectRowNumberSQL(){

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
		argumentsList.add(new SQLGeneratorParameter("part", "=", prefix));

		argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		String tempSQL = dq.generateMutipleWhereClauseConditionSQL(GET_EUDCFONT_BY_ROWNUMBER_BASESQL, argumentsList, true);
		
		
	     List<Object> okParameterValues = dq.getOKParameterValuesInList();
		
		  okParameterValues.add(startRow);
		  okParameterValues.add(endRow);
		
				
			System.out.println(tempSQL);
			
       String theWhereClause = " where cty = ? and chars = ? and part = ? and chajei like ? and phon like ?";

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
		argumentsList.add(new SQLGeneratorParameter("part", "=", prefix));
		
		argumentsList.add(new SQLGeneratorParameter("chajei", "like", cangjie).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		argumentsList.add(new SQLGeneratorParameter("phon", "like", phonetic).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		argumentsList.add(new SQLGeneratorParameter("cns", "like", cns_code).wrapValueWithPercentSign(SQLGeneratorParameter.PercentSignEnum.前後));
		
		
		
		String sql = dq.generateMutipleWhereClauseConditionSQL(GET_All_EUDCFONT_BASESQL, argumentsList, false);
		
		System.out.println(sql);

		
		List<Object> okParameterValues = dq.getOKParameterValuesInList();
				
		assertEquals(String.format("%s where cty = ? and chars = ? and part = ? and chajei like ? and cns like ?", GET_All_EUDCFONT_BASESQL), sql);
		assertArrayEquals(okParameterValues.toArray(), new Object[]{county, stroke, prefix, String.format("%%%s%%", cangjie), String.format("%%%s%%", cns_code)});
	
		 
		
	}

	
}
