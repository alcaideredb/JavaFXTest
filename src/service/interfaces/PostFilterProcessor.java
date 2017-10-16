package service.interfaces;

import java.util.Set;

public interface PostFilterProcessor {
	public void filter(Set<String> included, Set<String> excluded);
}
