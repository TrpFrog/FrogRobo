package net.trpfrog.frogrobo.hit_and_blow;

public class HitAndBlowPlayer {
	private long userId = 0;
	private int solution = 0;
	private int trials = 0;

	public long getUserId() {
		return this.userId;
	}

	public int getSolution() {
		return this.solution;
	}

	public int getTrials() {
		return this.trials;
	}

	public void addTrials() {
		this.trials++;
	}

	public HitAndBlowPlayer(long userId, int solution, int trials){
		this.userId = userId;
		this.solution = solution;
		this.trials = trials;
	}

	public HitAndBlowPlayer(String toStringData) throws NumberFormatException {
		String[] dataSet = toStringData.split("/");
		this.userId = Long.parseLong(dataSet[0]);
		this.solution = Integer.parseInt(dataSet[1]);
		this.trials = Integer.parseInt(dataSet[2]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + solution;
		result = prime * result + trials;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HitAndBlowPlayer){
			return ((HitAndBlowPlayer) obj).getUserId() == this.userId
					&& ((HitAndBlowPlayer) obj).getSolution() == this.solution
					&& ((HitAndBlowPlayer) obj).getTrials() == this.trials;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.userId);
		sb.append("/");
		sb.append(this.solution);
		sb.append("/");
		sb.append(this.trials);
		return sb.toString();
	}


}
