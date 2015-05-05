package defeatedcrow.showcase.common;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public void load(Configuration cfg)
	{
		try
		{
			cfg.load();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cfg.save();
		}
	}
}
