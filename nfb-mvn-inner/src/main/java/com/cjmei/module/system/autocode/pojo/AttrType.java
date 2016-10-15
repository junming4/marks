package com.cjmei.module.system.autocode.pojo;

//java类型与数据库类型对应
public enum AttrType {

	String{ 
    public String getOracleType(){ 
        return "varchar2";
    }

    public String getMybatisType() {
        return "VARCHAR";
      }
    public String getMySqlType() {
         return "varchar";
      }
    },
	
	Integer{
    public String getOracleType(){
        return "number";
        }
    public String getMySqlType(){
        return  "int";
        }

    @Override
    public String getMybatisType() {
        return "NUMERIC";
       }
    
    },
	
	Date{ 
    public String getOracleType(){ 
        return "date";
        }

    @Override
    public java.lang.String getMybatisType() {
        return "TIMESTAMP";
         }
    
    public String getMySqlType(){
        return "date";
    }
};
	
	
	//获取Oracle数据库类型
	public abstract String getOracleType();
	
	//获取mybatis类型
	public abstract String getMybatisType();
	
	//获取MySQL数据库类型
	public abstract String getMySqlType();
	
	public static void main(String[] args) {
		for(AttrType attrType : AttrType.values()){
			System.out.println(attrType.name() + "  " 
		+ attrType.getMybatisType() + " " + attrType.getOracleType() );
		}
		System.out.println(AttrType.valueOf("String").getOracleType());
//		System.out.println(AttrType.String.getOracleType());
	}
	
}
