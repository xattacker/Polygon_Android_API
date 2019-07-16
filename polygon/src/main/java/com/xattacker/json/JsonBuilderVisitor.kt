package com.xattacker.json

import com.google.gson.GsonBuilder

interface JsonBuilderVisitor
{
    fun onBuilderPrepared(aBuilder: GsonBuilder)
}
