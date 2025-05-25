/**
 * 
 */
package us.muit.fs.a4i.config;

/**
 * Clase incluida por el equipo 1 del curso 23/24 para apoyo a las métricas de
 * conformidad con convenciones de la organización
 */

public enum GitFlow {
	// MARK: - Enum cases
	/** The main branch */
	MAIN,
	/** The develop branch */
	DEVELOP,
	/** A feature branch */
	FEATURE,
	/** A release branch */
	RELEASE,
	/** A hotfix branch */
	HOTFIX;

	// MARK: - Methods
	@Override
	/**
	 * Returns the string representation of the enum case
	 */
	public String toString() {
		return switch (this) {
		case MAIN -> "main";
		case DEVELOP -> "develop";
		case FEATURE -> "feature";
		case RELEASE -> "release";
		case HOTFIX -> "hotfix";
		};
	}

	/**
	 * Checks if the branch name is a valid Git Flow branch
	 * 
	 * @param branchName the name of the branch
	 * @return true if the branch name is a valid Git Flow branch
	 */
	public static boolean isGitFlowBranch(String branchName) {
		return branchName.equals(MAIN.toString()) || branchName.equals(DEVELOP.toString())
				|| branchName.matches(branchNamePattern(FEATURE.toString()))
				|| branchName.matches(branchNamePattern(RELEASE.toString()))
				|| branchName.matches(branchNamePattern(HOTFIX.toString()));
	}

	/**
	 * Returns the pattern for the branch name
	 * 
	 * @param branchType the type of branch
	 * @return the regex pattern for the branch name
	 */
	private static String branchNamePattern(String branchType) {
		return "^" + branchType + "/[a-zA-Z0-9_-]+$";
	}
}