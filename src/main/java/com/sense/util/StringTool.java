/**
 * StringTool.java
 * Created at 2019-07-12
 * Created by Chenzhigang
 * Copyright C 20190712 SAIC SHANGHAI VOLKSWAGEN, All rights reserved.
 */
package com.sense.util;

/**
 * StringTool.java
 * Created at 2019-07-12
 * Created by lx
 * Copyright C 20190712 SAIC SHANGHAI VOLKSWAGEN, All rights reserved.
 */
public class StringTool
{
	/**
	 * ���ֻ���������
	 * @param mobile
	 * @return
	 */
	public static String getInvisibleMobile(String mobile)
	{
		if((mobile==null)||(mobile.trim().length()==0))
		{
			return mobile;
		}
		else if(mobile.trim().length()==11)
		{
			String temp=mobile.trim();
			return temp.substring(0, 3)+"****"+temp.substring(7,11);
		}
		else
		{
			return mobile;
		}
	}
	
	/**
	 * �ж��Ƿ�Ϊ��
	 * @param Str
	 * @return
	 */
	public static boolean isEmpty(String Str)
	{
		if((Str==null)||(Str.trim().length()==0))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
