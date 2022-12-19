package com.github.cylyl;

import io.github.sharelison.jsontojava.JsonToJava;

public class GenModel {
    static JsonToJava jsonToJava = new JsonToJava();

    public static void main(String[] args) {

        if(args.length < 3) {
            System.out.println("Usage: java GenModel <name> <json> <package> <output directory> ");
            System.out.println("name - name of model");
            System.out.println("json - json of model");
            System.out.println("package - package of model");
            System.out.println("outDir - outDir of model");
        }
        jsonToJava(args[0], args[1], args[2], args[3]);
    }

    public static void jsonToJava(String name, String json, String pack, String outDir) {
        jsonToJava.jsonToJava(
                json,
                name,
                pack,
                outDir
        );
    }
}
