# Dynamic Query
## 適用情境
  - 用JDBC在處理向資料庫查詢這一段時，如果查詢的參數數量不是固定的，在組出SQL句子時很容易會寫出大量if else+字串拼接的可怕程式碼。
  - 本Library欲解決這樣的窘境，讓在組出不定數量的查詢的ＳＱＬ句子給`PreparedStatement`用的時候的程式碼依然能保持整潔。
  
  
## 開發環境
  - 本專案使用`Gradle 4.10.2`來處理測試, 編譯, 打包出jar檔...等等雜事，所以開發用的電腦上必須安裝好`Gradle`。
  - 視情況看要不要把gradle加入PATH
  ``` sh
  # in your shell config file
  export PATH=/path/to/your/gradle_directory/bin:$PATH
  ```


  
  
## Gradle指令
  在terminal上，cd 到本專案資料夾底下後，執行
   - 編譯測試的java class  
``` shell
    gradle testClasses
```  
    
   - 執行測試   
``` shell
      gradle test
```  
   - 把上兩個一起做
``` shell
     gradle testClasses && gradle test
```  
   - 執行測試，然後編譯出byteCode，會放在`build/classes/main`底下
``` shell
     gradle build  
```  
   - 打包出jar檔，會放在`build/libs`底下
``` shell
     gradle jar
```  
   - 看總共可以用Gradle做些什麼 
``` shell
     gradle task
```
  
  
 ## 使用範例
- [@see testCase](https://github.com/akari0624/DynamicQuery/blob/master/src/test/java/com/util/dynamic_query/DynamicQueryTest.java#L174)




