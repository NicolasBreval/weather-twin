package org.nbreval.weather_twin.gateway.infrastructure.serializer;

import java.io.IOException;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;

/**
 * Custom serializer for MapDB using Kryo.
 */
public class KryoSerializer<T> implements Serializer<T> {

  final private static int INITIAL_BUFFER_SIZE = 1024;

  /**
   * Kryo instance for serialization.
   */
  final private Kryo kryo;

  @SuppressWarnings("unchecked")
  public KryoSerializer(Class<T> clazz, Class<?>... classes) {
    this.kryo = new Kryo();
    this.kryo.register(clazz);
    for (var c : classes) {
      if (c.isEnum()) {
        this.kryo.register(c, new DefaultSerializers.EnumSerializer((Class<? extends Enum<?>>) c));
      } else {
        this.kryo.register(c);
      }
    }
  }

  @Override
  public void serialize(DataOutput2 out, T value) throws IOException {
    try (final Output output = new Output(INITIAL_BUFFER_SIZE, -1)) {
      kryo.writeClassAndObject(output, value);

      final byte[] bytes = output.toBytes();
      final int size = output.position();

      out.writeLong(size);

      out.write(bytes, 0, size);
      out.flush();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(DataInput2 input, int available) throws IOException {
    final long sizeLong = input.readLong();

    if (sizeLong > Integer.MAX_VALUE) {
      throw new IOException("Serialized object size exceeds 2GB limit.");
    }
    final int size = (int) sizeLong;

    final byte[] bytes = new byte[size];
    input.readFully(bytes);

    try (final Input kryoInput = new Input(bytes)) {
      return (T) kryo.readClassAndObject(kryoInput);
    }
  }
}
