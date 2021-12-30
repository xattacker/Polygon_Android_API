package com.xattacker.json

import android.content.Context
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.InputStream

object JsonUtility
{
    @JvmOverloads
    fun createGson(aVisitor: ((builder: GsonBuilder) -> Unit)? = null): Gson
    {
        val builder = GsonBuilder()
        aVisitor?.invoke(builder)

        // ignore expose field to serialize
        builder.setExclusionStrategies(
            object : ExclusionStrategy
            {
                override fun shouldSkipField(f: FieldAttributes): Boolean
                {
                    return f.getAnnotation<Expose>(Expose::class.java) != null
                }

                override fun shouldSkipClass(clazz: Class<*>): Boolean
                {
                    return false
                }
            }
        )

        return builder.create()
    }

    @JvmOverloads
    fun <T> fromJson(aJson: String, aToken: TypeToken<T>, aVisitor:((builder: GsonBuilder) -> Unit)? = null): T?
    {
        val gson = createGson(aVisitor)
        return gson.fromJson<T>(aJson, aToken.type)
    }

    @JvmOverloads
    fun <T> fromJson(aJson: String, aType: Class<T>, aVisitor: ((builder: GsonBuilder) -> Unit)? = null): T
    {
        val gson = createGson(aVisitor)
        return gson.fromJson(aJson, aType)
    }

    @JvmOverloads
    @Throws(Exception::class)
    fun <T> fromJsonRes(
    context: Context,
    aRes: Int,
    aType: Class<T>,
    aVisitor: ((builder: GsonBuilder) -> Unit)? = null
    ): T?
    {
        var obj: T?
        var bout: ByteArrayOutputStream? = null
        var fin: InputStream? = null

        try
        {
            fin = context.resources.openRawResource(aRes)
            bout = ByteArrayOutputStream()

            val buffer = ByteArray(256)
            var index = 0

            while (true)
            {
                index = fin.read(buffer)
                if (index < 0)
                {
                    break
                }

                bout.write(buffer, 0, index)
                bout.flush()
            }

            obj = fromJson(
                    String(bout.toByteArray()),
                    aType,
                    aVisitor)
        }
        catch (ex: Exception)
        {
            throw ex
        }
        finally
        {
            try
            {
                fin?.close()
            }
            catch (ex: Exception)
            {
            }

            try
            {
                bout?.close()
            }
            catch (ex: Exception)
            {
            }
        }

        return obj
    }

    @JvmOverloads
    @Throws(Exception::class)
    fun <T> fromJsonRes(
    context: Context,
    aRes: Int,
    aToken: TypeToken<T>,
    aVisitor: ((builder: GsonBuilder) -> Unit)?= null
    ): T?
    {
        var obj: T?
        var bout: ByteArrayOutputStream? = null
        var fin: InputStream? = null

        try
        {
            fin = context.resources.openRawResource(aRes)
            bout = ByteArrayOutputStream()

            val buffer = ByteArray(256)
            var index = 0

            while (true)
            {
                index = fin.read(buffer)
                if (index < 0)
                {
                    break
                }

                bout.write(buffer, 0, index)
                bout.flush()
            }

            obj = fromJson(
                    String(bout.toByteArray()),
                    aToken,
                    aVisitor)
        }
        catch (ex: Exception)
        {
            throw ex
        }
        finally
        {
            try
            {
                fin?.close()
            }
            catch (ex: Exception)
            {
            }

            try
            {
                bout?.close()
            }
            catch (ex: Exception)
            {
            }
        }

        return obj
    }
}
