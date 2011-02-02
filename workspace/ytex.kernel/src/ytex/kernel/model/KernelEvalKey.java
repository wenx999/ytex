package ytex.kernel.model;

public class KernelEvalKey {
	private int instanceId1;
	private int instanceId2;
	public int getInstanceId1() {
		return instanceId1;
	}
	public int getInstanceId2() {
		return instanceId2;
	}
	public KernelEvalKey(int instanceId1, int instanceId2) {
		super();
		this.instanceId1 = instanceId1 <= instanceId2 ? instanceId1 : instanceId2;
		this.instanceId2 = instanceId1 <= instanceId2 ? instanceId2 : instanceId1;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + instanceId1;
		result = prime * result + instanceId2;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KernelEvalKey other = (KernelEvalKey) obj;
		if (instanceId1 != other.instanceId1)
			return false;
		if (instanceId2 != other.instanceId2)
			return false;
		return true;
	}

	
}
