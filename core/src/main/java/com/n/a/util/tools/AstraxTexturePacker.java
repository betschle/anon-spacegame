package com.n.a.util.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class XYZTexturePacker {

    public static void main(String[] args) {

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.paddingX = 1;
        settings.paddingY = 1;
        settings.maxWidth = 512 * 8;
        settings.maxHeight = 512 * 8;

        TexturePacker.process(settings,"D:\\Programming\\_games\\XYZ\\Graphic Source\\TexturePacker Source\\gfx",
                                      "D:\\Programming\\_games\\XYZ\\core\\assets\\gfx", "image_atlas");

        TexturePacker.process(settings,"D:\\Programming\\_games\\XYZ\\Graphic Source\\TexturePacker Source\\gfx\\ui",
                "D:\\Programming\\_games\\XYZ\\core\\assets\\ui\\skins\\XYZ-core", "styles");

    }
}
