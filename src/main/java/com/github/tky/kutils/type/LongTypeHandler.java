package com.github.tky.kutils.type;

import java.lang.reflect.Type;

public class LongTypeHandler extends DefaultTypeHandler {

	@Override
	public Type getType() {
		return Long.class;
	}

}
