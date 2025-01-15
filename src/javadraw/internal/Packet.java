//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public interface Packet extends Serializable {
    static String serialize(Packet packet) {
        String message = "Undefined";

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(packet);
            so.flush();
            message = new String(Base64.getEncoder().encode(bo.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    static Packet deserialize(String message) {
        Packet packet = null;

        try {
            byte[] b = Base64.getDecoder().decode(message.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            packet = (Packet)si.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packet;
    }
}
