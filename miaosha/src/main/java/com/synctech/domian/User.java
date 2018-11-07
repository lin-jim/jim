package com.synctech.domian;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class User {

	private int id;
	private String name;
}
