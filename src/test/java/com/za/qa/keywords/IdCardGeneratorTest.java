package com.za.qa.keywords; 

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月16日 上午9:45:12 
 * 类说明 
 */
public class IdCardGeneratorTest {

	@Test
	public void test() {
		IdCardGenerator idcg = new IdCardGenerator();
		System.out.println("start:"+System.currentTimeMillis());
		System.out.println("id1:"+idcg.generate());
		System.out.println("id2:"+idcg.generate(18));
		System.out.println("id3:"+idcg.generate(30, 5));
		System.out.println("id4:"+idcg.IdCreate("19981210", "1"));
		for(int i=0;i<20000;i++){
			String id1 = idcg.generate();
			if(id1.length()<18){
				System.out.println("id1:"+id1);
			}
			String id2 = idcg.generate(18);
			if(id2.length()<18){
				System.out.println("id2:"+id2);
			}
			String id3 = idcg.generate(30, 5);
			if(id3.length()<18){
				System.out.println("id3:"+id3);
			}		
			String id4=idcg.IdCreate("19981210", "1");
			//System.out.println(id);
			if(id4.length()<18){
				System.out.println("id4:"+id4);

			}
		}
		System.out.println("end  :"+System.currentTimeMillis());

	}

}
 