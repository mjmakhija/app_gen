package com.hm.app_gen;

import com.hm.app_gen.config.Configurations;

public class Main
{

	public static void main(String[] args)
	{
		String path = "";
		if (args.length > 0)
		{
			path = args[0];
		}
		System.out.println("Passed arg is " + path);

		ExportHelper eh = new ExportHelper(Configurations.getInstance(path));
		eh.export();

	}

}
