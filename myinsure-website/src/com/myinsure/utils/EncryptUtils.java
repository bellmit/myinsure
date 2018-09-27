package com.myinsure.utils;

import com.jfinal.kit.HashKit;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EncryptUtils extends HashKit
{
    public static String salt()
    {
	int random = (int) (10.0D + Math.random() * 10.0D);
	return UUID.randomUUID().toString().replace("-", "").substring(random);
    }

    public static String encryptPassword(String password, String salt)
    {
	return sha256(new StringBuilder().append(password).append(salt).toString());
    }

    public static boolean verlifyUser(String userPassword, String userSalt, String password)
    {
	if (userPassword == null)
	{
	    return false;
	}
	if (userSalt == null)
	{
	    return false;
	}
	return userPassword.equals(encryptPassword(password, userSalt));
    }

    public static String generateUcode(BigInteger id, String salt)
    {
	return md5(new StringBuilder().append(id).append(salt).toString());
    }

    public static String signForRequest(Map<String, String> params, String secret)
    {
	String [] keys = (String []) params.keySet().toArray(new String[0]);
	Arrays.sort(keys);

	StringBuilder query = new StringBuilder();
	query.append(secret);
	for (String key : keys)
	{
	    String value = (String) params.get(key);
	    if (StringUtils.areNotEmpty(new String[] { key, value }))
	    {
		query.append(key).append(value);
	    }
	}
	query.append(secret);
	return HashKit.md5(query.toString()).toUpperCase();
    }

    public static void main(String [] args)
    {
	System.out.println(encryptPassword("123456", "abc"));
    }
}