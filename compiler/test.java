package com.example;

public class TestMod {
    public static Block TestBlock;
    private void registerTestBlock() {
        TestBlock = new Block("TestBlock");
        TestBlock.displayName = "test_block";
        TestBlock.type = "stone";
        TestBlock.properties.put("hardness", "1.5");
        TestBlock.properties.put("sound", "stone");
        FunctionDefinition customFunctionFn = new FunctionDefinition("customFunction");
        customFunctionFn.actions.put("onUse", "doSomething");
        TestBlock.functions.put(customFunctionFn.name, customFunctionFn);
        registerInGroup("block", "BUILDING", TestBlock);
    }
    private void returnStatement1() {
        // return class
    }
    public void init() {
        registerTestBlock();
        returnStatement1();
    }
    public static void main(String[] args) {
        new TestMod().init();
    }
    private void registerInGroup(String objectType, String group, GameObject target) {
        if (target == null) {
            System.out.println("Registering " + objectType + " in group " + group + ": <null target>");
        } else {
            System.out.println("Registering " + objectType + " in group " + group + ": " + target.name);
        }
    }
    private static abstract class GameObject {
        public final String name;
        public String displayName;
        public String type;
        public final java.util.Map<String, String> properties = new java.util.LinkedHashMap<>();
        public final java.util.Map<String, FunctionDefinition> functions = new java.util.LinkedHashMap<>();
        public GameObject(String name) {
            this.name = name;
        }
    }
    private static class Block extends GameObject {
        public Block(String name) { super(name); }
    }
    private static class Item extends GameObject {
        public Item(String name) { super(name); }
    }
    private static class FunctionDefinition {
        public final String name;
        public final java.util.Map<String, String> actions = new java.util.LinkedHashMap<>();
        public FunctionDefinition(String name) {
            this.name = name;
        }
        }

}
