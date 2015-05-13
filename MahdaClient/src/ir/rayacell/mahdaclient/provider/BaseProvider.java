package ir.rayacell.mahdaclient.provider;

import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.param.BaseParam;

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