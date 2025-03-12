package juul.util.image;

import java.io.InputStream;

public class Resources {

    public static TextureImage getTexture(final String imageURL) {
        if (TextureManager.exists(imageURL)) {
            if (TextureManager.get(imageURL) != null) {
                return TextureManager.get(imageURL);
            }
        }
        TextureImage textureImage = new TextureImage();
        textureImage.location = imageURL;
        final TextureImage ti = textureImage;
        TextureManager.cache.add(ti);
        new Thread(() -> {
            try {
                InputStream is = Resources.class.getResourceAsStream("/assets/juul/images/" + imageURL);
                if (is == null) {
                    throw new RuntimeException("Image resource not found: " + imageURL);
                }

                ti.pixels = is.readAllBytes();
                is.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }).start();
        return ti;
    }
}