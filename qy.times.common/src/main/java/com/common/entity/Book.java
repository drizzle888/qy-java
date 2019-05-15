package com.common.entity;

import java.io.Serializable;

public class Book extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public int code;
	public int templateId;
}
