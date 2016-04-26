package net.trpfrog.frogrobo.hit_and_blow;

public class HitAndBlowResult {
	private int hit = 0;
	private int blow = 0;

	public HitAndBlowResult(int hit, int blow){
		this.hit = hit;
		this.blow = blow;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HitAndBlowResult){
			return ((HitAndBlowResult) obj).getHit() == this.hit
					&& ((HitAndBlowResult) obj).getBlow() == this.blow;
		}
		return false;
	}

	public int getHit() {
		return hit;
	}
	public int getBlow() {
		return blow;
	}
}