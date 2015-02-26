package itworx.github.task.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repositories {

	/**
	 * LIST OF LOADED REPOSITORIES.
	 */
	public static List<Repository> REPOSITORIES = new ArrayList<Repository>();

	/**
	 * A MAP OF REPOSITORIES, MAPPED by ID.
	 */
	public static Map<String, Repository> REPOSITORY_MAP = new HashMap<String, Repository>();

	
	/**
	 * A Method to Add Repository to both List and Map
	 */
	public static void addRepository(Repository REPOSITORY) {
		REPOSITORIES.add(REPOSITORY);
		REPOSITORY_MAP.put(REPOSITORY.getId(), REPOSITORY);
	}
	
	/**
	 * A Method to clear both List and Map
	 */
	public static void clearRepository() {
		REPOSITORIES.clear();
		REPOSITORY_MAP.clear();
	}

	public static void addRepository(List<Repository> repos,
			Map<String, Repository> repos_map) {
		REPOSITORIES.addAll(repos);
		REPOSITORY_MAP.putAll(repos_map);
	
	}
}
