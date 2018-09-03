import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xml.internal.security.Init;


public class TestRegex {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String pattern="title.*?,";
		String body=TestGetHtmlBody.getList("ƤЬ");
		List<String> x=matchesToList(pattern, body);
		System.out.println(x);
		for(int i=0;i<x.size();i++){
			System.out.println(x.get(i));
		}
		

	}
	public static List<String> matchesToList(String pattern,String text){
		List<String> set=new ArrayList<String>();
		Pattern pa=Pattern.compile(pattern);
        Matcher ms=pa.matcher(text);
        while(ms.find()){
        	set.add(ms.group());
        }
        return set;
	}
	public static String match(String pattern,String text){
		Pattern pa=Pattern.compile(pattern);
        Matcher ms=pa.matcher(text);
        if(ms.find()){
        	return ms.group();
        }else{
        	return "";
        }
	}

}
