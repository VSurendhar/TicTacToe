package com.voiddeveloper.tictactoe.domain.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.voiddeveloper.tictactoe.data.datastore.proto.GameDetailsCache
import java.io.InputStream
import java.io.OutputStream

object GameDetailCacheSerializer : Serializer<GameDetailsCache> {

    override val defaultValue: GameDetailsCache = GameDetailsCache.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): GameDetailsCache {
        try {
            return GameDetailsCache.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read AutoSaveUserPref proto.", exception)
        }
    }

    override suspend fun writeTo(t: GameDetailsCache, output: OutputStream) {
        t.writeTo(output)
    }

}
