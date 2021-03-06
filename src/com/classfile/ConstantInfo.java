package com.classfile;

import java.io.UnsupportedEncodingException;

public abstract class ConstantInfo {
	public static final byte CONSTANT_Class = 7;
	public static final byte CONSTANT_Fieldref = 9;
	public static final byte CONSTANT_Methodref = 10;
	public static final byte CONSTANT_InterfaceMethodref = 11;
	public static final byte CONSTANT_String = 8;
	public static final byte CONSTANT_Integer = 3;
	public static final byte CONSTANT_Float = 4;
	public static final byte CONSTANT_Long = 5;
	public static final byte CONSTANT_Double = 6;
	public static final byte CONSTANT_NameAndType = 12;
	public static final byte CONSTANT_Utf8 = 1;
	public static final byte CONSTANT_MethodHandle = 15;
	public static final byte CONSTANT_MethodType = 16;
	public static final byte CONSTANT_InvokeDynamic = 18;
	
	public abstract void readInfo(ClassReader cr);
	public static ConstantInfo readConstantInfo(ClassReader cr,ConstantPool cp){
		byte tag = cr.readUnit8();
		ConstantInfo ci = newConstantInfo(tag,cp);
		ci.readInfo(cr);
		return ci;
	}
	private static ConstantInfo newConstantInfo(byte tag,ConstantPool cp){
		switch (tag) {
		case CONSTANT_Integer: return new ConstantIntegerInfo();
		case CONSTANT_Float: return new ConstantFloatInfo();
		case CONSTANT_Long: return new ConstantLongInfo();
		case CONSTANT_Double: return new ConstantDoubleInfo();
		case CONSTANT_Utf8: return new ConstantUtf8Info();
		case CONSTANT_String: return new ConstantStringInfo(cp);
		case CONSTANT_Class: return new ConstantClassInfo(cp);
		case CONSTANT_Fieldref:
		return new ConstantFieldrefInfo(cp);
		case CONSTANT_Methodref:
		return new ConstantMethodrefInfo(cp);
		case CONSTANT_InterfaceMethodref:
		return new ConstantInterfaceMethodrefInfo(cp);
		case CONSTANT_NameAndType: return new ConstantNameAndTypeInfo();
//		case CONSTANT_MethodType: return new ConstantMethodTypeInfo();
//		case CONSTANT_MethodHandle: return new ConstantMethodHandleInfo();
//		case CONSTANT_InvokeDynamic: return new ConstantInvokeDynamicInfo();
		default: throw new ClassFormatError("constant pool tag!");
		}
	}
}

class ConstantIntegerInfo extends ConstantInfo{
	private int val;
	@Override
	public void readInfo(ClassReader cr) {
		this.val = cr.readUnit32();
	}
}
class ConstantFloatInfo extends ConstantInfo{
	private float val;
	@Override
	public void readInfo(ClassReader cr) {
		this.val = Float.intBitsToFloat(cr.readUnit32());
	}
}
class ConstantLongInfo extends ConstantInfo{
	private long val;
	@Override
	public void readInfo(ClassReader cr) {
		this.val = cr.readUnit64();
	}
}
class ConstantDoubleInfo extends ConstantInfo{
	private double val;
	@Override
	public void readInfo(ClassReader cr) {
		this.val = Double.longBitsToDouble(cr.readUnit64());
	}
}
class ConstantUtf8Info extends ConstantInfo{
	private String val;
	@Override
	public void readInfo(ClassReader cr) {
		int length = cr.readUnit16()&0xffff;
		byte[] bytes = cr.readBytes(length);
		try {
			this.val = new String(bytes,"utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String toString() {
		return this.val;
	}
}
class ConstantStringInfo extends ConstantInfo{
	private ConstantPool cp;
	private short stringIndex;
	public ConstantStringInfo(ConstantPool cp){
		this.cp = cp;
	}
	@Override
	public void readInfo(ClassReader cr) {
		this.stringIndex = cr.readUnit16();
	}
	public String getString() {
		return this.cp.getUtf8(this.stringIndex);
	}
}
class ConstantClassInfo extends ConstantInfo{
	private ConstantPool cp;
	protected short nameIndex;
	public ConstantClassInfo(ConstantPool cp){
		this.cp = cp;
	}
	@Override
	public void readInfo(ClassReader cr) {
		this.nameIndex = cr.readUnit16();
	}
	public String getName(){
		return this.cp.getUtf8(this.nameIndex);
	}
}
class ConstantNameAndTypeInfo extends ConstantInfo{
	protected short nameIndex;
	protected short descriptionIndex;
	@Override
	public void readInfo(ClassReader cr) {
		this.nameIndex = cr.readUnit16();
		this.descriptionIndex = cr.readUnit16();
	}
}
class ConstantMemberrefInfo extends ConstantInfo{
	private ConstantPool cp;
	protected short classIndex;
	protected short nameAndTypeIndex;
	public ConstantMemberrefInfo(ConstantPool cp){
		this.cp = cp;
	}
	@Override
	public void readInfo(ClassReader cr) {
		this.classIndex = cr.readUnit16();
		this.nameAndTypeIndex = cr.readUnit16();
	}
	public String getClassName(){
		return this.cp.getUtf8(classIndex);
	}
	public String getNameAndDescription(){
		return this.cp.getUtf8(nameAndTypeIndex);
	}
}
class ConstantFieldrefInfo extends ConstantMemberrefInfo{
	public ConstantFieldrefInfo(ConstantPool cp){
		super(cp);
	}
}
class ConstantMethodrefInfo extends ConstantMemberrefInfo{
	public ConstantMethodrefInfo(ConstantPool cp){
		super(cp);
	}
}
class ConstantInterfaceMethodrefInfo extends ConstantMemberrefInfo{
	public ConstantInterfaceMethodrefInfo(ConstantPool cp){
		super(cp);
	}
}