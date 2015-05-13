package ir.rayacell.mahda.provider;

import ir.rayacell.mahda.MainActivity;
import ir.rayacell.mahda.param.BaseParam;

public abstract class BaseProvider {

	public ProviderManager mProviderManager;
	public MainActivity activity;

	public BaseProvider(ProviderManager providerManager, MainActivity activity) {
		// TODO Auto-generated constructor stub
		this.mProviderManager = providerManager;
		this.activity = activity;
	}

	public abstract boolean connect(BaseParam param);

	public abstract boolean send(BaseParam param);

	public abstract void recieve(BaseParam param);
}
