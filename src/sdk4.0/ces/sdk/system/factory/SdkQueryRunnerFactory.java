package ces.sdk.system.factory;

import ces.sdk.system.common.decorator.SdkQueryRunner;

public abstract class SdkQueryRunnerFactory {

	private static SdkQueryRunner instance;
	
	public static SdkQueryRunner createSdkQueryRunner(){
		if(instance == null){
			instance = SdkQueryRunner.getInstance();
		}
		return instance;
	}
}
