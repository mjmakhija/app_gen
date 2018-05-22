package com.hm.app_gen;

import com.hm.app_gen.config.Configurations;

public class Main
{

	public static void main(String[] args)
	{

		ExportHelper eh = new ExportHelper(Configurations.getInstance(args[0]));
		eh.export();

	}

}
