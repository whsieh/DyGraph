package stat.path;

import stat.IStatResult;

public class PathData implements IStatResult {
	@Override
	public int getType() {
		return IStatResult.ABSTRACT;
	}
}
