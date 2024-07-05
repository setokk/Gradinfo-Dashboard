package edu.gradinfo.dashboard.enums;

public enum GitStatusEnum implements IEnum<GitStatusEnum> {
	NOT_CLONED,
	NONE,
	STASHED,
	STAGED,
	COMMITTED,
	/* Used for using methods of IEnum */
	INSTANCE
}
