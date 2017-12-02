package com.xattacker.json;

import com.google.gson.GsonBuilder;

public interface JsonBuilderVisitor
{
	void onBuilderPrepared(GsonBuilder aBuilder);
}
