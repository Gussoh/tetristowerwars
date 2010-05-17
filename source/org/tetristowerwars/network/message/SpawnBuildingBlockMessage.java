/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.network.NetworkClientModel;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class SpawnBuildingBlockMessage extends Message implements ClientMessage, EventQueueMessage {



    private final Vec2 position;
    private final Material material;
    private final short shape;
    public static final short CROSS = 0, LEFT_L = 1, LEFT_S = 2, LINE = 3, PYRAMID = 4, RIGHT_L = 5, RIGHT_S = 6, SQUARE = 7;
    private final static String materialPackage = "org.tetristowerwars.model.material.";


    public SpawnBuildingBlockMessage(Vec2 position, Material material, short shape) {
        this.position = new Vec2(position.x, position.y);
        this.material = material;
        this.shape = shape;
    }

    public SpawnBuildingBlockMessage(Vec2 position, Material material) {
        this(position, material, (short) MathUtil.randomInt(0, 7));
    }

    public SpawnBuildingBlockMessage(DataInputStream dataInputStream) throws IOException, ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        float x = dataInputStream.readFloat();
        float y = dataInputStream.readFloat();
        String materialName = dataInputStream.readUTF();
        shape = dataInputStream.readShort();

        Class materialClass = Class.forName(materialPackage + materialName);
        material = (Material) materialClass.getConstructor().newInstance();

        position = new Vec2(x, y);
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(position.x);
        dataOutputStream.writeFloat(position.y);
        dataOutputStream.writeUTF(material.getClass().getSimpleName());
        dataOutputStream.writeShort(shape);
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.addEventQueueMessage(this);
    }

    @Override
    public void executeMessage(NetworkClientModel networkClientModel) {
        networkClientModel.executeCreateBuildingBlock(position, material, shape);
    }
}
