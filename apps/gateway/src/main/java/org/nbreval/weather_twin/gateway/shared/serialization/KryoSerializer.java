package org.nbreval.weather_twin.gateway.shared.serialization;

import java.io.IOException;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

/**
 * Kryo serializer for serializing and deserializing objects.
 *
 * @param <T> the type of object to serialize/deserialize
 */
public class KryoSerializer<T> implements Serializer<T> {

  /**
   * Kryo instance for serialization.
   */
  private final Kryo kryo = new Kryo();

  public KryoSerializer() {
    kryo.setDefaultSerializer(FieldSerializer.class);
    kryo.setRegistrationRequired(false);
  }

  @Override
  public void serialize(DataOutput2 out, T value) throws IOException {
    try (final Output output = new Output(out)) {
      kryo.writeClassAndObject(output, value);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(DataInput2 input, int available) throws IOException {
    try {
      final byte[] bytes = new byte[available];
      input.readFully(bytes);
      Input kryoInput = new Input(bytes, 0, available);
      return (T) kryo.readClassAndObject(kryoInput);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
